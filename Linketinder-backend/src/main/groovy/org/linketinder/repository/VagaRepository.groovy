package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Competencia
import org.linketinder.model.Vaga

import java.sql.SQLException

class VagaRepository {
	static List<Vaga> vagas = []
	Sql sql
    CompetenciaRepository competenciaRepository

	VagaRepository(Sql sql, CompetenciaRepository competenciaRepository){
		this.sql = sql
        this.competenciaRepository = competenciaRepository

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
                Integer vagaId = row.vaga_id as Integer
                Vaga vaga = new Vaga(
                        vagaId,
                        row.vaga_nome as String,
                        row.vaga_descricao as String,
                        row.vaga_local as String,
                        row.empresa_id as Integer,
                        competenciaRepository.getCompetenciasPorIdDaVaga(vagaId)
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
                Integer vagaId = row.vaga_id as Integer
                Vaga vaga = new Vaga(
                        vagaId,
                        row.vaga_nome as String,
                        row.vaga_descricao as String,
                        row.vaga_local as String,
                        row.empresa_id as Integer,
                        competenciaRepository.getCompetenciasPorIdDaVaga(vagaId)
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

    Integer inserirVaga(Vaga vaga){
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
        } catch (SQLException e){
            e.printStackTrace()
        }
        return vaga.id
    }

    void inserirIdVagaCompetencia(List<Competencia> competencias, Integer idVaga) {
       try {
           competencias.each {Competencia competencia -> {
                sql.executeInsert("INSERT INTO vaga_competencia(id_vaga, id_competencia) VALUES (?, ?)", [idVaga, competencia.id])
           }}
        } catch (SQLException e) {
            e.printStackTrace()
        }
    }
}