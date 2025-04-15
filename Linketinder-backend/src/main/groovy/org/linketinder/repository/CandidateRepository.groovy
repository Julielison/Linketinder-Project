package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.model.*
import org.linketinder.util.Util

import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CandidateRepository {
    Sql sql
    AddressRepository addressRepository
    SkillRepository skillRepository
    FormationRepository formationRepository

    CandidateRepository(Sql sql, AddressRepository addressRepository,
                        SkillRepository skillRepository,
                        FormationRepository formationRepository)
    {
        this.sql = sql
        this.addressRepository = addressRepository
        this.skillRepository = skillRepository
        this.formationRepository = formationRepository
    }

    List<Candidate> getCandidatos() {
        List<Candidate> candidatos = []
        String query = selectAllFromCandidates()

        try {
            sql.eachRow(query) { GroovyResultSet row ->
                List<Skill> skills = skillRepository.extractSkillsData(row.competencias.toString())
                List<Formation> formations = formationRepository.extractFormationsData(row.formacoes.toString())
                LocalDate dateOfBirth = Util.convertToLocalDate(row.candidato_data_nascimento.toString(), 'yyyy-MM-dd')
                Address address = new Address(
                        row.endereco_id as Integer,
                        row.endereco_cep as String,
                        new Country(row.pais_nome as String, row.pais_id as Integer)
                )

                Candidate candidato = new Candidate (
                        row.candidato_id as Integer,
                        row.candidato_nome as String,
                        row.candidato_email as String,
                        row.candidato_cpf as String,
                        dateOfBirth,
                        address,
                        row.candidato_descricao_pessoal as String,
                        row.candidato_senha_de_login as String,
                        skills,
                        formations,
                        row.candidato_sobrenome as String)
                candidatos.add(candidato)
            }
        } catch (SQLException e) {
            e.printStackTrace()
        } catch (DateTimeParseException e){
            e.printStackTrace()
        }
        return candidatos
    }

	static String selectAllFromCandidates(){
        return """
        SELECT 
            c.id AS candidato_id,
            c.nome AS candidato_nome,
            c.sobrenome AS candidato_sobrenome,
            c.data_nascimento AS candidato_data_nascimento,
            c.email AS candidato_email,
            c.cpf AS candidato_cpf,
            c.descricao_pessoal AS candidato_descricao_pessoal,
            c.senha_de_login AS candidato_senha_de_login,
            e.id AS endereco_id,
            e.cep AS endereco_cep,
            p.id AS pais_id,
            p.nome AS pais_nome,
            
            STRING_AGG(DISTINCT CONCAT(comp.id, '.', comp.nome), ',') AS competencias,
            
            STRING_AGG(DISTINCT CONCAT(
                f.id, ':', 
                f.nome, ':', 
                f.instituicao, ':', 
                TO_CHAR(fc.data_inicio, 'YYYY-MM-DD'), ':', 
                TO_CHAR(fc.data_fim_previsao, 'YYYY-MM-DD')
            ), ';') AS formacoes
        FROM 
            CANDIDATO c
        JOIN 
            ENDERECO e ON c.id_endereco = e.id
        JOIN 
            PAIS_DE_RESIDENCIA p ON e.pais_id = p.id
        LEFT JOIN 
            CANDIDATO_COMPETENCIA cc ON c.id = cc.id_candidato
        LEFT JOIN 
            COMPETENCIA comp ON cc.id_competencia = comp.id
        LEFT JOIN 
            FORMACAO_CANDIDATO fc ON c.id = fc.id_candidato
        LEFT JOIN 
            FORMACAO f ON fc.id_formacao = f.id
        GROUP BY 
            c.id, c.nome, c.sobrenome, c.data_nascimento, c.email, c.cpf, 
            c.descricao_pessoal, c.senha_de_login, e.id, e.cep, p.id, p.nome"""
    }

    void addDataFromCandidate(Candidate candidate) {
        sql.withTransaction {
            try {
                Integer countryId = addressRepository.getIdCountry(candidate.address.country.name)
                countryId = !countryId ? addressRepository.insertCountry(candidate.address.country.name) : null
                Integer addressId = addressRepository.insertAddress(candidate.address.zipCode, countryId)
                Integer candidateId = insertCandidate(candidate, addressId)
                candidate.setId(candidateId)

                if (!candidate.formations.isEmpty()) {
                    Candidate candidateWithFormationsId = formationRepository.insertFormations(candidate)
                    insertIdsAndDatesFromFormation(candidateWithFormationsId)
                }
                insertCandidateSkills(candidate)
            } catch (Exception e) {
                e.printStackTrace()
                throw new RuntimeException("Erro ao adicionar dados do candidato: ${e.message}")
            }
        }
    }

    Integer insertCandidate(Candidate candidate, Integer idAddress) {
        String dateBirthFormated = Util.convertToString(candidate.dateBirth)

        List<List<Object>> result = sql.executeInsert("""
    INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco)
    VALUES (?, ?, ?::date, ?, ?, ?, ?, ?)""", [
                candidate.name,
                candidate.lastName,
                dateBirthFormated,
                candidate.email,
                candidate.cpf,
                candidate.description,
                candidate.passwordLogin,
                idAddress
        ])
        return result[0][0] as Integer
    }
    void insertIdsAndDatesFromFormation(Candidate candidate) {
        try {
            List<Formation> formations = candidate.formations

            String valuesSql = formations.collect { "(?, ?, ?, ?)" }.join(", ")
            String sqlQuery = """
            INSERT INTO formacao_candidato (id_formacao, id_candidato, data_inicio, data_fim_previsao)
            VALUES ${valuesSql}"""

            List<Object> params = formations.collectMany { Formation formation ->
                [
                        formation.id,
                        candidate.id,
                        formation.dateStart,
                        formation.dateEnd
                ]
            }
            sql.execute(sqlQuery, params)
        } catch (Exception e) {
            throw new Exception(e)
        }
    }

    void insertCandidateSkills(Candidate candidate){
        if (candidate.skills.isEmpty()) return
        List<Integer> skillsWithId =  skillRepository.insertSkillsReturningId(candidate.skills)
        associateSkillsToCandidate(candidate.id, skillsWithId)
    }

    void associateSkillsToCandidate(Integer candidateId, List<Integer> skillsIds) {
        try {
            String valuesSql = skillsIds.collect { "(?, ?)" }.join(", ")
            GString sqlQuery = """
            INSERT INTO candidato_competencia (id_candidato, id_competencia)
            VALUES ${valuesSql}
            ON CONFLICT DO NOTHING"""

            List<Integer> params = skillsIds.collectMany {Integer skillId -> [candidateId, skillId] }
            sql.execute(sqlQuery, params)
        } catch (Exception e) {
            e.printStackTrace()
        }
    }


    boolean removeCandidateById(Integer id) {
        try {
            return sql.executeUpdate("DELETE FROM candidato WHERE id = ?", [id]) > 0
        } catch (SQLException e) {
            e.printStackTrace()
        }
        return false
    }
}