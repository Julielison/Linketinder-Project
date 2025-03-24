package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Competencia

import java.sql.SQLException

class CompetenciaRepository {

	static List<Competencia> getCompetencias(Integer idCandidato){
		List<Competencia> competencias = new ArrayList<>()
		Sql sql = DatabaseConnection.getInstance()
		String query = """
            SELECT c.id, c.nome
            FROM competencia c
            JOIN candidato_competencia cc ON id_competencia = c.id 
            WHERE cc.id_candidato = ${idCandidato} 
            """

		try {
			sql.eachRow(query) {row ->
				competencias.add(new Competencia(
						row.id as Integer,
						row.nome as String
				))
			}
		} catch (SQLException e){
			e.printStackTrace()
		}
		return competencias
	}
}
