package org.linketinder.config

import groovy.sql.Sql
import org.linketinder.controller.CandidateController
import org.linketinder.controller.CompanyController
import org.linketinder.controller.JobController
import org.linketinder.controller.SkillController
import org.linketinder.dao.connection.DatabaseConnection
import org.linketinder.dao.impl.*
import org.linketinder.dao.interfaces.*
import org.linketinder.model.Candidate
import org.linketinder.model.Company
import org.linketinder.model.Job
import org.linketinder.service.CandidateService
import org.linketinder.service.CompanyService
import org.linketinder.service.JobService
import org.linketinder.service.SkillService
import org.linketinder.view.ViewFacade

class DependencyFactory {
	static DependencyFactory instance

	Sql sql

	CountryDao countryDao
	ISkillDao skillDao
	IAssociateEntity jobSkillDao
	ICRUD<Job> jobDao
	FormationDao formationDao
	FormationCandidateDao formationCandidateDao
	IAssociateEntity candidateSkillDao
	AddressDao addressDao
	ICRUD<Company> companyDao
	ICRUD<Candidate> candidateDao

	CompanyService companyService
	JobService jobService
	SkillService skillService
	CandidateService candidateService

	CandidateController candidateController
	CompanyController companyController
	JobController jobController
	SkillController skillController

	ViewFacade view

	private DependencyFactory() {
		initializeDependencies()
	}

	static synchronized DependencyFactory getInstance() {
		if (instance == null) {
			instance = new DependencyFactory()
		}
		return instance
	}

	private void initializeDependencies() {
		sql = DatabaseConnection.getInstance()
		initializeDAOs()
		initializeServices()
		initializeControllers()
		view = new ViewFacade()
	}

	private void initializeDAOs() {
		countryDao = new CountryDao(sql)
		skillDao = new SkillDao(sql)
		addressDao = new AddressDao(sql, countryDao)
		jobSkillDao = new JobSkillDao(sql)
		jobDao = new JobDao(sql, skillDao, jobSkillDao)
		formationDao = new FormationDao(sql)
		formationCandidateDao = new FormationCandidateDao(sql)
		candidateSkillDao = new CandidateSkillDao(sql)
		companyDao = new CompanyDao(sql, addressDao)
		candidateDao = new CandidateDao(sql, addressDao, formationDao, skillDao, formationCandidateDao, candidateSkillDao)
	}


	private void initializeServices() {
		candidateService = new CandidateService(candidateDao)
		companyService = new CompanyService(companyDao)
		jobService = new JobService(jobDao)
		skillService = new SkillService(skillDao)
	}

	private void initializeControllers() {
		candidateController = new CandidateController(candidateService)
		companyController = new CompanyController(companyService, jobService)
		jobController = new JobController(jobService)
		skillController = new SkillController(skillService)
	}
}