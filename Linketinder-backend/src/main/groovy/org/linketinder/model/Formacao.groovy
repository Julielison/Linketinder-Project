package org.linketinder.model

import java.text.SimpleDateFormat

class Formacao {
    Integer id
    String instituicao
    String nomeCurso
    Date dataInicio
    Date dataFim
    
    Formacao(Integer id,
             String instituicao,
             String nomeCurso,
             Date dataInicio,
             Date dataFim) {
        this.id = id
        this.instituicao = instituicao
        this.nomeCurso = nomeCurso
        this.dataInicio = dataInicio
        this.dataFim = dataFim
    }
    
    @Override
    String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy")
        String dataInicioStr = dataInicio ? sdf.format(dataInicio) : "N/A"
        String dataFimStr = dataFim ? sdf.format(dataFim) : "Atual"
        
        String periodo = "${dataInicioStr} - ${dataFimStr}"
        return "${nomeCurso} em ${instituicao} (${periodo})"
    }
}