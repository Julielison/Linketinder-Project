package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Skill

import java.sql.SQLException

class SkillRepository {
	Sql sql

	SkillRepository(Sql sql){
		this.sql = sql
	}

	List<Skill> getSkills(){
		List<Skill> skills = new ArrayList<>()
		try {
			sql.eachRow("SELECT * FROM competencia"){row->
				skills.add(new Skill(
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

	static List<Skill> extractSkillsData(String skillsData) {
		List<Skill> skills = new ArrayList<>()

		skillsData.split(',').each { String skillData ->
			String[] idSkillName = skillData.split('\\.')
			if (idSkillName.length > 0) {
				Integer id = idSkillName[0].toInteger()
				String name = idSkillName[1]
				skills.add(new Skill(id, name))
			}
		}
		return skills
	}

	List<Skill> getCompetenciasPorCandidatoId(Integer idCandidato){
		List<Skill> competencias = new ArrayList<>()
		String query = """
            SELECT c.id, c.nome
            FROM competencia c
            JOIN candidato_competencia cc ON id_competencia = c.id 
            WHERE cc.id_candidato = ${idCandidato} 
            """

		try {
			sql.eachRow(query) {row ->
				competencias.add(new Skill(
						row.id as Integer,
						row.nome as String
				))
			}
		} catch (SQLException e){
			e.printStackTrace()
		}
		return competencias
	}

	List<Skill> getCompetenciasPorIdDaVaga(Integer id){
		List<Skill> competencias = new ArrayList<>()
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
	Map<String, List<Skill>> setIdsCompetenciasExistentes(List<Skill> competencias) {
		List<Skill> competenciasComId = new ArrayList<>()
		List<Skill> competenciasSemId = new ArrayList<>()
		try {
			competencias.forEach { Skill it ->
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

	List<Skill> addCompetencias(List<Skill> competencias) {
		try {
			competencias.each { Skill competencia ->
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
	boolean removeSkillById(Integer id) {
		try {
			return sql.executeUpdate("DELETE FROM competencia WHERE id = ?", [id]) > 0
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return false
	}
}