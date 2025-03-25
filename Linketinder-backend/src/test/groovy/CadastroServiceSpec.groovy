import spock.lang.Specification
import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.service.CadastroService

class CadastroServiceSpec extends Specification {

    def "Deve cadastrar um novo candidato com sucesso"() {
        given: "Um mock do repositório e um novo candidato"
        CandidatoRepository candidatoRepository = Mock(CandidatoRepository)
        EmpresaRepository empresaRepository = Mock(EmpresaRepository)
        CadastroService cadastroService = new CadastroService(empresaRepository, candidatoRepository)

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
        given: "Um mock do repositório e uma nova empresa"
        EmpresaRepository empresaRepository = Mock(EmpresaRepository)
        CandidatoRepository candidatoRepository = Mock(CandidatoRepository)
        CadastroService cadastroService = new CadastroService(empresaRepository, candidatoRepository)

        Empresa empresa = new Empresa(
                null,
                "SmartTech",
                "contato@smarttech.com",
                "55667788000133",
                "01111-111",
                "Empresa de tecnologia",
                "senha123",
                "Brasil",
                new ArrayList<>()
        )

        when: "A empresa é adicionada ao repositório"
        cadastroService.cadastrarEmpresa([
                nome: empresa.nome,
                email: empresa.email,
                cnpj: empresa.cnpj,
                cep: empresa.cep,
                descricao: empresa.descricao,
                senha: empresa.senhaLogin,
                pais: empresa.paisOndeReside
        ])

        then: "O método addEmpresa deve ser chamado uma vez com a empresa correta"
        1 * empresaRepository.addEmpresa(_ as Empresa) >> { Empresa emp ->
            assert emp.nome == empresa.nome
            assert emp.email == empresa.email
            assert emp.cnpj == empresa.cnpj
            assert emp.cep == empresa.cep
            assert emp.descricao == empresa.descricao
            assert emp.senhaLogin == empresa.senhaLogin
            assert emp.paisOndeReside == empresa.paisOndeReside
        }
    }
}