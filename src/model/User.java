/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
/**
 * Represents a user of the Cleaning Inventory System.
 *
 * This class stores the personal information and login credentials for each user.
 * It acts as the model in the MVC architecture and is used to transfer user data
 * between the GUI, DAO, and database.
 *
 * @author Jordann
 */
public class User
{
    // User information
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String role;
    
    // ==========
    // Constructors
    // ==========
    
    // Creates an empty User object (retrieving user information before setting).
    public User()
    {
    }
    
    // Creates a User object with all required information.
    public User(String firstName, String lastName, String username, String email, String password, String role)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ==============
    // Getters and Setters
    // ==============
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
    
    // Returns a String representation of the User object.
    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", username='" + username + '\'' + ", email='" + email + '\'' + ", role='" + role + '\'' + '}';
    }
} //User
