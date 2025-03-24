package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Candidato

class CandidatoRepository {
    private static List<Candidato> candidatos = []

    static List<Candidato> getCandidatos() {
        candidatos.clear()
        Sql sql = DatabaseConnection.getInstance()

        try {
            sql.eachRow("SELECT * FROM candidato") { row ->
                Integer idCandidato = row.id

                Candidato candidato = new Candidato(
                        idCandidato,
                        row.nome as String,
                        row.email as String,
                        row.cpf as String,
                        row.data_nascimento as Date,
                        "fixo",
                        row.descricao_pessoal as String,
                        row.senha_de_login as String,
                        "fixo",
                        CompetenciaRepository.getCompetencias(idCandidato)
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