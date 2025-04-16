package org.linketinder.model

abstract class Person implements PersonInterface {
    Integer id
    String name
    String email
    Address address
    String description
    String passwordLogin

    Person(Integer id,
           String name,
           String email,
           Address address,
           String description,
           String passwordLogin)
    {
        this.id = id
        this.name = name
        this.email = email
        this.address = address
        this.description = description
        this.passwordLogin = passwordLogin
    }

    @Override
    String toString() {
        return """
        Id: ${id}
        Nome: ${name}
        Email: ${email}
        Endereço: ${address}
        Descrição: $description"""
    }
}
