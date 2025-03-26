package org.linketinder.controller

import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.CompetenciaRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.repository.EnderecoRepository
import org.linketinder.repository.FormacaoRepository
import org.linketinder.repository.VagaRepository
import org.linketinder.service.GestaoService
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.repository.DatabaseConnection

class MainController {
    MenuView view = new MenuView()
    GestaoService gestaoService

    MainController() {
        Sql sql = DatabaseConnection.getInstance()
        VagaRepository vagaRepository = new VagaRepository(sql)
        CompetenciaRepository competenciaRepository = new CompetenciaRepository(sql)
        FormacaoRepository formacaoRepository = new FormacaoRepository(sql)
        EnderecoRepository enderecoRepository = new EnderecoRepository()
        EmpresaRepository empresaRepository = new EmpresaRepository(sql, enderecoRepository, vagaRepository)
        CandidatoRepository candidatoRepository = new CandidatoRepository(sql, enderecoRepository, competenciaRepository, formacaoRepository)
        gestaoService = new GestaoService(empresaRepository, candidatoRepository)
    }

    void executar() {
        boolean sair = false
        while (!sair) {
            view.showMenu()
            String opcao = view.getUserInput()
            switch(opcao) {
                case "1":
                    view.showPessoas(gestaoService.listarCandidatos(), 'candidatos')
                    break
                case "2":
                    view.showPessoas(gestaoService.listarEmpresas(), 'empresas')
                    break
                case "3":
                    def dadosEmpresa = MenuView.getEmpresaInput()
                    String feedback = gestaoService.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedbackInsercao(feedback)
                    break
                case "4":
                    def dadosCandidato = MenuView.getCandidatoInput()
                    String feedback = gestaoService.cadastrarCandidato(dadosCandidato)
                    view.showFeedbackInsercao(feedback)
                    break
                case '0':
                    view.showExitMessage()
                    sair = true
                    break
                case "11":
                    view.showVagas(gestaoService.listarVagas())
                    break
                case "12":
                    view.showCompetencias(gestaoService.listarCompetencias())
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}