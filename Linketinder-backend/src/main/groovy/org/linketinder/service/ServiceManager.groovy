package org.linketinder.service

import org.linketinder.model.*
import org.linketinder.repository.CandidateRepository
import org.linketinder.repository.CompanyRepository

import org.linketinder.repository.JobRepository
import org.linketinder.repository.SkillRepository

import java.time.LocalDate

class ServiceManager {
	final CompanyRepository companyRepository
	final CandidateRepository candidateRepository
	final JobRepository jobRepository
	final SkillRepository skillRepository

	ServiceManager(CompanyRepository companyRepository, CandidateRepository candidateRepository,
				   JobRepository jobRepository, SkillRepository skillRepository) {
		this.companyRepository = companyRepository
		this.candidateRepository = candidateRepository
		this.jobRepository = jobRepository
		this.skillRepository = skillRepository
	}

	List<Candidato> listarCandidatos() {
		return candidateRepository.getCandidatos()
	}

	List<Company> listarEmpresas() {
		return companyRepository.getEmpresas()
	}

	List<Vaga> listarVagas(){
		return jobRepository.getJobs()
	}

	List<Competencia> listarCompetencias(){
		return skillRepository.getSkills()
	}

	String registerCandidate(Map<String, String> data) {
		List<Formacao> formations = extractFormationsFromMap(data)
		List<Competencia> skills = extractSkillsFromMap(data)
		Address address = new Address(null, data.zipCode, new Country(data.country, null))

		Pessoa newCandidate = new Candidato(
				null,
				data.name,
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

	String cadastrarEmpresa(Map<String, String> dados) {
		String feedback = "Cadastro feito com sucesso!"
		Pessoa novaEmpresa = new Company(
				null,
				dados.nome,
				dados.email,
				dados.cnpj,
				dados.cep,
				dados.descricao,
				dados.senha,
				dados.pais,
				new ArrayList<>()
		)
		try {
			companyRepository.addEmpresa(novaEmpresa)
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

	String cadastrarVaga(Integer idEmpresa, Map<String, ?> dados){
		List<Competencia> competencias = extrairCompetencias(dados)
		Vaga vaga = new Vaga (
				null,
				dados.descricao as String,
				dados.nome as String,
				dados.local as String,
				idEmpresa,
				competencias
		)
		String feedback = "Cadastro feito com sucesso!"
		try {
			companyRepository.addVaga(vaga)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

	private static List<Competencia> extractSkillsFromMap(Map<String, ?> dados){
		List<Competencia> competencias = new ArrayList<>()
		for (String competenciaStr : dados.get('competencias')){
			Competencia competencia = new Competencia(
					null,
					competenciaStr
			)
			competencias.add(competencia)
		}
		return competencias
	}

	private static List<Formacao> extractFormationsFromMap(Map<String, ?> data){
		List<Formacao> formations = []
		for (Map<String, ?> formationMap : data.get("formacoes") as List<Map<String, ?>>){
			Formacao formation = new Formacao(
					null,
					formationMap.get('instituicao') as String,
					formationMap.get('nome') as String,
					formationMap.get('dataIncio') as LocalDate,
					formationMap.get('dataFim') as LocalDate
			)
			formations.add(formation)
		}
		return formations
	}
}