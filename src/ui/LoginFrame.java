package ui;

import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class LoginFrame extends JFrame {

    private JTextField usernameField = new JTextField(20);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton loginBtn = new JButton("Login");

    // In-memory user store (username -> User)
    private static Map<String, User> users = new HashMap<>();

    static {
        // Pre-created admin account
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRole("ADMIN");
        users.put(admin.getUsername(), admin);
    }

    public LoginFrame() {
        super("Library Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
        p.add(new JLabel("Username:"));
        p.add(usernameField);
        p.add(new JLabel("Password:"));
        p.add(passwordField);
        p.add(new JLabel());
        p.add(loginBtn);

        add(p, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        loginBtn.addActionListener(this::onLogin);
    }

    private void onLogin(ActionEvent e) {
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());

        if(u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Username and Password cannot be empty.");
            return;
        }

        User user = users.get(u);

        if(user != null) {
            // Existing user
            if(user.getPassword().equals(p)) {
                JOptionPane.showMessageDialog(this,"Welcome back, " + u + "!");
                openDashboard(user);
            } else {
                JOptionPane.showMessageDialog(this,"Invalid password.","Error",JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // New user → auto-register as USER
            User newUser = new User();
            newUser.setUsername(u);
            newUser.setPassword(p);
            newUser.setRole("USER");
            users.put(u,newUser);
            JOptionPane.showMessageDialog(this,"New user created! Welcome, " + u + "!");
            openDashboard(newUser);
        }

        this.dispose();
    }

    private void openDashboard(User user) {
        if("ADMIN".equalsIgnoreCase(user.getRole())) {
            new AdminDashboard(user);
        } else {
            new UserDashboard(user);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
