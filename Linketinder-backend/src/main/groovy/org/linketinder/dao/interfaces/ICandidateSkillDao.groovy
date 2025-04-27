package org.linketinder.dao.interfaces

interface ICandidateSkillDao {
	void associateSkillsToCandidate(Integer candidateId, List<Integer> skillsIds) throws Exception;
}
