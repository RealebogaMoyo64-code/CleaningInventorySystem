package view;

import dao.CleanerDAO;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Cleaner;
import ui.RoundedPanel;
import ui.Theme;
import utils.Session;

/**
 * Cleaners management screen: add, view, update, delete, and search cleaner records.
 *
 * Embedded as a card inside {@link MainShell} instead of its own window.
 *
 * @author Sean
 */
public class CleanersPanel extends JPanel
{
    private final CleanerDAO cleanerDAO = new CleanerDAO();
    private int selectedId = -1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtName;
    private JTextField txtDepartment;
    private JTextField txtContactNumber;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;
    private JButton btnShowAll;
    private JButton btnGoToDashboard;
    private Consumer<String> navigator;
    // End of variables declaration//GEN-END:variables

    public CleanersPanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponents();
        loadCleaners(null);
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        RoundedPanel card = new RoundedPanel(new BorderLayout(0, 15), 15);
        card.setBackground(Theme.CARD_BG);
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchPanel.setOpaque(false);
        JLabel lblSearch = new JLabel("Search:");
        btnGoToDashboard = new JButton("Dashboard");
        Theme.styleSecondaryButton(btnGoToDashboard);
        btnGoToDashboard.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept("dashboard");
            }
        });
        lblSearch.setFont(Theme.FONT_BOLD);
        txtSearch = new JTextField(20);
        Theme.styleTextField(txtSearch);
        btnSearch = new JButton("Search");
        Theme.styleSecondaryButton(btnSearch);
        btnShowAll = new JButton("Show All");
        Theme.styleSecondaryButton(btnShowAll);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        searchPanel.add(btnGoToDashboard);
        card.add(searchPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Department", "Contact Number"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
            {
                populateFormFromSelectedRow();
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField(15);
        txtDepartment = new JTextField(15);
        txtContactNumber = new JTextField(12);
        Theme.styleTextField(txtName);
        Theme.styleTextField(txtDepartment);
        Theme.styleTextField(txtContactNumber);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 2; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 3; formPanel.add(txtDepartment, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1; formPanel.add(txtContactNumber, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        Theme.stylePrimaryButton(btnAdd);
        Theme.styleSecondaryButton(btnUpdate);
        Theme.styleDangerButton(btnDelete);
        Theme.styleSecondaryButton(btnClear);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        if (!Session.isSupervisor())
        {
            btnDelete.setEnabled(false);
            btnDelete.setToolTipText("Only Supervisors can delete cleaner records.");
        }

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        card.add(southPanel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> loadCleaners(txtSearch.getText().trim()));
        btnShowAll.addActionListener(e -> { txtSearch.setText(""); loadCleaners(null); });
    } // </editor-fold>//GEN-END:initComponents

    public void refresh()
    {
        loadCleaners(null);
    } //refresh

    private void loadCleaners(String keyword)
    {
        tableModel.setRowCount(0);
        List<Cleaner> cleaners = (keyword == null || keyword.isEmpty())
                ? cleanerDAO.getAllCleaners()
                : cleanerDAO.searchCleaners(keyword);

        for (Cleaner c : cleaners)
        {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDepartment(), c.getContactNumber()});
        }
    } //loadCleaners

    private void populateFormFromSelectedRow()
    {
        int row = table.getSelectedRow();
        selectedId = (int) tableModel.getValueAt(row, 0);
        txtName.setText(str(tableModel.getValueAt(row, 1)));
        txtDepartment.setText(str(tableModel.getValueAt(row, 2)));
        txtContactNumber.setText(str(tableModel.getValueAt(row, 3)));
    } //populateFormFromSelectedRow

    private String str(Object value)
    {
        return value == null ? "" : value.toString();
    }

    private void handleAdd()
    {
        Cleaner cleaner = validateAndBuildCleaner();
        if (cleaner == null) return;

        if (cleanerDAO.addCleaner(cleaner))
        {
            JOptionPane.showMessageDialog(this, "Cleaner added successfully.");
            clearForm();
            loadCleaners(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not add cleaner.", "Add Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleAdd

    private void handleUpdate()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a cleaner from the table first.");
            return;
        }

        Cleaner cleaner = validateAndBuildCleaner();
        if (cleaner == null) return;
        cleaner.setId(selectedId);

        if (cleanerDAO.updateCleaner(cleaner))
        {
            JOptionPane.showMessageDialog(this, "Cleaner updated successfully.");
            clearForm();
            loadCleaners(null);
        } else
        {
            JOptionPane.showMessageDialog(this, "Could not update cleaner.", "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleUpdate

    private void handleDelete()
    {
        if (selectedId == -1)
        {
            JOptionPane.showMessageDialog(this, "Select a cleaner from the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this cleaner? This cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (cleanerDAO.deleteCleaner(selectedId))
        {
            JOptionPane.showMessageDialog(this, "Cleaner deleted.");
            clearForm();
            loadCleaners(null);
        } else
        {
            JOptionPane.showMessageDialog(this,
                    "Could not delete cleaner. They may have existing issuance history linked to them.",
                    "Delete Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //handleDelete

    private void clearForm()
    {
        selectedId = -1;
        txtName.setText("");
        txtDepartment.setText("");
        txtContactNumber.setText("");
        table.clearSelection();
    } //clearForm

    private Cleaner validateAndBuildCleaner()
    {
        String name = txtName.getText().trim();
        String department = txtDepartment.getText().trim();
        String contactNumber = txtContactNumber.getText().trim();

        if (name.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtName.requestFocus();
            return null;
        }

        return new Cleaner(name, department, contactNumber);
    } //validateAndBuildCleaner
} //CleanersPanel
