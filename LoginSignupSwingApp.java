import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginSignupSwingApp extends JFrame {
    private static final String USERS_FILE = "users.txt";
    private static final String LOG_FILE = "login_log.txt";

    // Signup components
    private JTextField signupUserField;
    private JPasswordField signupPassField;
    private JLabel signupStatusLabel;

    // Login components
    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JLabel loginStatusLabel;

    public LoginSignupSwingApp() {
        setTitle("Login & Signup App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Signup Tab
        JPanel signupPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        signupPanel.add(new JLabel("New Username:"), gbc);
        gbc.gridx = 1;
        signupUserField = new JTextField(15);
        signupPanel.add(signupUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        signupPanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        signupPassField = new JPasswordField(15);
        signupPanel.add(signupPassField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton signupButton = new JButton("Signup");
        signupPanel.add(signupButton, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        signupStatusLabel = new JLabel(" ");
        signupPanel.add(signupStatusLabel, gbc);

        signupButton.addActionListener(e -> handleSignup());

        // Login Tab
        JPanel loginPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginUserField = new JTextField(15);
        loginPanel.add(loginUserField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPassField = new JPasswordField(15);
        loginPanel.add(loginPassField, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        loginStatusLabel = new JLabel(" ");
        loginPanel.add(loginStatusLabel, gbc);

        loginButton.addActionListener(e -> handleLogin());

        tabbedPane.addTab("Signup", signupPanel);
        tabbedPane.addTab("Login", loginPanel);

        add(tabbedPane);
    }

    private void handleSignup() {
        String username = signupUserField.getText().trim();
        String password = new String(signupPassField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            signupStatusLabel.setText("Please enter username and password.");
            return;
        }

        if (userExists(username)) {
            signupStatusLabel.setText("Username already exists.");
            return;
        }

        try (FileWriter fw = new FileWriter(USERS_FILE, true)) {
            fw.write(username + "," + password + "\n");
            signupStatusLabel.setText("Signup successful!");
            signupUserField.setText("");
            signupPassField.setText("");
        } catch (IOException e) {
            signupStatusLabel.setText("Error during signup.");
        }
    }

    private void handleLogin() {
        String username = loginUserField.getText().trim();
        String password = new String(loginPassField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            loginStatusLabel.setText("Please enter username and password.");
            logLoginAttempt(username, false);
            return;
        }

        int userCheck = checkUser(username, password);

        if (userCheck == 0) {
            loginStatusLabel.setText("Username invalid.");
            logLoginAttempt(username, false);
        } else if (userCheck == 1) {
            loginStatusLabel.setText("Password is incorrect for the user.");
            logLoginAttempt(username, false);
        } else {
            loginStatusLabel.setText("Login successful!");
            logLoginAttempt(username, true);
            showUserHome(username);
        }
    }

    // Returns: 0 = username not found, 1 = password incorrect, 2 = login success
    private int checkUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (parts[0].equals(username)) {
                        if (parts[1].equals(password)) {
                            return 2; // Success
                        } else {
                            return 1; // Password incorrect
                        }
                    }
                }
            }
        } catch (IOException e) {
            // File may not exist yet
        }
        return 0; // Username not found
    }

    private boolean userExists(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // File may not exist yet
        }
        return false;
    }

    private void logLoginAttempt(String username, boolean success) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = username + "," + timeStamp + "," + (success ? "SUCCESS" : "FAILURE") + "\n";
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(logEntry);
        } catch (IOException e) {
            // Ignore logging errors for now
        }
    }

    private void showUserHome(String username) {
        JFrame homeFrame = new JFrame("User Home");
        homeFrame.setSize(300, 150);
        homeFrame.setLocationRelativeTo(null);
        homeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        panel.add(logoutButton, BorderLayout.SOUTH);

        logoutButton.addActionListener(e -> {
            homeFrame.dispose();
            // Optionally, reset login fields
            loginUserField.setText("");
            loginPassField.setText("");
            loginStatusLabel.setText("Logged out successfully.");
            this.setVisible(true);
        });

        homeFrame.add(panel);
        this.setVisible(false);
        homeFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginSignupSwingApp app = new LoginSignupSwingApp();
            app.setVisible(true);
        });
    }
}
