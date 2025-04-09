package org.linketinder.model

class Competencia {
    Integer id
    String nome

    Competencia(Integer id, String nome) {
        this.id = id
        this.nome = nome
    }

    static List<Competencia> extractSkillsData(String skillsData) {
        List<Competencia> skills = new ArrayList<>()

        skillsData.split(',').each { String skillData ->
            String[] idSkillName = skillData.split('\\.')
            if (idSkillName.length > 0) {
                Integer id = idSkillName[0].toInteger()
                String name = idSkillName[1]
                skills.add(new Competencia(id, name))
            }
        }
        return skills
    }

    @Override
    String toString() {
        return "$nome"
    }
}

