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
import javax.swing.table.DefaultTableModel;
import model.Issuance;
import model.Material;
import ui.RoundedPanel;
import ui.Theme;
import utils.Session;

/**
 * Dashboard "home" panel: summary statistics (total materials, low-stock count, total
 * cleaners) and a table of recent stock issuances.
 *
 * Embedded as a card inside {@link MainShell} rather than shown as its own window. Call
 * {@link #refresh()} whenever it's brought back into view so the numbers stay accurate
 * without a manual reload — {@code MainShell} does this automatically.
 *
 * @author Sean
 */
public class DashboardPanel extends JPanel
{
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final CleanerDAO cleanerDAO = new CleanerDAO();
    private final IssuanceDAO issuanceDAO = new IssuanceDAO();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm");

    private JLabel lblTotalMaterials, lblLowStockCount, lblTotalCleaners;
    private DefaultTableModel recentIssuancesModel;
    private Consumer<String> navigator;

    public DashboardPanel()
    {
        setOpaque(false);
        setLayout(new BorderLayout(0, 15));
        initComponents();
    }

    public void setNavigator(Consumer<String> navigator)
    {
        this.navigator = navigator;
    }

    private void initComponents()
    {
        lblTotalMaterials = new JLabel("0");
        lblLowStockCount = new JLabel("0");
        lblTotalCleaners = new JLabel("0");

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);
        statsPanel.add(statCard("Total Materials", lblTotalMaterials, Theme.ACCENT));
        statsPanel.add(statCard("Low-Stock Items", lblLowStockCount, Theme.DANGER));
        statsPanel.add(statCard("Total Cleaners", lblTotalCleaners, Theme.ACCENT));
        statsPanel.setPreferredSize(new Dimension(0, 110));
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setOpaque(false);
        navPanel.add(createNavButton("Manage Materials", "materials"));
        navPanel.add(createNavButton("Manage Suppliers", "suppliers"));
        navPanel.add(createNavButton("Manage Cleaners", "cleaners"));
        navPanel.add(createNavButton("Issue Stock", "issuance"));
        if (Session.isSupervisor())
        {
            navPanel.add(createNavButton("Open Reports", "reports"));
        }

        add(statsPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel(new BorderLayout(0, 15));
        bodyPanel.setOpaque(false);
        bodyPanel.add(navPanel, BorderLayout.NORTH);

        RoundedPanel recentCard = new RoundedPanel(new BorderLayout(), 15);
        recentCard.setBackground(Theme.CARD_BG);
        recentCard.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblRecent = new JLabel("Recent Stock Issuances");
        lblRecent.setFont(Theme.FONT_SECTION);
        lblRecent.setForeground(Theme.TEXT_DARK);
        lblRecent.setBorder(new EmptyBorder(0, 0, 12, 0));
        recentCard.add(lblRecent, BorderLayout.NORTH);

        recentIssuancesModel = new DefaultTableModel(new String[]{"Material", "Cleaner", "Quantity", "Issued At"}, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        JTable recentTable = new JTable(recentIssuancesModel);
        Theme.styleTable(recentTable);

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        recentCard.add(scrollPane, BorderLayout.CENTER);

        bodyPanel.add(recentCard, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);
    } //initComponents

    private JButton createNavButton(String label, String cardName)
    {
        JButton button = new JButton(label);
        Theme.styleSecondaryButton(button);
        button.addActionListener(e -> {
            if (navigator != null)
            {
                navigator.accept(cardName);
            }
        });
        return button;
    } //createNavButton

    private JPanel statCard(String title, JLabel valueLabel, Color accent)
    {
        RoundedPanel card = new RoundedPanel(15);
        card.setBackground(Theme.CARD_BG);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel lblTitleText = new JLabel(title);
        lblTitleText.setFont(Theme.FONT_BODY);
        lblTitleText.setForeground(Theme.TEXT_MUTED);
        lblTitleText.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(Theme.FONT_STAT);
        valueLabel.setForeground(accent);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setBorder(new EmptyBorder(8, 0, 0, 0));

        card.add(lblTitleText);
        card.add(valueLabel);
        return card;
    } //statCard

    public void refresh()
    {
        List<Material> allMaterials = materialDAO.getAllMaterials();
        List<Material> lowStock = materialDAO.getLowStockMaterials();

        lblTotalMaterials.setText(String.valueOf(allMaterials.size()));
        lblLowStockCount.setText(String.valueOf(lowStock.size()));
        lblTotalCleaners.setText(String.valueOf(cleanerDAO.getAllCleaners().size()));

        recentIssuancesModel.setRowCount(0);
        List<Issuance> recent = issuanceDAO.getRecentIssuances(5);
        for (Issuance i : recent)
        {
            recentIssuancesModel.addRow(new Object[]{
                    i.getMaterialName(), i.getCleanerName(), i.getQuantity(),
                    i.getIssuedAt() != null ? dateFormat.format(i.getIssuedAt()) : ""
            });
        }
    } //refresh
} //DashboardPanel
