package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.SQLException

class EmailRepository {
	Sql sql

	EmailRepository(Sql sql){
		this.sql = sql
	}

	Boolean verificarSeEmailJaExiste(String email) throws SQLException {
		GroovyRowResult resultRow = sql.firstRow("SELECT email FROM candidato WHERE email = ?", [email])
		return resultRow ? true : false
	}
}