package org.linketinder.controller

import org.linketinder.enums.MenuOption
import org.linketinder.dao.connection.DatabaseConnection
import org.linketinder.service.ServiceManager
import org.linketinder.view.ViewFacade

class MainController {
    ViewFacade view
    ServiceManager serviceManager

    MainController(ViewFacade view, ServiceManager serviceManager) {
        this.view = view
        this.serviceManager = serviceManager
    }

    void executar() {
        boolean sair = false
        String feedback
        while (!sair) {
            view.showMenu()
            String option = view.getUserInput()
            MenuOption menu = MenuOption.fromValue(option)
            switch(menu) {
                case menu.LISTAR_CANDIDATOS:
                    view.showCandidates(serviceManager.listarCandidatos())
                    break
                case menu.CADASTRAR_CANDIDATO:
                    Map<String, ?> candidateData = view.getCandidateInput()
                    feedback = serviceManager.registerCandidate(candidateData)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_CANDIDATO:
                    Integer idCandidatoInput = view.getIdCandidateInput()
                    feedback = serviceManager.removeCandidate(idCandidatoInput)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_EMPRESAS:
                    view.showCompanies(serviceManager.listarEmpresas())
                    break
                case menu.CADASTRAR_EMPRESA:
                    Map<String, ?> companyData = view.getCompanyInput()
                    feedback = serviceManager.registerCompany(companyData)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_EMPRESA:
                    Integer idEmpresa = view.getIdCompanyInput()
                    feedback = serviceManager.removeCompany(idEmpresa)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_VAGAS:
                    view.showJobs(serviceManager.listJobs())
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
                    view.showSkills(serviceManager.listarCompetencias())
                    break
                case menu.REMOVER_COMPETENCIA:
                    Integer idCompetenciaInput = view.getIdSkillInput()
                    feedback = serviceManager.removeSkill(idCompetenciaInput)
                    view.showFeedback(feedback)
                    break
                case menu.SAIR:
                    view.showExitMessage()
                    sair = true
                    DatabaseConnection.getInstance().close()
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}