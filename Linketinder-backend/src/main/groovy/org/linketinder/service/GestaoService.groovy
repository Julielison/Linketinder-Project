package org.linketinder.service

import org.linketinder.model.*
import org.linketinder.repository.CandidatoRepository
import org.linketinder.repository.EmpresaRepository

class GestaoService {
	final EmpresaRepository empresaRepository
	final CandidatoRepository candidatoRepository

	GestaoService(EmpresaRepository empresaRepository, CandidatoRepository candidatoRepository) {
		this.empresaRepository = empresaRepository
		this.candidatoRepository = candidatoRepository
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

		List<Competencia> competencias = new ArrayList<>()
		for (String competenciaStr : dados.get('competencias')){
			Competencia competencia = new Competencia(
					null,
					competenciaStr
			)
			competencias.add(competencia)
		}

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

	String removerEmpresa(Integer idEmpresa){
		try {
			return empresaRepository.removerEmpresaPorId(idEmpresa) ? "Empresa removida com sucesso!" : "Id da empresa não existe!"
		} catch (Exception e){
			return e.getMessage()
		}
	}
	String removerCandidato(Integer idCandidato){
		try {
			return candidatoRepository.removerCandidatoPorId(idCandidato) ? "Candidato removido com sucesso!" : "Id do candidato não existe!"
		} catch (Exception e){
			return e.getMessage()
		}
	}
}