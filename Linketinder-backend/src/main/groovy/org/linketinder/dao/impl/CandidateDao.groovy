package org.linketinder.dao.impl

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.interfaces.ICandidateDao
import org.linketinder.model.Candidate
import org.linketinder.util.ConvertUtil

import java.sql.SQLException

class CandidateDao implements ICandidateDao {
    Sql sql
    AddressDao addressDao
    FormationDao formationDao
    SkillDao skillDao
    FormationCandidateDao formationCandidateDao
    CandidateSkillDao candidateSkillDao

    CandidateDao(Sql sql, AddressDao addressDao, FormationDao formationDao, SkillDao skillDao,
                 FormationCandidateDao formationCandidateDao, CandidateSkillDao candidateSkillDao) {
        this.sql = sql
        this.addressDao = addressDao
        this.formationDao = formationDao
        this.skillDao = skillDao
        this.formationCandidateDao = formationCandidateDao
        this.candidateSkillDao = candidateSkillDao
    }

    List<Map<String, Object>> getCandidatesRawData() {
        List<Map<String, Object>> candidates = []
        String query = selectAllFromCandidates()
        try {
            sql.eachRow(query) { GroovyResultSet row ->
                candidates.add(row.toRowResult())
            }
        } catch (SQLException e) {
            e.printStackTrace()
        }
        return candidates
    }

    static String selectAllFromCandidates() {
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

    void addAllDataFromCandidate(Candidate candidate) {
        try {
            sql.withTransaction {
                Integer countryId = addressDao.insertCountryReturningId(candidate.address.country.name)
                Integer addressId = addressDao.insertAddressReturningId(candidate.address.zipCode, countryId)
                Integer candidateId = this.insertCandidate(candidate, addressId)
                candidate.setId(candidateId)

                if (!candidate.formations.isEmpty()) {
                    Candidate candidateWithFormationsId = formationDao.insertFormations(candidate)
                    formationCandidateDao.insertIdsAndDatesFromFormation(candidateWithFormationsId)
                }
                if (!candidate.skills.isEmpty()) {
                    List<Integer> skillsWithId = skillDao.insertSkillsReturningId(candidate.skills)
                    candidateSkillDao.associateSkillsToCandidate(candidate.id, skillsWithId)
                }
            }
            } catch (Exception e) {
                e.printStackTrace()
                throw new RuntimeException("Erro ao adicionar dados do candidato: ${e.message}")
            }
    }

    private Integer insertCandidate(Candidate candidate, Integer idAddress) {
        String dateBirthFormated = ConvertUtil.convertToString(candidate.dateBirth)

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

    boolean removeCandidateById(Integer id) {
        try {
            return sql.executeUpdate("DELETE FROM candidato WHERE id = ?", [id]) > 0
        } catch (SQLException e) {
            e.printStackTrace()
        }
        return false
    }
}