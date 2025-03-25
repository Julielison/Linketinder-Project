package org.linketinder.controller

import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.service.CadastroService
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.repository.DatabaseConnection

class MainController {
    MenuView view = new MenuView()
    CadastroService cadastroService

    MainController() {
        Sql sql = DatabaseConnection.getInstance()
        EmpresaRepository empresaRepository = new EmpresaRepository(sql)
        CandidatoRepository candidatoRepository = new CandidatoRepository(sql)
        cadastroService = new CadastroService(empresaRepository, candidatoRepository)
    }

    void executar() {
        boolean sair = false
        while (!sair) {
            view.showMenu()
            int opcao = view.getUserInput()
            switch(opcao) {
                case 1:
                    view.showPessoas(cadastroService.listarCandidatos(), 'candidatos')
                    break
                case 2:
                    view.showPessoas(cadastroService.listarEmpresas(), 'empresas')
                    break
                case 3:
                    def dadosEmpresa = MenuView.getEmpresaInput()
                    String feedback = cadastroService.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedbackInsercao(feedback)
                    break
                case 4:
                    def dadosCandidato = MenuView.getCandidatoInput()
                    String feedback = cadastroService.cadastrarCandidato(dadosCandidato)
                    view.showFeedbackInsercao(feedback)
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