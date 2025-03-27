package org.linketinder.service

import org.linketinder.model.*
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository
import org.linketinder.repository.Repository

class GestaoService {
	final EmpresaRepository empresaRepository
	final CandidatoRepository candidatoRepository
	final Repository repository

	GestaoService(EmpresaRepository empresaRepository, CandidatoRepository candidatoRepository, Repository repository) {
		this.empresaRepository = empresaRepository
		this.candidatoRepository = candidatoRepository
		this.repository = repository
	}

	String cadastrarCandidato(Map<String, String> dados) {
		List<Formacao> formacoes = new ArrayList<>()
		for (Map<String, ?> formacaoMap : dados.get("formacoes") as List<Map<String, ?>>){
			Formacao formacao = new Formacao(
					null,
					formacaoMap.get('instituicao') as String,
					formacaoMap.get('nome') as String,
					formacaoMap.get('dataIncio') as Date,
					formacaoMap.get('dataFim') as Date
			)
			formacoes.add(formacao)
		}

		List<Competencia> competencias = extrairCompetencias(dados)

		Pessoa novoCandidato = new Candidato(
				null,
				dados.nome,
				dados.email,
				dados.cpf,
				dados.dataNascimento as Date,
				dados.cep,
				dados.descricao,
				dados.senha,
				dados.pais,
				competencias,
				formacoes,
				dados.sobrenome
		)
		String feedback = "Candidato cadastrado com sucesso!"
		try {
			candidatoRepository.addCandidato(novoCandidato)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
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

	List<Vaga> listarVagas(){
		return empresaRepository.vagaRepository.getVagas()
	}

	List<Competencia> listarCompetencias(){
		return candidatoRepository.competenciaRepository.getCompetencias()
	}

	private String removerEntidade(Integer id, String tabela) {
		try {
			return repository.removerPorId(tabela, id) ? "Remoção bem sucedida!" : "O id informado não existe!"
		} catch (Exception e) {
			return e.getMessage()
		}
	}
	String removerCandidato(Integer id){
		return removerEntidade(id, 'candidato')
	}
	String removerEmpresa(Integer id){
		return removerEntidade(id, 'empresa')
	}
	String removerVaga(Integer id){
		return removerEntidade(id, 'vaga')
	}
	String removerCompetencia(Integer id){
		return removerEntidade(id, 'competencia')
	}

	String cadastrarVaga(Integer idEmpresa, Map<String, ?> dados){
		List<Competencia> competencias = extrairCompetencias(dados)
		Vaga vaga = new Vaga (
				null,
				dados.descricao as String,
				dados.nome as String,
				dados.local as String,
				idEmpresa,
				competencias
		)
		String feedback = "Cadastro feito com sucesso!"
		try {
			empresaRepository.addVaga(vaga)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
	}

	private static List<Competencia> extrairCompetencias(Map<String, ?> dados){
		List<Competencia> competencias = new ArrayList<>()
		for (String competenciaStr : dados.get('competencias')){
			Competencia competencia = new Competencia(
					null,
					competenciaStr
			)
			competencias.add(competencia)
		}
		return competencias
	}
}