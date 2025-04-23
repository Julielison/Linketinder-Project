package org.linketinder.view

import org.linketinder.model.Person

class CompanyView {
	GenericView genericView

	CompanyView(GenericView genericView) {
		this.genericView = genericView
	}

	Map<String, ?> getCompanyInput() {
		println "\n--- Cadastro de Empresa ---"
		print "Nome: "
		String name = genericView.getUserInput()

		print "Email: "
		String email = genericView.getUserInput()

		String cnpj = genericView.getValidPattern("CNPJ (14 dígitos): ", /\d{14}/)

		print "País: "
		String country = genericView.getUserInput()

		String zipCode = genericView.getValidPattern("Cep (8 dígitos): ", /\d{8}/)

		print "Descrição: "
		String description = genericView.getUserInput()

		print "Senha de login: "
		String password = genericView.getUserInput()

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

	Integer getIdCompanyInput(){
		print "Informe o id da empresa: "
		return genericView.getIdValid()
	}

	static void showCompanies(List<Person> companies) {
		println "--- Lista de Empresas ---"
		if (companies.isEmpty()) {
			println "Nenhuma empresa cadastrada."
		} else {
			companies.each { Person it -> println it }
		}
	}
}