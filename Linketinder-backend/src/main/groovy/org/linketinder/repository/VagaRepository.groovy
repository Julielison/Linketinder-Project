package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Vaga

class VagaRepository {
	static List<Vaga> vagas = []
	Sql sql

	VagaRepository(Sql sql){
		this.sql = sql
	}

    List<Vaga> getVagas() {
        vagas.clear()
        String query = """
            SELECT 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.local AS vaga_local,
                v.id_empresa AS empresa_id
            FROM 
                VAGA v"""

        try {
            sql.eachRow(query) { row ->
                Vaga vaga = new Vaga(
                        row.vaga_id as Integer,
                        row.vaga_nome as String,
                        row.vaga_descricao as String,
                        row.vaga_local as String,
                        row.empresa_id as Integer
                )
                vagas.add(vaga)
            }
            return vagas
        } catch (Exception e) {
            println("Erro ao buscar vagas: ${e.message}")
            e.printStackTrace()
            return []
        }
    }

    List<Vaga> getVagasByEmpresaId(Integer empresaId) {
        List<Vaga> vagasEmpresa = []
        String query = """
            SELECT 
                v.id AS vaga_id,
                v.nome AS vaga_nome,
                v.descricao AS vaga_descricao,
                v.local AS vaga_local,
                v.id_empresa AS empresa_id
            FROM 
                VAGA v
            WHERE 
                v.id_empresa = ?"""

        try {
            sql.eachRow(query, [empresaId]) { row ->
                Vaga vaga = new Vaga(
                        row.vaga_id as Integer,
                        row.vaga_nome as String,
                        row.vaga_descricao as String,
                        row.vaga_local as String,
                        row.empresa_id as Integer
                )
                vagasEmpresa.add(vaga)
            }
            return vagasEmpresa
        } catch (Exception e) {
            println("Erro ao buscar vagas da empresa ID ${empresaId}: ${e.message}")
            e.printStackTrace()
            return []
        }
    }

    void addVaga(Vaga vaga) {
        try {
            def keys = sql.executeInsert("""
                INSERT INTO VAGA (nome, descricao, local, id_empresa)
                VALUES (?, ?, ?, ?)
            """, [
                vaga.nome,
                vaga.descricao,
                vaga.local,
                vaga.idEmpresa
            ])
            
            vaga.id = keys[0][0] as Integer
            
            println("Vaga '${vaga.nome}' adicionada com sucesso!")
            
        } catch (Exception e) {
            println("Erro ao adicionar vaga: ${e.message}")
            e.printStackTrace()
        }
    }

    void updateVaga(Vaga vaga) {
        try {
            sql.executeUpdate("""
                UPDATE VAGA 
                SET nome = ?, descricao = ?, local = ?, id_empresa = ?
                WHERE id = ?
            """, [
                vaga.nome,
                vaga.descricao,
                vaga.local,
                vaga.idEmpresa,
                vaga.id
            ])
            
            println("Vaga '${vaga.nome}' atualizada com sucesso!")
            
        } catch (Exception e) {
            println("Erro ao atualizar vaga: ${e.message}")
            e.printStackTrace()
        }
    }

    void deleteVaga(Integer vagaId) {
        try {
            sql.executeUpdate("DELETE FROM VAGA_COMPETENCIA WHERE id_vaga = ?", [vagaId])
            sql.executeUpdate("DELETE FROM CANDIDATO_CURTE_VAGA WHERE id_vaga = ?", [vagaId])
            
            sql.executeUpdate("DELETE FROM VAGA WHERE id = ?", [vagaId])
            
            println("Vaga ID ${vagaId} removida com sucesso!")
            
        } catch (Exception e) {
            println("Erro ao remover vaga: ${e.message}")
            e.printStackTrace()
        }
    }
}