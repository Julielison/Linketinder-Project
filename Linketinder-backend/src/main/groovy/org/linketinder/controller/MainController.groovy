package org.linketinder.controller

import org.linketinder.enums.MenuOption
import org.linketinder.repository.DatabaseConnection
import org.linketinder.service.ServiceManager
import org.linketinder.view.MenuView

class MainController {
    MenuView view
    ServiceManager serviceManager

    MainController(MenuView view, ServiceManager serviceManager) {
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
                    view.showPessoas(serviceManager.listarCandidatos(), 'candidatos')
                    break
                case menu.CADASTRAR_CANDIDATO:
                    Map<String, ?> candidateData = view.getCandidateInput()
                    feedback = serviceManager.registerCandidate(candidateData)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_CANDIDATO:
                    Integer idCandidatoInput = view.getIdCandidatoInput()
                    feedback = serviceManager.removeCandidate(idCandidatoInput)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_EMPRESAS:
                    view.showPessoas(serviceManager.listarEmpresas(), 'empresas')
                    break
                case menu.CADASTRAR_EMPRESA:
                    Map<String, ?> dadosEmpresa = view.getEmpresaInput()
                    feedback = serviceManager.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_EMPRESA:
                    Integer idEmpresa = view.getIdEmpresaInput()
                    feedback = serviceManager.removeCompany(idEmpresa)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_VAGAS:
                    view.showVagas(serviceManager.listarVagas())
                    break
                case menu.CADASTRAR_VAGA:
                    Integer idEmpresaInput = view.getIdEmpresaInput()
                    Map<String, ?> dadosVaga = view.getDadosVagaInput()
                    feedback = serviceManager.cadastrarVaga(idEmpresaInput, dadosVaga)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_VAGA:
                    Integer idVagaInput = view.getIdVagaInput()
                    feedback = serviceManager.removeJob(idVagaInput)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_COMPETENCIAS:
                    view.showCompetencias(serviceManager.listarCompetencias())
                    break
                case menu.REMOVER_COMPETENCIA:
                    Integer idCompetenciaInput = view.getIdCompetenciaInput()
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