package org.linketinder

import org.linketinder.controller.MainController
import org.linketinder.dao.CandidateDao
import org.linketinder.dao.SkillDao

import org.linketinder.dao.CompanyDao
import org.linketinder.dao.AddressDao
import org.linketinder.dao.FormationDao

import org.linketinder.dao.JobDao
import org.linketinder.service.ServiceManager
import org.linketinder.view.MenuView
import groovy.sql.Sql
import org.linketinder.dao.DatabaseConnection

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		SkillDao skillRepository = new SkillDao(sql)
		JobDao jobRepository = new JobDao(sql, skillRepository)
		FormationDao formacaoRepository = new FormationDao(sql)
		AddressDao enderecoRepository = new AddressDao(sql)
		CompanyDao empresaRepository = new CompanyDao(sql, enderecoRepository, jobRepository)
		CandidateDao candidatoRepository = new CandidateDao(sql, enderecoRepository, skillRepository, formacaoRepository)
		ServiceManager gestaoService = new ServiceManager(empresaRepository, candidatoRepository, jobRepository, skillRepository)
		MenuView view = new MenuView()

		MainController controller = new MainController(view, gestaoService)
		controller.executar()
	}
}