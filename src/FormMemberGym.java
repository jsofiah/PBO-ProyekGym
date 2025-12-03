package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class FormMemberGym extends JFrame{
    private JTextField txtKodeMember, txtNama, txtUsia, txtNoHp;
    private JTextArea txtAlamat;
    private JComboBox<String> cbJenisKelamin;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnEdit, btnHapus, btnRefresh, btnKeluar, btnBatal;
    
    private String selectedId = null;
    
    // Color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color LIGHT_GRAY = new Color(236, 240, 241);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                new FormInstrukturGym().setVisible(true);
            });
        });
    }
    
    public FormMemberGym() {
        initComponents();
        loadDataToTable();
    }
    
    private void initComponents() {
        setTitle("Manajemen Data Member Gym");
        setSize(950, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(LIGHT_GRAY);
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 950, 70);
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setLayout(null);
        add(headerPanel);
        
        JLabel lblTitle = new JLabel("MANAJEMEN MEMBER GYM");
        lblTitle.setBounds(20, 15, 500, 40);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Panel Form Input
        JPanel panelForm = new JPanel();
        panelForm.setBounds(20, 90, 400, 320);
        panelForm.setLayout(null);
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        add(panelForm);
        
        // Title form
        JLabel lblFormTitle = new JLabel("Form Data Member");
        lblFormTitle.setBounds(10, 5, 200, 25);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFormTitle.setForeground(DARK_GRAY);
        panelForm.add(lblFormTitle);
        
        // Kode Member
        JLabel lblKode = new JLabel("Kode Member:");
        lblKode.setBounds(15, 40, 120, 25);
        lblKode.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblKode);
        
        txtKodeMember = new JTextField();
        txtKodeMember.setBounds(140, 40, 240, 30);
        txtKodeMember.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtKodeMember.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelForm.add(txtKodeMember);
        
        // Nama
        JLabel lblNama = new JLabel("Nama:");
        lblNama.setBounds(15, 80, 120, 25);
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblNama);
        
        txtNama = new JTextField();
        txtNama.setBounds(140, 80, 240, 30);
        txtNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelForm.add(txtNama);
        
        // Jenis Kelamin
        JLabel lblJK = new JLabel("Jenis Kelamin:");
        lblJK.setBounds(15, 120, 120, 25);
        lblJK.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblJK);
        
        String[] jenisKelamin = {"Laki-laki", "Perempuan"};
        cbJenisKelamin = new JComboBox<>(jenisKelamin);
        cbJenisKelamin.setBounds(140, 120, 240, 30);
        cbJenisKelamin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbJenisKelamin.setBackground(Color.WHITE);
        panelForm.add(cbJenisKelamin);
        
        // Usia
        JLabel lblUsia = new JLabel("Usia:");
        lblUsia.setBounds(15, 160, 120, 25);
        lblUsia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblUsia);
        
        txtUsia = new JTextField();
        txtUsia.setBounds(140, 160, 240, 30);
        txtUsia.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsia.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelForm.add(txtUsia);
        
        // No HP
        JLabel lblHP = new JLabel("No. HP:");
        lblHP.setBounds(15, 200, 120, 25);
        lblHP.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblHP);
        
        txtNoHp = new JTextField();
        txtNoHp.setBounds(140, 200, 240, 30);
        txtNoHp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtNoHp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panelForm.add(txtNoHp);
        
        // Alamat
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setBounds(15, 240, 120, 25);
        lblAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelForm.add(lblAlamat);
        
        txtAlamat = new JTextArea();
        txtAlamat.setLineWrap(true);
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtAlamat.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        scrollAlamat.setBounds(140, 240, 240, 60);
        scrollAlamat.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelForm.add(scrollAlamat);
        
        // Panel Tombol
        JPanel panelButton = new JPanel();
        panelButton.setBounds(20, 420, 400, 100);
        panelButton.setLayout(new GridLayout(2, 3, 10, 10));
        panelButton.setBackground(LIGHT_GRAY);
        add(panelButton);
        
        btnSimpan = createStyledButton("Simpan", SUCCESS_COLOR);
        btnSimpan.addActionListener(e -> simpanData());
        panelButton.add(btnSimpan);
        
        btnEdit = createStyledButton("Edit", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> editData());
        panelButton.add(btnEdit);
        
        btnHapus = createStyledButton("Hapus", DANGER_COLOR);
        btnHapus.addActionListener(e -> hapusData());
        panelButton.add(btnHapus);
        
        btnBatal = createStyledButton("Batal", WARNING_COLOR);
        btnBatal.addActionListener(e -> batalEdit());
        panelButton.add(btnBatal);
        
        btnRefresh = createStyledButton("Refresh", new Color(52, 152, 219));
        btnRefresh.addActionListener(e -> loadDataToTable());
        panelButton.add(btnRefresh);
        
        btnKeluar = createStyledButton("Keluar", DARK_GRAY);
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin keluar?", 
                "Konfirmasi", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
        panelButton.add(btnKeluar);
        
        // Panel Tabel
        JPanel panelTable = new JPanel();
        panelTable.setBounds(440, 90, 490, 570);
        panelTable.setLayout(new BorderLayout());
        panelTable.setBackground(Color.WHITE);
        panelTable.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        add(panelTable);
        
        JLabel lblTableTitle = new JLabel("Data Member Terdaftar");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setForeground(DARK_GRAY);
        lblTableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelTable.add(lblTableTitle, BorderLayout.NORTH);
        
        // Tabel
        String[] columns = {"ID", "Kode Member", "Nama", "JK", "Usia", "No HP", "Alamat", "Tgl Daftar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(189, 195, 199));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        // Center alignment untuk kolom tertentu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        
        // Event klik tabel
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = tableModel.getValueAt(row, 0).toString();
                    txtKodeMember.setText(tableModel.getValueAt(row, 1).toString());
                    txtNama.setText(tableModel.getValueAt(row, 2).toString());
                    cbJenisKelamin.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtUsia.setText(tableModel.getValueAt(row, 4).toString());
                    txtNoHp.setText(tableModel.getValueAt(row, 5).toString());
                    txtAlamat.setText(tableModel.getValueAt(row, 6).toString());
                    
                    btnSimpan.setText("Update");
                }
            }
        });
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        panelTable.add(scrollTable, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db_gym",
            "postgres",
            "hione"
        );
    }
    
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT id_member, kode_member, nama, jenis_kelamin, usia, no_hp, alamat, tanggal_daftar " +
                 "FROM member_gym ORDER BY id_member DESC")) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_member"),
                    rs.getString("kode_member"),
                    rs.getString("nama"),
                    rs.getString("jenis_kelamin"),
                    rs.getInt("usia"),
                    rs.getString("no_hp"),
                    rs.getString("alamat"),
                    rs.getDate("tanggal_daftar")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Gagal memuat data!\n" + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void simpanData() {
        if (txtKodeMember.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Kode Member dan Nama wajib diisi!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int usia;
        try {
            usia = Integer.parseInt(txtUsia.getText().trim());
            if (usia < 10 || usia > 90) {
                JOptionPane.showMessageDialog(this, 
                    "Usia harus antara 10-90 tahun!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Usia harus berupa angka!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = getConnection()) {
            if (selectedId == null) {
                String sql = "INSERT INTO member_gym (kode_member, nama, jenis_kelamin, usia, no_hp, alamat) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, txtKodeMember.getText().trim());
                stmt.setString(2, txtNama.getText().trim());
                stmt.setString(3, cbJenisKelamin.getSelectedItem().toString());
                stmt.setInt(4, usia);
                stmt.setString(5, txtNoHp.getText().trim());
                stmt.setString(6, txtAlamat.getText().trim());
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil disimpan!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                String sql = "UPDATE member_gym SET kode_member=?, nama=?, jenis_kelamin=?, usia=?, no_hp=?, alamat=? " +
                            "WHERE id_member=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, txtKodeMember.getText().trim());
                stmt.setString(2, txtNama.getText().trim());
                stmt.setString(3, cbJenisKelamin.getSelectedItem().toString());
                stmt.setInt(4, usia);
                stmt.setString(5, txtNoHp.getText().trim());
                stmt.setString(6, txtAlamat.getText().trim());
                stmt.setInt(7, Integer.parseInt(selectedId));
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil diupdate!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            clearForm();
            loadDataToTable();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Gagal menyimpan data!\n" + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void editData() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang ingin diedit dari tabel!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        btnSimpan.setText("Update");
    }
    
    private void hapusData() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data yang ingin dihapus dari tabel!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM member_gym WHERE id_member=?")) {
                
                stmt.setInt(1, Integer.parseInt(selectedId));
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil dihapus!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearForm();
                loadDataToTable();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus data!\n" + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void batalEdit() {
        clearForm();
    }
    
    private void clearForm() {
        txtKodeMember.setText("");
        txtNama.setText("");
        cbJenisKelamin.setSelectedIndex(0);
        txtUsia.setText("");
        txtNoHp.setText("");
        txtAlamat.setText("");
        selectedId = null;
        btnSimpan.setText("Simpan");
        table.clearSelection();
    }
}