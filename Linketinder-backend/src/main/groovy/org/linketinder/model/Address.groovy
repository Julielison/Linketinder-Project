package org.linketinder.model

class Address {
	Integer id
	String zipCode
	Country country

	Address(Integer id, String zipCode, Country country) {
		this.id = id
		this.zipCode = zipCode
		this.country = country
	}

	@Override
	String toString() {
		return "Cep: $zipCode - País: $country"
	}
}
