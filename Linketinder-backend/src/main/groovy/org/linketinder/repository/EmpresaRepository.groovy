package org.linketinder.repository

import groovy.sql.Sql
import org.linketinder.model.Empresa

class EmpresaRepository {
    private static List<Empresa> empresas = []

    static List<Empresa> getEmpresas() {
        empresas.clear()
        Sql sql = DatabaseConnection.getInstance()
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

    static void addEmpresa(Empresa empresa) {
        Sql sql = DatabaseConnection.getInstance()
        
        try {
            // Verificar se o país já existe ou inserir
            def paisId = obterOuCriarPais(sql, empresa.paisOndeReside)
            
            // Inserir o endereço
            def enderecoId = inserirEndereco(sql, empresa.cep, paisId)
            
            // Inserir a empresa
            def keys = sql.executeInsert("""
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
            
            // Obter o ID da empresa recém-inserida
            def empresaId = keys[0][0]
            
            // Se a empresa tiver vagas, adicioná-las
            if (empresa.vagas) {
                empresa.vagas.each { vaga ->
                    // Assumindo uma implementação de VagaRepository com um método addVaga
                    vaga.idEmpresa = empresaId
                    VagaRepository.addVaga(vaga)
                }
            }
            
            println("Empresa ${empresa.nome} adicionada com sucesso!")
            
        } catch (Exception e) {
            println("Erro ao adicionar empresa: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private static Integer obterOuCriarPais(Sql sql, String nomePais) {
        def paisRow = sql.firstRow("SELECT id FROM PAIS_DE_RESIDENCIA WHERE nome = ?", [nomePais])
        if (paisRow) {
            return paisRow.id
        } else {
            def keys = sql.executeInsert("INSERT INTO PAIS_DE_RESIDENCIA (nome) VALUES (?)", [nomePais])
            return keys[0][0] as Integer
        }
    }
    
    private static Integer inserirEndereco(Sql sql, String cep, Integer paisId) {
        def enderecoRow = sql.firstRow("SELECT id FROM ENDERECO WHERE cep = ?", [cep])
        if (enderecoRow) {
            return enderecoRow.id
        } else {
            def keys = sql.executeInsert("INSERT INTO ENDERECO (cep, pais_id) VALUES (?, ?)", [cep, paisId])
            return keys[0][0] as Integer
        }
    }
}
