package org.linketinder.dao.impl

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.interfaces.ICompanyDao
import org.linketinder.model.Address
import org.linketinder.model.Country
import org.linketinder.model.Company
import org.linketinder.model.Job
import org.linketinder.model.Person

import java.sql.SQLException

class CompanyDao implements ICompanyDao {
	Sql sql
	AddressDao addressDao
	JobDao jobDao

	CompanyDao(Sql sql, AddressDao addressDao, JobDao jobDao) {
		this.sql = sql
		this.addressDao = addressDao
		this.jobDao = jobDao
	}

	List<Map<String, Object>> getCompaniesRawData() {
		List<Map<String, Object>> companies = []
		String query = selectAllFromCompanies()
		try {
			sql.eachRow(query) { GroovyResultSet row ->
				companies.add(row.toRowResult())
			}
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return companies
	}

	static String selectAllFromCompanies(){
		return """
		SELECT 
			e.id AS empresa_id,
			e.nome AS empresa_nome,
			e.email_corporativo AS empresa_email,
			e.cnpj AS empresa_cnpj,
			e.descricao_da_empresa AS empresa_descricao,
			e.senha_de_login AS empresa_senha,
			endereco.cep AS endereco_cep,
			p.nome AS pais_nome,
			endereco.id AS endereco_id,
			p.id AS pais_id,
			COALESCE(STRING_AGG(DISTINCT (
				v.id || ':' || 
				v.nome || ':' || 
				v.descricao || ':' || 
				v.local || ':' || 
				COALESCE((
					SELECT STRING_AGG(c.id || '.' || c.nome, ',')
					FROM VAGA_COMPETENCIA vc2
					JOIN COMPETENCIA c ON c.id = vc2.id_competencia
					WHERE vc2.id_vaga = v.id
				), '')
			), ';'), '') AS vagas
		FROM 
			EMPRESA e
		JOIN 
			ENDERECO endereco ON e.id_endereco = endereco.id
		JOIN 
			PAIS_DE_RESIDENCIA p ON endereco.pais_id = p.id
		LEFT JOIN 
			VAGA v ON e.id = v.id_empresa
		GROUP BY 
			e.id, e.nome, e.email_corporativo, e.cnpj, e.descricao_da_empresa, 
			e.senha_de_login, endereco.cep, p.nome, endereco.id, p.id"""
	}

	void addAllCompanyData(Person company) {
		sql.withTransaction {
			try {
				Integer countryId = addressDao.insertCountryReturningId(company.address.country.name)
				Integer addressId = addressDao.insertAddressReturningId(company.address.zipCode, countryId)
				Integer companyId = this.insertCompany(company, addressId)
				company.setId(companyId)

				if (company instanceof Company && !company.jobs.isEmpty()) {
					jobDao.insertJobsForCompany(company)
				}
			} catch (Exception e) {
				e.printStackTrace()
				throw new RuntimeException("Erro ao adicionar dados da empresa: ${e.message}")
			}
		}
	}

	Integer insertCompany(Person company, Integer addressId) {
		List<List<Object>> result = sql.executeInsert("""
			INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco)
			VALUES (?, ?, ?, ?, ?, ?)
		""", [
				company.name,
				company instanceof Company ? company.cnpj : "",
				company.email,
				company.description,
				company.passwordLogin,
				addressId
		])
		return result[0][0] as Integer
	}

	boolean deleteCompanyById(Integer id){
		try {
			return sql.executeUpdate("DELETE FROM empresa WHERE id = ?", [id]) > 0
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return false
	}
}