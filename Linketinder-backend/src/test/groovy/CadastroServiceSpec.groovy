import spock.lang.Specification
import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository


class CadastroServiceSpec extends Specification {

    def "Deve cadastrar um novo candidato com sucesso"() {
        given: "Um novo candidato"
        Candidato candidato = new Candidato("Lucas Martins", "lucas@gmail.com", "12312312312", 27, "SP", "04567-890", "Desenvolvedor Backend", ["Java", "Spring Boot"])

        when: "O candidato é adicionado ao repositório"
        CandidatoRepository.addCandidato(candidato)

        then: "O candidato deve estar na lista de candidatos"
        CandidatoRepository.getCandidatos().contains(candidato)
    }

    def "Deve cadastrar uma nova empresa com sucesso"() {
        given: "Uma nova empresa"
        Empresa empresa = new Empresa("SmartTech", "contato@smarttech.com", "55667788000133", "Brasil", "SP", "01111-111", "Empresa de tecnologia", ["JavaScript", "React"])

        when: "A empresa é adicionada ao repositório"
        EmpresaRepository.addEmpresa(empresa)

        then: "A empresa deve estar na lista de empresas"
        EmpresaRepository.getEmpresas().contains(empresa)
    }
}
