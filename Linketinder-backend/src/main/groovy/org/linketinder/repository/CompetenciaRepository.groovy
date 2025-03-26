package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Competencia

import java.sql.SQLException

class CompetenciaRepository {
	Sql sql

	CompetenciaRepository(Sql sql){
		this.sql = sql
	}

	List<Competencia> getCompetencias(){
		List<Competencia> competencias = new ArrayList<>()
		try {
			sql.eachRow("SELECT * FROM competencia"){row->
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


	List<Competencia> getCompetenciasPorCandidatoId(Integer idCandidato){
		List<Competencia> competencias = new ArrayList<>()
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

	Boolean verificarSeCompetenciaExiste(String nomeCompetencia){
		GroovyRowResult result = sql.firstRow("SELECT nome FROM competencia WHERE nome = ${nomeCompetencia}")
		return result.nome ? true : false
	}
}
