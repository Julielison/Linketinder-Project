package org.linketinder.service

import org.linketinder.model.*
import org.linketinder.dao.CandidateDao
import org.linketinder.dao.CompanyDao

import org.linketinder.dao.JobDao
import org.linketinder.dao.SkillDao

import java.time.LocalDate

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
		return companyRepository.getEmpresas()
	}

	List<Job> listarVagas(){
		return jobRepository.getJobs()
	}

	List<Skill> listarCompetencias(){
		return skillRepository.getSkills()
	}

	String registerCandidate(Map<String, String> data) {
		List<Formation> formations = extractFormationsFromMap(data)
		List<Skill> skills = extractSkillsFromMap(data)
		Address address = new Address(null, data.zipCode, new Country(data.country, null))

		Person newCandidate = new Candidate(
				null,
				data.firstName,
				data.email,
				data.cpf,
				data.dateBirth as LocalDate,
				address,
				data.description,
				data.password,
				skills,
				formations,
				data.lastName
		)
		String feedback = "Candidato cadastrado com sucesso!"
		try {
			candidateRepository.addDataFromCandidate(newCandidate)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

	String registerCompany(Map<String, String> data) {
		Address address = new Address(
				null,
				data.zipCode,
				new Country(data.country, null)
		)
		Person newCompany = new Company(
				null,
				data.name,
				data.email,
				data.cnpj,
				address,
				data.description,
				data.password,
				[]
		)
		String feedback = "Cadastro feito com sucesso!"
		try {
			companyRepository.addCompanyData(newCompany)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

	String removeCandidate(Integer id){
		return candidateRepository.removeCandidateById(id) ? "Candidato removido com sucesso!" : "Candidato não existe!"
	}
	String removeCompany(Integer id){
		return companyRepository.removeCompanyById(id) ? "Empresa removida com sucesso!" : "Empresa não existe!"
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

	private static List<Skill> extractSkillsFromMap(Map<String, ?> dados){
		List<Skill> skills = []
		for (String skillName : dados.get('skills')){
			Skill skill = new Skill(
					null,
					skillName
			)
			skills.add(skill)
		}
		return skills
	}

	private static List<Formation> extractFormationsFromMap(Map<String, ?> data){
		List<Formation> formations = []
		for (Map<String, ?> formationMap : data.get("formations") as List<Map<String, ?>>){
			Formation formation = new Formation(
					null,
					formationMap.get('institution') as String,
					formationMap.get('name') as String,
					formationMap.get('dateStart') as LocalDate,
					formationMap.get('dateEnd') as LocalDate
			)
			formations.add(formation)
		}
		return formations
	}
}