package org.linketinder.dao

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.dao.interfaces.IAddressDao

import java.sql.SQLException

class AddressDao implements IAddressDao{
	Sql sql
	CountryDao countryDao

	AddressDao(Sql sql, CountryDao countryDao){
		this.sql = sql
		this.countryDao = countryDao
	}

	Integer insertAddressReturningId(String cep, Integer paisId) {
		try {
			List<List<Object>> keys = sql.executeInsert(
					"""
            INSERT INTO ENDERECO (cep, pais_id) 
            VALUES (?, ?) 
            ON CONFLICT (cep) DO UPDATE 
            SET pais_id = EXCLUDED.pais_id 
            RETURNING id
            """,
					[cep, paisId]
			)
			return keys[0][0] as Integer
		} catch (SQLException e) {
			e.printStackTrace()
			return null
		}
	}

	Integer insertCountryReturningId(String name){
		countryDao.insertReturningId(name)
	}
}
