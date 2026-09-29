package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;

/**
 * Central place for the application's visual language: colours, fonts, and small styling
 * helpers for buttons, tables, and text fields.
 *
 * The palette (blue-to-navy gradient sidebar, white rounded "cards" on a light grey
 * background) mirrors the look of the modern dashboard reference UI supplied for this
 * project, re-created here in plain hand-written Swing rather than NetBeans GUI Builder
 * forms, matching how the rest of this codebase is built.
 *
 * @author Sean
 */
public final class Theme
{
    private Theme()
    {
    }

    // ===== Colours =====
    public static Color SIDEBAR_TOP = Color.decode("#333333");
    public static Color SIDEBAR_BOTTOM = Color.decode("#111111");
    public static Color BACKGROUND = Color.decode("#111111");
    public static Color CARD_BG = Color.decode("#242424");
    public static Color ACCENT = Color.decode("#F58220");
    public static Color TEXT_DARK = Color.decode("#F8F8F8");
    public static Color TEXT_MUTED = Color.decode("#C9C9C9");
    public static Color BORDER = Color.decode("#4A4A4A");
    public static Color LOW_STOCK_ROW = new Color(255, 221, 221);
    public static Color DANGER = new Color(220, 53, 69);
    private static boolean darkMode = true;

    static
    {
        updatePalette();
    }

    // ===== Fonts =====
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_STAT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_NAV = new Font("Segoe UI", Font.BOLD, 13);

    // ===== Button styling =====

    public static void stylePrimaryButton(JButton b)
    {
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BOLD);
        b.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    } //stylePrimaryButton

    public static void styleSecondaryButton(JButton b)
    {
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(TEXT_DARK);
        b.setFont(FONT_BOLD);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    } //styleSecondaryButton

    public static void styleDangerButton(JButton b)
    {
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(DANGER);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BOLD);
        b.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    } //styleDangerButton

    // ===== Table styling =====

    public static void styleTable(JTable table)
    {
        table.setRowHeight(28);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER);
        table.setSelectionBackground(new Color(210, 240, 250));
        table.setSelectionForeground(TEXT_DARK);
        table.setFont(FONT_BODY);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(new Color(250, 250, 252));
        header.setForeground(TEXT_MUTED);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 36));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));
    } //styleTable

    // ===== Input styling =====

    public static void styleTextField(JTextField field)
    {
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    } //styleTextField

    public static boolean isDarkMode()
    {
        return darkMode;
    }

    public static void toggleMode(Component root)
    {
        darkMode = !darkMode;
        updatePalette();
        applyTheme(root);
    }

    public static JMenuBar createMenuBar(JFrame frame)
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("View");
        JMenuItem themeItem = new JMenuItem(darkMode ? "Use light mode" : "Use dark mode");
        themeItem.addActionListener(e -> {
            toggleMode(frame);
            themeItem.setText(darkMode ? "Use light mode" : "Use dark mode");
            frame.revalidate();
            frame.repaint();
        });
        viewMenu.add(themeItem);
        menuBar.add(viewMenu);
        return menuBar;
    }

    public static void applyTheme(Component root)
    {
        if (root instanceof JFrame frame)
        {
            frame.getContentPane().setBackground(BACKGROUND);
        }
        applyThemeToChildren(root);
    }

    private static void applyThemeToChildren(Component component)
    {
        if (component instanceof RoundedPanel card)
        {
            card.setBackground(CARD_BG);
        } else if (component instanceof JPanel panel && panel.isOpaque())
        {
            panel.setBackground(BACKGROUND);
        }
        if (component instanceof JLabel label && !isInsideSidebar(label))
        {
            label.setForeground(label.getText() != null && label.getText().contains("Welcome") ? TEXT_MUTED : TEXT_DARK);
        }
        if (component instanceof JTextField field)
        {
            field.setBackground(CARD_BG);
            field.setForeground(TEXT_DARK);
            field.setCaretColor(TEXT_DARK);
        }
        if (component instanceof JComboBox<?> comboBox)
        {
            comboBox.setBackground(CARD_BG);
            comboBox.setForeground(TEXT_DARK);
        }
        if (component instanceof JScrollPane scrollPane)
        {
            scrollPane.setBackground(CARD_BG);
            scrollPane.getViewport().setBackground(CARD_BG);
        }
        if (component instanceof JButton button && !isInsideSidebar(button))
        {
            button.setBackground(ACCENT);
            button.setForeground(Color.WHITE);
        }
        if (component instanceof JMenu menu)
        {
            menu.setBackground(CARD_BG);
            menu.setForeground(TEXT_DARK);
        }
        if (component instanceof JMenuItem menuItem)
        {
            menuItem.setBackground(CARD_BG);
            menuItem.setForeground(TEXT_DARK);
        }
        if (component instanceof JTabbedPane tabs)
        {
            tabs.setBackground(CARD_BG);
            tabs.setForeground(TEXT_DARK);
        }
        if (component instanceof JTable table)
        {
            table.setBackground(CARD_BG);
            table.setForeground(TEXT_DARK);
            table.setGridColor(BORDER);
            table.setSelectionBackground(darkMode ? Color.decode("#155E75") : new Color(210, 240, 250));
            table.setSelectionForeground(Color.WHITE);
            table.getTableHeader().setBackground(CARD_BG);
            table.getTableHeader().setForeground(TEXT_MUTED);
        }
        if (component instanceof Container container)
        {
            for (Component child : container.getComponents())
            {
                applyThemeToChildren(child);
            }
        }
    }

    private static boolean isInsideSidebar(Component component)
    {
        for (Component current = component.getParent(); current != null; current = current.getParent())
        {
            if ("sidebar".equals(current.getName()))
            {
                return true;
            }
        }
        return false;
    }

    private static void updatePalette()
    {
        if (darkMode)
        {
            SIDEBAR_TOP = Color.decode("#333333");
            SIDEBAR_BOTTOM = Color.decode("#111111");
            BACKGROUND = Color.decode("#111111");
            CARD_BG = Color.decode("#242424");
            ACCENT = Color.decode("#F58220");
            TEXT_DARK = Color.decode("#F8F8F8");
            TEXT_MUTED = Color.decode("#C9C9C9");
            BORDER = Color.decode("#4A4A4A");
            LOW_STOCK_ROW = Color.decode("#4A2520");
        } else
        {
            SIDEBAR_TOP = Color.decode("#F58220");
            SIDEBAR_BOTTOM = Color.decode("#311b08");
            BACKGROUND = new Color(242, 242, 245);
            CARD_BG = Color.WHITE;
            ACCENT = Color.decode("#F58220");
            TEXT_DARK = new Color(45, 45, 45);
            TEXT_MUTED = new Color(130, 130, 140);
            BORDER = new Color(228, 228, 232);
            LOW_STOCK_ROW = new Color(255, 221, 221);
        }
    }
} //Theme
