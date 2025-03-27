package org.linketinder.repository


import groovy.sql.Sql
import org.linketinder.model.Empresa

import java.sql.SQLException

class EmpresaRepository {
    public static List<Empresa> empresas = []
	Sql sql
    EnderecoRepository endereco
    VagaRepository vagaRepository

    EmpresaRepository(Sql sql, EnderecoRepository enderecoRepository, VagaRepository vagaRepository) {
        this.sql = sql
        this.endereco = enderecoRepository
        this.vagaRepository = vagaRepository
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
                        vagaRepository.getVagasByEmpresaId(idEmpresa)
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
        try {
            def resultCnpj = sql.firstRow("SELECT cnpj FROM empresa WHERE cnpj = ${empresa.cnpj}")
            if (resultCnpj){
                throw new Exception("A empresa já existe no banco de dados!")
            }

            String paisOndeReside = empresa.getPaisOndeReside()
            def paisId = endereco.obterIdPais(paisOndeReside)
            if (!paisId) {
                paisId = endereco.inserirPais(paisOndeReside)
            }

            def enderecoId = endereco.inserirEndereco(empresa.getCep(), paisId)

            def empresaId = inserirEmpresa(empresa, enderecoId)

            empresa.setId(empresaId as Integer)
            empresas.add(empresa)

        } catch (SQLException e) {
            e.printStackTrace()
            throw new Exception("Erro ao inserir empresa no Banco de dados")
        }
    }

    Integer inserirEmpresa(Empresa empresa, Integer enderecoId) {
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
        return (result[0][0] as Integer)
    }
}