package view;

import dao.MaterialDAO;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Material;
import ui.Theme;

@SuppressWarnings("unused")

/**
 * Materials management screen: add, view, update, delete, and search cleaning materials.
 *
 * Embedded as a card inside {@link MainShell} instead of its own window. Written by hand
 * rather than via the NetBeans GUI Builder, since it needs conditional row colouring for
 * low-stock items that the drag-and-drop designer doesn't support well.
 *
 * @author Jordann
 */
public class MaterialsPanel extends JPanel
{
    private final MaterialDAO materialDAO = new MaterialDAO();
    private int selectedId = -1; // -1 means no row currently selected

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGoToDashboard;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblReorderLevel;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblUnit;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtReorderLevel;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUnit;
    private DefaultTableModel tableModel;
    private Consumer<String> navigator;
    // End of variables declaration//GEN-END:variables

    public MaterialsPanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponents();
        loadMaterials(null);
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        contentPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        btnGoToDashboard = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        formPanel = new javax.swing.JPanel();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtName.setColumns(15);
        lblQuantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        txtQuantity.setColumns(15);
        lblUnit = new javax.swing.JLabel();
        txtUnit = new javax.swing.JTextField();
        txtUnit.setColumns(15);
        lblReorderLevel = new javax.swing.JLabel();
        txtReorderLevel = new javax.swing.JTextField();
        txtReorderLevel.setColumns(15);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Quantity", "Unit", "Reorder Level"}, 0)
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
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column)
            {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                try
                {
                    int qty = Integer.parseInt(tbl.getValueAt(row, 2).toString());
                    int reorder = Integer.parseInt(tbl.getValueAt(row, 4).toString());
                    if (!isSelected)
                    {
                        c.setBackground(qty <= reorder ? Theme.LOW_STOCK_ROW : Theme.CARD_BG);
                    }
                } catch (NumberFormatException ex)
                {
                    // header row or non-numeric render pass; ignore
                }
                return c;
            }
        });
        scrollPane.setViewportView(table);

        setLayout(new java.awt.BorderLayout());

        contentPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 22, 20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new java.awt.BorderLayout());

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

        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 0));

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

        searchPanel.add(buttonPanel);

        contentPanel.add(searchPanel, java.awt.BorderLayout.NORTH);
        contentPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        formPanel.setOpaque(false);
        formPanel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 5, 5, 5);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        lblName.setText("Name:");
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblName, gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        lblQuantity.setText("Quantity:");
        gbc.gridx = 2; formPanel.add(lblQuantity, gbc);
        gbc.gridx = 3; formPanel.add(txtQuantity, gbc);

        lblUnit.setText("Unit:");
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblUnit, gbc);
        gbc.gridx = 1; formPanel.add(txtUnit, gbc);

        lblReorderLevel.setText("Reorder Level:");
        gbc.gridx = 2; formPanel.add(lblReorderLevel, gbc);
        gbc.gridx = 3; formPanel.add(txtReorderLevel, gbc);

        contentPanel.add(formPanel, java.awt.BorderLayout.SOUTH);

        add(contentPanel);

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> loadMaterials(txtSearch.getText().trim()));
        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadMaterials(null);
        });
        btnGoToDashboard.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept("dashboard");
            }
        });
    }// </editor-fold>//GEN-END:initComponents

    // =================
    // Data loading
    // =================

    public void refresh()
    {
        loadMaterials(null);
    } //refresh

    private void loadMaterials(String keyword)
    {
        tableModel.setRowCount(0);
        List<Material> materials = (keyword == null || keyword.isEmpty())
                ? materialDAO.getAllMaterials()
                : materialDAO.searchMaterials(keyword);

        for (Material m : materials)
        {
            tableModel.addRow(new Object[]{
                    m.getId(), m.getName(), m.getQuantity(), m.getUnit(), m.getReorderLevel()
            });
        }
    } //loadMaterials

    private void populateFormFromSelectedRow()
    {
        int row = table.getSelectedRow();
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtQuantity.setText(tableModel.getValueAt(row, 2).toString());
        txtUnit.setText(tableModel.getValueAt(row, 3).toString());
        txtReorderLevel.setText(tableModel.getValueAt(row, 4).toString());
    } //populateFormFromSelectedRow

    // =================
    // Button handlers
    // =================

    private void handleAdd()
    {
        Material material = validateAndBuildMaterial();
        if (material == null) return;

        if (materialDAO.addMaterial(material))
        {
            JOptionPane.showMessageDialog(this, "Material added successfully.");
            clearForm();
            loadMaterials(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not add material. A material with that name may already exist.",
                    "Add Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleAdd

    private void handleUpdate()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a material from the table first.");
            return;
        }

        Material material = validateAndBuildMaterial();
        if (material == null) return;
        material.setId(selectedId);

        if (materialDAO.updateMaterial(material))
        {
            JOptionPane.showMessageDialog(this, "Material updated successfully.");
            clearForm();
            loadMaterials(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not update material.", "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleUpdate

    private void handleDelete()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a material from the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this material? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (materialDAO.deleteMaterial(selectedId))
        {
            JOptionPane.showMessageDialog(this, "Material deleted.");
            clearForm();
            loadMaterials(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not delete material.", "Delete Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleDelete

    private void clearForm()
    {
        selectedId = -1;
        txtName.setText("");
        txtQuantity.setText("");
        txtUnit.setText("");
        txtReorderLevel.setText("");
        table.clearSelection();
    } //clearForm

    // =================
    // Validation
    // =================

    // Validates all form input and returns a populated Material, or null (with an error
    // dialog already shown) if validation fails.
    private Material validateAndBuildMaterial()
    {
        String name = txtName.getText().trim();
        String unit = txtUnit.getText().trim();
        String quantityText = txtQuantity.getText().trim();
        String reorderText = txtReorderLevel.getText().trim();

        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return null;
        }
        if (unit.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Unit is required (e.g. bottles, litres).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtUnit.requestFocus();
            return null;
        }

        int quantity, reorderLevel;
        try
        {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Quantity must be a whole number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtQuantity.requestFocus();
            return null;
        }
        try
        {
            reorderLevel = Integer.parseInt(reorderText);
        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Reorder level must be a whole number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtReorderLevel.requestFocus();
            return null;
        }

        if (quantity < 0 || reorderLevel < 0)
        {
            JOptionPane.showMessageDialog(this, "Quantity and reorder level cannot be negative.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Material(name, quantity, unit, reorderLevel);
    } //validateAndBuildMaterial
} //MaterialsPanel
