package org.linketinder.view

import org.linketinder.model.Person

import java.time.LocalDate

class CandidateView {
	GenericView genericView
	SkillView skillView

	CandidateView(GenericView genericView, SkillView skillView){
		this.genericView = genericView
		this.skillView = skillView
	}

	Map<String, ?> getCandidateInput() {
		println "\n--- Cadastro de Candidato ---"
		print "Nome: "
		String firstName = genericView.getUserInput()

		print "Sobrenome: "
		String lastName = genericView.getUserInput()

		print "Email: "
		String email = genericView.getUserInput()

		String cpf = genericView.getValidPattern("Cpf (11 dígitos): ", /\d{11}/)
		LocalDate dateBirth = genericView.getInputData("Data de Nascimento (dd/mm/aaaa): ")
		String zipCode = genericView.getValidPattern("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String description = genericView.getUserInput()

		List<String> skills = skillView.getSkillsInput()

		print("Senha de login: ")
		String password = genericView.getUserInput()

		print "País onde reside: "
		String country = genericView.getUserInput()

		List<Map<String, ?>> formations = getFormationInput()

		return [
				firstName: firstName,
				email: email,
				skills: skills,
				cpf: cpf,
				description: description,
				zipCode: zipCode,
				dateBirth: dateBirth,
				formations: formations,
				country: country,
				password: password,
				lastName: lastName
		]
	}

	List<Map<String, ?>> getFormationInput(){
		List<Map<String, ?>> formations = []
		boolean add = true
		while (add){
			println "Formações :"
			println "1. Adicionar "
			println "2. Pular"
			String option = genericView.getUserInput()
			switch (option){
				case "1":
					print "Nome da formação: "
					String name = genericView.getUserInput()

					print "Nome da instituição: "
					String institution = genericView.getUserInput()

					LocalDate dateStart = genericView.getInputData("Data de início: ")
					LocalDate dateEnd = genericView.getInputData("Data de fim/previsão: ")

					Map<String, ?> formation = new HashMap<>()
					formation.put("name", name )
					formation.put("institution", institution)
					formation.put("dateStart", dateStart)
					formation.put("dateEnd", dateEnd)
					formations.add(formation)
					break
				case "2":
					add = false
					break
				default:
					genericView.showInvalidOption()
			}
		}
		return formations
	}

	Integer getIdCandidateInput(){
		print "Informe o id do candidato: "
		return genericView.getIdValid()
	}

	static void showCandidates(List<Person> candidates) {
		println "--- Lista de Candidatos ---"
		if (candidates.isEmpty()) {
			println "Nenhum candidato(a) cadastrado(a)."
		} else {
			candidates.each { Person it -> println it }
		}
	}
}
