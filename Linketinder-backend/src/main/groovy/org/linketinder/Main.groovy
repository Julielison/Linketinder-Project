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
import org.linketinder.dao.interfaces.IAddressDao
import org.linketinder.dao.interfaces.ICandidateDao
import org.linketinder.dao.interfaces.ICandidateSkillDao
import org.linketinder.dao.interfaces.ICompanyDao
import org.linketinder.dao.interfaces.ICountryDao
import org.linketinder.dao.interfaces.IFormationCandidateDao
import org.linketinder.dao.interfaces.IFormationDao
import org.linketinder.dao.interfaces.IJobDao
import org.linketinder.dao.interfaces.IJobSkillDao
import org.linketinder.dao.interfaces.ISkillDao
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
		ICountryDao countryDao = new CountryDao(sql)
		ISkillDao skillDao = new SkillDao(sql)
		IJobSkillDao jobSkillDao = new JobSkillDao(sql)
		IJobDao jobDao = new JobDao(sql, skillDao, jobSkillDao)
		IFormationDao formationDao = new FormationDao(sql)
		IFormationCandidateDao formationCandidateDao = new FormationCandidateDao(sql)
		ICandidateSkillDao candidateSkillDao = new CandidateSkillDao(sql)
		IAddressDao addressDao = new AddressDao(sql, countryDao)
		ICompanyDao companyDao = new CompanyDao(sql, addressDao, jobDao)
		ICandidateDao candidateDao = new CandidateDao(sql, addressDao, formationDao, skillDao, formationCandidateDao, candidateSkillDao)
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