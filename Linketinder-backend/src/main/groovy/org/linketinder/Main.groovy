package org.linketinder

import org.linketinder.controller.MainController
import org.linketinder.dao.CandidateDao
import org.linketinder.dao.CountryDao
import org.linketinder.dao.SkillDao

import org.linketinder.dao.CompanyDao
import org.linketinder.dao.AddressDao
import org.linketinder.dao.FormationDao

import org.linketinder.dao.JobDao
import org.linketinder.service.ServiceManager

import groovy.sql.Sql
import org.linketinder.dao.connection.DatabaseConnection
import org.linketinder.view.ViewFacade

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		CountryDao countryDao = new CountryDao(sql)
		SkillDao skillDao = new SkillDao(sql)
		JobDao jobDao = new JobDao(sql, skillDao)
		FormationDao formationDao = new FormationDao(sql)
		AddressDao addressDao = new AddressDao(sql, countryDao)
		CompanyDao companyDao = new CompanyDao(sql, addressDao, jobDao)
		CandidateDao candidateDao = new CandidateDao(sql, addressDao, skillDao, formationDao)
		ServiceManager serviceManager = new ServiceManager(companyDao, candidateDao, jobDao, skillDao)
		ViewFacade view = new ViewFacade()

		MainController controller = new MainController(view, serviceManager)
		controller.executar()
	}
}