package org.linketinder.enums

enum MenuOption {
	LISTAR_CANDIDATOS("1"),
	CADASTRAR_CANDIDATO("2"),
	REMOVER_CANDIDATO("3"),
	LISTAR_EMPRESAS("4"),
	CADASTRAR_EMPRESA("5"),
	REMOVER_EMPRESA("6"),
	LISTAR_VAGAS("7"),
	CADASTRAR_VAGA("8"),
	REMOVER_VAGA("9"),
	LISTAR_COMPETENCIAS("10"),
	REMOVER_COMPETENCIA("11"),
	SAIR("0"),
	INVALID('-1')

	final String value

	MenuOption(String value) {
		this.value = value
	}

	static MenuOption fromValue(String value) {
		MenuOption option = values().find { MenuOption enumm -> enumm.value == value }
		return option ? option : INVALID
	}
}