package org.linketinder.view

import org.linketinder.enums.MenuOption
import org.linketinder.model.Competencia
import org.linketinder.model.Pessoa
import org.linketinder.model.Vaga
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

	static void showPessoas(List<Pessoa> pessoas, String tipoPessoa) {
		println "--- Lista de ${tipoPessoa} ---"
		if (pessoas.isEmpty()) {
			println "Nenhum ${tipoPessoa} cadastrado(a)."
		} else {
			pessoas.each { Pessoa it -> println it }
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
		String nome = getUserInput()

		print "Email: "
		String email = getUserInput()

		String cnpj = getPadraoValido("CNPJ (14 dígitos): ", /\d{14}/)

		print "País: "
		String pais = getUserInput()

		String cep = getPadraoValido("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String descricao = getUserInput()

		print "Senha de login: "
		String senha = getUserInput()

		return [
				nome: nome,
				email: email,
				cnpj: cnpj,
				pais: pais,
				cep: cep,
				descricao: descricao,
				senha: senha
		]
	}

	static Map<String, ?> getCandidatoInput() {
		println "\n--- Cadastro de Candidato ---"
		print "Nome: "
		String nome = getUserInput()

		print "Sobrenome: "
		String sobrenome = getUserInput()

		print "Email: "
		String email = getUserInput()

		String cpf = getPadraoValido("Cpf (11 dígitos): ", /\d{11}/)

		LocalDate dataNascimento = getInputData("Data de Nascimento (dd/mm/aaaa): ")

		String cep = getPadraoValido("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String descricao = getUserInput()

		print "Competências (separadas por vírgula): "
		List<String> competencias = getUserInput().split(",").collect { it.trim() }

		print("Senha de login: ")
		String senha = getUserInput()

		print "País onde reside: "
		String pais = getUserInput()

		List<Map<String, ?>> formacoes = new ArrayList<>()

		boolean adicionar = true
		while (adicionar){
			println "Formações :"
			println "1. Adicionar "
			println "2. Pular"
			String opcao = getUserInput()
			switch (opcao){
				case "1":
					formacoes.add(getFormacaoInput())
					break
				case "2":
					adicionar = false
					break
				default:
					showInvalidOption()
			}
		}

		return [
				nome: nome,
				email: email,
				competencias: competencias,
				cpf: cpf,
				descricao: descricao,
				cep: cep,
				dataNascimento: dataNascimento,
				formacoes: formacoes,
				pais: pais,
				senha: senha,
				sobrenome: sobrenome
		]
	}

	static Map<String, ?> getFormacaoInput(){
		print "Nome da formação: "
		String nome = getUserInput()

		print "Nome da instituição: "
		String instituicao = getUserInput()

		LocalDate dadtaInicio = getInputData("Data de início: ")
		LocalDate dataFim = getInputData("Data de fim/previsão: ")

		return [
				nome: nome,
				instituicao: instituicao,
				dataIncio: dadtaInicio,
				dataFim: dataFim
		]
	}

	static LocalDate getInputData(String label){
		LocalDate data
		while (true) {
			print label
			try {
				String dataStr = getUserInput()
				if (dataStr ==~ /\d{2}\/\d{2}\/\d{4}/) {
					data = Util.convertToLocalDate(dataStr, "dd/MM/yyyy")
					break
				} else {
					println "Por favor, insira uma data válida no formato dd/mm/aaaa."
				}
			} catch (Exception e) {
				println "Erro ao ler a data: ${e.message}"
			}
		}
		return data
	}

	static void showVagas(List<Vaga> vagas){
		println(" --- Vagas ---")
		vagas.forEach {it -> println(it)}
	}

	static void showCompetencias(List<Competencia> competencias){
		println(" --- Competencias ---")
		competencias.forEach {Competencia it -> println(it)}
	}
	static Integer getIdEmpresaInput(){
		print "Informe o id da empresa: "
		return getIdValido()
	}
	static Integer getIdCandidatoInput(){
		print "Informe o id do candidato: "
		return getIdValido()
	}
	static Integer getIdVagaInput(){
		print "Informe o id da vaga: "
		return getIdValido()
	}
	static Integer getIdValido(){
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
		return getIdValido()
	}
	static Map<String, ?> getDadosVagaInput(){
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
	static String getPadraoValido(String label, def padrao){
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
