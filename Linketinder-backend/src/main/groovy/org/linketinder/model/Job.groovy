package org.linketinder.model

class Job {
    Integer id
    String name
    String description
    String local
    Integer idCompany
    List<Skill> skills

    Job(Integer id,
        String name,
        String description,
        String local,
        Integer idCompany,
        List<Skill> skills)
    {
        this.id = id
        this.name = name
        this.description = description
        this.local = local
        this.idCompany = idCompany
        this.skills = skills
    }

    @Override
    String toString() {
        return """
            Id: ${id}
            Nome da Vaga: ${name}
            Descrição: ${description}
            Local: ${local}
            Id empresa: ${idCompany}
            Competências exigidas: ${!skills.isEmpty() ? skills.join(', ') : "Nenhuma"}"""
    }

}