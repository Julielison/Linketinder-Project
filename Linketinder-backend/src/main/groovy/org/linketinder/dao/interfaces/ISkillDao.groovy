package org.linketinder.dao.interfaces

import org.linketinder.model.Skill

interface ISkillDao {
	List<Map<String, Object>> getAll()
	List<Integer> insertSkillsReturningId(List<Skill> skills)
	boolean removeSkillById(Integer id)
}