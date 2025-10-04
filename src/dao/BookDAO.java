package dao;
import model.Book;
import db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import db.DBUtil;


public class BookDAO {

    public void save(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, isbn, year, copies_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getYear());
            ps.setInt(6, book.getCopiesAvailable());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) book.setId(rs.getInt(1));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Book book) {
        String sql = "UPDATE books SET title=?, author=?, publisher=?, isbn=?, year=?, copies_available=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getPublisher());
            ps.setString(4, book.getIsbn());
            ps.setInt(5, book.getYear());
            ps.setInt(6, book.getCopiesAvailable());
            ps.setInt(7, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Book findById(int id) {
        String sql = "SELECT * FROM books WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setTitle(rs.getString("title"));
        b.setAuthor(rs.getString("author"));
        b.setPublisher(rs.getString("publisher"));
        b.setIsbn(rs.getString("isbn"));
        b.setYear(rs.getInt("year"));
        b.setCopiesAvailable(rs.getInt("copies_available"));
        return b;
    }
}
