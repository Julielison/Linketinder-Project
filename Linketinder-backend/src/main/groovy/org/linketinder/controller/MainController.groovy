package org.linketinder.controller

import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.CompetenciaRepository
import org.linketinder.repository.EmailRepository
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
        EmailRepository emailRepository = new EmailRepository(sql)
        CompetenciaRepository competenciaRepository = new CompetenciaRepository(sql)
        FormacaoRepository formacaoRepository = new FormacaoRepository(sql)
        EnderecoRepository enderecoRepository = new EnderecoRepository(sql)
        EmpresaRepository empresaRepository = new EmpresaRepository(sql, enderecoRepository, vagaRepository)
        CandidatoRepository candidatoRepository = new CandidatoRepository(sql, enderecoRepository, competenciaRepository, formacaoRepository, emailRepository)
        this.gestaoService = new GestaoService(empresaRepository, candidatoRepository)
    }

    void executar() {
        boolean sair = false
        String feedback
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
                    Map<String, ?> dadosEmpresa = view.getEmpresaInput()
                    feedback = gestaoService.cadastrarEmpresa(dadosEmpresa)
                    view.showFeedbackInsercao(feedback)
                    break
                case "6":
                    Integer idEmprsa = view.getIdEmpresaInput()
                    feedback = gestaoService.removerEmpresa(idEmprsa)
                    view.showFeedbackInsercao(feedback)
                    break
                case "7":
                    Map<String, ?> dadosCandidato = view.getCandidatoInput()
                    feedback = gestaoService.cadastrarCandidato(dadosCandidato)
                    view.showFeedbackInsercao(feedback)
                    break
                case "11":
                    view.showVagas(gestaoService.listarVagas())
                    break
                case "12":
                    view.showCompetencias(gestaoService.listarCompetencias())
                    break
                case '0':
                    view.showExitMessage()
                    sair = true
                    break
                default:
                    view.showInvalidOption()
            }
        }
    }
}