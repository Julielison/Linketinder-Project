package org.linketinder.controller

import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.CompetenciaRepository
import org.linketinder.repository.EmailRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.repository.EnderecoRepository
import org.linketinder.repository.FormacaoRepository
import org.linketinder.repository.Repository
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
        EmailRepository emailRepository = new EmailRepository(sql)
        CompetenciaRepository competenciaRepository = new CompetenciaRepository(sql)
        VagaRepository vagaRepository = new VagaRepository(sql, competenciaRepository)
        FormacaoRepository formacaoRepository = new FormacaoRepository(sql)
        EnderecoRepository enderecoRepository = new EnderecoRepository(sql)
        Repository repository = new Repository(sql)
        EmpresaRepository empresaRepository = new EmpresaRepository(sql, enderecoRepository, vagaRepository)
        CandidatoRepository candidatoRepository = new CandidatoRepository(sql, enderecoRepository, competenciaRepository, formacaoRepository, emailRepository)
        this.gestaoService = new GestaoService(empresaRepository, candidatoRepository, repository)
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
                    view.showFeedback(feedback)
                    break
                case "6":
                    Integer idEmprsa = view.getIdEmpresaInput()
                    feedback = gestaoService.removerEmpresa(idEmprsa)
                    view.showFeedback(feedback)
                    break
                case "7":
                    Map<String, ?> dadosCandidato = view.getCandidatoInput()
                    feedback = gestaoService.cadastrarCandidato(dadosCandidato)
                    view.showFeedback(feedback)
                    break
                case "10":
                    Integer idCandidatoInput = view.getIdCandidatoInput()
                    feedback = gestaoService.removerCandidato(idCandidatoInput)
                    view.showFeedback(feedback)
                    break
                case "11":
                    view.showVagas(gestaoService.listarVagas())
                    break
                case "12":
                    view.showCompetencias(gestaoService.listarCompetencias())
                    break
                case "13":
                    Integer idEmpresaInput = view.getIdEmpresaInput()
                    Map<String, ?> dadosVaga = view.getDadosVagaInput()
                    feedback = gestaoService.cadastrarVaga(idEmpresaInput, dadosVaga)
                    view.showFeedback(feedback)
                    break
                case "14":
                    Integer idVagaInput = view.getIdVagaInput()
                    feedback = gestaoService.removerVaga(idVagaInput)
                    view.showFeedback(feedback)
                    break
                case "18":
                    Integer idCompetenciaInput = view.getIdCompetenciaInput()
                    feedback = gestaoService.removerCompetencia(idCompetenciaInput)
                    view.showFeedback(feedback)
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