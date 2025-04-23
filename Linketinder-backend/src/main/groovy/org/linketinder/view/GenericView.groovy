package org.linketinder.view

import org.linketinder.enums.MenuOption
import org.linketinder.util.convertUtil

import java.time.LocalDate

class GenericView {

	GenericView(){}

	static void showMenu() {
		println "=== Menu ==="
		Integer lastOption = 1
		MenuOption.values().take(MenuOption.values().size() - lastOption).each { MenuOption option ->
			println "${option.value}. ${option.name().replace('_', ' ').toLowerCase().capitalize()}"
		}
		print "Escolha uma opção: "
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

	static Integer getIdValid() {
		Integer id
		while (true) {
			String input = getUserInput()
			try {
				id = input.toInteger()
				if (id > 0) {
					break
				}
			} catch (Exception ignored) {
				println("Insira um número inteiro maior que zero!")
			}
		}
		return id
	}

	private static boolean inputIsValid(String input) {
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

	static LocalDate getInputData(String label){
		LocalDate date
		while (true) {
			print label
			try {
				String dateStr = getUserInput()
				if (dateStr ==~ /\d{2}\/\d{2}\/\d{4}/) {
					date = convertUtil.convertToLocalDate(dateStr, "dd/MM/yyyy")
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

	static String getValidPattern(String label, String pattern){
		String inputvalido
		while (true){
			print label
			inputvalido = getUserInput()
			if(inputvalido ==~ pattern){
				break
			}
			println("Entrada inválida! Digite apenas dígitos e na quantidade esperada!")
		}
		return inputvalido

	}
}