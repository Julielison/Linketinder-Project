package org.linketinder.model

abstract class Pessoa implements PessoaInterface {
    Integer id
    String nome
    String email
    String cep
    String descricao
    String senhaLogin
    String paisOndeReside

    Pessoa(Integer id,
           String nome,
           String email,
           String cep,
           String descricao,
           String senhaLogin,
           String paisOndeReside)
    {
        this.id = id
        this.nome = nome
        this.email = email
        this.cep = cep
        this.descricao = descricao
        this.senhaLogin = senhaLogin
        this.paisOndeReside = paisOndeReside
    }

    @Override
    String toString() {
        return """
        Id: ${id}
        Nome: ${nome}
        Email: ${email}
        CEP: ${cep}
        País Onde Reside: ${paisOndeReside}
        Descrição: ${descricao}"""
    }
}
