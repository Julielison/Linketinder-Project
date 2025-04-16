package org.linketinder.dao


import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Skill

import java.sql.SQLException

class SkillDao {
	Sql sql

	SkillDao(Sql sql){
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

	List<Integer> insertSkillsReturningId(List<Skill> skills) {
		List<Integer> skillsWithId = []
		try {
			List<String> names = skills*.name.unique()
			String valuesSql = names.collect { "(?)" }.join(", ")
			String sqlQuery = """
			INSERT INTO competencia (nome)
			VALUES ${valuesSql}
			ON CONFLICT (nome) DO UPDATE SET nome = competencia.nome
			RETURNING id"""

			List<GroovyRowResult> results = sql.rows(sqlQuery, names)
			results.each { GroovyRowResult row ->
				skillsWithId.add(row.id as Integer)
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		return skillsWithId
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