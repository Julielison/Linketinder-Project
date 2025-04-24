package org.linketinder

import org.linketinder.controller.CandidateController
import org.linketinder.controller.CompanyController
import org.linketinder.controller.JobController
import org.linketinder.controller.SkillController
import org.linketinder.dao.connection.DatabaseConnection
import org.linketinder.enums.MenuOption
import org.linketinder.model.Candidate
import org.linketinder.model.Company
import org.linketinder.model.Job
import org.linketinder.model.Skill
import org.linketinder.view.ViewFacade

class System {
	ViewFacade view
	CandidateController candidateController
	CompanyController companyController
	JobController jobController
	SkillController skillController

	System(ViewFacade view, CandidateController candidateController, CompanyController companyController,
		   JobController jobController, SkillController skillController) {
		this.view = view
		this.candidateController = candidateController
		this.companyController = companyController
		this.jobController = jobController
		this.skillController = skillController
	}

	void run(){
		boolean quit = false
		String feedback
		while (!quit) {
			view.showMenu()
			String option = view.getUserInput()
			MenuOption menu = MenuOption.fromValue(option)
			switch(menu) {
				case menu.LISTAR_CANDIDATOS:
					List<Candidate> candidates = candidateController.getAllCandidates()
					view.showCandidates(candidates)
					break
				case menu.CADASTRAR_CANDIDATO:
					Map<String, ?> candidateData = view.getCandidateInput()
					feedback = candidateController.createCandidate(candidateData)
					view.showFeedback(feedback)
					break
				case menu.REMOVER_CANDIDATO:
					Integer idCandidatoInput = view.getIdCandidateInput()
					feedback = candidateController.deleteCandidateById(idCandidatoInput)
					view.showFeedback(feedback)
					break
				case menu.LISTAR_EMPRESAS:
					List<Company> companies = companyController.getAllCompanies()
					view.showCompanies(companies)
					break
				case menu.CADASTRAR_EMPRESA:
					Map<String, String> companyData = view.getCompanyInput()
					feedback = companyController.createCompany(companyData)
					view.showFeedback(feedback)
					break
				case menu.REMOVER_EMPRESA:
					Integer idCompany = view.getIdCompanyInput()
					feedback = companyController.deleteCompanyById(idCompany)
					view.showFeedback(feedback)
					break
				case menu.LISTAR_VAGAS:
					List<Job> jobs = jobController.getAllJobs()
					view.showJobs(jobs)
					break
				case menu.CADASTRAR_VAGA:
					Integer idCompanyInput = view.getIdCompanyInput()
					Map<String, ?> dataJob = view.getDataJobInput()
					feedback = serviceManager.registerJob(idCompanyInput, dataJob)
					view.showFeedback(feedback)
					break
				case menu.REMOVER_VAGA:
					Integer idVagaInput = view.getIdJobInput()
					feedback = serviceManager.removeJob(idVagaInput)
					view.showFeedback(feedback)
					break
				case menu.LISTAR_COMPETENCIAS:
					List<Skill> skills = skillController.getAllSkills()
					view.showSkills(skills)
					break
				case menu.REMOVER_COMPETENCIA:
					Integer idCompetenciaInput = view.getIdSkillInput()
					feedback = serviceManager.removeSkill(idCompetenciaInput)
					view.showFeedback(feedback)
					break
				case menu.SAIR:
					view.showExitMessage()
					quit = true
					DatabaseConnection.getInstance().close()
					break
				default:
					view.showInvalidOption()
			}
		}
	}
}
