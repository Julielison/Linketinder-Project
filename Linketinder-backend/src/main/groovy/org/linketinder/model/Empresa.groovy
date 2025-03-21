package org.linketinder.model

class Empresa extends Pessoa {
    String cnpj
    String pais
    String estado
    String cep
    String descricao
    List<String> competencias

    Empresa(String nome, String email, String cnpj, String pais, String estado, String cep, String descricao, List<String> competencias) {
        super(nome, email)
        this.cnpj = cnpj
        this.pais = pais
        this.estado = estado
        this.cep = cep
        this.descricao = descricao
        this.competencias = competencias
    }

    @Override
    String toString() {
        return "Empresa: ${nome} | Email: ${email} | CNPJ: ${cnpj} | País: ${pais} | Estado: ${estado} | CEP: ${cep} | Descrição: ${descricao} | Competências: ${competencias.join(', ')}"
    }
}
