package org.linketinder.model

class Vaga {
    Integer id
    String nome
    String descricao
    String local
    Integer idEmpresa
    List<Competencia> competencias

    Vaga(Integer id,
         String nome,
         String descricao,
         String local,
         Integer idEmpresa,
         List<Competencia> competencias)
    {
        this.id = id
        this.nome = nome
        this.descricao = descricao
        this.local = local
        this.idEmpresa = idEmpresa
        this.competencias = competencias
    }

    static List<Vaga> extractJobsData(String jobsData, Integer id_company) {
        List<Vaga> jobs = []
        jobsData.split(';').each { String jobStr ->
            String[] idJobDescriptionLocalSkills = jobStr.split(':')
            if (idJobDescriptionLocalSkills.length > 1) {
                Integer id = idJobDescriptionLocalSkills[0].toInteger()
                String name = idJobDescriptionLocalSkills[1]
                String description = idJobDescriptionLocalSkills[2]
                String local = idJobDescriptionLocalSkills[3]
                List<Competencia> skills = []
                if (idJobDescriptionLocalSkills.length == 5){
                     skills = Competencia.extractSkillsData(idJobDescriptionLocalSkills[4])
                }
                jobs.add(new Vaga(id, name, description, local, id_company, skills))
            }
        }
        return jobs
    }

    @Override
    String toString() {
        return """
            Id: ${id}
            Nome da Vaga: ${nome}
            Descrição: ${descricao}
            Local: ${local}
            Id empresa: ${idEmpresa}
            Competências exigidas: ${competencias}"""
    }

}