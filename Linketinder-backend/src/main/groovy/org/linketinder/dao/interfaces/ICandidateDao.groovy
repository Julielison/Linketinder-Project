package org.linketinder.dao.interfaces

import org.linketinder.model.Candidate

interface ICandidateDao {
	List<Map<String, Object>> getCandidatesRawData();
	void addAllDataFromCandidate(Candidate candidate);
	boolean removeCandidateById(Integer id);
}
