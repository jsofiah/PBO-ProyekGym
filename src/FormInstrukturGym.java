package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FormInstrukturGym extends JFrame {
    
    private JTextField txtKode, txtNama, txtUsia, txtHP;
    private JComboBox<String> cbJK;
    private JTextArea txtAlamat;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnSimpan, btnEdit, btnHapus, btnReset, btnRefresh, btnKeluar;
    
    private Connection conn;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_BLUE = new Color(52, 73, 94);
    
    public FormInstrukturGym() {
        setTitle("Form Registrasi Instruktur Gym");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
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
        
        JLabel lblTitle = new JLabel("MANAJEMEN INSTRUKTUR GYM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitle = new JLabel("Kelola data instruktur");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(236, 240, 241));
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titleContainer = new JPanel(new BorderLayout(0, 5));
        titleContainer.setBackground(PRIMARY_COLOR);
        titleContainer.add(lblTitle, BorderLayout.CENTER);
        titleContainer.add(lblSubtitle, BorderLayout.SOUTH);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(20, 25, 20, 25)
        ));
        
        formPanel.add(createFormRow("Kode Instruktur:", txtKode = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(createFormRow("Nama Lengkap:", txtNama = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        formPanel.add(createFormRow("Usia:", txtUsia = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        String[] jkList = {"Laki-laki", "Perempuan"};
        cbJK = new JComboBox<>(jkList);
        cbJK.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbJK.setBackground(WHITE);
        cbJK.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbJK.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(createFormRow("Jenis Kelamin:", cbJK));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // No HP
        formPanel.add(createFormRow("No HP:", txtHP = createTextField()));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Alamat
        JPanel alamatPanel = new JPanel(new BorderLayout(10, 5));
        alamatPanel.setBackground(WHITE);
        alamatPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlamat.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtAlamat = new JTextArea(3, 20);
        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setBorder(new LineBorder(new Color(189, 195, 199), 1, true));
        
        alamatPanel.add(lblAlamat, BorderLayout.NORTH);
        alamatPanel.add(scrollAlamat, BorderLayout.CENTER);
        
        formPanel.add(alamatPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        btnSimpan = createStyledButton("Simpan", SUCCESS_COLOR);
        btnEdit = createStyledButton("Edit", WARNING_COLOR);
        btnHapus = createStyledButton("Hapus", DANGER_COLOR);
        btnReset = createStyledButton("Reset", PRIMARY_COLOR);
        btnRefresh = createStyledButton("Refresh", DARK_BLUE);
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRefresh);

        model = new DefaultTableModel(
            new String[]{"Kode", "Nama", "Usia", "JK", "No HP", "Alamat"}, 0) {
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
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                label.setForeground(Color.WHITE);
                label.setOpaque(true);
                label.setBackground(new Color(41, 128, 185));
                return label;
            }
        });

        
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        scrollTable.getViewport().setBackground(WHITE);
        
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(LIGHT_BG);
        
        JLabel lblTableTitle = new JLabel("Data Instruktur");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollTable, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(LIGHT_BG);
        
        btnKeluar = createStyledButton("Keluar", new Color(52, 73, 94));
        btnKeluar.setPreferredSize(new Dimension(150, 40));
        bottomPanel.add(btnKeluar);
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(WHITE);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JScrollPane scrollTop = new JScrollPane(topPanel);
        scrollTop.setBorder(null);
        scrollTop.getVerticalScrollBar().setUnitIncrement(16);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTop, tablePanel);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerSize(8);
        splitPane.setDividerLocation(350);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setupEventHandlers();
    }
    
    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(160, 25));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return txt;
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
    
    private void setupEventHandlers() {
        btnSimpan.addActionListener(e -> simpanData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadTable());
        
        btnReset.addActionListener(e -> {
            txtKode.setText("");
            txtNama.setText("");
            txtUsia.setText("");
            txtHP.setText("");
            txtAlamat.setText("");
            cbJK.setSelectedIndex(0);
            table.clearSelection();
        });
        
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin keluar dari aplikasi?", 
                    "Konfirmasi Keluar", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtKode.setText(model.getValueAt(row, 0).toString());
                    txtNama.setText(model.getValueAt(row, 1).toString());
                    txtUsia.setText(model.getValueAt(row, 2).toString());
                    cbJK.setSelectedItem(model.getValueAt(row, 3).toString());
                    txtHP.setText(model.getValueAt(row, 4).toString());
                    txtAlamat.setText(model.getValueAt(row, 5).toString());
                }
            }
        });
    }
    
    private void loadTable() {
        model.setRowCount(0);
        try {
            String sql = "SELECT kode_instruktur, nama, usia, jenis_kelamin, no_hp, alamat " +
                         "FROM instruktur_gym ORDER BY kode_instruktur ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kode_instruktur"),
                    rs.getString("nama"),
                    rs.getInt("usia"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("no_hp"),
                    rs.getString("alamat")
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void simpanData() {
        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String usiaStr = txtUsia.getText().trim();
        String jk = cbJK.getSelectedItem().toString();
        String hp = txtHP.getText().trim();
        String alamat = txtAlamat.getText().trim();
        
        if (kode.isEmpty() || nama.isEmpty() || usiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kode, Nama, dan Usia wajib diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int usia;
        try {
            usia = Integer.parseInt(usiaStr);
            if (usia < 18 || usia > 70) {
                JOptionPane.showMessageDialog(this,
                        "Usia instruktur harus antara 18-70 tahun!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Usia harus berupa angka!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String sql = "INSERT INTO instruktur_gym (kode_instruktur, nama, usia, jenis_kelamin, no_hp, alamat) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            stmt.setString(2, nama);
            stmt.setInt(3, usia);
            stmt.setString(4, jk);
            stmt.setString(5, hp);
            stmt.setString(6, alamat);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                    "Instruktur berhasil disimpan!",
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            btnReset.doClick();
            loadTable();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate key")) {
                JOptionPane.showMessageDialog(this,
                        "Kode instruktur sudah ada!\nGunakan kode yang berbeda.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan ke database!\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String kodeAsli = model.getValueAt(row, 0).toString();
        String kode = txtKode.getText().trim();
        String nama = txtNama.getText().trim();
        String usiaStr = txtUsia.getText().trim();
        String jk = cbJK.getSelectedItem().toString();
        String hp = txtHP.getText().trim();
        String alamat = txtAlamat.getText().trim();
        
        if (kode.isEmpty() || nama.isEmpty() || usiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Kode, Nama, dan Usia wajib diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int usia;
        try {
            usia = Integer.parseInt(usiaStr);
            if (usia < 18 || usia > 70) {
                JOptionPane.showMessageDialog(this,
                        "Usia instruktur harus antara 18-70 tahun!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Usia harus berupa angka!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String sql = "UPDATE instruktur_gym SET kode_instruktur=?, nama=?, usia=?, " +
                         "jenis_kelamin=?, no_hp=?, alamat=? WHERE kode_instruktur=?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, kode);
            stmt.setString(2, nama);
            stmt.setInt(3, usia);
            stmt.setString(4, jk);
            stmt.setString(5, hp);
            stmt.setString(6, alamat);
            stmt.setString(7, kodeAsli);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            loadTable();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal edit data!\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String kode = model.getValueAt(row, 0).toString();
        String nama = model.getValueAt(row, 1).toString();
        
        int konfirmasi = JOptionPane.showConfirmDialog(
                this, 
                "Yakin ingin menghapus data instruktur?\n\n" +
                "Kode: " + kode + "\nNama: " + nama, 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM instruktur_gym WHERE kode_instruktur = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, kode);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                btnReset.doClick();
                loadTable();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal hapus data!\n" + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
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
            new FormInstrukturGym().setVisible(true);
        });
    }
}