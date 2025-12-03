package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FormKeahlianGym extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtNama;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh, btnKeluar;
    private Connection conn;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;

    public FormKeahlianGym() {
        setTitle("Manajemen Keahlian Instruktur Gym");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        connectDB();
        initUI();
        loadTable();
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/db_gym",
                    "postgres",
                    "hione"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("MANAJEMEN KEAHLIAN GYM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitle = new JLabel("Kelola data keahlian instruktur");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(236, 240, 241));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titleContainer = new JPanel(new BorderLayout(0, 5));
        titleContainer.setBackground(PRIMARY_COLOR);
        titleContainer.add(lblTitle, BorderLayout.CENTER);
        titleContainer.add(lblSubtitle, BorderLayout.SOUTH);

        headerPanel.add(titleContainer, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblNama = new JLabel("Nama Keahlian:");
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        inputPanel.add(lblNama, gbc);

        txtNama = new JTextField();
        txtNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        inputPanel.add(txtNama, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnTambah = createStyledButton("Tambah", SUCCESS_COLOR);
        btnEdit = createStyledButton("Edit", WARNING_COLOR);
        btnHapus = createStyledButton("Hapus", DANGER_COLOR);
        btnRefresh = createStyledButton("Refresh", PRIMARY_COLOR);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);

        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(WHITE);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"ID", "Nama Keahlian"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(41, 128, 185));
                c.setForeground(Color.WHITE);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        scroll.getViewport().setBackground(WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(LIGHT_BG);
        
        JLabel lblTableTitle = new JLabel("Data Keahlian");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(LIGHT_BG);
        
        btnKeluar = createStyledButton("Keluar", new Color(52, 73, 94));
        btnKeluar.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(btnKeluar);

        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadTable());
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin keluar dari aplikasi?", 
                    "Konfirmasi Keluar", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int baris = table.getSelectedRow();
                if (baris != -1) {
                    txtNama.setText(model.getValueAt(baris, 1).toString());
                }
            }
        });

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private void loadTable() {
        model.setRowCount(0);
        try {
            String sql = "SELECT id_keahlian, nama_keahlian FROM keahlian_gym ORDER BY id_keahlian ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_keahlian"),
                        rs.getString("nama_keahlian")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahData() {
        String nama = txtNama.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama keahlian wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "INSERT INTO keahlian_gym (nama_keahlian) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            txtNama.setText("");
            loadTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        String nama = txtNama.getText().trim();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama keahlian wajib diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "UPDATE keahlian_gym SET nama_keahlian = ? WHERE id_keahlian = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama);
            stmt.setInt(2, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            loadTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal edit data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        int konfirmasi = JOptionPane.showConfirmDialog(
                this, "Yakin ingin menghapus data ini?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM keahlian_gym WHERE id_keahlian = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                txtNama.setText("");
                loadTable();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal hapus data!\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new FormKeahlianGym().setVisible(true);
        });
    }
}