package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Competencia

import java.sql.SQLException

class SkillRepository {
	Sql sql

	SkillRepository(Sql sql){
		this.sql = sql
	}

	List<Competencia> getSkills(){
		List<Competencia> skills = new ArrayList<>()
		try {
			sql.eachRow("SELECT * FROM competencia"){row->
				skills.add(new Competencia(
						row.id as Integer,
						row.nome as String
				))
			}
		} catch (SQLException e){
			e.printStackTrace()
		} catch (Exception e){
			e.printStackTrace()
		}
		return skills
	}

	static List<Competencia> extractSkillsData(String skillsData) {
		List<Competencia> skills = new ArrayList<>()

		skillsData.split(',').each { String skillData ->
			String[] idSkillName = skillData.split('\\.')
			if (idSkillName.length > 0) {
				Integer id = idSkillName[0].toInteger()
				String name = idSkillName[1]
				skills.add(new Competencia(id, name))
			}
		}
		return skills
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

	List<Competencia> getCompetenciasPorIdDaVaga(Integer id){
		List<Competencia> competencias = new ArrayList<>()
		try {
			sql.eachRow("""
				SELECT c.nome as nome_competencia
				FROM vaga_competencia vc
				JOIN competencia c
				ON c.id = vc.id_competencia
				WHERE vc.id_vaga = ?
		""", [id]){ GroovyResultSet it ->
				competencias.add(it.nome_competencia)
			}
		} catch (SQLException e){
			e.printStackTrace()
			throw new SQLException(e.getMessage())
		}
		return competencias
	}
	Map<String, List<Competencia>> setIdsCompetenciasExistentes(List<Competencia> competencias) {
		List<Competencia> competenciasComId = new ArrayList<>()
		List<Competencia> competenciasSemId = new ArrayList<>()
		try {
			competencias.forEach {Competencia it ->
				GroovyRowResult result = sql.firstRow("SELECT id FROM competencia WHERE nome = ?", [it.nome])
				if (result != null) {
					it.setId(result.id as Integer)
					competenciasComId.add(it)
				} else {
					competenciasSemId.add(it)
				}
			}
		} catch (SQLException e){
			e.printStackTrace()
		}

		return [
		        comId: competenciasComId,
				semId: competenciasSemId
		]
	}

	List<Competencia> addCompetencias(List<Competencia> competencias) {
		try {
			competencias.each { Competencia competencia ->
				def result = sql.executeInsert("INSERT INTO competencia (nome) VALUES (?)", [competencia.nome])
				if (result) {
					competencia.id = result[0][0] as Integer
				}
			}
		} catch (SQLException e){
			e.printStackTrace()
		}

		return competencias
	}
}
