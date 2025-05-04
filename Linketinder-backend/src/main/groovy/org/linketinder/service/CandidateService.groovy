package org.linketinder.service


import org.linketinder.dao.interfaces.ICRUD
import org.linketinder.model.*
import org.linketinder.util.ConvertUtil

import java.time.LocalDate

class CandidateService {
	ICRUD<Candidate> candidateDao

	CandidateService(ICRUD<Candidate> candidateDao) {
		this.candidateDao = candidateDao
	}

	List<Candidate> listAllCandidates() {
		List<Map<String, Object>> rawCandidates = candidateDao.getAll()
		return setupCandidatesToController(rawCandidates)
	}

	Candidate registerCandidate(Map<String, String> data) {
		try {
			Candidate newCandidate = setupCandidateToDao(data)
			return candidateDao.save(newCandidate)
		} catch (Exception e) {
			throw new RuntimeException("Erro ao registrar o candidato.", e)
		}
	}

	static private Candidate setupCandidateToDao(Map<String, String> data){
		List<Formation> formations = FormationService.extractFormationsFromMap(data)
		List<Skill> skills = SkillService.extractSkillsFromMap(data)
		Address address = new Address(null, data.zipCode, new Country(data.country, null))
		Person newCandidate = new Candidate(
				null,
				data.firstName,
				data.email,
				data.cpf,
				LocalDate.parse(data.dateBirth),
				address,
				data.description,
				data.password,
				skills,
				formations,
				data.lastName
		)
		return newCandidate
	}

	static private List<Candidate> setupCandidatesToController(List<Map<String, Object>> rawCandidates){
		return rawCandidates.collect { Map<String, Object> row ->
			List<Skill> skills = SkillService.extractSkills(row['competencias'].toString())
			List<Formation> formations = FormationService.extractFormationsData(row['formacoes'].toString())
			LocalDate dateOfBirth = LocalDate.parse(row['candidato_data_nascimento'].toString())
			Address address = new Address(
					row['endereco_id'] as Integer,
					row['endereco_cep'] as String,
					new Country(row['pais_nome'] as String, row['pais_id'] as Integer)
			)
			new Candidate(
					row['candidato_id'] as Integer,
					row['candidato_nome'] as String,
					row['candidato_email'] as String,
					row['candidato_cpf'] as String,
					dateOfBirth,
					address,
					row['candidato_descricao_pessoal'] as String,
					row['candidato_senha_de_login'] as String,
					skills,
					formations,
					row['candidato_sobrenome'] as String
			)
		}
	}

	boolean removeCandidate(Integer id) {
		return candidateDao.deleteById(id)
	}
}