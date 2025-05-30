package org.linketinder.dao.impl

import groovy.sql.Sql
import org.linketinder.dao.interfaces.IAssociateEntity

class CandidateSkillDao implements IAssociateEntity {
	Sql sql

	CandidateSkillDao(Sql sql) {
		this.sql = sql
	}

	void associateEntityWithSkill(Integer candidateId, List<Integer> skillsIds) {
		try {
			String valuesSql = skillsIds.collect { "(?, ?)" }.join(", ")
			GString sqlQuery = """
            INSERT INTO candidato_competencia (id_candidato, id_competencia)
            VALUES ${valuesSql}
            ON CONFLICT DO NOTHING"""

			List<Integer> params = skillsIds.collectMany {Integer skillId -> [candidateId, skillId] }
			sql.execute(sqlQuery, params)
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}
