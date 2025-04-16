package org.linketinder.repository

import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.model.Address
import org.linketinder.model.Country
import org.linketinder.model.Company
import org.linketinder.model.Job

import java.sql.SQLException

class CompanyRepository {
	Sql sql
	AddressRepository addressRepository
	JobRepository jobRepository

	CompanyRepository(Sql sql, AddressRepository addressRepository, JobRepository jobRepository) {
		this.sql = sql
		this.addressRepository = addressRepository
		this.jobRepository = jobRepository
	}

	List<Company> getEmpresas() {
		List<Company> empresas = []
		String query = selectAllFromCompanies()
		try {
			sql.eachRow(query) { GroovyResultSet row ->
				Integer id_company = row.empresa_id as Integer
				List<Job> jobs = jobRepository.extractJobsData(row.vagas.toString(), id_company)
				Address address = new Address(
						row.endereco_id as Integer,
						row.endereco_cep as String,
						new Country(row.pais_nome as String, row.pais_id as Integer)
				)
				Company empresa = new Company(
						id_company,
						row.empresa_nome as String,
						row.empresa_email as String,
						row.empresa_cnpj as String,
						address,
						row.empresa_descricao as String,
						row.empresa_senha as String,
						jobs
				)
				empresas.add(empresa)
			}
		} catch (Exception e) {
			println("Erro ao buscar empresas: ${e.message}")
			e.printStackTrace()
			return []
		}
		return empresas
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

	void addCompanyData(Company company) {
		try {
			Integer countryId = addressRepository.getIdCountry(company.address.country.name)
			countryId = !countryId ? addressRepository.insertCountry(company.address.country.name) : null
			Integer addressId = addressRepository.insertAddress(company.address.zipCode, countryId)
			insertCompany(company, addressId)
		} catch (Exception e) {
			e.printStackTrace()
			throw new Exception("Erro ao inserir empresa no Banco de dados")
		}
	}

	void insertCompany(Company company, Integer addressId) {
		try {
			sql.executeInsert("""
				INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco)
				VALUES (?, ?, ?, ?, ?, ?)
			""", [
					company.name,
					company.cnpj,
					company.email,
					company.description,
					company.passwordLogin,
					addressId
			])
		} catch (Exception e){
			throw new Exception(e.message)
		}
	}

	boolean removeCompanyById(Integer id){
		try {
			return sql.executeUpdate("DELETE FROM empresa WHERE id = ?", [id]) > 0
		} catch (SQLException e) {
			e.printStackTrace()
		}
		return false
	}
}