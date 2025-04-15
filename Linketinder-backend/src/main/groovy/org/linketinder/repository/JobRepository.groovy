package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.model.Job
import org.linketinder.model.Skill

import java.sql.SQLException

class JobRepository {
	Sql sql
	SkillRepository skillRepository

	JobRepository(Sql sql, SkillRepository skillRepository){
		this.sql = sql
		this.skillRepository = skillRepository

	}

	List<Job> getJobs() {
		List<Job> jobs = []
		String query = selectAllJobs()

		try {
			sql.eachRow(query) { GroovyResultSet row ->
				Integer jobId = row.vaga_id as Integer
				List<Skill> skills = skillRepository.extractSkillsData(row.competencias.toString())
				Job job = new Job(
						jobId,
						row.vaga_nome as String,
						row.vaga_descricao as String,
						row.vaga_local as String,
						row.empresa_id as Integer,
						skills
				)
				jobs.add(job)
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

	List<Job> extractJobsData(String jobsData, Integer id_company) {
		List<Job> jobs = []
		jobsData.split(';').each { String jobStr ->
			String[] idJobDescriptionLocalSkills = jobStr.split(':')
			if (idJobDescriptionLocalSkills.length > 1) {
				Integer id = idJobDescriptionLocalSkills[0].toInteger()
				String name = idJobDescriptionLocalSkills[1]
				String description = idJobDescriptionLocalSkills[2]
				String local = idJobDescriptionLocalSkills[3]
				List<Skill> skills = []
				if (idJobDescriptionLocalSkills.length == 5){
					skills = skillRepository.extractSkillsData(idJobDescriptionLocalSkills[4])
				}
				jobs.add(new Job(id, name, description, local, id_company, skills))
			}
		}
		return jobs
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
		List<Integer> skillsIds = skillRepository.insertSkillsReturningId(job.skills)
		associateSkillsToJob(job.id, skillsIds)
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