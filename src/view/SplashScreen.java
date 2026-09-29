package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import model.User;

/** Short branded transition shown after successful authentication. */
public class SplashScreen extends JFrame
{
    private static final int SPLASH_WIDTH = 420;
    private static final int SPLASH_HEIGHT = 260;
    private static final int FADE_STEP = 10;
    private static final int FADE_DELAY = 15;

    private final User user;
    private float opacity;

    public SplashScreen(User user)
    {
        this.user = user;
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
        setLocationRelativeTo(null);
        setOpacity(0.01f);
        setContentPane(buildContent());
    }

    private JPanel buildContent()
    {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        JLabel logo = new JLabel(loadLogo(), JLabel.CENTER);
        content.add(logo, BorderLayout.CENTER);
        return content;
    }

    private ImageIcon loadLogo()
    {
        ImageIcon source = new ImageIcon("BC-logo.png");
        Image scaled = source.getImage().getScaledInstance(180, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void start()
    {
        setVisible(true);
        Timer fadeTimer = new Timer(FADE_DELAY, null);
        fadeTimer.addActionListener(e -> {
            opacity = Math.min(1.0f, opacity + FADE_STEP / 100.0f);
            setOpacity(opacity);
            if (opacity >= 1.0f)
            {
                fadeTimer.stop();
                Timer handoffTimer = new Timer(350, handoffEvent -> openShell());
                handoffTimer.setRepeats(false);
                handoffTimer.start();
            }
        });
        fadeTimer.start();
    }

    private void openShell()
    {
        MainShell mainShell = new MainShell(user);
        mainShell.setLocationRelativeTo(null);
        mainShell.setVisible(true);
        dispose();
    }
}
