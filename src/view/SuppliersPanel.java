package view;

import dao.SupplierDAO;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Supplier;
import ui.Theme;
import utils.Session;

/**
 * Suppliers management screen: add, view, update, delete, and search suppliers.
 *
 * Delete is restricted to Supervisors (demonstrates role-based access control).
 * Embedded as a card inside {@link MainShell} instead of its own window.
 *
 * @author Jordann
 */
public class SuppliersPanel extends JPanel
{
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private int selectedId = -1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGoToDashboard;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblContactPerson;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtContactPerson;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSearch;
    private DefaultTableModel tableModel;
    private Consumer<String> navigator;
    // End of variables declaration//GEN-END:variables

    public SuppliersPanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponents();
        loadSuppliers(null);
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchPanel = new javax.swing.JPanel();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        btnGoToDashboard = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        formPanel = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtName.setColumns(15);
        lblContactPerson = new javax.swing.JLabel();
        txtContactPerson = new javax.swing.JTextField();
        txtContactPerson.setColumns(15);
        lblPhone = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        txtPhone.setColumns(15);
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtEmail.setColumns(15);
        lblAddress = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        txtAddress.setColumns(15);
        buttonPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Contact Person", "Phone", "Email", "Address"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new javax.swing.JTable(tableModel);
        Theme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
            {
                populateFormFromSelectedRow();
            }
        });

        setLayout(new java.awt.BorderLayout());

        searchPanel.setOpaque(false);
        searchPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        lblSearch.setText("Search:");
        lblSearch.setFont(Theme.FONT_BOLD);
        searchPanel.add(lblSearch);

        txtSearch.setColumns(20);
        Theme.styleTextField(txtSearch);
        searchPanel.add(txtSearch);

        btnSearch.setText("Search");
        Theme.styleSecondaryButton(btnSearch);
        searchPanel.add(btnSearch);

        btnShowAll.setText("Show All");
        Theme.styleSecondaryButton(btnShowAll);
        searchPanel.add(btnShowAll);

        btnGoToDashboard.setText("Dashboard");
        Theme.styleSecondaryButton(btnGoToDashboard);
        searchPanel.add(btnGoToDashboard);

        add(searchPanel, BorderLayout.NORTH);

        scrollPane.setViewportView(table);

        add(scrollPane, BorderLayout.CENTER);

        formPanel.setOpaque(false);
        formPanel.setLayout(new java.awt.GridBagLayout());

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        lblName.setText("Name:");
        formPanel.add(lblName, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        lblContactPerson.setText("Contact Person:");
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblContactPerson, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        formPanel.add(txtContactPerson, gbc);

        lblPhone.setText("Phone:");
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblPhone, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        lblEmail.setText("Email:");
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblEmail, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        lblAddress.setText("Address:");
        gbc.weightx = 0;
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblAddress, gbc);
        gbc.weightx = 1;
        gbc.gridx = 1;
        formPanel.add(txtAddress, gbc);

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnAdd.setText("Add");
        Theme.stylePrimaryButton(btnAdd);
        buttonPanel.add(btnAdd);

        btnUpdate.setText("Update");
        Theme.styleSecondaryButton(btnUpdate);
        buttonPanel.add(btnUpdate);

        btnDelete.setText("Delete");
        Theme.styleDangerButton(btnDelete);
        buttonPanel.add(btnDelete);

        btnClear.setText("Clear");
        Theme.styleSecondaryButton(btnClear);
        buttonPanel.add(btnClear);

        if (!Session.isSupervisor())
        {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Only Supervisors can delete suppliers.");
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> loadSuppliers(txtSearch.getText().trim()));
        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadSuppliers(null);
        });
        btnGoToDashboard.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept("dashboard");
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    public void refresh()
    {
        loadSuppliers(null);
    } //refresh

    private void loadSuppliers(String keyword)
    {
        tableModel.setRowCount(0);
        List<Supplier> suppliers = (keyword == null || keyword.isEmpty())
                ? supplierDAO.getAllSuppliers()
                : supplierDAO.searchSuppliers(keyword);

        for (Supplier s : suppliers)
        {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getContactPerson(), s.getPhone(), s.getEmail(), s.getAddress()
            });
        }
    } //loadSuppliers

    private void populateFormFromSelectedRow()
    {
        int row = table.getSelectedRow();
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtName.setText(str(tableModel.getValueAt(row, 1)));
        txtContactPerson.setText(str(tableModel.getValueAt(row, 2)));
        txtPhone.setText(str(tableModel.getValueAt(row, 3)));
        txtEmail.setText(str(tableModel.getValueAt(row, 4)));
        txtAddress.setText(str(tableModel.getValueAt(row, 5)));
    } //populateFormFromSelectedRow

    private String str(Object value)
    {
        return value == null ? "" : value.toString();
    }

    private void handleAdd()
    {
        Supplier supplier = validateAndBuildSupplier();
        if (supplier == null) return;

        if (supplierDAO.addSupplier(supplier))
        {
            JOptionPane.showMessageDialog(this, "Supplier added successfully.");
            clearForm();
            loadSuppliers(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not add supplier. A supplier with that name may already exist.",
                    "Add Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleAdd

    private void handleUpdate()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a supplier from the table first.");
            return;
        }

        Supplier supplier = validateAndBuildSupplier();
        if (supplier == null) return;
        supplier.setId(selectedId);

        if (supplierDAO.updateSupplier(supplier))
        {
            JOptionPane.showMessageDialog(this, "Supplier updated successfully.");
            clearForm();
            loadSuppliers(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not update supplier.", "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleUpdate

    private void handleDelete()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a supplier from the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this supplier? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (supplierDAO.deleteSupplier(selectedId))
        {
            JOptionPane.showMessageDialog(this, "Supplier deleted.");
            clearForm();
            loadSuppliers(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not delete supplier.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleDelete

    private void clearForm()
    {
        selectedId = -1;
        txtName.setText("");
        txtContactPerson.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        table.clearSelection();
    } //clearForm

    private Supplier validateAndBuildSupplier()
    {
        String name = txtName.getText().trim();
        String contactPerson = txtContactPerson.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String address = txtAddress.getText().trim();

        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return null;
        }
        if (!email.isEmpty() && !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$"))
        {
            JOptionPane.showMessageDialog(this, "Enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return null;
        }

        return new Supplier(name, contactPerson, phone, email, address);
    } //validateAndBuildSupplier
} //SuppliersPanel
