package ui;

import model.User;
import dao.BookDAO;
import dao.TransactionDAO;
import dao.MemberDAO;
import model.Book;
import model.Transaction;
import model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class UserDashboard extends JFrame {

    private final User loggedInUser;
    private JTabbedPane tabbedPane = new JTabbedPane();

    public UserDashboard(User user) {
        this.loggedInUser = user;

        setTitle("Library Management System - User Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());

        tabbedPane.addTab("Browse Books", createBookPanel());
        tabbedPane.addTab("My Transactions", createTransactionPanel());

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
                "Library Management System\nUser Panel\n© 2025", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        return menuBar;
    }

    // Panel to browse/search books
    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        BookDAO bookDAO = new BookDAO();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Title","Author","Publisher","ISBN","Year","Copies"},0);
        JTable table = new JTable(model);

        // Load books
        List<Book> books = bookDAO.findAll();
        for(Book b : books){
            model.addRow(new Object[]{b.getId(),b.getTitle(),b.getAuthor(),b.getPublisher(),b.getIsbn(),b.getYear(),b.getCopiesAvailable()});
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Search box
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("Search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);
        panel.add(searchPanel, BorderLayout.NORTH);

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim().toLowerCase();
            model.setRowCount(0);
            for(Book b : bookDAO.findAll()){
                if(b.getTitle().toLowerCase().contains(keyword) || b.getAuthor().toLowerCase().contains(keyword)){
                    model.addRow(new Object[]{b.getId(),b.getTitle(),b.getAuthor(),b.getPublisher(),b.getIsbn(),b.getYear(),b.getCopiesAvailable()});
                }
            }
        });

        return panel;
    }

    // Panel to view user's transactions
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        TransactionDAO txDAO = new TransactionDAO();
        MemberDAO memberDAO = new MemberDAO();
        BookDAO bookDAO = new BookDAO();

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Book","Issue Date","Return Date","Status"},0);
        JTable table = new JTable(model);

        // Find transactions of this user
        Member member = memberDAO.findAll().stream()
                .filter(m -> m.getEmail()!=null && m.getEmail().equalsIgnoreCase(loggedInUser.getUsername()))
                .findFirst().orElse(null);

        if(member!=null){
            List<Transaction> txs = txDAO.findAll();
            for(Transaction t : txs){
                if(t.getMemberId() == member.getId()){
                    Book b = bookDAO.findById(t.getBookId());
                    model.addRow(new Object[]{
                            b!=null?b.getTitle():"Unknown",
                            t.getIssueDate(),
                            t.getReturnDate(),
                            t.getStatus()
                    });
                }
            }
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
