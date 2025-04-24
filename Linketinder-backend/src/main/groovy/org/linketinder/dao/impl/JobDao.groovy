package org.linketinder.dao.impl

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.interfaces.IJobDao
import org.linketinder.model.Job
import org.linketinder.model.Skill

import java.sql.SQLException

class JobDao implements IJobDao{
	Sql sql
	SkillDao skillDao

	JobDao(Sql sql, SkillDao skillDao){
		this.sql = sql
		this.skillDao = skillDao

	}

	List<Map<String, Object>> getJobsRawData() {
		List<Map<String, Object>> jobs = []
		String query = selectAllJobs()
		try {
			sql.eachRow(query) { GroovyResultSet row ->
				jobs.add(row.toRowResult())
			}
		} catch (Exception e) {
			e.printStackTrace()
		}
		return jobs
	}

	static String selectAllJobs(){
		return """
			SELECT 
				v.id AS vaga_id,
				v.nome AS vaga_nome,
				v.descricao AS vaga_descricao,
				v.local AS vaga_local,
				v.id_empresa AS empresa_id,
				STRING_AGG(DISTINCT CONCAT(c.id, '.', c.nome), ',') AS competencias
			FROM 
				VAGA v
			LEFT JOIN VAGA_COMPETENCIA vc
				ON vc.id_vaga = v.id
			LEFT JOIN COMPETENCIA c
				ON c.id = vc.id_competencia
			GROUP BY 
				v.id, v.nome, v.descricao, v.local, v.id_empresa"""
	}

	void addJobData(Job job){
		try {
			Integer jobId = insertJob(job)
			job.setId(jobId)
			insertJobSkills(job)
		} catch (Exception e){
			e.printStackTrace()
			throw new Exception(e)
		}
	}

	void insertJobSkills(Job job){
		if (job.skills.isEmpty()) return
		List<Integer> skillsIds = skillDao.insertSkillsReturningId(job.skills)
		associateSkillsToJob(job.id, skillsIds)
	}

	Integer insertJob(Job job){
		try {
			List<List<Object>> keys = sql.executeInsert("""
                    INSERT INTO VAGA (nome, descricao, local, id_empresa)
                    VALUES (?, ?, ?, ?)
                """, [
					job.name,
					job.description,
					job.local,
					job.idCompany
			])
			return keys[0][0] as Integer
		} catch (SQLException e){
			throw new Exception(e.message)
		}
	}

	boolean removeJobById(Integer id){
		try {
			return sql.executeUpdate("DELETE FROM vaga WHERE id = ?", [id]) > 0
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return false
	}
}