package ui;

import dao.BookDAO;
import model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {
    private BookDAO dao = new BookDAO();
    private JTable table = new JTable();
    private DefaultTableModel model;

    public BookPanel() {
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(new Object[]{"ID","Title","Author","Publisher","ISBN","Year","Copies"},0);
        table.setModel(model);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showBookDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) showBookDialog(getBookFromRow(row));
            else JOptionPane.showMessageDialog(this,"Select a book to edit.");
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int)table.getValueAt(row,0);
                dao.delete(id);
                refreshTable();
            } else JOptionPane.showMessageDialog(this,"Select a book to delete.");
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Book> books = dao.findAll();
        for (Book b : books) {
            model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getPublisher(),
                    b.getIsbn(), b.getYear(), b.getCopiesAvailable()});
        }
    }

    private Book getBookFromRow(int row) {
        Book b = new Book();
        b.setId((int)table.getValueAt(row,0));
        b.setTitle((String)table.getValueAt(row,1));
        b.setAuthor((String)table.getValueAt(row,2));
        b.setPublisher((String)table.getValueAt(row,3));
        b.setIsbn((String)table.getValueAt(row,4));
        b.setYear((int)table.getValueAt(row,5));
        b.setCopiesAvailable((int)table.getValueAt(row,6));
        return b;
    }

    private void showBookDialog(Book book) {
        JTextField title = new JTextField(book!=null?book.getTitle():"");
        JTextField author = new JTextField(book!=null?book.getAuthor():"");
        JTextField publisher = new JTextField(book!=null?book.getPublisher():"");
        JTextField isbn = new JTextField(book!=null?book.getIsbn():"");
        JTextField year = new JTextField(book!=null?String.valueOf(book.getYear()):"");
        JTextField copies = new JTextField(book!=null?String.valueOf(book.getCopiesAvailable()):"");

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel("Title:")); panel.add(title);
        panel.add(new JLabel("Author:")); panel.add(author);
        panel.add(new JLabel("Publisher:")); panel.add(publisher);
        panel.add(new JLabel("ISBN:")); panel.add(isbn);
        panel.add(new JLabel("Year:")); panel.add(year);
        panel.add(new JLabel("Copies:")); panel.add(copies);

        int result = JOptionPane.showConfirmDialog(this,panel,
                book==null?"Add Book":"Edit Book",JOptionPane.OK_CANCEL_OPTION);
        if (result==JOptionPane.OK_OPTION) {
            if (book==null) book = new Book();
            book.setTitle(title.getText());
            book.setAuthor(author.getText());
            book.setPublisher(publisher.getText());
            book.setIsbn(isbn.getText());
            book.setYear(Integer.parseInt(year.getText()));
            book.setCopiesAvailable(Integer.parseInt(copies.getText()));
            if (book.getId()==0) dao.save(book); else dao.update(book);
            refreshTable();
        }
    }
}
