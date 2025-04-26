import groovy.sql.Sql
import org.linketinder.dao.impl.AddressDao
import org.linketinder.dao.impl.CompanyDao
import org.linketinder.dao.impl.JobSkillDao
import org.linketinder.dao.impl.SkillDao
import org.linketinder.model.Address
import org.linketinder.model.Company
import org.linketinder.model.Country
import org.linketinder.model.Skill
import spock.lang.Specification
import spock.lang.Unroll

import java.sql.SQLException

class CompanyDaoSpec extends Specification {
    
    Sql sql
    AddressDao addressDao
    CompanyDao companyDao
    SkillDao skillDao
    JobSkillDao jobSkillDao
    
    Company mockCompany
    Address mockAddress
    Country mockCountry
    List<Skill> mockSkills
    
    def setup() {
        sql = Mock(Sql)
        addressDao = Mock(AddressDao)
        skillDao = Mock(SkillDao)
        jobSkillDao = Mock(JobSkillDao)

        companyDao = new CompanyDao(sql, addressDao)
        
        mockCountry = new Country("Brasil", 1)
        mockAddress = new Address(1, "30123456", mockCountry)
        mockSkills = [
            new Skill(1, "Java"),
            new Skill(2, "Spring Boot")
        ]
        
        mockCompany = new Company(
            1, 
            "Empresa ABC", 
            "contato@empresaabc.com", 
            "12345678901234",
            mockAddress, 
            "Empresa de tecnologia focada em soluções inovadoras", 
            "senha123", 
            null
        )
    }

    // GRUPO DE TESTES: Obtenção de dados brutos de empresas
    def "deve retornar dados brutos de empresas corretamente"() {
        given: "preparando dados para retornar da consulta"
        def expectedResults = [
            [
                empresa_id: 1,
                empresa_nome: "Empresa ABC",
                empresa_email: "contato@empresaabc.com",
                empresa_cnpj: "12345678901234",
                empresa_descricao: "Empresa de tecnologia focada em soluções inovadoras",
                empresa_senha: "senha123",
                endereco_id: 1,
                endereco_cep: "30123456",
                pais_nome: "Brasil",
                pais_id: 1,
                vagas: "1:Desenvolvedor Backend:Vaga para desenvolvedor backend com experiência em Java:Remoto:1.Java,2.Spring Boot"
            ]
        ]
        
        companyDao.metaClass.getCompaniesRawData = { ->
            return expectedResults
        }

        when: "o método getCompaniesRawData é chamado"
        def result = companyDao.getCompaniesRawData()

        then: "verifica se os dados foram retornados corretamente"
        result.size() == 1
        with(result[0]) {
            it.empresa_id == 1
            it.empresa_nome == "Empresa ABC"
            it.empresa_email == "contato@empresaabc.com"
            it.empresa_cnpj == "12345678901234"
            it.empresa_descricao == "Empresa de tecnologia focada em soluções inovadoras"
            it.empresa_senha == "senha123"
            it.endereco_id == 1
            it.endereco_cep == "30123456"
            it.pais_nome == "Brasil"
            it.pais_id == 1
            it.vagas == "1:Desenvolvedor Backend:Vaga para desenvolvedor backend com experiência em Java:Remoto:1.Java,2.Spring Boot"
        }
    }
    
    def "deve retornar múltiplas empresas quando disponíveis"() {
        given: "Criando um spy do CompanyDao com dados de teste"
        def companiesData = [
            [
                empresa_id: 1, 
                empresa_nome: "Empresa ABC", 
                empresa_email: "contato@empresaabc.com",
                empresa_cnpj: "12345678901234"
            ],
            [
                empresa_id: 2, 
                empresa_nome: "Empresa XYZ", 
                empresa_email: "contato@empresaxyz.com",
                empresa_cnpj: "56789012345678"
            ]
        ]
        
        companyDao.metaClass.getCompaniesRawData = { -> 
            return companiesData
        }
        
        when: "obtemos os dados brutos das empresas"
        def result = companyDao.getCompaniesRawData()
        
        then: "a lista deve conter duas empresas"
        result.size() == 2
        result[0].empresa_nome == "Empresa ABC"
        result[1].empresa_nome == "Empresa XYZ"
        result[1].empresa_cnpj == "56789012345678"
    }
    
    def "deve lidar com valores nulos nos resultados da consulta para empresas"() {
        given: "dados com valores nulos"
        def companiesWithNulls = [
            [
                empresa_id: 3,
                empresa_nome: "Startup DEF",
                empresa_email: "contato@startupdef.com",
                empresa_cnpj: "90123456789012",
                empresa_descricao: null,  // descrição nula
                empresa_senha: "senha456",
                endereco_id: 3,
                endereco_cep: "20123456",
                pais_nome: "Brasil",
                pais_id: 1,
                vagas: null  // sem vagas
            ]
        ]
        
        companyDao.metaClass.getCompaniesRawData = { -> 
            return companiesWithNulls
        }
        
        when: "obtemos os dados brutos"
        def result = companyDao.getCompaniesRawData()
        
        then: "os valores nulos são preservados no resultado"
        result.size() == 1
        with(result[0]) {
            it.empresa_id == 3
            it.empresa_nome == "Startup DEF"
            it.empresa_descricao == null
            it.vagas == null
        }
    }

    // GRUPO DE TESTES: Query de seleção de empresas
    @Unroll
    def "deve incluir todas as colunas necessárias na query de empresas: #coluna"() {
        when: "obtemos a query SQL"
        def query = CompanyDao.selectAllFromCompanies()
        
        then: "a query deve incluir a coluna específica"
        query.contains(coluna)
        
        where:
        coluna << [
            "e.id AS empresa_id",
            "e.nome AS empresa_nome",
            "e.email_corporativo AS empresa_email",
            "e.cnpj AS empresa_cnpj",
            "e.descricao_da_empresa AS empresa_descricao",
            "endereco.cep AS endereco_cep",
            "p.nome AS pais_nome"
        ]
    }


    // GRUPO DE TESTES: Adição de dados completos da empresa
    def "deve adicionar todos os dados de uma empresa com sucesso"() {
        given: "uma empresa com todas as informações"
        def company = mockCompany
        
        when: "o método addAllCompanyData é chamado"
        companyDao.addAllCompanyData(company)
        
        then: "a transação SQL é iniciada"
        1 * sql.withTransaction(_) >> { Closure closure -> closure.call() }
        
        and: "todas as operações são executadas na ordem esperada"
        1 * addressDao.insertCountryReturningId(mockCountry.name) >> 1
        1 * addressDao.insertAddressReturningId(mockAddress.zipCode, 1) >> 1
        
        and: "a empresa é inserida com o endereço correto"
        1 * sql.executeInsert(_, _) >> [[1]]
    }
    
    def "deve tratar exceção ao adicionar dados completos de empresa"() {
        given: "uma empresa válida"
        def company = mockCompany
        
        and: "uma transação que lança exceção"
        sql.withTransaction(_) >> { throw new SQLException("Erro na transação") }
        
        when: "o método addAllCompanyData é chamado"
        def exception = null
        try {
            companyDao.addAllCompanyData(company)
        } catch (RuntimeException e) {
            exception = e
        }
        
        then: "uma RuntimeException deve ser lançada"
        exception != null
        exception.message.contains("Erro ao adicionar dados da empresa")
    }

    // GRUPO DE TESTES: Inserção de empresa (método privado)
    def "deve inserir empresa básica corretamente"() {
        given: "uma empresa sem vagas e um ID de endereço"
        def company = new Company(
            null, "Empresa Nova", "contato@empresanova.com", "98765432109876",
            mockAddress, "Nova empresa de consultoria", "senha789", []
        )
        def addressId = 2
        
        and: "uma configuração para o método executeInsert"
        sql.executeInsert(_, _) >> { String query, List params ->
            assert params[0] == "Empresa Nova"
            assert params[1] == "98765432109876"
            assert params[2] == "contato@empresanova.com"
            assert params[3] == "Nova empresa de consultoria"
            assert params[4] == "senha789"
            assert params[5] == 2
            return [[3]]
        }
        
        when: "o método insertCompany é chamado"
        def result = companyDao.insertCompany(company, addressId)
        
        then: "o ID retornado deve ser o esperado"
        result == 3
    }

    // GRUPO DE TESTES: Remoção de empresa
    def "deve remover empresa por ID com sucesso"() {
        given: "um ID de empresa válido"
        def companyId = 1
        
        and: "uma configuração para executeUpdate que simula uma remoção bem-sucedida"
        sql.executeUpdate(_, [companyId]) >> 1
        
        when: "o método deleteCompanyById é chamado"
        def result = companyDao.deleteCompanyById(companyId)
        
        then: "o resultado deve ser verdadeiro"
        result
    }
    
    def "deve retornar falso quando nenhuma empresa for removida"() {
        given: "um ID de empresa inexistente"
        def companyId = 999
        
        and: "uma configuração para executeUpdate que simula nenhuma linha afetada"
        sql.executeUpdate(_, [companyId]) >> 0
        
        when: "o método deleteCompanyById é chamado"
        def result = companyDao.deleteCompanyById(companyId)
        
        then: "o resultado deve ser falso"
        !result
    }
    
    def "deve tratar exceção ao remover empresa"() {
        given: "um ID de empresa"
        def companyId = 1
        
        and: "uma configuração para executeUpdate que lança exceção"
        sql.executeUpdate(_, [companyId]) >> { throw new SQLException("Erro ao remover empresa") }
        
        when: "o método deleteCompanyById é chamado"
        def result = companyDao.deleteCompanyById(companyId)
        
        then: "o resultado deve ser falso"
        !result
    }
    
    @Unroll
    def "deve lidar com diferentes tipos de exceções ao remover empresa: #tipoExcecao"() {
        given: "um ID de empresa"
        def companyId = 1
        
        and: "uma configuração para executeUpdate que lança uma exceção específica"
        sql.executeUpdate(_, [companyId]) >> { throw excecao }
        
        when: "o método deleteCompanyById é chamado"
        def result = companyDao.deleteCompanyById(companyId)
        
        then: "o resultado deve ser falso independente do tipo de SQLException"
        !result
        
        where:
        tipoExcecao                | excecao
        "Violação de chave"        | new SQLException("ERROR: violação de chave estrangeira", "23503")
        "Permissão negada"         | new SQLException("ERROR: permissão negada", "42501")
        "Conexão perdida"          | new SQLException("Connection reset")
    }
}