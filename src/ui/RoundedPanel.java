package ui;

import java.awt.*;
import javax.swing.*;

/**
 * A JPanel with a rounded-rectangle background, used throughout the dashboard shell to
 * give content sections the white "card" look of the reference UI.
 *
 * @author Sean
 */
public class RoundedPanel extends JPanel
{
    private final int radius;

    public RoundedPanel(int radius)
    {
        this.radius = radius;
        setOpaque(false);
    }

    public RoundedPanel(LayoutManager layout, int radius)
    {
        super(layout);
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    } //paintComponent
} //RoundedPanel
