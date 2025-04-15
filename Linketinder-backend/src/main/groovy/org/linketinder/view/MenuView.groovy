package org.linketinder.view

import org.linketinder.enums.MenuOption
import org.linketinder.model.Skill
import org.linketinder.model.Person
import org.linketinder.model.Job
import org.linketinder.util.Util

import java.time.LocalDate

class MenuView {
	static final Integer LAST = 1

	static void showMenu() {
		println "=== Menu ==="
		for (int i = 0; i < MenuOption.values().size() - LAST; i++) {
			MenuOption option = MenuOption.values()[i]
			println "${option.value}. ${option.name().replace('_', ' ').toLowerCase().capitalize()}"
		}
		print "Escolha uma opção: "
	}

	static void showPessoas(List<Person> pessoas, String tipoPessoa) {
		println "--- Lista de ${tipoPessoa} ---"
		if (pessoas.isEmpty()) {
			println "Nenhum ${tipoPessoa} cadastrado(a)."
		} else {
			pessoas.each { Person it -> println it }
		}
	}

	static String getUserInput() {
		String input
		while (true) {
			input = System.in.newReader().readLine()
			if (inputIsValid(input)) {
				return input
			}
			println "Entrada vazia. Por favor, digite o que se pede."
		}
	}

	static boolean inputIsValid(String input){
		return input != null && !input.trim().isEmpty()
	}

	static void showExitMessage() {
		println "\nEncerrando o sistema..."
	}

	static void showInvalidOption() {
		println "\nOpção inválida. Tente novamente."
	}

	static void showFeedback(String feedback){
		println feedback
	}

	static Map<String, ?> getEmpresaInput() {
		println "\n--- Cadastro de Empresa ---"
		print "Nome: "
		String name = getUserInput()

		print "Email: "
		String email = getUserInput()

		String cnpj = getValidPattern("CNPJ (14 dígitos): ", /\d{14}/)

		print "País: "
		String country = getUserInput()

		String zipCode = getValidPattern("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String description = getUserInput()

		print "Senha de login: "
		String password = getUserInput()

		return [
				name: name,
				email: email,
				cnpj: cnpj,
				country: country,
				zipCode: zipCode,
				description: description,
				password: password
		]
	}

	static Map<String, ?> getCandidateInput() {
		println "\n--- Cadastro de Candidato ---"
		print "Nome: "
		String firstName = getUserInput()

		print "Sobrenome: "
		String lastName = getUserInput()

		print "Email: "
		String email = getUserInput()

		String cpf = getValidPattern("Cpf (11 dígitos): ", /\d{11}/)
		LocalDate dateBirth = getInputData("Data de Nascimento (dd/mm/aaaa): ")
		String zipCode = getValidPattern("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String description = getUserInput()

		print "Competências: "
		List<String> skills = getSkillsInput()

		print("Senha de login: ")
		String password = getUserInput()

		print "País onde reside: "
		String country = getUserInput()

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

	static List<String> getSkillsInput(){
		List<String> skills = []
		boolean add = true
		while (add){
			println "-- Competências --"
			println "1. Adicionar "
			println "2. Pular"
			String option = getUserInput()
			switch (option){
				case "1":
					println "Nome da competência"
					skills.add(getUserInput())
					break
				case "2":
					add = false
					break
				default:
					showInvalidOption()
			}
		}
		return skills
	}

	static List<Map<String, ?>> getFormationInput(){
		List<Map<String, ?>> formations = []
		boolean add = true
		while (add){
			println "Formações :"
			println "1. Adicionar "
			println "2. Pular"
			String option = getUserInput()
			switch (option){
				case "1":
					print "Nome da formação: "
					String name = getUserInput()

					print "Nome da instituição: "
					String institution = getUserInput()

					LocalDate dateStart = getInputData("Data de início: ")
					LocalDate dateEnd = getInputData("Data de fim/previsão: ")

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
					showInvalidOption()
			}
		}
		return formations
	}

	static LocalDate getInputData(String label){
		LocalDate date
		while (true) {
			print label
			try {
				String dateStr = getUserInput()
				if (dateStr ==~ /\d{2}\/\d{2}\/\d{4}/) {
					date = Util.convertToLocalDate(dateStr, "dd/MM/yyyy")
					break
				} else {
					println "Por favor, insira uma data válida no formato dd/mm/aaaa."
				}
			} catch (Exception e) {
				println "Erro ao ler a data: ${e.message}"
			}
		}
		return date
	}

	static void showVagas(List<Job> vagas){
		println(" --- Vagas ---")
		vagas.forEach {it -> println(it)}
	}

	static void showCompetencias(List<Skill> competencias){
		println(" --- Competencias ---")
		competencias.forEach { Skill it -> println(it)}
	}
	static Integer getIdCompanyInput(){
		print "Informe o id da empresa: "
		return getIdValid()
	}
	static Integer getIdCandidatoInput(){
		print "Informe o id do candidato: "
		return getIdValid()
	}
	static Integer getIdVagaInput(){
		print "Informe o id da vaga: "
		return getIdValid()
	}
	static Integer getIdValid(){
		Integer id
		while (true){
			String input = getUserInput()
			try {
				id = input.toInteger()
				if (id > 0){
					break
				}
			} catch (Exception ignored){
				println("Insira um número inteiro maior que zero!")
			}
		}
		return id
	}
	static Integer getIdCompetenciaInput() {
		print "Informe o id da competência: "
		return getIdValid()
	}
	static Map<String, ?> getDataJobInput(){
		print "Nome da vaga: "
		String nome = getUserInput()

		print "Descrição: "
		String descricao = getUserInput()

		print "Local da vaga: "
		String local = getUserInput()

		List<String> competencias = getCompetenciasInput()

		return [
		        nome: nome,
				descricao: descricao,
				local: local,
				competencias: competencias
		]

	}
	static List<String> getCompetenciasInput(){
		List<String> competencias = new ArrayList<>()
		boolean adicionar = true
		while (adicionar){
			println " -- Competências --"
			println "1. Adicionar"
			println "2. Próximo"
			String opcao = getUserInput()
			switch (opcao){
				case "1":
					print "Nome (ex: Java): "
					competencias.add(getUserInput())
					break
				case "2":
					adicionar = false
					break
				default:
					showInvalidOption()
			}
		}
		return competencias
	}
	static String getValidPattern(String label, def padrao){
		String inputvalido
		while (true){
			print label
			inputvalido = getUserInput()
			if(inputvalido ==~ padrao){
				break
			}
			println("Entrada inválida! Digite apenas dígitos e na quantidade esperada!")
		}
		return inputvalido

	}
}
