package org.linketinder.dao.interfaces


import org.linketinder.model.Company
import org.linketinder.model.Person

interface ICompanyDao {
	List<Company> getCompaniesRawData()
	Integer insertCompany(Company company, Integer addressId)
	boolean deleteCompanyById(Integer id)
}