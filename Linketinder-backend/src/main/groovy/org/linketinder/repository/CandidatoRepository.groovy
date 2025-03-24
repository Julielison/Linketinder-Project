package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Candidato

class CandidatoRepository {
    private static List<Candidato> candidatos = []
    private static  Sql sql = DatabaseConnection.getInstance()

    static List<Candidato> getCandidatos() {
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
                        CompetenciaRepository.getCompetencias(idCandidato),
                        FormacaoRepository.getFormacoesByIdCandidato(idCandidato)
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

}