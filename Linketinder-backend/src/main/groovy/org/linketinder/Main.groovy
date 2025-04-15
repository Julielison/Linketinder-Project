package org.linketinder

import org.linketinder.controller.MainController
import org.linketinder.repository.CandidateRepository
import org.linketinder.repository.SkillRepository

import org.linketinder.repository.CompanyRepository
import org.linketinder.repository.AddressRepository
import org.linketinder.repository.FormationRepository

import org.linketinder.repository.JobRepository
import org.linketinder.service.ServiceManager
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.repository.DatabaseConnection

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		SkillRepository skillRepository = new SkillRepository(sql)
		JobRepository jobRepository = new JobRepository(sql, skillRepository)
		FormationRepository formacaoRepository = new FormationRepository(sql)
		AddressRepository enderecoRepository = new AddressRepository(sql)
		CompanyRepository empresaRepository = new CompanyRepository(sql, enderecoRepository, jobRepository)
		CandidateRepository candidatoRepository = new CandidateRepository(sql, enderecoRepository, skillRepository, formacaoRepository)
		ServiceManager gestaoService = new ServiceManager(empresaRepository, candidatoRepository, jobRepository, skillRepository)
		MenuView view = new MenuView()

		MainController controller = new MainController(view, gestaoService)
		controller.executar()
	}
}