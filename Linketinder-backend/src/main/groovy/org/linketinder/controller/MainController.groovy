package org.linketinder.controller

import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.service.CadastroService
import org.linketinder.view.MenuView


class MainController {
    MenuView view = new MenuView()

    void executar() {
        boolean sair = false
        while (!sair) {
            view.showMenu()
            int opcao = view.getUserInput()
            switch(opcao) {
                case 1:
                    view.showPessoas(CandidatoRepository.getCandidatos(), 'candidados')
                    break
                case 2:
                    view.showPessoas(EmpresaRepository.getEmpresas(), 'empresas')
                    break
                case 3:
                    def dadosEmpresa = MenuView.getEmpresaInput()
                    CadastroService.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedbackInsercao("E'mpresa")
                    break
                case 4:
                    def dadosCandidato = MenuView.getCandidatoInput()
                    CadastroService.cadastrarCandidato(dadosCandidato)
                    view.showFeedbackInsercao("Candidato")
                    break
                case 0:
                    view.showExitMessage()
                    sair = true
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}
