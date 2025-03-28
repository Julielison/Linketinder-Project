package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Candidato
import org.linketinder.model.Formacao

class FormacaoRepository {
    Sql sql

    FormacaoRepository(Sql sql){
        this.sql = sql
    }
    
    List<Formacao> getFormacoesByIdCandidato(Integer candidatoId) {
        List<Formacao> formacoes = []

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
                
                Formacao formacao = new Formacao(
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
    
    Candidato inserirFormacoes(Candidato candidato) {
        try {
            candidato.formacoes.each {Formacao formacao ->
                def result = sql.executeInsert(
                        """INSERT INTO formacao (nome,  instituicao) VALUES (?, ?)""",
                        [formacao.nomeCurso, formacao.instituicao])
                if (result){
                    formacao.setId(result[0][0] as Integer)
                }
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return candidato
    }
}