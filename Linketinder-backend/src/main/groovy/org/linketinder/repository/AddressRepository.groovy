package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.SQLException

class AddressRepository {
	Sql sql

	AddressRepository(Sql sql){
		this.sql = sql
	}

	Integer getIdCountry(String nameCountry) {
		try {
			GroovyRowResult row = sql.firstRow("SELECT id FROM PAIS_DE_RESIDENCIA WHERE nome = ?", [nameCountry])
			return row?.id as Integer
		} catch (Exception e) {
			e.printStackTrace()
			return null
		}
	}

	Integer insertAddress(String cep, Integer paisId) {
		GroovyRowResult enderecoRow = sql.firstRow("SELECT id FROM ENDERECO WHERE cep = ?", [cep])
		if (enderecoRow) {
			return enderecoRow.id as Integer
		} else {
			List<List<Object>> keys = sql.executeInsert("INSERT INTO ENDERECO (cep, pais_id) VALUES (?, ?)", [cep, paisId])
			return keys[0][0] as Integer
		}
	}
	Integer insertCountry(String nome) {
		try {
			List<List<Object>> row = sql.executeInsert("INSERT INTO PAIS_DE_RESIDENCIA(nome) VALUES(?)", [nome])
			return row[0][0] as Integer
		} catch (SQLException e) {
			e.printStackTrace()
			return null
		}
	}
}
