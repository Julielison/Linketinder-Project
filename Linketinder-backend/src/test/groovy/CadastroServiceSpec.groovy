

import groovy.sql.Sql
import org.linketinder.model.Vaga
import spock.lang.Specification
import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.service.GestaoService

class CadastroServiceSpec extends Specification {

    def "Deve cadastrar um novo candidato com sucesso"() {
        given: "Um mock do repositório e um novo candidato"
        CandidatoRepository candidatoRepository = Mock(CandidatoRepository)
        EmpresaRepository empresaRepository = Mock(EmpresaRepository)
        GestaoService cadastroService = new GestaoService(empresaRepository, candidatoRepository)

        Candidato candidato = new Candidato(
                null,
                "Lucas Martins",
                "lucas@gmail.com",
                "12312312312",
                "1994-05-15",
                "SP",
                "04567-890",
                "Desenvolvedor Backend",
                ["Java", "Spring Boot"]
        )

        when: "O candidato é adicionado ao repositório"
        cadastroService.cadastrarCandidato([
                nome: candidato.nome,
                email: candidato.email,
                cpf: candidato.cpf,
                dataNascimento: candidato.dataNascimento,
                estado: candidato.estado,
                cep: candidato.cep,
                descricao: candidato.descricao,
                competencias: candidato.competencias
        ])

        then: "O método addCandidato deve ser chamado uma vez com o candidato correto"
        1 * candidatoRepository.addCandidato(_ as Candidato) >> { Candidato cand ->
            assert cand.nome == candidato.nome
            assert cand.email == candidato.email
            assert cand.cpf == candidato.cpf
            assert cand.dataNascimento == candidato.dataNascimento
            assert cand.estado == candidato.estado
            assert cand.cep == candidato.cep
            assert cand.descricao == candidato.descricao
            assert cand.competencias == candidato.competencias
        }
    }

    def "Deve cadastrar uma nova empresa com sucesso"() {
        given:
        def sqlMock = Mock(Sql)
        EmpresaRepository empresaRepository = Spy(EmpresaRepository, constructorArgs: [sqlMock]) {
            obterIdPais(_) >> 1
            inserirPais(_) >> 1
            inserirEndereco(_, _) >> 1
            inserirEmpresa(_, _) >> { Empresa emp, Integer enderecoId ->
                emp.setId(1)
                EmpresaRepository.empresas.add(emp)
                return 1
            }
        }
        List<Vaga> vagas = new ArrayList<>()
        Empresa empresa = new Empresa(
                null,
                "Empresa Teste",
                "123456789",
                "teste@empresa.com",
                "Descrição da Empresa",
                "senha123",
                "12345-678",
                "Brasil",
                vagas
        )

        when:
        empresaRepository.addEmpresa(empresa)

        then:
        noExceptionThrown()
        empresa.id == 1
        EmpresaRepository.empresas.contains(empresa)
    }
}