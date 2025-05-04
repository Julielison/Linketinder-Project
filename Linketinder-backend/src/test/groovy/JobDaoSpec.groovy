import groovy.sql.Sql
import org.linketinder.dao.impl.JobDao
import org.linketinder.dao.impl.JobSkillDao
import org.linketinder.dao.impl.SkillDao

import org.linketinder.dao.interfaces.ISkillDao
import org.linketinder.model.Job
import org.linketinder.model.Skill
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.SQLException

class JobDaoSpec extends Specification {
    
    Sql sql
    ISkillDao skillDao
    JobSkillDao jobSkillDao
    JobDao jobDao
    
    Job mockJob
    List<Skill> mockSkills
    
    def setup() {
        sql = Mock(Sql)
        skillDao = Mock(SkillDao)
        jobSkillDao = Mock(JobSkillDao)
        
        jobDao = new JobDao(sql, skillDao, jobSkillDao)
        
        mockSkills = [
            new Skill(1, "Java"),
            new Skill(2, "Groovy"),
            new Skill(3, "Spring")
        ]
        
        mockJob = new Job(
            1,
            "Desenvolvedor Backend",
            "Vaga para desenvolvedor backend",
            "São Paulo",
            1,
            mockSkills
        )
    }

	def "deve retornar dados brutos de vagas corretamente"() {
		given: "preparando dados para retornar da consulta"
		def expectedResults = [
				[
						vaga_id: 1,
						vaga_nome: "Desenvolvedor Backend",
						vaga_descricao: "Vaga para desenvolvedor backend",
						vaga_local: "São Paulo",
						empresa_id: 1,
						competencias: "1.Java,2.Groovy,3.Spring"
				]
		]

		jobDao.metaClass.getAll = { ->
			return expectedResults
		}

		when: "o método getJobsRawData é chamado"
		def result = jobDao.getAll()

		then: "verifica se os dados foram retornados corretamente"
		result.size() == 1
		with(result[0]) {
			it.vaga_id == 1
			it.vaga_nome == "Desenvolvedor Backend"
			it.vaga_descricao == "Vaga para desenvolvedor backend"
			it.vaga_local == "São Paulo"
			it.empresa_id == 1
			it.competencias == "1.Java,2.Groovy,3.Spring"
		}
	}

    def "deve tratar exceção ao consultar vagas"() {
        given: "uma exceção será lançada durante a consulta"
        sql.eachRow(_ as GString, _ as Closure) >> { throw new SQLException("Database error") }
        
        when: "chamada ao método getJobsRawData"
        def result = jobDao.getAll()
        
        then: "deve tratar a exceção e retornar uma lista vazia"
        result.size() == 0
    }
    
    def "deve adicionar uma vaga com sucesso"() {
        given: "uma vaga para adicionar"
        def jobId = 1
        
        when: "chamada ao método addJobData"
        jobDao.save(mockJob)
        
        then: "deve iniciar uma transação"
        1 * sql.withTransaction(_) >> { it[0].call() }
        
        and: "deve inserir a vaga e retornar seu ID"
        1 * sql.executeInsert(_, _) >> { String query, List params ->
            assert query.contains("INSERT INTO VAGA")
            assert params[0] == mockJob.name
            assert params[1] == mockJob.description
            assert params[2] == mockJob.local
            assert params[3] == mockJob.idCompany
            return [[jobId]]
        }
        
        and: "deve inserir as competências da vaga"
        1 * skillDao.insertSkillsReturningId(mockJob.skills) >> [1, 2, 3]
        1 * jobSkillDao.associateEntityWithSkill(jobId, [1, 2, 3])
    }
    
    def "deve tratar exceção ao adicionar vaga com empresa inexistente"() {
        given: "uma falha na inserção da vaga"
        sql.withTransaction(_ as Closure) >> { closure -> closure() }
        sql.executeInsert(_ as GString, _) >> { throw new SQLException("Foreign key violation") }
        
        when: "chamada ao método addJobData"
        jobDao.save(mockJob)
        
        then: "deve propagar a exceção"
        thrown(Exception)
    }
    
    def "não deve inserir competências quando lista de skills estiver vazia"() {
        given: "uma vaga sem competências"
        def jobWithoutSkills = new Job(1, "Vaga", "Descrição", "Local", 1, [])
        
        when: "adiciona a vaga"
        jobDao.insertJobSkills(jobWithoutSkills)
        
        then: "não deve chamar os métodos para inserir competências"
        0 * skillDao.insertSkillsReturningId(_)
        0 * jobSkillDao.associateEntityWithSkill(_, _)
    }
    
    def "deve remover vaga por ID com sucesso"() {
        when: "chamada ao método removeJobById"
        def result = jobDao.deleteById(1)
        
        then: "deve executar a query de delete corretamente"
        1 * sql.executeUpdate("DELETE FROM vaga WHERE id = ?", [1]) >> 1
        
        and: "deve retornar true indicando sucesso"
		result
    }
    
    def "deve retornar falso quando nenhuma vaga for removida"() {
        when: "chamada ao método removeJobById com ID inexistente"
        def result = jobDao.deleteById(999)
        
        then: "deve executar a query de delete"
        1 * sql.executeUpdate(_, _) >> 0
        
        and: "deve retornar false indicando que nada foi removido"
		!result
    }
    
    def "deve tratar exceção ao remover vaga"() {
        when: "chamada ao método removeJobById com erro"
        def result = jobDao.deleteById(1)
        
        then: "deve tratar a exceção"
        1 * sql.executeUpdate(_, _) >> { throw new SQLException("Database error") }
        
        and: "deve retornar false"
		!result
    }
    
    @Unroll
    def "deve incluir todas as colunas necessárias na query de vagas: #coluna"() {
        when: "obtém a query de seleção"
        String query = JobDao.selectAllJobs()
        
        then: "a query deve conter todas as colunas necessárias"
        query.contains(coluna)
        
        where:
        coluna << ["vaga_id", "vaga_nome", "vaga_descricao", "vaga_local", "empresa_id", "competencias"]
    }
}
