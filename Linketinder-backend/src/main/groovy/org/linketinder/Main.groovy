package org.linketinder

import org.linketinder.controller.MainController
import org.linketinder.repository.CandidateRepository
import org.linketinder.repository.SkillRepository
import org.linketinder.repository.EmailRepository
import org.linketinder.repository.CompanyRepository
import org.linketinder.repository.EnderecoRepository
import org.linketinder.repository.FormacaoRepository

import org.linketinder.repository.JobRepository
import org.linketinder.service.ServiceManager
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.repository.DatabaseConnection

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		EmailRepository emailRepository = new EmailRepository(sql)
		SkillRepository skillRepository = new SkillRepository(sql)
		JobRepository jobRepository = new JobRepository(sql, skillRepository)
		FormacaoRepository formacaoRepository = new FormacaoRepository(sql)
		EnderecoRepository enderecoRepository = new EnderecoRepository(sql)
		CompanyRepository empresaRepository = new CompanyRepository(sql, enderecoRepository, jobRepository)
		CandidateRepository candidatoRepository = new CandidateRepository(sql, enderecoRepository, skillRepository, formacaoRepository, emailRepository)
		ServiceManager gestaoService = new ServiceManager(empresaRepository, candidatoRepository, jobRepository, skillRepository)
		MenuView view = new MenuView()

		MainController controller = new MainController(view, gestaoService)
		controller.executar()
	}
}