package org.linketinder.model

import org.linketinder.util.Util

class Candidato extends Pessoa {
    String sobrenome
    String cpf
    Date dataNascimento
    List<Competencia> competencias
    List<Formacao> formacoes

    Candidato(Integer id,
              String nome,
              String email,
              String cpf,
              Date dataNascimento,
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
        Data de Nascimento: ${Util.formatarData(dataNascimento, "dd/MM/yyyy")}
        Competências: ${competencias.join(', ')}
        Formações: ${formacoes.join('\n\t')}"""
    }
}
