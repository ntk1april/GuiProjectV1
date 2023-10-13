import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class SearchGUI {
    private JTextField keywordField;
    private JTable resultTable;

    public SearchGUI() {
        JFrame frame = new JFrame("SearchApp");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        keywordField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        resultTable = new JTable();

        panel.add(keywordField);
        panel.add(searchButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordField.getText();
                searchAndDisplayResults(keyword);
            }
        });

        frame.setVisible(true);
    }

    private void searchAndDisplayResults(String keyword) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("StudentID");
        model.addColumn("FirstName");
        model.addColumn("LastName");
        model.addColumn("Address");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/gui?user=root&password=");
            String sql = "SELECT * FROM data WHERE StudentID LIKE ? OR FirstName LIKE ? OR LastName LIKE ? OR Address LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            pstmt.setString(4, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[] { rs.getInt("StudentID"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Address") });
            }

            resultTable.setModel(model);

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SearchGUI();
            }
        });
    }
}