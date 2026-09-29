/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;
/**
 * Handles all database operations related to users.
 *
 * This Data Access Object (DAO) is responsible for communicating with the PostgreSQL database.
 * It provides methods for registering users, authenticating login credentials, and checking for duplicate
 * usernames or email addresses.
 *
 * Separating database logic from the user interface follows the
 * MVC architecture and improves code organisation and maintainability.
 *
 * @author Jordann
 */
public class UserDAO
{
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(UserDAO.class.getName());
    private static final List<User> FALLBACK_USERS = new ArrayList<>();

    static
    {
        User demoUser = new User();
        demoUser.setId(1);
        demoUser.setFirstName("Demo");
        demoUser.setLastName("Supervisor");
        demoUser.setUsername("demo");
        demoUser.setEmail("demo@cleaning-inventory.example");
        demoUser.setPassword(utils.PasswordUtil.hash("demo1234"));
        demoUser.setRole(utils.Roles.SUPERVISOR);
        FALLBACK_USERS.add(demoUser);
    }

    // =========
    // Registration
    // =========
    
    // Registers a new user in the database.
    public boolean registerUser(User user)
    {
        if (user == null)
        {
            return false;
        }

        if (usernameExists(user.getUsername()))
        {
            System.out.println("Username already exists.");
            return false;
        }
        
        if (emailExists(user.getEmail()))
        {
            System.out.println("Email already exists.");
            return false;
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            User fallbackUser = new User();
            fallbackUser.setId(FALLBACK_USERS.size() + 1);
            fallbackUser.setFirstName(user.getFirstName());
            fallbackUser.setLastName(user.getLastName());
            fallbackUser.setUsername(user.getUsername());
            fallbackUser.setEmail(user.getEmail());
            fallbackUser.setPassword(utils.PasswordUtil.hash(user.getPassword()));
            fallbackUser.setRole(user.getRole());
            FALLBACK_USERS.add(fallbackUser);
            return true;
        }

        String sql = "INSERT INTO users " + "(first_name, last_name, username, email, password, role) " + "VALUES (?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getUsername());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, utils.PasswordUtil.hash(user.getPassword()));
            stmt.setString(6, user.getRole());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex)
        {
            LOGGER.log(java.util.logging.Level.SEVERE, "Failed to register user", ex);
            return false;
        }
    } //registerUser
    
    // =======
    // Validation
    // =======
    
    // Checks whether a username already exists.
    public boolean usernameExists(String username)
    {
        if (username == null || username.trim().isEmpty())
        {
            return false;
        }

        for (User user : FALLBACK_USERS)
        {
            if (username.equalsIgnoreCase(user.getUsername()))
            {
                return true;
            }
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex)
        {
            LOGGER.log(java.util.logging.Level.SEVERE, "Failed to check username", ex);
            return false;
        }
    } //usernameExists
    
    // Checks whether an email address already exists.
    public boolean emailExists(String email)
    {
        if (email == null || email.trim().isEmpty())
        {
            return false;
        }

        for (User user : FALLBACK_USERS)
        {
            if (email.equalsIgnoreCase(user.getEmail()))
            {
                return true;
            }
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return false;
        }

        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException ex)
        {
            LOGGER.log(java.util.logging.Level.SEVERE, "Failed to check email", ex);
            return false;
        }
    } //emailExists
    
    // ==========
    // Authentication
    // ==========
    
    // Authenticates a user using their username and password.
    public User loginUser(String username, String password)
    {
        for (User user : FALLBACK_USERS)
        {
            if (username.equalsIgnoreCase(user.getUsername()) && utils.PasswordUtil.verify(password, user.getPassword()))
            {
                return user;
            }
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null)
        {
            return null;
        }

        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                String storedHash = rs.getString("password");
                
                if (!utils.PasswordUtil.verify(password, storedHash))
                {
                    return null;
                }
                
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(storedHash);
                user.setRole(rs.getString("role"));
                
                return user;
            }
        } catch (SQLException ex)
        {
            LOGGER.log(java.util.logging.Level.SEVERE, "Login failed", ex);
        }
        
        return null;
    } //loginUser
} //UserDAO
