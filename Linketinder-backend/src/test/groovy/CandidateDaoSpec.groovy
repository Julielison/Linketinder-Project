import groovy.sql.GroovyResultSet
import groovy.sql.Sql
import org.linketinder.dao.impl.AddressDao
import org.linketinder.dao.impl.CandidateDao
import org.linketinder.dao.impl.FormationDao
import org.linketinder.dao.impl.SkillDao
import org.linketinder.model.*
import spock.lang.Specification

import java.sql.SQLException
import java.time.LocalDate

class CandidateDaoSpec extends Specification {

	Sql sql
	AddressDao addressDao
	SkillDao skillDao
	CandidateDao candidateDao
	FormationDao formationDao

	def setup() {
		sql = Mock(Sql)
		addressDao = Mock(AddressDao)
		skillDao = Mock(SkillDao)
		formationDao = Mock(FormationDao)
		candidateDao = new CandidateDao(sql, addressDao, skillDao, formationDao)
	}

	def "deve retornar candidatos corretamente"() {
		given: "um resultado de consulta SQL simulando um candidato"
		def row = Mock(GroovyResultSet) {
			getAt('candidato_id') >> 1
			getAt('candidato_nome') >> "João"
			getAt('candidato_email') >> "joao@example.com"
			getAt('candidato_cpf') >> "12345678901"
			getAt('candidato_data_nascimento') >> "1990-05-15"
			getAt('candidato_descricao_pessoal') >> "Desenvolvedor Full Stack"
			getAt('candidato_senha_de_login') >> "senha123"
			getAt('candidato_sobrenome') >> "Silva"
			getAt('endereco_id') >> 1
			getAt('endereco_cep') >> "30123456"
			getAt('pais_nome') >> "Brasil"
			getAt('pais_id') >> 1
			getAt('competencias') >> "1.Java,2.Python"
			getAt('formacoes') >> "1:Engenharia:UFMG:2020-01-01:2024-12-31"
		}

		sql.eachRow(_, _) >> { String query, Closure c -> c.call(row) }
		skillDao.extractSkillsData("1.Java,2.Python") >> [
				new Skill(1, "Java"),
				new Skill(2, "Python")
		]
		formationDao.extractFormationsData("1:Engenharia:UFMG:2020-01-01:2024-12-31") >> [
				new Formation(1, "Engenharia", "UFMG",
						LocalDate.of(2020, 1, 1),
						LocalDate.of(2024, 12, 31))
		]

		when: "busca os candidatos"
		def candidatos = candidateDao.getCandidates()

		then: "verifica se os dados foram retornados corretamente"
		candidatos.size() == 1
		with(candidatos[0]) {
			id == 1
			name == "João"
			email == "joao@example.com"
			cpf == "12345678901"
			dateBirth == LocalDate.of(1990, 5, 15)
			description == "Desenvolvedor Full Stack"
			passwordLogin == "senha123"
			lastName == "Silva"
			address.id == 1
			address.zipCode == "30123456"
			address.country.name == "Brasil"
			address.country.id == 1
			skills.size() == 2
			formations.size() == 1
		}
	}

	def "deve lidar com exceção SQLException ao obter candidatos"() {
		given: "uma configuração para lançar SQLException"

		when: "o método getCandidatos é chamado"
		def result = candidateDao.getCandidates()

		then: "a consulta SQL lança uma exceção"
		1 * sql.eachRow(CandidateDao.selectAllFromCandidates(), _) >> { throw new SQLException("Erro de SQL") }

		and: "o resultado é uma lista vazia"
		result.empty
	}

	def "deve inserir um candidato e retornar o ID"() {
		given: "um candidato válido e um ID de endereço"
		def country = new Country("Brasil", null)
		def address = new Address(null, "30123456", country)
		def candidate = new Candidate(
				null, "João", "joao@example.com", "12345678901",
				LocalDate.of(1990, 5, 15), address, "Desenvolvedor Full Stack",
				"senha123", [], [], "Silva"
		)
		def addressId = 1

		when: "o método insertCandidate é chamado"
		def result = candidateDao.insertCandidate(candidate, addressId)

		then: "a query SQL é executada e retorna um ID"
		1 * sql.executeInsert(_, _) >> [
				[1] // Simulando o retorno do ID
		]

		and: "o resultado é o ID esperado"
		result == 1
	}

	def "deve inserir formações corretamente"() {
		given: "um candidato com formações"
		def formation1 = new Formation(1, "Engenharia de Software", "UFMG", LocalDate.of(2020, 1, 1), LocalDate.of(2024, 12, 31))
		def formation2 = new Formation(2, "MBA em Gestão", "FGV", LocalDate.of(2025, 1, 1), LocalDate.of(2026, 12, 31))
		def formations = [formation1, formation2]
		def candidate = new Candidate(1, "João", "joao@example.com", "12345678901",
				LocalDate.of(1990, 5, 15), null, "Desenvolvedor",
				"senha123", [], formations, "Silva")

		when: "o método insertIdsAndDatesFromFormation é chamado"
		candidateDao.insertIdsAndDatesFromFormation(candidate)

		then: "a query SQL é executada com os parâmetros corretos"
		1 * sql.execute(_, _) >> { String query, List params ->
			assert query.contains("INSERT INTO formacao_candidato")
			assert params.size() == 8 // 4 parâmetros por formação * 2 formações
			assert params[0] == 1 // id da formação 1
			assert params[1] == 1 // id do candidato
			assert params[2] == LocalDate.of(2020, 1, 1) // data de início da formação 1
			assert params[3] == LocalDate.of(2024, 12, 31) // data de fim da formação 1
			assert params[4] == 2 // id da formação 2
			assert params[5] == 1 // id do candidato
			assert params[6] == LocalDate.of(2025, 1, 1) // data de início da formação 2
			assert params[7] == LocalDate.of(2026, 12, 31) // data de fim da formação 2
		}
	}

	def "deve associar competências a um candidato"() {
		given: "um ID de candidato e uma lista de IDs de competências"
		def candidateId = 1
		def skillIds = [1, 2, 3]

		when: "o método associateSkillsToCandidate é chamado"
		candidateDao.associateSkillsToCandidate(candidateId, skillIds)

		then: "a query SQL é executada com os parâmetros corretos"
		1 * sql.execute(_, _) >> { String query, List params ->
			assert query.contains("INSERT INTO candidato_competencia")
			assert query.contains("ON CONFLICT DO NOTHING")
			assert params.size() == 6 // 2 parâmetros por competência * 3 competências
			assert params[0] == 1 // id do candidato
			assert params[1] == 1 // id da competência 1
			assert params[2] == 1 // id do candidato
			assert params[3] == 2 // id da competência 2
			assert params[4] == 1 // id do candidato
			assert params[5] == 3 // id da competência 3
		}
	}

	def "não deve inserir competências quando a lista estiver vazia"() {
		given: "um candidato sem competências"
		def candidate = new Candidate(1, "João", "joao@example.com", "12345678901",
				LocalDate.of(1990, 5, 15), null, "Desenvolvedor",
				"senha123", [], [], "Silva")

		when: "o método insertCandidateSkills é chamado"
		candidateDao.insertCandidateSkills(candidate)

		then: "nenhuma operação de inserção é realizada"
		0 * skillDao.insertSkillsReturningId(_)
		0 * candidateDao.associateSkillsToCandidate(_, _)
	}

	def "deve remover um candidato pelo ID com sucesso"() {
		given: "um ID de candidato válido"
		def candidateId = 1

		when: "o método removeCandidateById é chamado"
		def result = candidateDao.removeCandidateById(candidateId)

		then: "a query SQL é executada e retorna sucesso"
		1 * sql.executeUpdate("DELETE FROM candidato WHERE id = ?", [candidateId]) >> 1

		and: "o resultado é verdadeiro"
		result
	}

	def "deve retornar falso ao tentar remover um candidato que não existe"() {
		given: "um ID de candidato que não existe"
		def candidateId = 999

		when: "o método removeCandidateById é chamado"
		def result = candidateDao.removeCandidateById(candidateId)

		then: "a query SQL é executada e não afeta nenhuma linha"
		1 * sql.executeUpdate("DELETE FROM candidato WHERE id = ?", [candidateId]) >> 0

		and: "o resultado é falso"
		!result
	}

	def "deve lidar com exceção ao tentar remover um candidato"() {
		given: "um ID de candidato"
		def candidateId = 1

		when: "o método removeCandidateById é chamado e ocorre uma exceção"
		def result = candidateDao.removeCandidateById(candidateId)

		then: "a query SQL lança uma exceção"
		1 * sql.executeUpdate("DELETE FROM candidato WHERE id = ?", [candidateId]) >> { throw new SQLException("Erro ao remover candidato") }

		and: "o resultado é falso"
		!result
	}
}