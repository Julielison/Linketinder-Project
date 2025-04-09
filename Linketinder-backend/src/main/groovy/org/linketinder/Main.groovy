package org.linketinder

import org.linketinder.controller.MainController
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.CompetenciaRepository
import org.linketinder.repository.EmailRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.repository.EnderecoRepository
import org.linketinder.repository.FormacaoRepository
import org.linketinder.repository.Repository
import org.linketinder.repository.JobRepository
import org.linketinder.service.GestaoService
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.repository.DatabaseConnection

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		EmailRepository emailRepository = new EmailRepository(sql)
		CompetenciaRepository competenciaRepository = new CompetenciaRepository(sql)
		JobRepository jobRepository = new JobRepository(sql, competenciaRepository)
		FormacaoRepository formacaoRepository = new FormacaoRepository(sql)
		EnderecoRepository enderecoRepository = new EnderecoRepository(sql)
		Repository repository = new Repository(sql)
		EmpresaRepository empresaRepository = new EmpresaRepository(sql, enderecoRepository, jobRepository)
		CandidatoRepository candidatoRepository = new CandidatoRepository(sql, enderecoRepository, competenciaRepository, formacaoRepository, emailRepository)
		GestaoService gestaoService = new GestaoService(empresaRepository, candidatoRepository, repository, jobRepository)
		MenuView view = new MenuView()

		MainController controller = new MainController(view, gestaoService)
		controller.executar()
	}
}