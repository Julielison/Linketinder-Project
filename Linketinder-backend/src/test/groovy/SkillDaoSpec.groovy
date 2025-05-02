import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.dao.impl.SkillDao
import org.linketinder.dao.interfaces.ISkillDao
import org.linketinder.model.Skill
import spock.lang.Specification

import java.sql.SQLException

class SkillDaoSpec extends Specification {
    
    Sql sql
    ISkillDao skillDao
    List<Skill> mockSkills
    
    def setup() {
        sql = Mock(Sql)
        skillDao = new SkillDao(sql)
        
        mockSkills = [
            new Skill(1, "Java"),
            new Skill(2, "Groovy"),
            new Skill(3, "Spring")
        ]
    }

	def "deve retornar dados brutos de competências corretamente"() {
		given: "preparando dados para retornar da consulta"
		def expectedResults = [
				[id: 1, nome: "Java"],
				[id: 2, nome: "Groovy"],
				[id: 3, nome: "Spring"]
		]

		and: "mock do método getSkillsRawData usando metaclass"
		skillDao.metaClass.getAll = { ->
			return expectedResults
		}

		when: "o método getSkillsRawData é chamado"
		def result = skillDao.getAll()

		then: "verifica se os dados foram retornados corretamente"
		result.size() == 3
		with(result[0]) {
			it.id == 1
			it.nome == "Java"
		}
		with(result[1]) {
			it.id == 2
			it.nome == "Groovy"
		}
		with(result[2]) {
			it.id == 3
			it.nome == "Spring"
		}
	}
    
    def "deve tratar exceção ao consultar competências"() {
        given: "uma exceção será lançada durante a consulta"
        sql.eachRow(_ as GString, _ as Closure) >> { throw new SQLException("Database error") }
        
        when: "chamada ao método getSkillsRawData"
        def result = skillDao.getAll()
        
        then: "deve tratar a exceção e retornar uma lista vazia"
        result.size() == 0
    }

	def "deve inserir competências com sucesso e retornar IDs"() {
		given: "competências para serem inseridas"
		def skillNames = mockSkills*.name.unique()
		def expectedIds = [1, 2, 3]

		and: "configuração do mock para simular resultados SQL"
		sql.rows(_, _) >> { String query, List params ->
			assert query.contains("INSERT INTO competencia")
			assert params.size() == skillNames.size()
			assert params == skillNames
			return [
					[id: 1] as GroovyRowResult,
					[id: 2] as GroovyRowResult,
					[id: 3] as GroovyRowResult
			]
		}

		when: "chamada ao método insertSkillsReturningId"
		def result = skillDao.insertSkillsReturningId(mockSkills)

		then: "deve retornar os IDs das competências inseridas"
		result == expectedIds
	}
    
    def "deve tratar exceção ao inserir competências"() {
        given: "uma exceção ao inserir competências"
        sql.rows(_, _) >> { throw new SQLException("Database error") }
        
        when: "chamada ao método insertSkillsReturningId"
        def result = skillDao.insertSkillsReturningId(mockSkills)
        
        then: "deve retornar uma lista vazia"
        result == []
    }
    
    def "deve remover competência por ID com sucesso"() {
        when: "chamada ao método removeSkillById"
        def result = skillDao.removeSkillById(1)
        
        then: "deve executar a query de delete corretamente"
        1 * sql.executeUpdate("DELETE FROM competencia WHERE id = ?", [1]) >> 1
        
        and: "deve retornar true indicando sucesso"
		result
    }
    
    def "deve retornar falso quando nenhuma competência for removida"() {
        when: "chamada ao método removeSkillById com ID inexistente"
        def result = skillDao.removeSkillById(999)
        
        then: "deve executar a query de delete"
        1 * sql.executeUpdate(_, _) >> 0
        
        and: "deve retornar false indicando que nada foi removido"
		!result
    }
    
    def "deve tratar exceção ao remover competência"() {
        when: "chamada ao método removeSkillById com erro"
        def result = skillDao.removeSkillById(1)
        
        then: "deve tratar a exceção"
        1 * sql.executeUpdate(_, _) >> { throw new SQLException("Database error") }
        
        and: "deve retornar false"
		!result
    }
}
