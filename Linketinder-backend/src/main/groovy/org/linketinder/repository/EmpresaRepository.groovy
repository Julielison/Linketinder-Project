package org.linketinder.repository

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.linketinder.model.Empresa

import java.sql.SQLException

class EmpresaRepository {
    private static List<Empresa> empresas = []
    private final Sql sql

    EmpresaRepository(Sql sql) {
        this.sql = sql
    }

    List<Empresa> getEmpresas() {
        empresas.clear()
        String query = """
            SELECT 
                e.id AS empresa_id,
                e.nome AS empresa_nome,
                e.email_corporativo AS empresa_email,
                e.cnpj AS empresa_cnpj,
                e.descricao_da_empresa AS empresa_descricao,
                e.senha_de_login AS empresa_senha,
                endereco.cep AS endereco_cep,
                p.nome AS pais_nome
            FROM 
                EMPRESA e
            JOIN 
                ENDERECO endereco ON e.id_endereco = endereco.id
            JOIN 
                PAIS_DE_RESIDENCIA p ON endereco.pais_id = p.id"""

        try {
            sql.eachRow(query) { row ->
                Integer idEmpresa = row.empresa_id

                Empresa empresa = new Empresa(
                        idEmpresa,
                        row.empresa_nome as String,
                        row.empresa_email as String,
                        row.empresa_cnpj as String,
                        row.endereco_cep as String,
                        row.empresa_descricao as String,
                        row.empresa_senha as String,
                        row.pais_nome as String,
                        VagaRepository.getVagasByEmpresaId(idEmpresa)
                )
                empresas.add(empresa)
            }
            return empresas
        } catch (Exception e) {
            println("Erro ao buscar empresas: ${e.message}")
            e.printStackTrace()
            return []
        }
    }

    void addEmpresa(Empresa empresa) {
        String paisOndeReside = empresa.getPaisOndeReside()

        try {
            def paisId = obterIdPais(paisOndeReside)
            if (!paisId) {
                paisId = inserirPais(paisOndeReside)
            }

            def enderecoId = inserirEndereco(empresa.getCep(), paisId)

            def result = sql.executeInsert("""
            INSERT INTO EMPRESA (nome, cnpj, email_corporativo, descricao_da_empresa, senha_de_login, id_endereco)
            VALUES (?, ?, ?, ?, ?, ?)
        """, [
                    empresa.nome,
                    empresa.cnpj,
                    empresa.email,
                    empresa.descricao,
                    empresa.senhaLogin,
                    enderecoId
            ])

            def empresaId = result[0][0]
            empresa.setId(empresaId as Integer)
            empresas.add(empresa)

        } catch (Exception e) {
            println("Erro ao adicionar empresa: ${e.message}")
            e.printStackTrace()
        }
    }

    private Integer obterIdPais(String nomePais) {
        try {
            GroovyRowResult paisRow = sql.firstRow("SELECT id FROM PAIS_DE_RESIDENCIA WHERE nome = ?", [nomePais])
            if (paisRow) {
                return paisRow.id as Integer
            }
        } catch (SQLException e) {
            println(e.getMessage())
            e.printStackTrace()
        }
        return null
    }

    private Integer inserirEndereco(String cep, Integer paisId) {
        def enderecoRow = sql.firstRow("SELECT id FROM ENDERECO WHERE cep = ?", [cep])
        if (enderecoRow) {
            return enderecoRow.id
        } else {
            def keys = sql.executeInsert("INSERT INTO ENDERECO (cep, pais_id) VALUES (?, ?)", [cep, paisId])
            return keys[0][0] as Integer
        }
    }

    private Integer inserirPais(String nome) {
        try {
            def row = sql.executeInsert("INSERT INTO PAIS_DE_RESIDENCIA(nome) VALUES(?)", [nome])
            return row[0][0]
        } catch (SQLException e) {
            e.printStackTrace()
            return null
        }
    }
}