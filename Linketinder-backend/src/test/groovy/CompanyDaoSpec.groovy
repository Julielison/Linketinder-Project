import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.AddressDao
import org.linketinder.dao.CompanyDao
import org.linketinder.dao.JobDao
import org.linketinder.model.Address
import org.linketinder.model.Company
import org.linketinder.model.Country
import org.linketinder.model.Job
import spock.lang.Specification

import java.sql.SQLException

class CompanyDaoSpec extends Specification {

	Sql sql
	AddressDao addressDao
	JobDao jobDao
	CompanyDao companyDao

	def setup() {
		sql = Mock(Sql)
		addressDao = Mock(AddressDao)
		jobDao = Mock(JobDao)
		companyDao = new CompanyDao(sql, addressDao, jobDao)
	}

	def "deve retornar empresas corretamente"() {
		given: "um resultado de consulta SQL simulando uma empresa"
		def country = new Country("Brasil", 1)
		def address = new Address(1, "30123456", country)
		def job = new Job(10, "Dev", "Desenvolvedor", "Remoto", 1, [])

		GroovyResultSet row = Mock() {
			getAt('empresa_id') >> 1
			getAt('empresa_nome') >> "Empresa X"
			getAt('empresa_email') >> "contato@empresax.com"
			getAt('empresa_cnpj') >> "12345678000100"
			getAt('empresa_descricao') >> "Empresa de tecnologia"
			getAt('empresa_senha') >> "senha123"
			getAt('endereco_id') >> 1
			getAt('endereco_cep') >> "30123456"
			getAt('pais_nome') >> "Brasil"
			getAt('pais_id') >> 1
			getAt('vagas') >> "10:Dev:Desenvolvedor:Remoto:1.Java,2.Python"
		}

		sql.eachRow(_, _) >> { String query, Closure c -> c.call(row) }
		jobDao.extractJobsData("10:Dev:Desenvolvedor:Remoto:1.Java,2.Python", 1) >> [job]

		when: "busca as empresas"
		def empresas = companyDao.getEmpresas()

		then: "verifica se os dados foram retornados corretamente"
		empresas.size() == 1
		with(empresas[0]) {
			id == 1
			name == "Empresa X"
			email == "contato@empresax.com"
			cnpj == "12345678000100"
			description == "Empresa de tecnologia"
			passwordLogin == "senha123"
			address.id == 1
			address.zipCode == "30123456"
			address.country.name == "Brasil"
			address.country.id == 1
			jobs.size() == 1
			jobs[0].name == "Dev"
		}
	}

	def "deve retornar lista vazia e imprimir erro ao ocorrer exceção em getEmpresas"() {
		given:
		sql.eachRow(_, _) >> { throw new SQLException("Erro ao consultar") }

		when:
		def empresas = companyDao.getEmpresas()

		then:
		empresas == []
	}

	def "deve inserir empresa corretamente"() {
		given:
		def company = new Company(
				null, "Empresa Z", "contato@empresaz.com", "55544433322211",
				null, "Empresa exemplo", "senha", []
		)

		when:
		companyDao.insertCompany(company, 100)

		then:
		1 * sql.executeInsert(_, _) >> [[1]]
	}

	def "deve lançar exceção ao inserir empresa com erro"() {
		given:
		def company = new Company(
				null, "Empresa Z", "contato@empresaz.com", "55544433322211",
				null, "Empresa exemplo", "senha", []
		)
		sql.executeInsert(_, _) >> { throw new SQLException("Erro ao inserir") }

		when:
		companyDao.insertCompany(company, 100)

		then:
		thrown(Exception)
	}

	def "deve remover empresa corretamente e retornar true"() {
		when:
		def result = companyDao.removeCompanyById(1)

		then:
		1 * sql.executeUpdate("DELETE FROM empresa WHERE id = ?", [1]) >> 1
		result
	}


	def "deve retornar false ao tentar remover empresa inexistente"() {
		given:
		sql.executeUpdate(_, _) >> 0

		when:
		def result = companyDao.removeCompanyById(123)

		then:
		!result
	}

	def "deve retornar false e imprimir erro ao remover empresa com exceção"() {
		given:
		sql.executeUpdate(_, _) >> { throw new SQLException("Erro ao remover") }

		when:
		def result = companyDao.removeCompanyById(1)

		then:
		!result
	}
}