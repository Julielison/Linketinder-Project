package org.linketinder.controller

import org.linketinder.model.Skill
import org.linketinder.service.SkillService

class SkillController {
	SkillService skillService

	SkillController(SkillService skillService) {
		this.skillService = skillService
	}

	List<Skill> getAllSkills(){
		return skillService.listAllSkills()
	}

	String deleteSkillById(Integer id){
		return skillService.removeSkillById(id)
	}
}
