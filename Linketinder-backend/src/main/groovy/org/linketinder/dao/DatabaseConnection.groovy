package org.linketinder.dao

import groovy.sql.Sql
import java.sql.SQLException

class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost/linketinder"
    private static final String USER = 'julielison'
    private static final String PASSWORD = 'teste'
    private static final String DRIVER = 'org.postgresql.Driver'
    private static Sql sqlInstance

    private DatabaseConnection() {}

    static Sql getInstance() {
        try {
            sqlInstance = sqlInstance == null ? Sql.newInstance(URL, USER, PASSWORD, DRIVER) : sqlInstance
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace()
        }
        return sqlInstance
    }
}