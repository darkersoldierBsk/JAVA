import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PomodoroTimer {

    private static JFrame frame;
    private static JLabel timerLabel;
    private static Timer timer;
    private static int timeLeft = 25 * 60; // 25 minutes
    private static final int WIDTH = 500;  // Updated width
    private static final int HEIGHT = 300; // Updated height

    private static JButton toggleButton;
    private static JButton resetButton;
    private static JButton settingsButton;
    private static int counter = 0;
    private static boolean isRunning = false;
    private static ImageIcon resetIcon;
    private static ImageIcon settingsIcon;
    private static Image originalResetImage;

    public static void showPage() {
        frame = new JFrame("Pomodoro Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null); // Center on screen

        // Load reset icon
        resetIcon = new ImageIcon(PomodoroTimer.class.getResource("/assets/reset.png"));
        originalResetImage = resetIcon.getImage();

        // Load settings icon
        settingsIcon = new ImageIcon(PomodoroTimer.class.getResource("/assets/settings.png"));

        // Background image
        Image backgroundImage = new ImageIcon(
                PomodoroTimer.class.getResource("/assets/background1.jpg")).getImage();
        BackgroundPanel panel = new BackgroundPanel(backgroundImage);
        panel.setLayout(new BorderLayout());

        // Timer label
        timerLabel = new JLabel(formatTime(timeLeft), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 40));
        timerLabel.setForeground(Color.WHITE);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adjusted vertical gap

        // Settings button (icon)
        settingsButton = new JButton();
        settingsButton.setIcon(settingsIcon);
        settingsButton.setPreferredSize(new Dimension(50, 50)); // Icon size
        settingsButton.setBorderPainted(false);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setFocusPainted(false);
        settingsButton.addActionListener(e -> openSettings());

        // Start button
        toggleButton = new JButton("Start");
        toggleButton.setPreferredSize(new Dimension(100, 50));
        toggleButton.setFont(new Font("Arial", Font.BOLD, 18));
        toggleButton.addActionListener(e -> toggleTimer());

        // Reset button with icon
        resetButton = new JButton();
        resetButton.setIcon(resetIcon); // Set the icon
        resetButton.setPreferredSize(new Dimension(50, 50)); // Size of the icon
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> resetTimer());

        // Add buttons to the panel
        buttonPanel.add(settingsButton);  // Added settings button
        buttonPanel.add(toggleButton);
        buttonPanel.add(resetButton);

        // Center everything
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(timerLabel, BorderLayout.CENTER);  // Add timer to the center
        centerPanel.add(buttonPanel, BorderLayout.SOUTH); // Add button panel at the bottom

        panel.add(centerPanel, BorderLayout.CENTER); // Add the centered panel to the main frame

        frame.setContentPane(panel);
        frame.setVisible(true);

        // Add a window listener to dynamically resize components on window size change
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });

        // Initially set the layout based on window size
        updateLayout();
    }

    private static void toggleTimer() {
        counter++;

        if (timer == null) {
            timer = new Timer(1000, e -> {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerLabel.setText(formatTime(timeLeft));
                } else {
                    timer.stop();
                    isRunning = false;
                    toggleButton.setText("Start");
                    JOptionPane.showMessageDialog(frame, "Time's up!");
                    counter = 0; // Reset counter
                    timeLeft = 25 * 60;
                    timerLabel.setText(formatTime(timeLeft));
                }
            });
        }

        if (!isRunning) {
            timer.start();
            isRunning = true;
            toggleButton.setText("Pause");
        } else {
            timer.stop();
            isRunning = false;
            toggleButton.setText("Resume");
        }
    }

    private static void resetTimer() {
        if (timer != null) {
            timer.stop();
            isRunning = false;
            counter = 0;
            timeLeft = 25 * 60;
            timerLabel.setText(formatTime(timeLeft));
            toggleButton.setText("Start");
        }
    }

    private static String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private static void updateLayout() {
        int width = frame.getWidth();

        String sizeClass;
        if (width < 500) {
            sizeClass = "SMALL";
        } else if (width < 1000) {
            sizeClass = "MEDIUM";
        } else {
            sizeClass = "LARGE";
        }

        int iconSize;
        Font timerFont;
        Font buttonFont;
        Font settingsButtonFont;
        Dimension buttonSize;

        switch (sizeClass) {
            case "SMALL":
                timerFont = new Font("Arial", Font.BOLD, 100);
                buttonFont = new Font("Arial", Font.PLAIN, 14);
                settingsButtonFont = new Font("Arial", Font.PLAIN, 14);
                buttonSize = new Dimension(80, 40);
                iconSize = 32;
                break;
            case "MEDIUM":
                timerFont = new Font("Arial", Font.BOLD, 90);
                buttonFont = new Font("Arial", Font.BOLD, 18);
                settingsButtonFont = new Font("Arial", Font.BOLD, 18);
                buttonSize = new Dimension(100, 50);
                iconSize = 48;
                break;
            case "LARGE":
                timerFont = new Font("Arial", Font.BOLD, 250);
                buttonFont = new Font("Arial", Font.BOLD, 24);
                settingsButtonFont = new Font("Arial", Font.BOLD, 24);
                buttonSize = new Dimension(150, 70);
                iconSize = 64;
                break;
            default:
                timerFont = new Font("Arial", Font.BOLD, 36);
                buttonFont = new Font("Arial", Font.BOLD, 18);
                settingsButtonFont = new Font("Arial", Font.BOLD, 18);
                buttonSize = new Dimension(100, 50);
                iconSize = 48;
        }

        // Apply font sizes
        timerLabel.setFont(timerFont);
        toggleButton.setFont(buttonFont);
        resetButton.setFont(buttonFont); // Apply same font for consistency
        settingsButton.setFont(settingsButtonFont);

        // Apply button sizes
        toggleButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);

        // Resize icon for settings button
        Image scaled = settingsIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        settingsButton.setIcon(new ImageIcon(scaled));

        frame.revalidate();
        frame.repaint();
    }

    private static void openSettings() {
        PomodoroSettings.showSettings();  // Open the settings window when the icon is clicked
    }

    // Background image panel
    static class BackgroundPanel extends JPanel {
        private Image image;

        public BackgroundPanel(Image img) {
            this.image = img;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
