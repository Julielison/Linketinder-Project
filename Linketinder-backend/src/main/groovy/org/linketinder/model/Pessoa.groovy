package org.linketinder.model

abstract class Pessoa implements PessoaInterface {
    Integer id
    String nome
    String email
    Address address
    String descricao
    String senhaLogin

    Pessoa(Integer id,
           String nome,
           String email,
           Address address,
           String descricao,
           String senhaLogin)
    {
        this.id = id
        this.nome = nome
        this.email = email
        this.address = address
        this.descricao = descricao
        this.senhaLogin = senhaLogin
    }

    @Override
    String toString() {
        return """
        Id: ${id}
        Nome: ${nome}
        Email: ${email}
        Endereço: ${address}
        Descrição: ${descricao}"""
    }
}
