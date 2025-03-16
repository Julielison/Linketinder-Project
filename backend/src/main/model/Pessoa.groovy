package main.model

abstract class Pessoa implements PessoaInterface {
    String nome
    String email

    Pessoa(String nome, String email) {
        this.nome = nome
        this.email = email
    }

    String getNome() {
        return nome
    }
    String getEmail() {
        return email
    }
}
