package org.linketinder.view

import org.linketinder.model.Job

class JobView {
	GenericView genericView
	SkillView skillView

	JobView(GenericView genericView, SkillView skillView) {
		this.genericView = genericView
		this.skillView = skillView
	}

	static void showJobs(List<Job> jobs){
		println(" --- Vagas ---")
		jobs.forEach { Job it -> println(it)}
	}

	Integer getIdJobInput(){
		print "Informe o id da vaga: "
		return genericView.getIdValid()
	}

	Map<String, ?> getDataJobInput(){
		print "Nome da vaga: "
		String name = genericView.getUserInput()

		print "Descrição: "
		String description = genericView.getUserInput()

		print "Local da vaga: "
		String local = genericView.getUserInput()

		List<String> skills = skillView.getSkillsInput()

		return [
				name: name,
				description: description,
				local: local,
				skills: skills
		]
	}
}
