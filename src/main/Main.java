/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import view.LoginForm;
/**
 * Entry point for the Cleaning Inventory System.
 *
 * This class launches the application by displaying the Login form.
 *
 * @author Jordann
 */
public class Main
{
    // Starts the application.
    public static void main(String[] args)
    {
        java.awt.EventQueue.invokeLater(() ->
                {
                    // Launch the Login window.
                    LoginForm loginForm = new LoginForm();
                    loginForm.setLocationRelativeTo(null);
                    loginForm.setVisible(true);
                });
    } //main
} //Main
