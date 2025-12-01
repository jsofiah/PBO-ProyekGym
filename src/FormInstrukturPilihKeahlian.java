package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class FormInstrukturPilihKeahlian extends JFrame {
    
    private JComboBox<String> cbInstruktur, cbKeahlian;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnTambah, btnEdit, btnHapus, btnReset, btnRefresh, btnKeluar;
    
    private Connection conn;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_BLUE = new Color(52, 73, 94);
    
    public FormInstrukturPilihKeahlian() {
        setTitle("Manajemen Keahlian Instruktur");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        connectDB();
        initUI();
        loadComboBox();
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
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("MANAJEMEN KEAHLIAN INSTRUKTUR");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitle = new JLabel("Hubungkan instruktur dengan keahliannya");
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
                new EmptyBorder(25, 30, 25, 30)
        ));
        
        cbInstruktur = createComboBox();
        formPanel.add(createFormRow("Pilih Instruktur:", cbInstruktur));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        cbKeahlian = createComboBox();
        formPanel.add(createFormRow("Pilih Keahlian:", cbKeahlian));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        btnTambah = createStyledButton("Tambah", SUCCESS_COLOR);
        btnEdit = createStyledButton("Edit", WARNING_COLOR);
        btnHapus = createStyledButton("Hapus", DANGER_COLOR);
        btnReset = createStyledButton("Reset", PRIMARY_COLOR);
        btnRefresh = createStyledButton("Refresh", DARK_BLUE);
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);
        buttonPanel.add(btnRefresh);
        
        model = new DefaultTableModel(
            new String[]{"Kode Instruktur", "Nama Instruktur", "Keahlian"}, 0) {
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
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setForeground(WHITE);
                return label;
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), w, 0, new Color(21, 67, 96));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                
                super.paintComponent(g);
            }
        });

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        scrollTable.getViewport().setBackground(WHITE);
        
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(LIGHT_BG);
        
        JLabel lblTableTitle = new JLabel("📋 Data Keahlian Instruktur");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        tablePanel.add(lblTableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollTable, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(LIGHT_BG);
        
        btnKeluar = createStyledButton("🚪 Keluar", new Color(52, 73, 94));
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
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerSize(8);
        splitPane.setDividerLocation(280);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        setupEventHandlers();
    }
    
    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(180, 25));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JComboBox<String> createComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cb.setBackground(WHITE);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cb.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return cb;
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
        btnTambah.addActionListener(e -> tambahData());
        btnEdit.addActionListener(e -> editData());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> {
            loadComboBox();
            loadTable();
        });
        
        btnReset.addActionListener(e -> {
            if (cbInstruktur.getItemCount() > 0) cbInstruktur.setSelectedIndex(0);
            if (cbKeahlian.getItemCount() > 0) cbKeahlian.setSelectedIndex(0);
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
                    String kodeInstruktur = model.getValueAt(row, 0).toString();
                    String namaInstruktur = model.getValueAt(row, 1).toString();
                    String namaKeahlian = model.getValueAt(row, 2).toString();
                    
                    for (int i = 0; i < cbInstruktur.getItemCount(); i++) {
                        String item = cbInstruktur.getItemAt(i);
                        if (item.contains(kodeInstruktur) && item.contains(namaInstruktur)) {
                            cbInstruktur.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    for (int i = 0; i < cbKeahlian.getItemCount(); i++) {
                        String item = cbKeahlian.getItemAt(i);
                        if (item.contains(namaKeahlian)) {
                            cbKeahlian.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }
    
    private void loadComboBox() {
        cbInstruktur.removeAllItems();
        cbKeahlian.removeAllItems();
        
        try {
            String sql1 = "SELECT kode_instruktur, nama FROM instruktur_gym ORDER BY nama";
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            ResultSet rs1 = stmt1.executeQuery();
            
            while (rs1.next()) {
                cbInstruktur.addItem(rs1.getString("kode_instruktur") + " - " + rs1.getString("nama"));
            }
            
            String sql2 = "SELECT id_keahlian, nama_keahlian FROM keahlian_gym ORDER BY nama_keahlian";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            ResultSet rs2 = stmt2.executeQuery();
            
            while (rs2.next()) {
                cbKeahlian.addItem(rs2.getInt("id_keahlian") + " - " + rs2.getString("nama_keahlian"));
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load ComboBox!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadTable() {
        model.setRowCount(0);
        try {
            String sql = "SELECT i.kode_instruktur, i.nama, k.nama_keahlian " +
                         "FROM instruktur_keahlian ik " +
                         "JOIN instruktur_gym i ON ik.id_instruktur = i.id_instruktur " +
                         "JOIN keahlian_gym k ON ik.id_keahlian = k.id_keahlian " +
                         "ORDER BY i.nama, k.nama_keahlian";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("kode_instruktur"),
                    rs.getString("nama"),
                    rs.getString("nama_keahlian")
                });
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tambahData() {
        if (cbInstruktur.getSelectedItem() == null || cbKeahlian.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                    "Instruktur dan Keahlian wajib dipilih!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String kodeInstruktur = cbInstruktur.getSelectedItem().toString().split(" - ")[0];
            
            int idKeahlian = Integer.parseInt(cbKeahlian.getSelectedItem().toString().split(" - ")[0]);
            
            String sqlGetId = "SELECT id_instruktur FROM instruktur_gym WHERE kode_instruktur = ?";
            PreparedStatement stmtGetId = conn.prepareStatement(sqlGetId);
            stmtGetId.setString(1, kodeInstruktur);
            ResultSet rsId = stmtGetId.executeQuery();
            
            if (!rsId.next()) {
                JOptionPane.showMessageDialog(this, "Instruktur tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idInstruktur = rsId.getInt("id_instruktur");
            
            String sql = "INSERT INTO instruktur_keahlian (id_instruktur, id_keahlian) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idInstruktur);
            stmt.setInt(2, idKeahlian);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                    "Keahlian instruktur berhasil ditambahkan!",
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            loadTable();
            btnReset.doClick();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate key") || ex.getMessage().contains("unique")) {
                JOptionPane.showMessageDialog(this,
                        "Instruktur sudah memiliki keahlian ini!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal tambah data!\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Terjadi kesalahan!\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cbInstruktur.getSelectedItem() == null || cbKeahlian.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                    "Instruktur dan Keahlian wajib dipilih!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String kodeInstrukturLama = model.getValueAt(row, 0).toString();
            String namaKeahlianLama = model.getValueAt(row, 2).toString();

            String sqlGetIdLama = "SELECT i.id_instruktur, k.id_keahlian " +
                                  "FROM instruktur_gym i, keahlian_gym k " +
                                  "WHERE i.kode_instruktur = ? AND k.nama_keahlian = ?";
            PreparedStatement stmtGetIdLama = conn.prepareStatement(sqlGetIdLama);
            stmtGetIdLama.setString(1, kodeInstrukturLama);
            stmtGetIdLama.setString(2, namaKeahlianLama);
            ResultSet rsLama = stmtGetIdLama.executeQuery();
            
            if (!rsLama.next()) {
                JOptionPane.showMessageDialog(this, "Data lama tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idInstrukturLama = rsLama.getInt("id_instruktur");
            int idKeahlianLama = rsLama.getInt("id_keahlian");
            
            String kodeInstrukturBaru = cbInstruktur.getSelectedItem().toString().split(" - ")[0];
            int idKeahlianBaru = Integer.parseInt(cbKeahlian.getSelectedItem().toString().split(" - ")[0]);

            String sqlGetIdBaru = "SELECT id_instruktur FROM instruktur_gym WHERE kode_instruktur = ?";
            PreparedStatement stmtGetIdBaru = conn.prepareStatement(sqlGetIdBaru);
            stmtGetIdBaru.setString(1, kodeInstrukturBaru);
            ResultSet rsBaru = stmtGetIdBaru.executeQuery();
            
            if (!rsBaru.next()) {
                JOptionPane.showMessageDialog(this, "Instruktur tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int idInstrukturBaru = rsBaru.getInt("id_instruktur");
            
            String sqlDelete = "DELETE FROM instruktur_keahlian WHERE id_instruktur=? AND id_keahlian=?";
            PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete);
            stmtDelete.setInt(1, idInstrukturLama);
            stmtDelete.setInt(2, idKeahlianLama);
            stmtDelete.executeUpdate();
            
            String sqlInsert = "INSERT INTO instruktur_keahlian (id_instruktur, id_keahlian) VALUES (?, ?)";
            PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
            stmtInsert.setInt(1, idInstrukturBaru);
            stmtInsert.setInt(2, idKeahlianBaru);
            stmtInsert.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            loadTable();
            
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate key") || ex.getMessage().contains("unique")) {
                JOptionPane.showMessageDialog(this,
                        "Instruktur sudah memiliki keahlian ini!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal edit data!\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
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
        
        String kodeInstruktur = model.getValueAt(row, 0).toString();
        String namaInstruktur = model.getValueAt(row, 1).toString();
        String namaKeahlian = model.getValueAt(row, 2).toString();
        
        int konfirmasi = JOptionPane.showConfirmDialog(
                this, 
                "Yakin ingin menghapus data?\n\n" +
                "Instruktur: " + namaInstruktur + "\n" +
                "Keahlian: " + namaKeahlian, 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        
        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                String sqlGetId = "SELECT i.id_instruktur, k.id_keahlian " +
                                  "FROM instruktur_gym i, keahlian_gym k " +
                                  "WHERE i.kode_instruktur = ? AND k.nama_keahlian = ?";
                PreparedStatement stmtGetId = conn.prepareStatement(sqlGetId);
                stmtGetId.setString(1, kodeInstruktur);
                stmtGetId.setString(2, namaKeahlian);
                ResultSet rs = stmtGetId.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int idInstruktur = rs.getInt("id_instruktur");
                int idKeahlian = rs.getInt("id_keahlian");

                String sql = "DELETE FROM instruktur_keahlian WHERE id_instruktur = ? AND id_keahlian = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idInstruktur);
                stmt.setInt(2, idKeahlian);
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
            new FormInstrukturPilihKeahlian().setVisible(true);
        });
    }
}