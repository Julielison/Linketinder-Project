package org.linketinder.controller

import org.linketinder.enums.MenuOption
import org.linketinder.service.GestaoService
import org.linketinder.view.MenuView

class MainController {
    MenuView view
    GestaoService gestaoService

    MainController(MenuView view, GestaoService gestaoService) {
        this.view = view
        this.gestaoService = gestaoService
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
                    view.showPessoas(gestaoService.listarCandidatos(), 'candidatos')
                    break
                case menu.CADASTRAR_CANDIDATO:
                    Map<String, ?> dadosCandidato = view.getCandidatoInput()
                    feedback = gestaoService.cadastrarCandidato(dadosCandidato)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_CANDIDATO:
                    Integer idCandidatoInput = view.getIdCandidatoInput()
                    feedback = gestaoService.removerCandidato(idCandidatoInput)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_EMPRESAS:
                    view.showPessoas(gestaoService.listarEmpresas(), 'empresas')
                    break
                case menu.CADASTRAR_EMPRESA:
                    Map<String, ?> dadosEmpresa = view.getEmpresaInput()
                    feedback = gestaoService.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_EMPRESA:
                    Integer idEmpresa = view.getIdEmpresaInput()
                    feedback = gestaoService.removerEmpresa(idEmpresa)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_VAGAS:
                    view.showVagas(gestaoService.listarVagas())
                    break
                case menu.CADASTRAR_VAGA:
                    Integer idEmpresaInput = view.getIdEmpresaInput()
                    Map<String, ?> dadosVaga = view.getDadosVagaInput()
                    feedback = gestaoService.cadastrarVaga(idEmpresaInput, dadosVaga)
                    view.showFeedback(feedback)
                    break
                case menu.REMOVER_VAGA:
                    Integer idVagaInput = view.getIdVagaInput()
                    feedback = gestaoService.removerVaga(idVagaInput)
                    view.showFeedback(feedback)
                    break
                case menu.LISTAR_COMPETENCIAS:
                    view.showCompetencias(gestaoService.listarCompetencias())
                    break
                case menu.REMOVER_COMPETENCIA:
                    Integer idCompetenciaInput = view.getIdCompetenciaInput()
                    feedback = gestaoService.removerCompetencia(idCompetenciaInput)
                    view.showFeedback(feedback)
                    break
                case menu.SAIR:
                    view.showExitMessage()
                    sair = true
                    gestaoService.empresaRepository.sql.close()
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}