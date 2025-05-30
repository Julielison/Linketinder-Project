package org.linketinder.dao.impl


import groovy.sql.Sql

import java.sql.SQLException

class CountryDao {
	Sql sql

	CountryDao(Sql sql) {
		this.sql = sql
	}

	Integer insertReturningId(String name) {
		try {
			List<List<Object>> rows = sql.executeInsert(
					"""
            INSERT INTO PAIS_DE_RESIDENCIA(nome) 
            VALUES(?) 
            ON CONFLICT (nome) DO UPDATE 
            SET nome = EXCLUDED.nome 
            RETURNING id
            """,
					[name]
			)
			return rows[0][0] as Integer
		} catch (SQLException e) {
			e.printStackTrace()
			return null
		}
	}
}
