package service

import model.Candidato
import model.Empresa
import model.Pessoa
import repository.CandidatoRepository
import repository.EmpresaRepository

class CadastroService {

    static void cadastrarCandidato(Map<String, String> dados) {

        Pessoa novoCandidato = new Candidato(
                dados.nome,
                dados.email,
                dados.cpf,
                dados.idade.toInteger(),
                dados.estado,
                dados.cep,
                dados.descricao,
                dados.competencias as List<String>
        )
        CandidatoRepository.addCandidato(novoCandidato)
    }

    static void cadastrarEmpresa(Map<String, String> dados) {
        Pessoa novaEmpresa = new Empresa(
                dados.nome,
                dados.email,
                dados.cnpj,
                dados.pais,
                dados.estado,
                dados.cep,
                dados.descricao,
                dados.competencias as List<String>
        )
        EmpresaRepository.addEmpresa(novaEmpresa)
    }
}