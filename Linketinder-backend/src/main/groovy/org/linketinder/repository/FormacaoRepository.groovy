package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Formacao

class FormacaoRepository {
    
    static List<Formacao> getFormacoesByIdCandidato(Integer candidatoId) {
        List<Formacao> formacoes = []
        Sql sql = DatabaseConnection.getInstance()
        
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
    
    static void addFormacao(Formacao formacao, Integer candidatoId) {
        Sql sql = DatabaseConnection.getInstance()
        
        try {
            def formacaoRow = sql.firstRow("""
                SELECT id FROM FORMACAO 
                WHERE nome = ? AND instituicao = ?
            """, [formacao.nomeCurso, formacao.instituicao])
            
            Integer formacaoId
            
            if (!formacaoRow) {
                def keys = sql.executeInsert("""
                    INSERT INTO FORMACAO (nome, instituicao) 
                    VALUES (?, ?)
                """, [formacao.nomeCurso, formacao.instituicao])
                
                formacaoId = keys[0][0] as Integer
            } else {
                formacaoId = formacaoRow.id
            }
            
            sql.executeInsert("""
                INSERT INTO FORMACAO_CANDIDATO (id_formacao, id_candidato, data_inicio, data_fim_previsao)
                VALUES (?, ?, ?, ?)
            """, [
                formacaoId,
                candidatoId,
                formacao.dataInicio,
                formacao.dataFim
            ])
            
            println("Formação adicionada com sucesso para o candidato ID ${candidatoId}")
            
        } catch (Exception e) {
            println("Erro ao adicionar formação para o candidato ID ${candidatoId}: ${e.message}")
            e.printStackTrace()
        }
    }
    
    static void updateFormacao(Formacao formacao, Integer candidatoId) {
        Sql sql = DatabaseConnection.getInstance()
        
        try {
            sql.executeUpdate("""
                UPDATE FORMACAO_CANDIDATO 
                SET data_inicio = ?, data_fim_previsao = ?
                WHERE id_formacao = ? AND id_candidato = ?
            """, [
                formacao.dataInicio,
                formacao.dataFim,
                formacao.id,
                candidatoId
            ])
            
            println("Formação atualizada com sucesso para o candidato ID ${candidatoId}")
            
        } catch (Exception e) {
            println("Erro ao atualizar formação para o candidato ID ${candidatoId}: ${e.message}")
            e.printStackTrace()
        }
    }
    
    static void deleteFormacao(Integer formacaoId, Integer candidatoId) {
        Sql sql = DatabaseConnection.getInstance()
        
        try {
            sql.executeUpdate("""
                DELETE FROM FORMACAO_CANDIDATO 
                WHERE id_formacao = ? AND id_candidato = ?
            """, [formacaoId, candidatoId])
            
            def count = sql.firstRow("""
                SELECT COUNT(*) as total 
                FROM FORMACAO_CANDIDATO 
                WHERE id_formacao = ?
            """, [formacaoId]).total
            
            if (count == 0) {
                sql.executeUpdate("DELETE FROM FORMACAO WHERE id = ?", [formacaoId])
            }
            
            println("Formação removida com sucesso do candidato ID ${candidatoId}")
            
        } catch (Exception e) {
            println("Erro ao remover formação do candidato ID ${candidatoId}: ${e.message}")
            e.printStackTrace()
        }
    }
}