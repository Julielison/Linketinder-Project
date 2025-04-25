package org.linketinder.dao.impl


import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.dao.interfaces.ISkillDao
import org.linketinder.model.Skill

import java.sql.SQLException

class SkillDao implements ISkillDao{
	Sql sql

	SkillDao(Sql sql){
		this.sql = sql
	}

	List<Map<String, Object>> getSkillsRawData(){
		List<Skill> skills = []
		try {
			sql.eachRow("SELECT * FROM competencia"){row->
				skills.add(row.toRowResult())
			}
		} catch (SQLException e) {
			e.printStackTrace()
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