package org.linketinder.service

import org.linketinder.dao.impl.JobDao
import org.linketinder.dao.interfaces.ICRUD
import org.linketinder.model.Job
import org.linketinder.model.Skill

class JobService {
	ICRUD<Job> jobDao

	JobService(ICRUD<Job> jobDao) {
		this.jobDao = jobDao
	}

	List<Job> listAllJobs(){
		List<Map<String, Object>> rawJobs = jobDao.getAll()
		return setupJobsToController(rawJobs)
	}

	boolean removeJobById(Integer id){
		return jobDao.deleteById(id)
	}

	Job registerJob(Integer idCompany, Map<String, ?> data){
		Job job = setupJobToDao(idCompany, data)
		try {
			return jobDao.save(job)
		} catch (Exception e) {
			throw e
		}
	}

	private static setupJobToDao(Integer idCompany, Map<String, ?> data){
		List<Skill> skills = SkillService.extractSkillsFromMap(data)
		return new Job (
				null,
				data.description as String,
				data.name as String,
				data.local as String,
				idCompany,
				skills
		)
	}

	private static List<Job> setupJobsToController(List<Map<String, Object>> rawJobs){
		return rawJobs.collect {Map<String, Object> row ->
			Integer jobId = row.vaga_id as Integer
			List<Skill> skills = SkillService.extractSkills(row.competencias.toString())
			new Job(
					jobId,
					row.vaga_nome as String,
					row.vaga_descricao as String,
					row.vaga_local as String,
					row.empresa_id as Integer,
					skills
			)
		}
	}

	static List<Job> extractJobsData(String jobsData, Integer id_company) {
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
					skills = SkillService.extractSkills(idJobDescriptionLocalSkills[4])
				}
				jobs.add(new Job(id, name, description, local, id_company, skills))
			}
		}
		return jobs
	}
}
