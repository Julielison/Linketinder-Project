package org.linketinder.service

import org.linketinder.dao.impl.CompanyDao
import org.linketinder.model.Address
import org.linketinder.model.Company
import org.linketinder.model.Country
import org.linketinder.model.Job

class CompanyService {
	CompanyDao companyDao

	CompanyService(CompanyDao companyDao) {
		this.companyDao = companyDao
	}

	List<Company> listAllCompanies() {
		List<Map<String, Object>> rawCompanies = companyDao.getCompaniesRawData()
		return setupCompaniesToController(rawCompanies)
	}

	String registerCompany(Map<String, String> data) {
		Company newCompany = setupCompanyToDao(data)
		String feedback = "Empresa cadastrada com sucesso!"
		try {
			companyDao.addAllCompanyData(newCompany)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
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

	String removeCompany(Integer id){
		return companyDao.deleteCompanyById(id) ? "Empresa removida com sucesso!" : "Empresa não existe!"
	}
}