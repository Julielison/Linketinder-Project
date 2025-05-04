package org.linketinder.dao.interfaces

interface IAssociateEntity {
	void associateEntityWithSkill(Integer entityId, List<Integer> skillsIds)
}