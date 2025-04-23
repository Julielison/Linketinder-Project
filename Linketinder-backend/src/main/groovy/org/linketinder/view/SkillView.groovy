package org.linketinder.view

import org.linketinder.model.Skill

class SkillView {
	GenericView genericView

	SkillView(GenericView genericView) {
		this.genericView = genericView
	}

	List<String> getSkillsInput(){
		List<String> skills = []
		boolean add = true
		while (add){
			println "-- Competências --"
			println "1. Adicionar "
			println "2. Pular"
			String option = genericView.getUserInput()
			switch (option){
				case "1":
					print "Nome da competência (ex: Java): "
					skills.add(genericView.getUserInput())
					break
				case "2":
					add = false
					break
				default:
					genericView.showInvalidOption()
			}
		}
		return skills
	}

	static void showSkills(List<Skill> skills){
		println(" --- Competencias ---")
		skills.forEach { Skill it -> println(it)}
	}

	Integer getIdSkillInput() {
		print "Informe o id da competência: "
		return genericView.getIdValid()
	}
}
