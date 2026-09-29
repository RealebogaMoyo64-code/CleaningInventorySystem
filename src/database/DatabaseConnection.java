/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the connection between the application and the PostgreSQL database.
 *
 * The connection settings can be overridden with environment variables or JVM
 * system properties so the application can connect to a different local or remote
 * PostgreSQL instance without editing this file.
 * @author Jordann
**/
public class DatabaseConnection
{
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    private static final String USER = getSetting("DB_USER", "postgres");
    private static final String PASSWORD = getSetting("DB_PASSWORD", "Xerials19");
    private static final String DATABASE_NAME = getSetting("DB_NAME", "cleaning_inventory_db");
    private static final String URL = buildUrl();
    private static final String ADMIN_URL = buildAdminUrl();

    private static String buildAdminUrl()
    {
        String configuredUrl = getSetting("DB_URL", null);
        if (configuredUrl != null && !configuredUrl.isBlank())
        {
            return configuredUrl;
        }

        String host = getSetting("DB_HOST", "127.0.0.1");
        String port = getSetting("DB_PORT", "5432");
        return "jdbc:postgresql://" + host + ":" + port + "/postgres";
    }

    private static String buildUrl()
    {
        String configuredUrl = getSetting("DB_URL", null);
        if (configuredUrl != null && !configuredUrl.isBlank())
        {
            return configuredUrl;
        }

        String host = getSetting("DB_HOST", "127.0.0.1");
        String port = getSetting("DB_PORT", "5432");
        return "jdbc:postgresql://" + host + ":" + port + "/" + DATABASE_NAME;
    }

    private static String getSetting(String key, String defaultValue)
    {
        String environmentValue = System.getenv(key);
        if (environmentValue != null && !environmentValue.isBlank())
        {
            return environmentValue;
        }

        String propertyValue = System.getProperty(key);
        if (propertyValue != null && !propertyValue.isBlank())
        {
            return propertyValue;
        }

        return defaultValue;
    }

    // Establishes and returns a connection to the PostgreSQL database.
    public static Connection getConnection()
    {
        try
        {
            DriverManager.setLoginTimeout(5);
            Class.forName("org.postgresql.Driver");
            ensureDatabaseExists();
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initializeSchema(connection);
            return connection;
        }
        catch (ClassNotFoundException ex)
        {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC driver not found on the classpath.", ex);
            return null;
        }
        catch (SQLException ex)
        {
            LOGGER.log(Level.WARNING, "Unable to connect to PostgreSQL at {0} using user {1}.", new Object[]{URL, USER});
            LOGGER.log(Level.WARNING, "Database connection failed: {0}", ex.getMessage());
            return null;
        }
    } //getConnection

    private static void ensureDatabaseExists() throws SQLException
    {
        try (Connection adminConnection = DriverManager.getConnection(ADMIN_URL, USER, PASSWORD))
        {
            if (!databaseExists(adminConnection, DATABASE_NAME))
            {
                try (Statement stmt = adminConnection.createStatement())
                {
                    stmt.execute("CREATE DATABASE \"" + DATABASE_NAME + "\"");
                }
            }
        }
    }

    private static void initializeSchema(Connection connection)
    {
        if (connection == null)
        {
            return;
        }

        try
        {
            try (Statement stmt = connection.createStatement())
            {
                stmt.execute("CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, first_name VARCHAR(50) NOT NULL, last_name VARCHAR(50) NOT NULL, username VARCHAR(50) UNIQUE NOT NULL, email VARCHAR(100) UNIQUE NOT NULL, password VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS materials (id SERIAL PRIMARY KEY, name VARCHAR(100) UNIQUE NOT NULL, quantity INT NOT NULL DEFAULT 0 CHECK (quantity >= 0), unit VARCHAR(20) NOT NULL, reorder_level INT NOT NULL DEFAULT 0 CHECK (reorder_level >= 0), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS suppliers (id SERIAL PRIMARY KEY, name VARCHAR(100) UNIQUE NOT NULL, contact_person VARCHAR(100), phone VARCHAR(20), email VARCHAR(100), address VARCHAR(255), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS cleaners (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, department VARCHAR(100), contact_number VARCHAR(20), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS issuances (id SERIAL PRIMARY KEY, material_id INT NOT NULL REFERENCES materials(id), cleaner_id INT NOT NULL REFERENCES cleaners(id), quantity INT NOT NULL CHECK (quantity > 0), issued_by INT NOT NULL REFERENCES users(id), issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            }
        }
        catch (SQLException ex)
        {
            LOGGER.log(Level.WARNING, "Failed to initialize schema: {0}", ex.getMessage());
        }
    }

    private static boolean databaseExists(Connection connection, String databaseName)
    {
        try (Statement stmt = connection.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + databaseName + "'"))
        {
            return rs.next();
        }
        catch (SQLException ex)
        {
            LOGGER.log(Level.INFO, "Unable to inspect databases: {0}", ex.getMessage());
            return false;
        }
    }
} //DatabaseConnection
