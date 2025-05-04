package org.linketinder.service

import org.linketinder.dao.impl.CompanyDao
import org.linketinder.dao.interfaces.ICRUD
import org.linketinder.model.Address
import org.linketinder.model.Company
import org.linketinder.model.Country
import org.linketinder.model.Job

class CompanyService {
	ICRUD<Company> companyDao

	CompanyService(ICRUD<Company> companyDao) {
		this.companyDao = companyDao
	}

	List<Company> listAllCompanies() {
		List<Map<String, Object>> rawCompanies = companyDao.getAll()
		return setupCompaniesToController(rawCompanies)
	}

	Company registerCompany(Map<String, String> data) {
		Company newCompany = setupCompanyToDao(data)
		try {
			return companyDao.save(newCompany)
		} catch (Exception e){
			throw e
		}
	}

	boolean removeCompany(Integer id){
		return companyDao.deleteById(id)
	}

	private static List<Company> setupCompaniesToController(List<Map<String, Object>> rawCompanies){
		return rawCompanies.collect {Map<String, Object> row ->
			Integer id_company = row['empresa_id'] as Integer
			List<Job> jobs = JobService.extractJobsData(row['vagas'].toString(), id_company)
			Address address = new Address(
					row['endereco_id'] as Integer,
					row['endereco_cep'] as String,
					new Country(row['pais_nome'] as String, row['pais_id'] as Integer)
			)
			new Company(
					id_company,
					row['empresa_nome'] as String,
					row['empresa_email'] as String,
					row['empresa_cnpj'] as String,
					address,
					row['empresa_descricao'] as String,
					row['empresa_senha'] as String,
					jobs
			)
		}
	}

	static private Company setupCompanyToDao(Map<String, String> data) {
		List<Job> jobs = []
		Address address = new Address(null, data.zipCode, new Country(data.country, null))
		return new Company(
				null, data.name, data.email, data.cnpj,
				address, data.description, data.password, jobs
		)
	}
}