package org.linketinder.repository

import groovy.sql.Sql

import java.sql.SQLException

class Repository {
	Sql sql

	Repository(Sql sql){
		this.sql = sql
	}

	boolean removerPorId(String tabela, Integer id) {
		try {
			int rowsAffected = sql.executeUpdate("DELETE FROM ${tabela} WHERE id = ?", [id])
			return rowsAffected > 0
		} catch (SQLException e) {
			e.printStackTrace()
			throw new Exception("Erro ao remover da tabela ${tabela}: ${e.message}")
		}
	}
}