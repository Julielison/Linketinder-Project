package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.model.Address
import org.linketinder.model.Candidato
import org.linketinder.model.Competencia
import org.linketinder.model.Country
import org.linketinder.model.Formacao
import org.linketinder.util.Util

import java.sql.SQLException
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CandidateRepository {
    Sql sql
    EnderecoRepository adressRepository
    SkillRepository skillRepository
    FormacaoRepository formacaoRepository
    EmailRepository emailRepository

    CandidateRepository(Sql sql, EnderecoRepository adressRepository,
                        SkillRepository skillRepository,
                        FormacaoRepository formacaoRepository,
                        EmailRepository emailRepository)
    {
        this.sql = sql
        this.adressRepository = adressRepository
        this.skillRepository = skillRepository
        this.formacaoRepository = formacaoRepository
        this.emailRepository = emailRepository
    }

    List<Candidato> getCandidatos() {
        List<Candidato> candidatos = []
        String query = selectAllFromCandidates()

        try {
            sql.eachRow(query) { GroovyResultSet row ->
                List<Competencia> skills = skillRepository.extractSkillsData(row.competencias.toString())
                List<Formacao> formations = Formacao.extractFormationsData(row.formacoes.toString())
                LocalDate dateOfBirth = Util.convertToLocalDate(row.candidato_data_nascimento.toString(), 'yyyy-MM-dd')
                Address address = new Address(
                        row.endereco_id as Integer,
                        row.endereco_cep as String,
                        new Country(row.pais_nome as String, row.pais_id as Integer)
                )

                Candidato candidato = new Candidato (
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

    void addDataFromCandidate(Candidato candidate){
        try {
            if (candidateIsValid(candidate)){
                Integer paisId = adressRepository.obterIdPais(candidate.address.country.name)
                if (!paisId) {
                    paisId = adressRepository.inserirPais(candidate.paisOndeReside)
                }
                Integer enderecoId = adressRepository.inserirEndereco(candidate.cep, paisId)
                Integer candidateId = inserirCandidato(candidate, enderecoId)
                candidate.setId(candidateId)

                if(!candidate.formacoes.isEmpty()){
                    Candidato candidateComIdFormacoes = formacaoRepository.inserirFormacoes(candidate)
                    inserirIdsFormacaoCandidatoEDatas(candidateComIdFormacoes)
                }
            }
        } catch (SQLException e){
            e.printStackTrace()
        } catch (Exception e){
            throw new Exception(e.message)
        }
    }

    boolean candidateIsValid(Candidato candidate) throws Exception {
        def resultCpf = sql.firstRow("SELECT cpf FROM candidato WHERE cpf = ?", [candidate.cpf])
        if (resultCpf){
            throw new Exception("Cpf já existe!")
        }
        if (emailRepository.checkIfEmailAlreadyExists(candidate.email)){
            throw new Exception("Email já existe!")
        }
        return true
    }

    Integer inserirCandidato(Candidato candidato, Integer idEndereco) {
        String dataNascimentoFormatada = Util.formatarData(candidato.dataNascimento,'yyyy-MM-dd')

        def result = sql.executeInsert("""
    INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco)
    VALUES (?, ?, ?::date, ?, ?, ?, ?, ?)
""", [
                candidato.nome,
                candidato.sobrenome,
                dataNascimentoFormatada,
                candidato.email,
                candidato.cpf,
                candidato.descricao,
                candidato.senhaLogin,
                idEndereco
        ])
        return result[0][0] as Integer
    }
    void inserirIdsFormacaoCandidatoEDatas(Candidato candidato){
        try {
            candidato.formacoes.each { Formacao formacao ->
                sql.executeInsert("""
                    INSERT INTO formacao_candidato VALUES (?, ?, ?::date, ?::date)""",
                        [formacao.id, candidato.id, Util.formatarData(formacao.dataInicio, "yyyy-MM-dd"), Util.formatarData(formacao.dataFim,"yyyy-MM-dd")])
            }
        } catch (SQLException e){
            throw new Exception(e.getMessage())
        } catch (Exception e){
            throw new Exception(e.getMessage())
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