package view;

import dao.IssuanceDAO;
import dao.MaterialDAO;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Issuance;
import model.Material;
import ui.RoundedPanel;
import ui.Theme;

/**
 * Reports screen: Inventory, Low-Stock, Issuance History, and Material Usage reports,
 * each with CSV export (no external libraries required — plain java.io).
 *
 * Access to this screen is restricted to Supervisors — {@link MainShell} only shows the
 * "Reports" sidebar item at all when the signed-in user is a Supervisor.
 *
 * @author Jordann
 */
public class ReportsPanel extends JPanel
{
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final IssuanceDAO issuanceDAO = new IssuanceDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm");

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private DefaultTableModel inventoryModel;
    private DefaultTableModel lowStockModel;
    private DefaultTableModel issuanceModel;
    private DefaultTableModel usageModel;
    private JTabbedPane tabs;
    private JButton btnGoToDashboard;
    private Consumer<String> navigator;
    // End of variables declaration//GEN-END:variables

    public ReportsPanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout());
        initComponents();
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        RoundedPanel card = new RoundedPanel(new BorderLayout(), 15);
        card.setBackground(Theme.CARD_BG);
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);
        btnGoToDashboard = new JButton("Dashboard");
        Theme.styleSecondaryButton(btnGoToDashboard);
        btnGoToDashboard.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept("dashboard");
            }
        });
        topBar.add(btnGoToDashboard);
        card.add(topBar, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BOLD);

        inventoryModel = new DefaultTableModel(new String[]{"Name", "Quantity", "Unit", "Reorder Level"}, 0);
        lowStockModel = new DefaultTableModel(new String[]{"Name", "Quantity", "Unit", "Reorder Level"}, 0);
        issuanceModel = new DefaultTableModel(new String[]{"Material", "Cleaner", "Quantity", "Issued By", "Issued At"}, 0);
        usageModel = new DefaultTableModel(new String[]{"Material", "Total Quantity Issued", "Times Issued"}, 0);

        tabs.addTab("Inventory Report", wrapWithExport(inventoryModel, "inventory_report.csv"));
        tabs.addTab("Low-Stock Report", wrapWithExport(lowStockModel, "low_stock_report.csv"));
        tabs.addTab("Issuance History", wrapWithExport(issuanceModel, "issuance_history.csv"));
        tabs.addTab("Material Usage", wrapWithExport(usageModel, "material_usage_report.csv"));

        card.add(tabs, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    // =================
    // Shared table + CSV export panel
    // =================

    private JPanel wrapWithExport(DefaultTableModel model, String defaultFileName)
    {
        JTable table = new JTable(model);
        Theme.styleTable(table);

        JButton btnExport = new JButton("Export to CSV");
        Theme.styleSecondaryButton(btnExport);
        btnExport.addActionListener(e -> exportToCsv(model, defaultFileName));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(8, 0, 8, 0));
        topBar.add(btnExport);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(topBar, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    } //wrapWithExport

    private void exportToCsv(DefaultTableModel model, String defaultFileName)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File(defaultFileName));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        try (FileWriter writer = new FileWriter(chooser.getSelectedFile()))
        {
            // Header row
            for (int col = 0; col < model.getColumnCount(); col++)
            {
                writer.append(model.getColumnName(col));
                writer.append(col < model.getColumnCount() - 1 ? "," : "\n");
            }
            // Data rows
            for (int row = 0; row < model.getRowCount(); row++)
            {
                for (int col = 0; col < model.getColumnCount(); col++)
                {
                    Object value = model.getValueAt(row, col);
                    writer.append(value == null ? "" : value.toString().replace(",", ";"));
                    writer.append(col < model.getColumnCount() - 1 ? "," : "\n");
                }
            }
            JOptionPane.showMessageDialog(this, "Report exported successfully.");
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, "Could not export report: " + ex.getMessage(),
                    "Export Failed", JOptionPane.ERROR_MESSAGE);
        }
    } //exportToCsv

    // =================
    // Data loading
    // =================

    public void refresh()
    {
        inventoryModel.setRowCount(0);
        for (Material m : materialDAO.getAllMaterials())
        {
            inventoryModel.addRow(new Object[]{m.getName(), m.getQuantity(), m.getUnit(), m.getReorderLevel()});
        }

        lowStockModel.setRowCount(0);
        for (Material m : materialDAO.getLowStockMaterials())
        {
            lowStockModel.addRow(new Object[]{m.getName(), m.getQuantity(), m.getUnit(), m.getReorderLevel()});
        }

        issuanceModel.setRowCount(0);
        for (Issuance i : issuanceDAO.getAllIssuances())
        {
            issuanceModel.addRow(new Object[]{
                    i.getMaterialName(), i.getCleanerName(), i.getQuantity(), i.getIssuedByUsername(),
                    i.getIssuedAt() != null ? dateFormat.format(i.getIssuedAt()) : ""
            });
        }

        usageModel.setRowCount(0);
        for (Object[] row : issuanceDAO.getMaterialUsageSummary())
        {
            usageModel.addRow(row);
        }
    } //refresh
} //ReportsPanel
