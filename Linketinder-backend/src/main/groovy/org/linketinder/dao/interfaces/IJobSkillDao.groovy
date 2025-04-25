package org.linketinder.dao.interfaces

interface IJobSkillDao {
	void associateSkillsToJob(Integer jobId, List<Integer> skillsIds)
}
