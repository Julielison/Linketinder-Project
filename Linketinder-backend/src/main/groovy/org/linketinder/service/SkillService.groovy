package org.linketinder.service

import org.linketinder.dao.impl.SkillDao
import org.linketinder.model.Skill

class SkillService {
	SkillDao skillDao

	SkillService(SkillDao skillDao) {
		this.skillDao = skillDao
	}

	List<Skill> listAllSkills(){
		List<Map<String, Object>> rawSkills = skillDao.getSkillsRawData()
		return setupSkillsToController(rawSkills)
	}

	private static List<Skill> setupSkillsToController(List<Map<String, Object>> rawSkills){
		return rawSkills.collect { Map<String, Object> row ->
			new Skill(row['id'] as Integer, row['nome'] as String)
			}
	}

	static List<Skill> extractSkills(String skillsData) {
		List<Skill> skills = new ArrayList<>()

		skillsData.split(',').each { String skillData ->
			String[] idSkillName = skillData.split('\\.')
			if (idSkillName.length > 0) {
				Integer id = idSkillName[0].toInteger()
				String name = idSkillName[1]
				skills.add(new Skill(id, name))
			}
		}
		return skills
	}

	static List<Skill> extractSkillsFromMap(Map<String, ?> dados){
		List<Skill> skills = []
		for (String skillName : dados.get('skills')){
			Skill skill = new Skill(
					null,
					skillName
			)
			skills.add(skill)
		}
		return skills
	}
}
