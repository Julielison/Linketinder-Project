package org.linketinder.controller


import org.linketinder.model.Company
import org.linketinder.service.CompanyService

class CompanyController {
	CompanyService companyService

	CompanyController(CompanyService companyService) {
		this.companyService = companyService
	}

	List<Company> getAllCompanies(){
		return companyService.listAllCompanies()
	}

	String createCompany(Map<String, String> data){
		return companyService.registerCompany(data)
	}

	String deleteCompanyById(Integer id) {
		return companyService.removeCompany(id)
	}
}
