package org.linketinder.model

import org.linketinder.util.Util

import java.time.LocalDate

class Formacao {
    Integer id
    String instituicao
    String nomeCurso
    LocalDate dataInicio
    LocalDate dataFim
    
    Formacao(Integer id,
             String instituicao,
             String nomeCurso,
             LocalDate dataInicio,
             LocalDate dataFim)
    {
        this.id = id
        this.instituicao = instituicao
        this.nomeCurso = nomeCurso
        this.dataInicio = dataInicio
        this.dataFim = dataFim
    }

    static List<Formacao> extractFormationsData(String formationsStr) {
        List<Formacao> formations = []
        formationsStr.toString().split(';').each { String formationStr ->
            String[] formationData = formationStr.split(':')
            if (formationData.length > 0){
                Integer id = formationData[0].toInteger()
                String name = formationData[1]
                String institution = formationData[2]
                LocalDate date_start = LocalDate.parse(formationData[3])
                LocalDate date_end = LocalDate.parse(formationData[4])
                formations.add(new Formacao(id, institution, name, date_start, date_end))
            }
        }
        return formations
    }
    
    @Override
    String toString() {
        String periodo = "${Util.convertToBrFormat(dataInicio)} - ${Util.convertToBrFormat(dataFim)}"
        return "${nomeCurso} - ${instituicao} (${periodo})"
    }
}