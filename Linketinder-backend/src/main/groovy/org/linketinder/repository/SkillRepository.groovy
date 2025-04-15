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

	List<Skill> insertSkillsReturningId(List<Skill> skills) {
		List<Skill> skillsWithId = []
		try {
			List<String> names = skills*.name.unique()
			String valuesSql = names.collect { "(?)" }.join(", ")
			String sqlQuery = """
			INSERT INTO competencia (nome)
			VALUES ${valuesSql}
			ON CONFLICT (nome) DO UPDATE SET nome = competencia.nome
			RETURNING id, nome"""

			List<GroovyRowResult> results = sql.rows(sqlQuery, names)
			results.each { GroovyRowResult row ->
				skillsWithId.add(new Skill(
						row.id as Integer,
						row.nome as String
				))
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		return skillsWithId
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