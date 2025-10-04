package dao;

import model.Transaction;
import db.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void save(Transaction tx) {
        String sql = "INSERT INTO transactions (member_id, book_id, issue_date, return_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tx.getMemberId());
            ps.setInt(2, tx.getBookId());
            ps.setDate(3, Date.valueOf(tx.getIssueDate()));
            if (tx.getReturnDate() != null) {
                ps.setDate(4, Date.valueOf(tx.getReturnDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, tx.getStatus());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) tx.setId(rs.getInt(1));

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void update(Transaction tx) {
        String sql = "UPDATE transactions SET member_id=?, book_id=?, issue_date=?, return_date=?, status=? WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, tx.getMemberId());
            ps.setInt(2, tx.getBookId());
            ps.setDate(3, Date.valueOf(tx.getIssueDate()));
            if (tx.getReturnDate() != null) {
                ps.setDate(4, Date.valueOf(tx.getReturnDate()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, tx.getStatus());
            ps.setInt(6, tx.getId());

            ps.executeUpdate();

        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM transactions WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Transaction findById(int id) {
        String sql = "SELECT * FROM transactions WHERE id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY issue_date DESC";
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction tx = new Transaction();
        tx.setId(rs.getInt("id"));
        tx.setMemberId(rs.getInt("member_id"));
        tx.setBookId(rs.getInt("book_id"));
        tx.setIssueDate(rs.getDate("issue_date").toLocalDate());
        Date ret = rs.getDate("return_date");
        tx.setReturnDate(ret != null ? ret.toLocalDate() : null);
        tx.setStatus(rs.getString("status"));
        return tx;
    }
}
