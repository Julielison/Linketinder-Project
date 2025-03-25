package org.linketinder.service

import org.linketinder.model.Candidato
import org.linketinder.model.Empresa
import org.linketinder.model.Pessoa
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository

class CadastroService {

	private final EmpresaRepository empresaRepository
	private final CandidatoRepository candidatoRepository

	CadastroService(EmpresaRepository empresaRepository, CandidatoRepository candidatoRepository) {
		this.empresaRepository = empresaRepository
		this.candidatoRepository = candidatoRepository
	}

	String cadastrarCandidato(Map<String, String> dados) {
		Pessoa novoCandidato = new Candidato(
				null,
				dados.nome,
				dados.email,
				dados.cpf,
				dados.dataNascimento,
				dados.estado,
				dados.cep,
				dados.descricao,
				dados.competencias as List<String>
		)
		candidatoRepository.addCandidato(novoCandidato)
	}

	String cadastrarEmpresa(Map<String, String> dados) {
		String feedback = "Cadastro feito com sucesso!"
		Pessoa novaEmpresa = new Empresa(
				null,
				dados.nome,
				dados.email,
				dados.cnpj,
				dados.cep,
				dados.descricao,
				dados.senha,
				dados.pais,
				new ArrayList<>()
		)
		try {
			empresaRepository.addEmpresa(novaEmpresa)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

	List<Candidato> listarCandidatos() {
		return candidatoRepository.getCandidatos()
	}

	List<Empresa> listarEmpresas() {
		return empresaRepository.getEmpresas()
	}
}