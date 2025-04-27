package org.linketinder.service

import org.linketinder.dao.impl.CandidateDao
import org.linketinder.model.Address
import org.linketinder.model.Candidate
import org.linketinder.model.Country
import org.linketinder.model.Formation
import org.linketinder.model.Person
import org.linketinder.model.Skill
import org.linketinder.util.ConvertUtil

import java.time.LocalDate

class CandidateService {
	CandidateDao candidateDao

	CandidateService(CandidateDao candidateDao) {
		this.candidateDao = candidateDao
	}

	List<Candidate> listAllCandidates() {
		List<Map<String, Object>> rawCandidates = candidateDao.getCandidatesRawData()
		return setupCandidatesToController(rawCandidates)
	}

	String registerCandidate(Map<String, String> data) {
		Candidate newCandidate = setupCandidateToDao(data)
		String feedback = "Candidato cadastrado com sucesso!"
		try {
			candidateDao.addAllDataFromCandidate(newCandidate)
		} catch (Exception e){
			feedback = e.getMessage()
		}
		return feedback
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
				data.dateBirth as LocalDate,
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
			LocalDate dateOfBirth = ConvertUtil.convertToLocalDate(row['candidato_data_nascimento'].toString(), 'yyyy-MM-dd')
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

	String removeCandidate(Integer id) {
		return candidateDao.removeCandidateById(id) ? "Candidato removido com sucesso!" : "Candidato não existe!"
	}
}