package org.linketinder

import org.linketinder.controller.CandidateController
import org.linketinder.controller.CompanyController
import org.linketinder.controller.JobController
import org.linketinder.controller.SkillController
import org.linketinder.dao.impl.CandidateDao
import org.linketinder.dao.impl.CandidateSkillDao
import org.linketinder.dao.impl.CountryDao
import org.linketinder.dao.impl.FormationCandidateDao
import org.linketinder.dao.impl.JobSkillDao
import org.linketinder.dao.impl.SkillDao

import org.linketinder.dao.impl.CompanyDao
import org.linketinder.dao.impl.AddressDao
import org.linketinder.dao.impl.FormationDao

import org.linketinder.dao.impl.JobDao
import org.linketinder.service.CandidateService
import groovy.sql.Sql
import org.linketinder.dao.connection.DatabaseConnection
import org.linketinder.service.CompanyService
import org.linketinder.service.JobService
import org.linketinder.service.SkillService
import org.linketinder.view.ViewFacade

class Main {
	static void main(String[] args) {
		Sql sql = DatabaseConnection.getInstance()
		CountryDao countryDao = new CountryDao(sql)
		SkillDao skillDao = new SkillDao(sql)
		JobSkillDao jobSkillDao = new JobSkillDao(sql)
		JobDao jobDao = new JobDao(sql, skillDao, jobSkillDao)
		FormationDao formationDao = new FormationDao(sql)
		FormationCandidateDao formationCandidateDao = new FormationCandidateDao(sql)
		CandidateSkillDao candidateSkillDao = new CandidateSkillDao(sql)
		AddressDao addressDao = new AddressDao(sql, countryDao)
		CompanyDao companyDao = new CompanyDao(sql, addressDao, jobDao)
		CandidateDao candidateDao = new CandidateDao(sql, addressDao, formationDao, skillDao, formationCandidateDao, candidateSkillDao)
		ViewFacade view = new ViewFacade()
		CandidateService candidateService = new CandidateService(candidateDao)
		JobService jobService = new JobService(jobDao)
		SkillService skillService = new SkillService(skillDao)
		CandidateController candidateController = new CandidateController(candidateService)
		CompanyService companyService = new CompanyService(companyDao)
		CompanyController companyController = new CompanyController(companyService)
		JobController jobController = new JobController(jobService)
		SkillController skillController = new SkillController(skillService)

		System system = new System(view, candidateController, companyController, jobController, skillController)
		system.run()
	}
}