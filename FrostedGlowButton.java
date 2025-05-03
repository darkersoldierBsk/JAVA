import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrostedGlowButton extends JButton {
    private float glowAlpha = 0f;
    private Timer glowTimer;
    private boolean hovering = false;

    public FrostedGlowButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                startGlowAnimation(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                startGlowAnimation(false);
            }
        });
    }

    public FrostedGlowButton(ImageIcon icon) {
        super(icon);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                startGlowAnimation(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                startGlowAnimation(false);
            }
        });
    }


    private void startGlowAnimation(boolean fadeIn) {
        if (glowTimer != null && glowTimer.isRunning()) {
            glowTimer.stop();
        }

        glowTimer = new Timer(20, e -> {
            if (fadeIn) {
                glowAlpha += 0.05f;
                if (glowAlpha >= 1f) {
                    glowAlpha = 1f;
                    glowTimer.stop();
                }
            } else {
                glowAlpha -= 0.05f;
                if (glowAlpha <= 0f) {
                    glowAlpha = 0f;
                    glowTimer.stop();
                }
            }
            repaint();
        });
        glowTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        if (glowAlpha > 0f) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Frosted background
            Color frostedColor = new Color(255, 255, 255, (int) (80 * glowAlpha));
            g2.setColor(frostedColor);
            g2.fillRoundRect(0, 0, width, height, 20, 20);

            // Glow border
            Color glowColor = new Color(255, 255, 255, (int) (120 * glowAlpha));
            g2.setColor(glowColor);
            g2.setStroke(new BasicStroke(5));
            g2.drawRoundRect(2, 2, width - 4, height - 4, 20, 20);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
