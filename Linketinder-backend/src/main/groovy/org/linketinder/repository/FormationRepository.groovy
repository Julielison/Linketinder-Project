package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Candidate
import org.linketinder.model.Formation

import java.time.LocalDate

class FormationRepository {
    Sql sql

    FormationRepository(Sql sql){
        this.sql = sql
    }
    
    List<Formation> getFormacoesByIdCandidato(Integer candidatoId) {
        List<Formation> formacoes = []

        try {
            String query = """
                SELECT 
                    f.id AS formacao_id,
                    f.nome AS formacao_nome,
                    f.instituicao AS formacao_instituicao,
                    fc.data_inicio AS formacao_data_inicio,
                    fc.data_fim_previsao AS formacao_data_fim
                FROM 
                    FORMACAO f
                JOIN 
                    FORMACAO_CANDIDATO fc ON f.id = fc.id_formacao
                WHERE 
                    fc.id_candidato = ?
            """
            
            sql.eachRow(query, [candidatoId]) { row ->
                Date dataInicio = row.formacao_data_inicio ? row.formacao_data_inicio as Date : null
                Date dataFim = row.formacao_data_fim ? row.formacao_data_fim as Date : null
                
                Formation formacao = new Formation(
                    row.formacao_id as Integer,
                    row.formacao_instituicao as String,
                    row.formacao_nome as String,
                    dataInicio,
                    dataFim
                )
                
                formacoes.add(formacao)
            }
            
            return formacoes
            
        } catch (Exception e) {
            println("Erro ao buscar formações do candidato ID ${candidatoId}: ${e.message}")
            e.printStackTrace()
            return []
        }
    }

    Candidate insertFormations(Candidate candidato) {
        try {
            candidato.formations.each { Formation formation ->
                List<List<Object>> result = sql.executeInsert(
                        """INSERT INTO formacao (nome,  instituicao) VALUES (?, ?)""",
                        [formation.nameCourse, formation.institution])
                if (result){
                    formation.setId(result[0][0] as Integer)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return candidato
    }

    static List<Formation> extractFormationsData(String formationsStr) {
        List<Formation> formations = []
        formationsStr.toString().split(';').each { String formationStr ->
            String[] formationData = formationStr.split(':')
            if (formationData.length > 0){
                Integer id = formationData[0].toInteger()
                String name = formationData[1]
                String institution = formationData[2]
                LocalDate date_start = LocalDate.parse(formationData[3])
                LocalDate date_end = LocalDate.parse(formationData[4])
                formations.add(new Formation(id, institution, name, date_start, date_end))
            }
        }
        return formations
    }
}