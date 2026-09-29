package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.User;
import ui.RoundedPanel;
import ui.Theme;
import utils.Session;

/**
 * Main application shell shown after a successful login.
 *
 * Provides a dark gradient sidebar for navigation and a header bar showing the signed-in
 * user, wrapped around a swappable content area — the same overall layout (sidebar + header
 * + card-based content) as the modern dashboard reference UI supplied for this project.
 * Every module (Materials, Suppliers, Cleaners, Stock Issuance, Reports) lives on its own
 * panel and is swapped into view via a CardLayout instead of opening a separate window,
 * so the sidebar and header stay on screen the whole time.
 *
 * Written by hand (not via the NetBeans GUI Builder) so the sidebar's gradient paint,
 * role-based menu items, and content swapping are straightforward to follow and modify.
 *
 * @author Jordann
 */
public class MainShell extends JFrame
{
    private static final String CARD_DASHBOARD = "dashboard";
    private static final String CARD_MATERIALS = "materials";
    private static final String CARD_SUPPLIERS = "suppliers";
    private static final String CARD_CLEANERS = "cleaners";
    private static final String CARD_ISSUANCE = "issuance";
    private static final String CARD_REPORTS = "reports";
    private static final int SIDEBAR_WIDTH = 220;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 42;
    private static final int SIDEBAR_ANIMATION_STEP = 20;

    private final User user;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentCards = new JPanel(cardLayout);
    private JPanel sidebar;
    private Timer sidebarTimer;
    private boolean sidebarVisible = true;
    private final JLabel lblSectionTitle = new JLabel();
    private final Map<String, JLabel> navItems = new LinkedHashMap<>();

    private final DashboardPanel dashboardPanel;
    private final MaterialsPanel materialsPanel;
    private final SuppliersPanel suppliersPanel;
    private final CleanersPanel cleanersPanel;
    private final IssuancePanel issuancePanel;
    private final ReportsPanel reportsPanel;

    public MainShell(User user)
    {
        Session.setCurrentUser(user);
        this.user = user;

        setTitle("University Cleaning Inventory & Issuance System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 620));
        setSize(1150, 700);
        setLocationRelativeTo(null);

        dashboardPanel = new DashboardPanel();
        materialsPanel = new MaterialsPanel();
        suppliersPanel = new SuppliersPanel();
        cleanersPanel = new CleanersPanel();
        issuancePanel = new IssuancePanel();
        reportsPanel = new ReportsPanel();

        dashboardPanel.setNavigator(this::showSection);
        materialsPanel.setNavigator(this::showSection);
        suppliersPanel.setNavigator(this::showSection);
        cleanersPanel.setNavigator(this::showSection);
        issuancePanel.setNavigator(this::showSection);
        reportsPanel.setNavigator(this::showSection);

        initComponents();
        initializeAppearance();
        showSection(CARD_DASHBOARD);
    }

    private void initializeAppearance()
    {
        Theme.applyTheme(this);
    }

    private void initComponents()
    {
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());

        sidebar = buildSidebar();
        add(sidebar, BorderLayout.WEST);
        add(buildMainArea(), BorderLayout.CENTER);
    } //initComponents

    // =================
    // Sidebar
    // =================

    private JPanel buildSidebar()
    {
        JPanel sidebarPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Theme.SIDEBAR_TOP, 0, getHeight(), Theme.SIDEBAR_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebarPanel.setName("sidebar");
        sidebarPanel.setOpaque(true);
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setBorder(new EmptyBorder(25, 0, 20, 0));

        JButton menuButton = createMenuButton();
        JPanel menuRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        menuRow.setOpaque(false);
        menuRow.add(menuButton);
        sidebarPanel.add(menuRow, BorderLayout.NORTH);

        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel(loadLogo());
        lblLogo.setHorizontalAlignment(SwingConstants.LEFT);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(0, 22, 20, 22));
        navigation.add(lblLogo);

        navigation.add(navItem("Dashboard", CARD_DASHBOARD));
        navigation.add(navItem("Materials", CARD_MATERIALS));
        navigation.add(navItem("Suppliers", CARD_SUPPLIERS));
        navigation.add(navItem("Cleaners", CARD_CLEANERS));
        navigation.add(navItem("Stock Issuance", CARD_ISSUANCE));

        // Role-based access: only Supervisors see the Reports section at all.
        if (Session.isSupervisor())
        {
            navigation.add(navItem("Reports", CARD_REPORTS));
        }
        JPanel sidebarBody = new JPanel(new BorderLayout());
        sidebarBody.setOpaque(false);
        sidebarBody.add(navigation, BorderLayout.NORTH);

        JPanel utilityPanel = new JPanel();
        utilityPanel.setOpaque(false);
        utilityPanel.setLayout(new BoxLayout(utilityPanel, BoxLayout.Y_AXIS));

        JLabel lblThemeMode = sidebarItem(Theme.isDarkMode() ? "Light mode" : "Dark mode");
        lblThemeMode.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                Theme.toggleMode(MainShell.this);
                lblThemeMode.setText(Theme.isDarkMode() ? "Light mode" : "Dark mode");
                MainShell.this.revalidate();
                MainShell.this.repaint();
            }
        });
        utilityPanel.add(lblThemeMode);

        JLabel lblLogout = sidebarItem("Logout");
        lblLogout.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                handleLogout();
            }
        });
        utilityPanel.add(lblLogout);
        sidebarBody.add(utilityPanel, BorderLayout.SOUTH);
        sidebarPanel.add(sidebarBody, BorderLayout.CENTER);

        return sidebarPanel;
    } //buildSidebar

    private JButton createMenuButton()
    {
        JButton button = new JButton();
        ImageIcon source = loadMenuIcon();
        if (source != null)
        {
            Image image = source.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(image));
        } else
        {
            button.setIcon(new HamburgerIcon());
        }
        button.setToolTipText("Open or close navigation");
        button.setPreferredSize(new Dimension(30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addActionListener(e -> toggleSidebar());
        return button;
    }

    private ImageIcon loadMenuIcon()
    {
        String[] locations = {"menu-icon.jpg", "../menu-icon.jpg", "src/menu-icon.jpg"};
        for (String location : locations)
        {
            ImageIcon icon = new ImageIcon(location);
            if (icon.getIconWidth() > 0)
            {
                return icon;
            }
        }
        return null;
    }

    private static final class HamburgerIcon implements Icon
    {
        @Override
        public int getIconWidth()
        {
            return 22;
        }

        @Override
        public int getIconHeight()
        {
            return 22;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y)
        {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int offset : new int[]{5, 10, 15})
            {
                g2.drawLine(x + 2, y + offset, x + 20, y + offset);
            }
            g2.dispose();
        }
    }

    private ImageIcon loadLogo()
    {
        ImageIcon source = new ImageIcon("BC-logo.png");
        Image image = source.getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private JLabel navItem(String label, String cardName)
    {
        JLabel item = sidebarItem(label);
        item.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                showSection(cardName);
            }
        });
        navItems.put(cardName, item);
        return item;
    } //navItem

    private JLabel sidebarItem(String label)
    {
        JLabel item = new JLabel(label);
        item.setForeground(Color.WHITE);
        item.setFont(Theme.FONT_NAV);
        item.setOpaque(false);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setPreferredSize(new Dimension(220, 44));
        item.setMinimumSize(new Dimension(220, 44));
        item.setMaximumSize(new Dimension(220, 44));
        item.setBorder(new EmptyBorder(12, 22, 12, 22));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return item;
    } //sidebarItem

    // =================
    // Header + content area
    // =================

    private JPanel buildMainArea()
    {
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setOpaque(false);
        mainArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainArea.add(buildHeader(), BorderLayout.NORTH);

        contentCards.setOpaque(false);
        contentCards.setBorder(new EmptyBorder(15, 0, 0, 0));
        contentCards.add(dashboardPanel, CARD_DASHBOARD);
        contentCards.add(materialsPanel, CARD_MATERIALS);
        contentCards.add(suppliersPanel, CARD_SUPPLIERS);
        contentCards.add(cleanersPanel, CARD_CLEANERS);
        contentCards.add(issuancePanel, CARD_ISSUANCE);
        contentCards.add(reportsPanel, CARD_REPORTS);
        mainArea.add(contentCards, BorderLayout.CENTER);

        return mainArea;
    } //buildMainArea

    private JPanel buildHeader()
    {
        RoundedPanel header = new RoundedPanel(new BorderLayout(), 15);
        header.setBackground(Theme.CARD_BG);
        header.setBorder(new EmptyBorder(15, 22, 15, 22));
        header.setPreferredSize(new Dimension(0, 70));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);

        lblSectionTitle.setFont(Theme.FONT_TITLE);
        lblSectionTitle.setForeground(Theme.TEXT_DARK);
        lblSectionTitle.setBorder(new EmptyBorder(0, 14, 0, 0));
        titlePanel.add(lblSectionTitle);
        header.add(titlePanel, BorderLayout.WEST);

        JLabel lblWelcome = new JLabel(user.getFirstName() + " " + user.getLastName() + "   \u2022   " + user.getRole());
        lblWelcome.setFont(Theme.FONT_BODY);
        lblWelcome.setForeground(Theme.TEXT_MUTED);
        header.add(lblWelcome, BorderLayout.EAST);

        return header;
    } //buildHeader

    private void toggleSidebar()
    {
        if (sidebarTimer != null && sidebarTimer.isRunning())
        {
            sidebarTimer.stop();
        }

        int startWidth = sidebar.getWidth();
        int targetWidth = sidebarVisible ? SIDEBAR_COLLAPSED_WIDTH : SIDEBAR_WIDTH;
        sidebarVisible = !sidebarVisible;
        int direction = targetWidth > startWidth ? 1 : -1;

        sidebarTimer = new Timer(15, e -> {
            int nextWidth = sidebar.getWidth() + direction * SIDEBAR_ANIMATION_STEP;
            boolean finished = direction > 0 ? nextWidth >= targetWidth : nextWidth <= targetWidth;
            int width = finished ? targetWidth : nextWidth;

            sidebar.setPreferredSize(new Dimension(width, 0));
            sidebar.setMinimumSize(new Dimension(width, 0));
            sidebar.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
            sidebar.revalidate();
            getContentPane().revalidate();
            getContentPane().repaint();

            if (finished)
            {
                ((Timer) e.getSource()).stop();
            }
        });
        sidebarTimer.start();
    }

    // =================
    // Navigation
    // =================

    public final void showSection(String cardName)
    {
        cardLayout.show(contentCards, cardName);

        for (Map.Entry<String, JLabel> entry : navItems.entrySet())
        {
            boolean active = entry.getKey().equals(cardName);
            JLabel item = entry.getValue();
            item.setOpaque(active);
            item.setBackground(new Color(255, 255, 255, 45));
            item.repaint();
        }
        contentCards.revalidate();
        contentCards.repaint();

        switch (cardName)
        {
            case CARD_DASHBOARD ->
            {
                lblSectionTitle.setText("Dashboard");
                dashboardPanel.refresh();
            }
            case CARD_MATERIALS ->
            {
                lblSectionTitle.setText("Materials Management");
                materialsPanel.refresh();
            }
            case CARD_SUPPLIERS ->
            {
                lblSectionTitle.setText("Suppliers Management");
                suppliersPanel.refresh();
            }
            case CARD_CLEANERS ->
            {
                lblSectionTitle.setText("Cleaners Management");
                cleanersPanel.refresh();
            }
            case CARD_ISSUANCE ->
            {
                lblSectionTitle.setText("Stock Issuance");
                issuancePanel.refresh();
            }
            case CARD_REPORTS ->
            {
                lblSectionTitle.setText("Reports");
                reportsPanel.refresh();
            }
            default ->
            {
            }
        }
    } //showSection

    private void handleLogout()
    {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION)
        {
            Session.clear();

            LoginForm loginForm = new LoginForm();
            loginForm.setLocationRelativeTo(null);
            loginForm.setVisible(true);

            this.dispose();
        }
    } //handleLogout
} //MainShell
