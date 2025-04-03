package org.linketinder.model

import org.linketinder.util.Util

import java.time.LocalDate

class Candidato extends Pessoa {
    String sobrenome
    String cpf
    LocalDate dataNascimento
    List<Competencia> competencias
    List<Formacao> formacoes

    Candidato(Integer id,
              String nome,
              String email,
              String cpf,
              LocalDate dataNascimento,
              String cep,
              String descricao,
              String senhaLogin,
              String paisOndeReside,
              List<Competencia> competencias,
              List<Formacao> formacoes,
              String sobrenome)
    {
        super(id, nome, email, cep, descricao, senhaLogin, paisOndeReside)
        this.cpf = cpf
        this.sobrenome = sobrenome
        this.dataNascimento = dataNascimento
        this.competencias = competencias
        this.formacoes = formacoes
    }


    @Override
    String toString() {
        return super.toString() + """
        Sobrenome: ${sobrenome}
        CPF: ${cpf}
        Data de Nascimento: ${Util.convertToBrFormat(dataNascimento)}
        Competências: ${competencias.join(', ')}
        Formações: ${formacoes.join('\n\t')}"""
    }
}
