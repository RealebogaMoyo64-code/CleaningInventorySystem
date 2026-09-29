package view;

import dao.CleanerDAO;
import dao.IssuanceDAO;
import dao.MaterialDAO;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import model.Cleaner;
import model.Issuance;
import model.Material;
import ui.RoundedPanel;
import ui.Theme;
import utils.InsufficientStockException;
import utils.Session;

/**
 * Stock Issuance screen: issue materials to cleaners, with the business rules
 * (stock deduction, over-issue prevention) enforced by IssuanceDAO, and a
 * history table of everything issued so far.
 *
 * Embedded as a card inside {@link MainShell} instead of its own window.
 *
 * @author Jordann
 */
public class IssuancePanel extends JPanel
{
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final CleanerDAO cleanerDAO = new CleanerDAO();
    private final IssuanceDAO issuanceDAO = new IssuanceDAO();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JComboBox<Material> comboMaterial;
    private JComboBox<Cleaner> comboCleaner;
    private JTextField txtQuantity;
    private JTextField txtSearch;
    private JLabel lblAvailable;
    private JButton btnIssue;
    private JButton btnRefresh;
    private JButton btnSearch;
    private JButton btnGoToDashboard;
    private JTable table;
    private DefaultTableModel tableModel;
    private Consumer<String> navigator;
    // End of variables declaration//GEN-END:variables

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");

    public IssuancePanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout(0, 15));
        initComponents();
        refreshMaterialCombo();
        refreshCleanerCombo();
        loadHistory(null);
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        // ===== Top: issuance form =====
        RoundedPanel issueCard = new RoundedPanel(new GridBagLayout(), 15);
        issueCard.setBackground(Theme.CARD_BG);
        issueCard.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(16, 22, 16, 22),
                BorderFactory.createTitledBorder(null, "Issue Stock", TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, Theme.FONT_SECTION, Theme.TEXT_DARK)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboMaterial = new JComboBox<>();
        comboCleaner = new JComboBox<>();
        txtQuantity = new JTextField(6);
        Theme.styleTextField(txtQuantity);
        lblAvailable = new JLabel("Available: -");
        lblAvailable.setFont(Theme.FONT_BOLD);
        btnIssue = new JButton("Issue");
        Theme.stylePrimaryButton(btnIssue);

        comboMaterial.addActionListener(e -> updateAvailableLabel());

        gbc.gridx = 0; gbc.gridy = 0; issueCard.add(new JLabel("Material:"), gbc);
        gbc.gridx = 1; issueCard.add(comboMaterial, gbc);
        gbc.gridx = 2; issueCard.add(lblAvailable, gbc);

        gbc.gridx = 0; gbc.gridy = 1; issueCard.add(new JLabel("Cleaner:"), gbc);
        gbc.gridx = 1; issueCard.add(comboCleaner, gbc);

        gbc.gridx = 0; gbc.gridy = 2; issueCard.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; issueCard.add(txtQuantity, gbc);
        gbc.gridx = 2; issueCard.add(btnIssue, gbc);

        add(issueCard, BorderLayout.NORTH);

        // ===== Centre: history table =====
        RoundedPanel historyCard = new RoundedPanel(new BorderLayout(0, 10), 15);
        historyCard.setBackground(Theme.CARD_BG);
        historyCard.setBorder(new EmptyBorder(16, 22, 16, 22));

        JPanel historyHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        historyHeader.setOpaque(false);
        JLabel lblHistoryTitle = new JLabel("Issuance History:");
        lblHistoryTitle.setFont(Theme.FONT_SECTION);
        txtSearch = new JTextField(15);
        Theme.styleTextField(txtSearch);
        btnSearch = new JButton("Search");
        Theme.styleSecondaryButton(btnSearch);
        btnRefresh = new JButton("Refresh");
        Theme.styleSecondaryButton(btnRefresh);
        historyHeader.add(lblHistoryTitle);
        historyHeader.add(txtSearch);
        historyHeader.add(btnSearch);
        historyHeader.add(btnRefresh);

        btnGoToDashboard = new JButton("Dashboard");
        Theme.styleSecondaryButton(btnGoToDashboard);
        btnGoToDashboard.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept("dashboard");
            }
        });
        historyHeader.add(btnGoToDashboard);

        tableModel = new DefaultTableModel(
                new String[]{"Material", "Cleaner", "Quantity", "Issued By", "Issued At"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);

        historyCard.add(historyHeader, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        historyCard.add(scrollPane, BorderLayout.CENTER);
        add(historyCard, BorderLayout.CENTER);

        btnIssue.addActionListener(e -> handleIssue());
        btnSearch.addActionListener(e -> loadHistory(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> { txtSearch.setText(""); loadHistory(null); });
    } // </editor-fold>//GEN-END:initComponents

    // =================
    // Combo box population
    // =================

    private void refreshMaterialCombo()
    {
        comboMaterial.removeAllItems();
        List<Material> materials = materialDAO.getAllMaterials();
        for (Material m : materials)
        {
            comboMaterial.addItem(m);
        }
        comboMaterial.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getName() + " (" + value.getUnit() + ")"));
        updateAvailableLabel();
    } //refreshMaterialCombo

    private void refreshCleanerCombo()
    {
        comboCleaner.removeAllItems();
        List<Cleaner> cleaners = cleanerDAO.getAllCleaners();
        for (Cleaner c : cleaners)
        {
            comboCleaner.addItem(c); // Cleaner.toString() already formats nicely for the dropdown
        }
    } //refreshCleanerCombo

    private void updateAvailableLabel()
    {
        Material selected = (Material) comboMaterial.getSelectedItem();
        lblAvailable.setText(selected == null ? "Available: -" : "Available: " + selected.getQuantity() + " " + selected.getUnit());
    } //updateAvailableLabel

    // =================
    // Issuance handling
    // =================

    public void refresh()
    {
        refreshMaterialCombo();
        refreshCleanerCombo();
        loadHistory(null);
    } //refresh

    private void handleIssue()
    {
        Material material = (Material) comboMaterial.getSelectedItem();
        Cleaner cleaner = (Cleaner) comboCleaner.getSelectedItem();

        if (material == null || cleaner == null)
        {
            JOptionPane.showMessageDialog(this, "Add at least one material and one cleaner before issuing stock.",
                    "Cannot Issue", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int quantity;
        try
        {
            quantity = Integer.parseInt(txtQuantity.getText().trim());
        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Quantity must be a whole number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtQuantity.requestFocus();
            return;
        }

        if (quantity <= 0)
        {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            txtQuantity.requestFocus();
            return;
        }

        if (Session.getCurrentUser() == null)
        {
            JOptionPane.showMessageDialog(this, "No user is logged in. Please log in again.", "Session Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Issuance issuance = new Issuance(material.getId(), cleaner.getId(), quantity, Session.getCurrentUser().getId());

        try
        {
            if (issuanceDAO.issueMaterial(issuance))
            {
                JOptionPane.showMessageDialog(this,
                        quantity + " " + material.getUnit() + " of " + material.getName() + " issued to " + cleaner.getName() + ".");
                txtQuantity.setText("");
                refreshMaterialCombo(); // stock level changed
                loadHistory(null);
            } else
            {
                JOptionPane.showMessageDialog(this, "Could not issue stock due to a database error. Please try again.",
                        "Issue Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (InsufficientStockException ex)
        {
            // The exact business rule from the brief: never allow issuing more stock than available.
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Insufficient Stock", JOptionPane.WARNING_MESSAGE);
        }
    } //handleIssue

    // =================
    // History table
    // =================

    private void loadHistory(String keyword)
    {
        tableModel.setRowCount(0);
        List<Issuance> issuances = (keyword == null || keyword.isEmpty())
                ? issuanceDAO.getAllIssuances()
                : issuanceDAO.searchIssuances(keyword);

        for (Issuance i : issuances)
        {
            tableModel.addRow(new Object[]{
                    i.getMaterialName(), i.getCleanerName(), i.getQuantity(), i.getIssuedByUsername(),
                    i.getIssuedAt() != null ? dateFormat.format(i.getIssuedAt()) : ""
            });
        }
    } //loadHistory
} //IssuancePanel
