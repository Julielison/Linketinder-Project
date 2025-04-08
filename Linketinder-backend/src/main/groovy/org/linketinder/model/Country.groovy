package org.linketinder.model

class Country {
	Integer id
	String name

	Country(String name, Integer id) {
		this.name = name
		this.id = id
	}

	@Override
	String toString() {
		return "$name"
	}
}
