package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Candidato

import java.sql.SQLException
import java.text.SimpleDateFormat

class CandidatoRepository {
    private static List<Candidato> candidatos = []
    Sql sql
    EnderecoRepository endereco
    CompetenciaRepository competenciaRepository
    FormacaoRepository formacaoRepository
    EmailRepository emailRepository

    CandidatoRepository(Sql sql, EnderecoRepository enderecoRepository,
                        CompetenciaRepository competenciaRepository, FormacaoRepository formacaoRepository, EmailRepository emailRepository) {
        this.sql = sql
        this.endereco = enderecoRepository
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
            sql.eachRow(query) { row ->
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

            Integer paisId = endereco.obterIdPais(candidato.paisOndeReside)
            if (!paisId) {
                paisId = endereco.inserirPais(candidato.paisOndeReside)
            }

            Integer enderecoId = endereco.inserirEndereco(candidato.cep, paisId)
            Integer candidatoId = inserirCandidato(candidato, enderecoId)


            candidato.setId(candidatoId)
            candidatos.add(candidato)
        } catch (SQLException e){
            e.printStackTrace()
            throw new Exception(e.getMessage())
        }
    }

    Integer inserirCandidato(Candidato candidato, Integer idEndereco) {
        // Converter a data para string no formato yyyy-MM-dd
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
        String dataNascimentoFormatada = dateFormat.format(candidato.dataNascimento)

        def result = sql.executeInsert("""
    INSERT INTO CANDIDATO (nome, sobrenome, data_nascimento, email, cpf, descricao_pessoal, senha_de_login, id_endereco)
    VALUES (?, ?, ?::date, ?, ?, ?, ?, ?)
""", [
                candidato.nome,
                candidato.sobrenome,
                dataNascimentoFormatada, // Convertendo a data para string no formato yyyy-MM-dd
                candidato.email,
                candidato.cpf,
                candidato.descricao,
                candidato.senhaLogin,
                idEndereco
        ])
        return result[0][0] as Integer
    }
}