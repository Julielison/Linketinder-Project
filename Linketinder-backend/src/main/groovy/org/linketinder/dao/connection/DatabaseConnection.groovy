package org.linketinder.dao.connection

import groovy.sql.Sql
import io.github.cdimascio.dotenv.Dotenv

import java.sql.SQLException

class DatabaseConnection {
    private static Sql sqlInstance
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load()
    private static final String URL = dotenv.get("DB_URL")
    private static final String USER = dotenv.get("DB_USER")
    private static final String PASSWORD = dotenv.get("DB_PASSWORD")
    private static final String DRIVER = dotenv.get("DB_DRIVER")

    private DatabaseConnection() {}

    static Sql getInstance() {
        try {
            if (sqlInstance == null) {
                sqlInstance = Sql.newInstance(URL, USER, PASSWORD, DRIVER)
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace()
        }
        return sqlInstance
    }
}
