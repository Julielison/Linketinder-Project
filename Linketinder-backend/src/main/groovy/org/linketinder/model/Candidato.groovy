package org.linketinder.model

class Candidato extends Pessoa {
    String cpf
    Date dataNascimento
    List<Competencia> competencias

    Candidato(Integer id,
              String nome,
              String email,
              String cpf,
              Date dataNascimento,
              String cep,
              String descricao,
              String senhaLogin,
              String paisOndeReside,
              List<Competencia> competencias)
    {
        super(id, nome, email, cep, descricao, senhaLogin, paisOndeReside)
        this.cpf = cpf
        this.dataNascimento = dataNascimento
        this.competencias = competencias
    }


    @Override
    String toString() {
        return super.toString() + """
        CPF: ${cpf}
        Data de Nascimento: ${dataNascimento}
        Competências: ${competencias.join(', ')}"""
    }
}
