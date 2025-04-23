package org.linketinder.dao.interfaces

interface IAddressDao {
	Integer insertAddressReturningId(String cep, Integer paisId)
	Integer insertCountryReturningId(String name)
}