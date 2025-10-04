package ui;

import model.User;
import javax.swing.*;
import java.awt.*;

// Import the CRUD panels
import ui.BookPanel;
import ui.MemberPanel;
import ui.TransactionPanel;

public class AdminDashboard extends JFrame {

    private final User loggedInUser;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public AdminDashboard(User user) {
        this.loggedInUser = user;

        setTitle("Library Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Menu bar
        setJMenuBar(createMenuBar());

        // Add CRUD panels to tabs
        tabbedPane.addTab("Books", new BookPanel());
        tabbedPane.addTab("Members", new MemberPanel());
        tabbedPane.addTab("Transactions", new TransactionPanel());
        tabbedPane.addTab("Users", createPlaceholderPanel("👤 Manage Users (only Admin)"));

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true); // Make sure the frame is visible
    }

    // Menu bar
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");

        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Library Management System\nAdmin Panel\n© 2025", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    // Simple placeholder panel for Users tab
    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
