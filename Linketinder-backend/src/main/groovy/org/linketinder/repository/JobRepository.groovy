package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Skill
import org.linketinder.model.Job

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
			sql.eachRow(query) { row ->
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
		} catch (SQLException e) {
			e.printStackTrace()
		} catch (Exception e){
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
			JOIN VAGA_COMPETENCIA vc
				ON vc.id_vaga = v.id
			JOIN COMPETENCIA c
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

	List<Job> getVagasByEmpresaId(Integer empresaId) {
		List<Job> vagasEmpresa = []
		String query = """
            SELECT 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.local AS vaga_local,
                v.id_empresa AS empresa_id
            FROM 
                VAGA v
            WHERE 
                v.id_empresa = ?"""

		try {
			sql.eachRow(query, [empresaId]) { row ->
				Integer vagaId = row.vaga_id as Integer
				Job vaga = new Job(
						vagaId,
						row.vaga_nome as String,
						row.vaga_descricao as String,
						row.vaga_local as String,
						row.empresa_id as Integer,
						skillRepository.getCompetenciasPorIdDaVaga(vagaId)
				)
				vagasEmpresa.add(vaga)
			}
			return vagasEmpresa
		} catch (Exception e) {
			println("Erro ao buscar vagas da empresa ID ${empresaId}: ${e.message}")
			e.printStackTrace()
			return []
		}
	}

	Integer inserirVaga(Job vaga){
		try {
			def keys = sql.executeInsert("""
                    INSERT INTO VAGA (nome, descricao, local, id_empresa)
                    VALUES (?, ?, ?, ?)
                """, [
					vaga.nome,
					vaga.descricao,
					vaga.local,
					vaga.idEmpresa
			])
			vaga.id = keys[0][0] as Integer
		} catch (SQLException e){
			e.printStackTrace()
		}
		return vaga.id
	}

	void inserirIdVagaCompetencia(List<Skill> competencias, Integer idVaga) {
		try {
			competencias.each { Skill competencia -> {
				sql.executeInsert("INSERT INTO vaga_competencia(id_vaga, id_competencia) VALUES (?, ?)", [idVaga, competencia.id])
			}}
		} catch (SQLException e) {
			e.printStackTrace()
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