package org.linketinder.dao.interfaces

import org.linketinder.model.Candidate

interface IFormationCandidateDao {
	void insertIdsAndDatesFromFormation(Candidate candidate) throws Exception;
}
