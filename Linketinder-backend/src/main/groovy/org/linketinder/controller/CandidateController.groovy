package org.linketinder.controller

import org.linketinder.model.Candidate
import org.linketinder.service.CandidateService

class CandidateController {
	CandidateService candidateService

	CandidateController(CandidateService candidateService) {
		this.candidateService = candidateService
	}

	List<Candidate> getAllCandidates() {
		return candidateService.listAllCandidates()
	}

	String createCandidate(Map<String, String> data) {
		return candidateService.registerCandidate(data)
	}

	String deleteCandidateById(Integer id){
		return candidateService.removeCandidate(id)
	}
}