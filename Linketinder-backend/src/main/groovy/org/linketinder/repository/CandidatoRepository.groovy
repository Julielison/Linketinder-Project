package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.model.Candidato
import org.linketinder.model.Formacao
import org.linketinder.util.Util

import java.sql.SQLException

class CandidatoRepository {
    private static List<Candidato> candidatos = []
    Sql sql
    EnderecoRepository enderecoRepository
    CompetenciaRepository competenciaRepository
    FormacaoRepository formacaoRepository
    EmailRepository emailRepository

    CandidatoRepository(Sql sql, EnderecoRepository enderecoRepository,
                        CompetenciaRepository competenciaRepository, FormacaoRepository formacaoRepository, EmailRepository emailRepository) {
        this.sql = sql
        this.enderecoRepository = enderecoRepository
        this.competenciaRepository = competenciaRepository
        this.formacaoRepository = formacaoRepository
        this.emailRepository = emailRepository
    }

    List<Candidato> getCandidatos() {
        candidatos.clear()
        String query = """
            SELECT 
                c.id AS candidato_id,
                c.nome AS candidato_nome,
                c.sobrenome AS candidato_sobrenome,
                c.data_nascimento AS candidato_data_nascimento,
                c.email AS candidato_email,
                c.cpf AS candidato_cpf,
                c.descricao_pessoal AS candidato_descricao_pessoal,
                c.senha_de_login AS candidato_senha_de_login,
                e.cep AS endereco_cep,
                p.nome AS pais_nome
            FROM 
                CANDIDATO c
            JOIN 
                ENDERECO e ON c.id_endereco = e.id
            JOIN 
                PAIS_DE_RESIDENCIA p ON e.pais_id = p.id"""

        try {
            sql.eachRow(query) { GroovyResultSet row ->
                Integer idCandidato = row.candidato_id

                Candidato candidato = new Candidato(
                        idCandidato,
                        row.candidato_nome as String,
                        row.candidato_email as String,
                        row.candidato_cpf as String,
                        row.candidato_data_nascimento as Date,
                        row.endereco_cep as String,
                        row.candidato_descricao_pessoal as String,
                        row.candidato_senha_de_login as String,
                        row.pais_nome as String,
                        competenciaRepository.getCompetenciasPorCandidatoId(idCandidato),
                        formacaoRepository.getFormacoesByIdCandidato(idCandidato),
                        row.candidato_sobrenome as String
                )
                candidatos.add(candidato)
            }
            return candidatos
        } catch (Exception e) {
            println("Erro ao buscar candidatos: ${e.message}")
            e.printStackTrace()
            return []
        }
    }

    void addCandidato(Candidato candidato){
        try {
            def resultCpf = sql.firstRow("SELECT cpf FROM candidato WHERE cpf = ?", [candidato.cpf])

            if (resultCpf){
                throw new Exception("Candidato já existe!")
            }
            if (emailRepository.verificarSeEmailJaExiste(candidato.email)){
                throw new Exception("Email já existe! Candidato não criado!")
            }

            Integer paisId = enderecoRepository.obterIdPais(candidato.paisOndeReside)
            if (!paisId) {
                paisId = enderecoRepository.inserirPais(candidato.paisOndeReside)
            }

            Integer enderecoId = enderecoRepository.inserirEndereco(candidato.cep, paisId)
            Integer candidatoId = inserirCandidato(candidato, enderecoId)
            candidato.setId(candidatoId)

            if(!candidato.formacoes.isEmpty()){
                Candidato candidatoComIdFormacoes = formacaoRepository.inserirFormacoes(candidato)
                inserirIdsFormacaoCandidatoEDatas(candidatoComIdFormacoes)
            }
        } catch (SQLException e){
            e.printStackTrace()
            throw new Exception(e.getMessage())
        } catch (Exception e){
            throw new Exception(e.getMessage())
        }
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
}