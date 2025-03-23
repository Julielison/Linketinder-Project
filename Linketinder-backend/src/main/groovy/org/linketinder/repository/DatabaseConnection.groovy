package org.linketinder.repository

import groovy.sql.Sql
import java.sql.SQLException

class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost/Linketinder"
    private static final String USER = 'julielison'
    private static final String PASSWORD = 'teste'
    private static final String DRIVER = 'org.postgresql.Driver'

    private static Sql sqlInstance

    static Sql getInstance() {
        if (sqlInstance == null) {
            try {
                sqlInstance = Sql.newInstance(URL, USER, PASSWORD, DRIVER)
            } catch (SQLException e) {
                println("Erro ao conectar ao banco de dados: ${e.message}")
                e.printStackTrace()
            }
        }
        return sqlInstance
    }

    static void close() {
        if (sqlInstance != null) {
            try {
                sqlInstance.close()
            } catch (SQLException e) {
                println("Erro ao fechar a conexão: ${e.message}")
                e.printStackTrace()
            } finally {
                sqlInstance = null
            }
        }
    }
}
