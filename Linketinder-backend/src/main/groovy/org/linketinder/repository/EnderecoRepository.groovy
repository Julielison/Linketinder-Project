package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.SQLException

class EnderecoRepository {
	Sql sql

	EnderecoRepository(Sql sql){
		this.sql = sql
	}

	Integer obterIdPais(String nomePais) {
		try {
			GroovyRowResult paisRow = sql.firstRow("SELECT id FROM PAIS_DE_RESIDENCIA WHERE nome = ?", [nomePais])
			if (paisRow) {
				return paisRow.id as Integer
			}
		} catch (SQLException e) {
			e.printStackTrace()
		} catch (Exception e){
			e.printStackTrace()
		}
		return null
	}

	Integer inserirEndereco(String cep, Integer paisId) {
		def enderecoRow = sql.firstRow("SELECT id FROM ENDERECO WHERE cep = ?", [cep])
		if (enderecoRow) {
			return enderecoRow.id as Integer
		} else {
			def keys = sql.executeInsert("INSERT INTO ENDERECO (cep, pais_id) VALUES (?, ?)", [cep, paisId])
			return keys[0][0] as Integer
		}
	}
	Integer inserirPais(String nome) {
		try {
			def row = sql.executeInsert("INSERT INTO PAIS_DE_RESIDENCIA(nome) VALUES(?)", [nome])
			return row[0][0] as Integer
		} catch (SQLException e) {
			e.printStackTrace()
			return null
		}
	}
}
