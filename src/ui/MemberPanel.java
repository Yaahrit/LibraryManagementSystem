package ui;

import dao.MemberDAO;
import model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MemberPanel extends JPanel {
    private MemberDAO dao = new MemberDAO();
    private JTable table = new JTable();
    private DefaultTableModel model;

    public MemberPanel() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID","Name","Email","Phone"},0);
        table.setModel(model);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(addBtn); buttons.add(editBtn); buttons.add(deleteBtn);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showMemberDialog(null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0) showMemberDialog(getMemberFromRow(row));
            else JOptionPane.showMessageDialog(this,"Select a member to edit.");
        });
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int)table.getValueAt(row,0);
                dao.delete(id);
                refreshTable();
            } else JOptionPane.showMessageDialog(this,"Select a member to delete.");
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Member> list = dao.findAll();
        for(Member m : list){
            model.addRow(new Object[]{m.getId(), m.getName(), m.getEmail(), m.getPhone()});
        }
    }

    private Member getMemberFromRow(int row){
        Member m = new Member();
        m.setId((int)table.getValueAt(row,0));
        m.setName((String)table.getValueAt(row,1));
        m.setEmail((String)table.getValueAt(row,2));
        m.setPhone((String)table.getValueAt(row,3));
        return m;
    }

    private void showMemberDialog(Member member){
        JTextField name = new JTextField(member!=null?member.getName():"");
        JTextField email = new JTextField(member!=null?member.getEmail():"");
        JTextField phone = new JTextField(member!=null?member.getPhone():"");

        JPanel panel = new JPanel(new GridLayout(0,2));
        panel.add(new JLabel("Name:")); panel.add(name);
        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Phone:")); panel.add(phone);

        int result = JOptionPane.showConfirmDialog(this,panel,
                member==null?"Add Member":"Edit Member",JOptionPane.OK_CANCEL_OPTION);
        if(result==JOptionPane.OK_OPTION){
            if(member==null) member = new Member();
            member.setName(name.getText());
            member.setEmail(email.getText());
            member.setPhone(phone.getText());
            if(member.getId()==0) dao.save(member); else dao.update(member);
            refreshTable();
        }
    }
}
