package org.linketinder.model

import org.linketinder.util.ConvertUtil

import java.time.LocalDate

class Candidate extends Person {
    String lastName
    String cpf
    LocalDate dateBirth
    List<Skill> skills
    List<Formation> formations

    Candidate(Integer id,
              String firstName,
              String email,
              String cpf,
              LocalDate dateBirth,
              Address address,
              String descricao,
              String passwordLogin,
              List<Skill> skills,
              List<Formation> formations,
              String lastName)
    {
        super(id, firstName, email, address, descricao, passwordLogin)
        this.cpf = cpf
        this.lastName = lastName
        this.dateBirth = dateBirth
        this.skills = skills
        this.formations = formations
    }

    @Override
    String toString() {
        return super.toString() + """
        Sobrenome: ${lastName}
        CPF: ${cpf}
        Data de Nascimento: ${ConvertUtil.convertToBrFormat(dateBirth)}
        Competências: ${!skills.isEmpty() ? skills.join(', ') : 'Nenhuma'}
        Formações: ${!formations.isEmpty() ? formations.join('\n\t') : 'Nenhuma'}"""
    }
}