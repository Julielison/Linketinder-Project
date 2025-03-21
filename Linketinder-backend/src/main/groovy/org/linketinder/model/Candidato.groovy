package org.linketinder.model

class Candidato extends Pessoa {
    String cpf
    int idade
    String estado
    String cep
    String descricao
    List<String> competencias

    Candidato(String nome, String email, String cpf, int idade, String estado, String cep, String descricao, List<String> competencias) {
        super(nome, email)
        this.cpf = cpf
        this.idade = idade
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }

    @Override
    String toString() {
        return "Candidato: ${nome} | Email: ${email} | CPF: ${cpf} | Idade: ${idade} | Estado: ${estado} | CEP: ${cep} | Descrição: ${descricao} | Competências: ${competencias.join(', ')}"
    }
}
