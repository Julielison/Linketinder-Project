package org.linketinder.dao.impl

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.interfaces.IAssociateEntity
import org.linketinder.dao.interfaces.ICRUD
import org.linketinder.dao.interfaces.ISkillDao
import org.linketinder.model.Job

import java.sql.SQLException

class JobDao implements ICRUD<Job> {
	Sql sql
	ISkillDao skillDao
	IAssociateEntity jobSkillDao

	JobDao(Sql sql, ISkillDao skillDao, IAssociateEntity jobSkillDao){
		this.sql = sql
		this.skillDao = skillDao
		this.jobSkillDao = jobSkillDao
	}

	List<Map<String, Object>> getAll() {
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

	Job save(Job job){
		try {
			sql.withTransaction {
				Integer jobId = insertJob(job)
				job.setId(jobId)
				this.insertJobSkills(job)
			}
		} catch (Exception e){
			e.printStackTrace()
			throw e
		}
		return job
	}

	void insertJobSkills(Job job){
		if (job.skills.isEmpty()) return
		List<Integer> skillsIds = skillDao.insertSkillsReturningId(job.skills)
		jobSkillDao.associateEntityWithSkill(job.id, skillsIds)
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
		} catch (SQLException ignored){
			throw new Exception("Empresa não existe!")
		}
	}

	boolean deleteById(Integer id){
		try {
			return sql.executeUpdate("DELETE FROM vaga WHERE id = ?", [id]) > 0
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return false
	}

	boolean update(Job job){
		// TODO
		return false
	}
}