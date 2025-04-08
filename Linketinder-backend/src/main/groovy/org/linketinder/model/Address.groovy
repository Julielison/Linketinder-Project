package org.linketinder.model

class Address {
	Integer id
	String cep
	Country country

	Address(Integer id, String cep, Country country) {
		this.id = id
		this.cep = cep
		this.country = country
	}

	@Override
	String toString() {
		return "Cep: $cep - País: $country"
	}
}
