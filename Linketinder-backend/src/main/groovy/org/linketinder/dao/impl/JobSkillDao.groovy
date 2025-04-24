package org.linketinder.dao.impl

import groovy.sql.Sql

class JobSkillDao {
	Sql sql

	JobSkillDao(Sql sql) {
		this.sql = sql
	}

	void associateSkillsToJob(Integer jobId, List<Integer> skillsIds) {
		try {
			String valuesSql = skillsIds.collect { "(?, ?)" }.join(", ")
			GString sqlQuery = """
            INSERT INTO vaga_competencia (id_vaga, id_competencia)
            VALUES ${valuesSql}
            ON CONFLICT DO NOTHING"""

			List<Integer> params = skillsIds.collectMany {Integer skillId -> [jobId, skillId] }
			sql.execute(sqlQuery, params)
		} catch (Exception e) {
			e.printStackTrace()
		}
	}
}
