package org.linketinder.service

import org.linketinder.model.*
import org.linketinder.dao.impl.CandidateDao
import org.linketinder.dao.impl.CompanyDao

import org.linketinder.dao.impl.JobDao
import org.linketinder.dao.impl.SkillDao

class ServiceManager {
	final CompanyDao companyRepository
	final CandidateDao candidateRepository
	final JobDao jobRepository
	final SkillDao skillRepository

	ServiceManager(CompanyDao companyRepository, CandidateDao candidateRepository,
				   JobDao jobRepository, SkillDao skillRepository) {
		this.companyRepository = companyRepository
		this.candidateRepository = candidateRepository
		this.jobRepository = jobRepository
		this.skillRepository = skillRepository
	}

	List<Candidate> listarCandidatos() {
		return candidateRepository.getCandidates()
	}

	List<Company> listarEmpresas() {
		return companyRepository.getCompanies()
	}

	List<Job> listJobs(){
		return jobRepository.getJobs()
	}

	List<Skill> listarCompetencias(){
		return skillRepository.getSkills()
	}
	String removeJob(Integer id){
		return jobRepository.removeJobById(id) ? "Vaga removida com sucesso!" : "Vaga não existe!"
	}
	String removeSkill(Integer id){
		return skillRepository.removeSkillById(id) ? "Competência removida com sucesso!" : "Competência não existe!"
	}

	String registerJob(Integer idCompany, Map<String, ?> data){
		List<Skill> skills = extractSkillsFromMap(data)
		Job job = new Job (
				null,
				data.description as String,
				data.name as String,
				data.local as String,
				idCompany,
				skills
		)
		String feedback = "Cadastro feito com sucesso!"
		try {
			jobRepository.addJobData(job)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

}