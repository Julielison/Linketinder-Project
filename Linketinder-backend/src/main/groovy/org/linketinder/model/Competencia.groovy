package org.linketinder.model

class Competencia {
    Integer id
    String nome

    Competencia(Integer id, String nome) {
        this.id = id
        this.nome = nome
    }

    @Override
    String toString() {
        return nome
    }
}

