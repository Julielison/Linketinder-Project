import groovy.sql.Sql
import org.linketinder.dao.impl.*
import org.linketinder.model.*
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.SQLException
import java.time.LocalDate

class CandidateDaoSpec extends Specification {

    Sql sql
    AddressDao addressDao
    SkillDao skillDao
    CandidateDao candidateDao
    FormationDao formationDao
    FormationCandidateDao formationCandidateDao
    CandidateSkillDao candidateSkillDao
    
    Candidate mockCandidate
    Address mockAddress
    Country mockCountry
    List<Skill> mockSkills
    List<Formation> mockFormations

    def setup() {
        sql = Mock(Sql)
        addressDao = Mock(AddressDao)
        skillDao = Mock(SkillDao)
        formationDao = Mock(FormationDao)
        formationCandidateDao = Mock(FormationCandidateDao)
        candidateSkillDao = Mock(CandidateSkillDao)
        
        candidateDao = new CandidateDao(sql, addressDao, formationDao, skillDao, 
                                      formationCandidateDao, candidateSkillDao)
        
        mockCountry = new Country("Brasil", 1)
        mockAddress = new Address(1, "30123456", mockCountry)
        mockSkills = [
            new Skill(1, "Java"),
            new Skill(2, "Python")
        ]
        mockFormations = [
            new Formation(1, "Engenharia", "UFMG", 
                LocalDate.of(2020, 1, 1), 
                LocalDate.of(2024, 12, 31))
        ]
        mockCandidate = new Candidate(
            1, "João", "joao@example.com", "12345678901",
            LocalDate.of(1990, 5, 15), mockAddress, "Desenvolvedor Full Stack",
            "senha123", mockSkills, mockFormations, "Silva"
        )
    }

    def "deve retornar dados brutos de candidatos corretamente"() {
        given: "preparando dados para retornar da consulta"
        def expectedResults = [
            [
                candidato_id: 1,
                candidato_nome: "João",
                candidato_sobrenome: "Silva",
                candidato_email: "joao@example.com",
                candidato_cpf: "12345678901",
                candidato_descricao_pessoal: "Desenvolvedor Full Stack",
                candidato_senha_de_login: "senha123",
                endereco_id: 1,
                endereco_cep: "30123456",
                pais_nome: "Brasil",
                pais_id: 1,
                competencias: "1.Java,2.Python",
                formacoes: "1:Engenharia:UFMG:2020-01-01:2024-12-31"
            ]
        ]
        
        candidateDao.metaClass.getCandidatesRawData = { ->
            return expectedResults
        }

        when: "o método getCandidatesRawData é chamado"
        def result = candidateDao.getCandidatesRawData()

        then: "verifica se os dados foram retornados corretamente"
        result.size() == 1
        with(result[0]) {
            it.candidato_id == 1
            it.candidato_nome == "João"
            it.candidato_sobrenome == "Silva"
            it.candidato_email == "joao@example.com"
            it.candidato_cpf == "12345678901"
            it.candidato_descricao_pessoal == "Desenvolvedor Full Stack"
            it.candidato_senha_de_login == "senha123"
            it.endereco_id == 1
            it.endereco_cep == "30123456"
            it.pais_nome == "Brasil"
            it.pais_id == 1
            it.competencias == "1.Java,2.Python"
            it.formacoes == "1:Engenharia:UFMG:2020-01-01:2024-12-31"
        }
    }
    
    def "deve retornar múltiplos candidatos quando disponíveis"() {
        given: "Criando um spy do CandidateDao com dados de teste"
        def candidatesData = [
            [
                candidato_id: 1, candidato_nome: "João", candidato_sobrenome: "Silva",
                candidato_email: "joao@example.com", candidato_cpf: "12345678901"
            ],
            [
                candidato_id: 2, candidato_nome: "Maria", candidato_sobrenome: "Oliveira",
                candidato_email: "maria@example.com", candidato_cpf: "98765432101"
            ]
        ]
        
        candidateDao.metaClass.getCandidatesRawData = { -> 
            return candidatesData
        }
        
        when: "obtemos os dados brutos dos candidatos"
        def result = candidateDao.getCandidatesRawData()
        
        then: "a lista deve conter dois candidatos"
        result.size() == 2
        result[0].candidato_nome == "João"
        result[1].candidato_nome == "Maria"
        result[1].candidato_cpf == "98765432101"
    }
    
    def "deve lidar com valores nulos nos resultados da consulta"() {
        given: "dados com valores nulos"
        def candidatesWithNulls = [
            [
                candidato_id: 4,
                candidato_nome: "Eduardo",
                candidato_sobrenome: null,
                candidato_email: "eduardo@example.com",
                candidato_cpf: "44455566677",
                candidato_descricao_pessoal: null,
                candidato_senha_de_login: "senha444",
                endereco_id: 4,
                endereco_cep: "50123456",
                pais_nome: "Brasil",
                pais_id: 1,
                competencias: null,
                formacoes: null
            ]
        ]
        
        candidateDao.metaClass.getCandidatesRawData = { -> 
            return candidatesWithNulls
        }
        
        when: "obtemos os dados brutos"
        def result = candidateDao.getCandidatesRawData()
        
        then: "os valores nulos são preservados no resultado"
        result.size() == 1
        with(result[0]) {
            it.candidato_id == 4
            it.candidato_nome == "Eduardo"
            it.candidato_sobrenome == null
            it.candidato_descricao_pessoal == null
            it.competencias == null
            it.formacoes == null
        }
    }

    def "deve adicionar todos os dados de um candidato com sucesso"() {
        given: "um candidato com todas as informações"
        def candidate = mockCandidate

        when: "o método addAllDataFromCandidate é chamado"
        candidateDao.addAllDataFromCandidate(candidate)

        then: "a transação SQL é iniciada"
        1 * sql.withTransaction(_) >> { Closure closure -> closure.call() }

        and: "todas as operações são executadas na ordem esperada"
        1 * addressDao.insertCountryReturningId(mockCountry.name) >> 1
        1 * addressDao.insertAddressReturningId(mockAddress.zipCode, 1) >> 1

        and: "o candidato é inserido com o endereço correto"
        1 * sql.executeInsert(_, _) >> [[1]]

        and: "as formações são inseridas"
        1 * formationDao.insertFormations(candidate) >> candidate
        1 * formationCandidateDao.insertIdsAndDatesFromFormation(candidate)

        and: "as competências são inseridas"
        1 * skillDao.insertSkillsReturningId(mockSkills) >> [1, 2]
        1 * candidateSkillDao.associateSkillsToCandidate(1, [1, 2])
    }

    def "deve tratar exceção ao adicionar dados completos de candidato"() {
        given: "um candidato válido"
        def candidate = mockCandidate

        and: "uma transação que lança exceção"
        sql.withTransaction(_) >> { throw new SQLException("Erro na transação") }

        when: "o método addAllDataFromCandidate é chamado"
        def exception = null
        try {
            candidateDao.addAllDataFromCandidate(candidate)
        } catch (RuntimeException e) {
            exception = e
        }

        then: "uma RuntimeException deve ser lançada"
        exception != null
        exception.message.contains("Erro ao adicionar dados do candidato")
    }

    def "deve inserir candidato básico corretamente"() {
        given: "um candidato sem competências ou formações e um ID de endereço"
        def candidate = new Candidate(
                null, "Maria", "maria@example.com", "98765432109",
                LocalDate.of(1995, 10, 20), mockAddress, "Analista de Dados",
                "senha456", [], [], "Oliveira"
        )
        def addressId = 2

        and: "uma configuração para o método executeInsert"
        sql.executeInsert(_, _) >> { String query, List params ->
            assert params[0] == "Maria"
            assert params[1] == "Oliveira"
            assert params[2] == "1995-10-20"
            assert params[3] == "maria@example.com"
            assert params[4] == "98765432109"
            assert params[5] == "Analista de Dados"
            assert params[6] == "senha456"
            assert params[7] == 2
            return [[3]]
        }

        when: "tentamos acessar o método privado insertCandidate via reflexão"
        def method = CandidateDao.class.getDeclaredMethod("insertCandidate", Candidate.class, Integer.class)
        method.setAccessible(true)
        def result = method.invoke(candidateDao, candidate, addressId)

        then: "o ID retornado deve ser o esperado"
        result == 3
    }

    def "deve formatar data de nascimento corretamente para SQL"() {
        given: "um candidato com data específica"
        def dataEspecifica = LocalDate.of(2000, 12, 31)
        def candidate = new Candidate(
                null, "Lucas", "lucas@example.com", "11122233344",
                dataEspecifica, mockAddress, "Desenvolvedor Frontend",
                "senha789", [], [], "Ribeiro"
        )
        def addressId = 5

        when: "executamos a inserção do candidato"
        sql.executeInsert(_, _) >> { String query, List params ->
            assert params[2] == "2000-12-31"
            return [[10]]
        }

        def method = CandidateDao.class.getDeclaredMethod("insertCandidate", Candidate.class, Integer.class)
        method.setAccessible(true)
        def result = method.invoke(candidateDao, candidate, addressId)

        then: "o método deve usar o formato correto da data e retornar o ID esperado"
        result == 10
    }

    def "deve remover candidato por ID com sucesso"() {
        given: "um ID de candidato válido"
        def candidateId = 1

        and: "uma configuração para executeUpdate que simula uma remoção bem-sucedida"
        sql.executeUpdate(_, [candidateId]) >> 1

        when: "o método removeCandidateById é chamado"
        def result = candidateDao.removeCandidateById(candidateId)

        then: "o resultado deve ser verdadeiro"
		result
    }

    def "deve retornar falso quando nenhum candidato for removido"() {
        given: "um ID de candidato inexistente"
        def candidateId = 999

        and: "uma configuração para executeUpdate que simula nenhuma linha afetada"
        sql.executeUpdate(_, [candidateId]) >> 0

        when: "o método removeCandidateById é chamado"
        def result = candidateDao.removeCandidateById(candidateId)

        then: "o resultado deve ser falso"
		!result
    }

    def "deve tratar exceção ao remover candidato"() {
        given: "um ID de candidato"
        def candidateId = 1

        and: "uma configuração para executeUpdate que lança exceção"
        sql.executeUpdate(_, [candidateId]) >> { throw new SQLException("Erro ao remover candidato") }

        when: "o método removeCandidateById é chamado"
        def result = candidateDao.removeCandidateById(candidateId)

        then: "o resultado deve ser falso"
		!result
    }


    def "deve processar candidato sem formações corretamente"() {
        given: "um candidato sem formações"
        def candidateWithoutFormations = new Candidate(
                1, "Carlos", "carlos@example.com", "45678912301",
                LocalDate.of(1988, 8, 18), mockAddress, "Arquiteto de Software",
                "senha789", mockSkills, [], "Ferreira"
        )

        when: "o método addAllDataFromCandidate é chamado"
        sql.withTransaction(_) >> { Closure closure -> closure.call() }
        addressDao.insertCountryReturningId(_) >> 1
        addressDao.insertAddressReturningId(_, _) >> 2
        sql.executeInsert(_, _) >> [[3]]
        skillDao.insertSkillsReturningId(_) >> [4, 5]

        candidateDao.addAllDataFromCandidate(candidateWithoutFormations)

        then: "não deve chamar os métodos relacionados a formações"
        0 * formationDao.insertFormations(_)
        0 * formationCandidateDao.insertIdsAndDatesFromFormation(_)

        and: "deve chamar os métodos relacionados a competências"
        1 * candidateSkillDao.associateSkillsToCandidate(_, _)
    }

    def "deve processar candidato sem competências corretamente"() {
        given: "um candidato sem competências"
        def candidateWithoutSkills = new Candidate(
                1, "Ana", "ana@example.com", "78945612301",
                LocalDate.of(1992, 3, 25), mockAddress, "Gerente de Projetos",
                "senha321", [], mockFormations, "Lima"
        )

        when: "o método addAllDataFromCandidate é chamado"
        sql.withTransaction(_) >> { Closure closure -> closure.call() }
        addressDao.insertCountryReturningId(_) >> 1
        addressDao.insertAddressReturningId(_, _) >> 2
        sql.executeInsert(_, _) >> [[3]]
        formationDao.insertFormations(_) >> candidateWithoutSkills

        candidateDao.addAllDataFromCandidate(candidateWithoutSkills)

        then: "não deve chamar os métodos relacionados a competências"
        0 * skillDao.insertSkillsReturningId(_)
        0 * candidateSkillDao.associateSkillsToCandidate(_, _)

        and: "deve chamar os métodos relacionados a formações"
        1 * formationCandidateDao.insertIdsAndDatesFromFormation(_)
    }

    def "deve processar candidato sem formações nem competências"() {
        given: "um candidato sem formações nem competências"
        def candidateWithoutSkillsFormations = new Candidate(
                1, "Roberto", "roberto@example.com", "11133355599",
                LocalDate.of(1980, 7, 10), mockAddress, "Diretor de Tecnologia",
                "senha999", [], [], "Martins"
        )

        when: "o método addAllDataFromCandidate é chamado"
        sql.withTransaction(_) >> { Closure closure -> closure.call() }
        addressDao.insertCountryReturningId(_) >> 1
        addressDao.insertAddressReturningId(_, _) >> 2
        sql.executeInsert(_, _) >> [[3]]

        candidateDao.addAllDataFromCandidate(candidateWithoutSkillsFormations)

        then: "não deve chamar nenhum método relacionado a formações e competências"
        0 * formationDao.insertFormations(_)
        0 * formationCandidateDao.insertIdsAndDatesFromFormation(_)
        0 * skillDao.insertSkillsReturningId(_)
        0 * candidateSkillDao.associateSkillsToCandidate(_, _)
    }
}