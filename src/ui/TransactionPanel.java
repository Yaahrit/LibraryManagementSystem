package ui;

import dao.TransactionDAO;
import dao.MemberDAO;
import dao.BookDAO;
import model.Transaction;
import model.Member;
import model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TransactionPanel extends JPanel {
    private TransactionDAO dao = new TransactionDAO();
    private BookDAO bookDAO = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private JTable table = new JTable();
    private DefaultTableModel model;

    public TransactionPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID","Member","Book","Issue Date","Return Date","Status"},0);
        table.setModel(model);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(addBtn); buttons.add(editBtn); buttons.add(deleteBtn);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showTransactionDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0) showTransactionDialog(getTransactionFromRow(row));
            else JOptionPane.showMessageDialog(this,"Select a transaction to edit.");
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int)table.getValueAt(row,0);
                dao.delete(id);
                refreshTable();
            } else JOptionPane.showMessageDialog(this,"Select a transaction to delete.");
        });
    }

    private void refreshTable(){
        model.setRowCount(0);
        List<Transaction> list = dao.findAll();
        for(Transaction tx : list){
            Member m = memberDAO.findById(tx.getMemberId());
            Book b = bookDAO.findById(tx.getBookId());
            model.addRow(new Object[]{
                    tx.getId(),
                    m!=null?m.getName():"Unknown",
                    b!=null?b.getTitle():"Unknown",
                    tx.getIssueDate(),
                    tx.getReturnDate(),
                    tx.getStatus()
            });
        }
    }

    private Transaction getTransactionFromRow(int row){
        Transaction tx = new Transaction();
        tx.setId((int)table.getValueAt(row,0));
        // For editing, you may fetch full object from DAO
        return tx;
    }

    private void showTransactionDialog(Transaction tx){
        List<Member> members = memberDAO.findAll();
        List<Book> books = bookDAO.findAll();

        JComboBox<Member> memberBox = new JComboBox<>(members.toArray(new Member[0]));
        JComboBox<Book> bookBox = new JComboBox<>(books.toArray(new Book[0]));
        JTextField issueDate = new JTextField(LocalDate.now().toString());
        JTextField returnDate = new JTextField("");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"ISSUED","RETURNED","OVERDUE"});

        if(tx!=null){
            Member m = memberDAO.findById(tx.getMemberId());
            Book b = bookDAO.findById(tx.getBookId());
            memberBox.setSelectedItem(m);
            bookBox.setSelectedItem(b);
            issueDate.setText(tx.getIssueDate().toString());
            returnDate.setText(tx.getReturnDate()!=null?tx.getReturnDate().toString():"");
            statusBox.setSelectedItem(tx.getStatus());
        }

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel("Member:")); panel.add(memberBox);
        panel.add(new JLabel("Book:")); panel.add(bookBox);
        panel.add(new JLabel("Issue Date:")); panel.add(issueDate);
        panel.add(new JLabel("Return Date:")); panel.add(returnDate);
        panel.add(new JLabel("Status:")); panel.add(statusBox);

        int result = JOptionPane.showConfirmDialog(this,panel,
                tx==null?"Add Transaction":"Edit Transaction",JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION){
            if(tx==null) tx = new Transaction();
            tx.setMemberId(((Member)memberBox.getSelectedItem()).getId());
            tx.setBookId(((Book)bookBox.getSelectedItem()).getId());
            tx.setIssueDate(LocalDate.parse(issueDate.getText()));
            tx.setReturnDate(returnDate.getText().isEmpty()?null:LocalDate.parse(returnDate.getText()));
            tx.setStatus((String)statusBox.getSelectedItem());
            if(tx.getId()==0) dao.save(tx); else dao.update(tx);
            refreshTable();
        }
    }
}
