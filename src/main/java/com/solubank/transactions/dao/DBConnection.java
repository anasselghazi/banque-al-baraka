package com.solubank.transactions.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String CONFIG_FILE = "application.properties";
    private static String url;
    private static String user;
    private static String password;

    static {
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Fichier " + CONFIG_FILE + " introuvable dans resources.");
            }
            props.load(input);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Erreur de lecture de la configuration : " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}