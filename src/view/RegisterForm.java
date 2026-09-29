/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import dao.UserDAO;
import java.util.regex.Pattern;
import javax.swing.*;
import model.User;
import ui.Theme;
import utils.Roles;
/**
 * Registration window for the Cleaning Inventory System.
 *
 * This form allows authorised staff members to create new user accounts.
 * User information is validated before being stored in the PostgreSQL database.
 *
 * @author Jordann
 */
public class RegisterForm extends javax.swing.JFrame
{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RegisterForm.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

   // Creates the registration form and initialises all GUI components.
    public RegisterForm()
    {
        initComponents();
        Theme.applyTheme(this);
        
        // Populate the role selection drop-down with the available user roles.
        cbxRole.removeAllItems();
        cbxRole.addItem(Roles.STOREKEEPER);
        cbxRole.addItem(Roles.SUPERVISOR);
        clearFields();
    }
    
    // =======
    // Validation
    // =======
    
    // Validates all user input before attempting registration (all required fields, matching passwords, and basic validation).
    private boolean validateInput()
     {
         // Validate personal information.
         if (txtFirstName.getText().trim().isEmpty())
         {
             JOptionPane.showMessageDialog(this, "First name is required.");
             txtFirstName.requestFocus();
                
             return false;
         }
         // Validate personal information.   
         if (txtLastName.getText().trim().isEmpty())
         {
             JOptionPane.showMessageDialog(this, "Last name is required.");
             txtLastName.requestFocus();
                
             return false;
         }
            
         // Validate account information.
         if (txtUsername.getText().trim().isEmpty())
         {
             JOptionPane.showMessageDialog(this, "Username is required.");
             txtUsername.requestFocus();
                
             return false;
         }
            
         // Validate account information.
         if (txtEmail.getText().trim().isEmpty())
         {
             JOptionPane.showMessageDialog(this, "Email is required.");
             txtEmail.requestFocus();
                
             return false;
         }

         if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches())
         {
             JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
             txtEmail.requestFocus();

             return false;
         }
            
         // Validate password requirements.
         if (String.valueOf(txtPassword.getPassword()).isEmpty())
         {
             JOptionPane.showMessageDialog(this, "Password is required.");
             txtPassword.requestFocus();
              
             return false;
         }
         
         // Validate password requirements.
         if (String.valueOf(txtPassword.getPassword()).length() < 8)
         {
             JOptionPane.showMessageDialog(this, "Password must have at least 8 characters.");
             txtPassword.requestFocus();
             
             return false;
         }
            
         // Validate password requirements.
         if (String.valueOf(txtConfirmPassword.getPassword()).isEmpty())
         {
             JOptionPane.showMessageDialog(this, "Password confirmation is required.");
             txtConfirmPassword.requestFocus();
                
             return false;
         }
            
         // Ensure both passwords match.
         if (!String.valueOf(txtPassword.getPassword()).equals(String.valueOf(txtConfirmPassword.getPassword())))
         {
             JOptionPane.showMessageDialog(this, "Passwords do not match.");
             txtConfirmPassword.requestFocus();
                
             return false;
         }
            
         return true;
     } //validateInput
    
    // Creates a User object using the information entered on the registration form.
    private User createUser()
    {
        User user = new User();
        
        // Populate the User object using the values entered by the user.
        user.setFirstName(txtFirstName.getText().trim());
        user.setLastName(txtLastName.getText().trim());
        user.setUsername(txtUsername.getText().trim());
        user.setEmail(txtEmail.getText().trim());
        user.setPassword(String.valueOf(txtPassword.getPassword()));
        // Assign the role selected from the drop-down list.
        user.setRole(cbxRole.getSelectedItem().toString());
        
        return user;
    } //createUser
    
    // Clear registration form input fields.
    private void clearFields()
    {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        cbxRole.setSelectedItem(Roles.STOREKEEPER);

        txtFirstName.requestFocus();
    } //clearFields

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnRegister = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblRegister = new javax.swing.JLabel();
        lblFirstName = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtFirstName.setColumns(15);
        txtLastName = new javax.swing.JTextField();
        txtLastName.setColumns(15);
        lblUsername = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtUsername.setColumns(15);
        lblEmail = new javax.swing.JLabel();
        lblConfirmPassword = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtEmail.setColumns(15);
        btnBack = new javax.swing.JButton();
        cbxRole = new javax.swing.JComboBox<>();
        txtPassword = new javax.swing.JPasswordField();
        txtPassword.setColumns(15);
        txtConfirmPassword = new javax.swing.JPasswordField();
        txtConfirmPassword.setColumns(15);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cleaning Inventory System - Register");

        btnRegister.setBackground(new java.awt.Color(153, 204, 255));
        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegister.setText("Register");
        btnRegister.addActionListener(this::btnRegisterActionPerformed);

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("University Inventory Cleaning System");

        lblRegister.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRegister.setText("Register");

        lblFirstName.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblFirstName.setText("First Name:");

        lblLastName.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblLastName.setText("Last Name:");

        txtFirstName.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        txtLastName.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        lblUsername.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblUsername.setText("Username:");

        lblPassword.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblPassword.setText("Password:");

        txtUsername.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblEmail.setText("Email:");

        lblConfirmPassword.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        lblConfirmPassword.setText("Confirm Password:");

        txtEmail.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        btnBack.setBackground(new java.awt.Color(153, 204, 255));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(lblTitle))
                            .addComponent(btnRegister)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxRole, javax.swing.GroupLayout.Alignment.LEADING, 0, 125, Short.MAX_VALUE)
                                    .addComponent(lblFirstName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtFirstName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUsername, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblEmail)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblConfirmPassword))
                                    .addComponent(txtLastName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblLastName)
                                    .addComponent(btnBack, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtConfirmPassword)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(lblRegister)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addComponent(lblRegister)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblFirstName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblLastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPassword))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblEmail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblConfirmPassword)))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(cbxRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegister)
                    .addComponent(btnBack))
                .addGap(21, 21, 21))
        );

        setSize(new java.awt.Dimension(364, 428));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // =================
    // Button Event Handlers
    // =================
    
    // Returns the user to the Login form.
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnBackActionPerformed
    {//GEN-HEADEREND:event_btnBackActionPerformed
        if (evt != null)
        {
            Object source = evt.getSource();
            if (source != null)
            {
                source.toString();
            }
        }

        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
        
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    // Registers a new user and stores the information in the PostgreSQL database.
    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRegisterActionPerformed
    {//GEN-HEADEREND:event_btnRegisterActionPerformed
        if (evt != null)
        {
            Object source = evt.getSource();
            if (source != null)
            {
                source.toString();
            }
        }

        // Validate user input.
        if(!validateInput())
        {
            return;
        }
        
        // Create a User object from the form.
        User user = createUser();
        // Register the new user.
        UserDAO dao = new UserDAO();
        
        // Return to the login screen after successful registration.
        if(dao.registerUser(user))
        {
            clearFields();
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            
            // Open the Login form upon successful registration.
            LoginForm loginForm = new LoginForm();
            loginForm.setLocationRelativeTo(null);
            loginForm.setVisible(true);
            
            this.dispose();
        } else
        {
            // Display an error message if registration fails.
            JOptionPane.showMessageDialog(this, "Registration Failed.");
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new RegisterForm().setVisible(true));
    }

    // =============
    // GUI Components
    // =============
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRegister;
    private javax.swing.JComboBox<String> cbxRole;
    private javax.swing.JLabel lblConfirmPassword;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblRegister;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
