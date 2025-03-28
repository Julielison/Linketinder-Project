package org.linketinder.view

import org.linketinder.model.Competencia
import org.linketinder.model.Pessoa
import org.linketinder.model.Vaga

import java.text.SimpleDateFormat

class MenuView {
	static void showMenu() {
		println "\n=== Menu ==="
		println "1. Listar dados dos candidatos"
		println "2. Listar dados das Empresas"
		println "3. Adicionar Empresa"
		println "4. Remover Empresa"
		println "5. Adicionar Candidato"
		println "6. Remover candidato"
		println "7. Listar todas as vagas"
		println "8. Listar todas as competências"
		println "9. Adicionar vaga"
		println "10. Remover vaga"
		println "11. Remover competencia"
		println "0. Sair"
		print "Escolha uma opção: "
	}

	static void showPessoas(List<Pessoa> pessoas, String tipoPessoa) {
		println "\n--- Lista de ${tipoPessoa} ---"
		if (pessoas.isEmpty()) {
			println "Nenhum ${tipoPessoa} cadastrado."
		} else {
			pessoas.each {Pessoa it -> println it }
		}
	}

	static String getUserInput() {
		String input = System.in.newReader().readLine()
		if (input == null || input.trim().isEmpty()) {
			println "Entrada vazia. Por favor, digite um número."
			return null
		}
		return input
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
		String nome = System.in.newReader().readLine()

		print "Email: "
		String email = System.in.newReader().readLine()

		String cnpj = getPadraoValido("CNPJ (14 dígitos): ", /\d{14}/)

		print "País: "
		String pais = System.in.newReader().readLine()

		String cep = getPadraoValido("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String descricao = System.in.newReader().readLine()

		print "Senha de login: "
		String senha = System.in.newReader().readLine()

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
		String nome = System.in.newReader().readLine()

		print "Sobrenome: "
		String sobrenome = System.in.newReader().readLine()

		print "Email: "
		String email = System.in.newReader().readLine()

		String cpf = getPadraoValido("Cpf (11 dígitos): ", /\d{11}/)

		Date dataNascimento = getInputData("Data de Nascimento (dd/mm/aaaa): ")

		String cep = getPadraoValido("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String descricao = System.in.newReader().readLine()

		print "Competências (separadas por vírgula): "
		List<String> competencias = System.in.newReader().readLine().split(",").collect { it.trim() }

		print("Senha de login: ")
		String senha = System.in.newReader().readLine()

		print "País onde reside: "
		String pais = System.in.newReader().readLine()

		List<Map<String, ?>> formacoes = new ArrayList<>()

		boolean adicionar = true
		while (adicionar){
			println "Formações :"
			println "1. Adicionar "
			println "2. Pular"
			String opcao = System.in.newReader().readLine()
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
		String nome = System.in.newReader().readLine()

		print "Nome da instituição: "
		String instituicao = System.in.newReader().readLine()

		Date dadtaInicio = getInputData("Data de início: ")
		Date dataFim = getInputData("Data de fim/previsão: ")

		return [
				nome: nome,
				instituicao: instituicao,
				dataIncio: dadtaInicio,
				dataFim: dataFim
		]
	}

	static Date getInputData(String label){
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy")
		Date data

		while (true) {
			print label
			try {
				String dataStr = System.in.newReader().readLine()
				if (dataStr ==~ /\d{2}\/\d{2}\/\d{4}/) {
					data = dateFormat.parse(dataStr)
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
