package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class FormPendaftaranKelas extends JFrame {
    private JFrame frame;
    private JTextField txtIdPendaftaran, txtTanggalDaftar;
    private JComboBox<String> cbMember, cbKelas;
    private JTextArea txtCatatan;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSimpan, btnUpdate, btnDelete, btnRefresh, btnReset, btnKeluar;
    
    private Connection conn;

    public FormPendaftaranKelas() {
        initialize();
        connectDatabase();
        loadMembers();
        loadKelas();
        loadTableData();
    }

    private void initialize() {
        frame = new JFrame("Form Pendaftaran Kelas Gym");
        frame.setSize(900, 600);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        // Panel Form Input
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBounds(20, 20, 400, 350);
        panelForm.setBorder(BorderFactory.createTitledBorder("Data Pendaftaran"));
        panelForm.setBackground(Color.WHITE);
        frame.add(panelForm);

        // ID Pendaftaran
        JLabel lblId = new JLabel("ID Pendaftaran:");
        lblId.setBounds(20, 30, 120, 25);
        panelForm.add(lblId);

        txtIdPendaftaran = new JTextField();
        txtIdPendaftaran.setBounds(150, 30, 220, 25);
        txtIdPendaftaran.setEditable(false);
        txtIdPendaftaran.setBackground(new Color(230, 230, 230));
        panelForm.add(txtIdPendaftaran);

        // Member
        JLabel lblMember = new JLabel("Member:");
        lblMember.setBounds(20, 70, 120, 25);
        panelForm.add(lblMember);

        cbMember = new JComboBox<>();
        cbMember.setBounds(150, 70, 220, 25);
        panelForm.add(cbMember);

        // Kelas Gym
        JLabel lblKelas = new JLabel("Kelas Gym:");
        lblKelas.setBounds(20, 110, 120, 25);
        panelForm.add(lblKelas);

        cbKelas = new JComboBox<>();
        cbKelas.setBounds(150, 110, 220, 25);
        panelForm.add(cbKelas);

        // Tanggal Daftar
        JLabel lblTanggal = new JLabel("Tanggal Daftar:");
        lblTanggal.setBounds(20, 150, 120, 25);
        panelForm.add(lblTanggal);

        txtTanggalDaftar = new JTextField();
        txtTanggalDaftar.setBounds(150, 150, 220, 25);
        txtTanggalDaftar.setText(java.time.LocalDate.now().toString());
        panelForm.add(txtTanggalDaftar);

        // Catatan
        JLabel lblCatatan = new JLabel("Catatan:");
        lblCatatan.setBounds(20, 190, 120, 25);
        panelForm.add(lblCatatan);

        txtCatatan = new JTextArea();
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        JScrollPane scrollCatatan = new JScrollPane(txtCatatan);
        scrollCatatan.setBounds(150, 190, 220, 100);
        panelForm.add(scrollCatatan);

        // Panel Buttons
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(3, 2, 10, 10));
        panelButtons.setBounds(20, 380, 400, 150);
        panelButtons.setBackground(new Color(240, 240, 240));
        frame.add(panelButtons);

        btnSimpan = new JButton("Simpan");
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        panelButtons.add(btnSimpan);

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        panelButtons.add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        panelButtons.add(btnDelete);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(241, 196, 15));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        panelButtons.add(btnRefresh);

        btnReset = new JButton("Reset");
        btnReset.setBackground(new Color(149, 165, 166));
        btnReset.setForeground(Color.WHITE);
        btnReset.setFocusPainted(false);
        panelButtons.add(btnReset);

        btnKeluar = new JButton("Keluar");
        btnKeluar.setBackground(new Color(52, 73, 94));
        btnKeluar.setForeground(Color.WHITE);
        btnKeluar.setFocusPainted(false);
        panelButtons.add(btnKeluar);

        // Table
        String[] columns = {"ID", "ID Member", "Nama Member", "ID Kelas", "Nama Kelas", "Tanggal", "Catatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(440, 20, 430, 510);
        frame.add(scrollPane);

        // Event Listeners
        btnSimpan.addActionListener(e -> simpanData());
        btnUpdate.addActionListener(e -> updateData());
        btnDelete.addActionListener(e -> deleteData());
        btnRefresh.addActionListener(e -> loadTableData());
        btnReset.addActionListener(e -> resetForm());
        btnKeluar.addActionListener(e -> {
            closeConnection();
            System.exit(0);
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtIdPendaftaran.setText(tableModel.getValueAt(row, 0).toString());
                    
                    String idMember = tableModel.getValueAt(row, 1).toString();
                    String namaMember = tableModel.getValueAt(row, 2).toString();
                    cbMember.setSelectedItem(idMember + " - " + namaMember);
                    
                    String idKelas = tableModel.getValueAt(row, 3).toString();
                    String namaKelas = tableModel.getValueAt(row, 4).toString();
                    cbKelas.setSelectedItem(idKelas + " - " + namaKelas);
                    
                    txtTanggalDaftar.setText(tableModel.getValueAt(row, 5).toString());
                    txtCatatan.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
                }
            }
        });

        frame.setVisible(true);
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Proyek_Gym",
                "postgres",
                "12345"
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal koneksi database!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMembers() {
        try {
            cbMember.removeAllItems();
            String sql = "SELECT id_member, nama FROM member_gym ORDER BY nama";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                cbMember.addItem(rs.getInt("id_member") + " - " + rs.getString("nama"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal load data member!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadKelas() {
        try {
            cbKelas.removeAllItems();
            String sql = "SELECT id_kelas, nama_kelas FROM jadwal_kelas ORDER BY nama_kelas";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                cbKelas.addItem(rs.getInt("id_kelas") + " - " + rs.getString("nama_kelas"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal load data kelas!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT p.id_pendaftaran, p.id_member, m.nama as nama_member, " +
                        "p.id_kelas, j.nama_kelas, p.tanggal_daftar, p.catatan " +
                        "FROM pendaftaran_kelas p " +
                        "JOIN member_gym m ON p.id_member = m.id_member " +
                        "JOIN jadwal_kelas j ON p.id_kelas = j.id_kelas " +
                        "ORDER BY p.id_pendaftaran DESC";
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_pendaftaran"),
                    rs.getInt("id_member"),
                    rs.getString("nama_member"),
                    rs.getInt("id_kelas"),
                    rs.getString("nama_kelas"),
                    rs.getDate("tanggal_daftar"),
                    rs.getString("catatan")
                };
                tableModel.addRow(row);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal load data tabel!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanData() {
        if (cbMember.getSelectedItem() == null || cbKelas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, 
                "Silakan pilih member dan kelas!", 
                "Validasi", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String memberStr = cbMember.getSelectedItem().toString();
            int idMember = Integer.parseInt(memberStr.split(" - ")[0]);
            
            String kelasStr = cbKelas.getSelectedItem().toString();
            int idKelas = Integer.parseInt(kelasStr.split(" - ")[0]);
            
            String tanggal = txtTanggalDaftar.getText().trim();
            String catatan = txtCatatan.getText().trim();

            String sql = "INSERT INTO pendaftaran_kelas (id_member, id_kelas, tanggal_daftar, catatan) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idMember);
            pstmt.setInt(2, idKelas);
            pstmt.setDate(3, Date.valueOf(tanggal));
            pstmt.setString(4, catatan.isEmpty() ? null : catatan);
            
            pstmt.executeUpdate();
            pstmt.close();
            
            JOptionPane.showMessageDialog(frame, 
                "Pendaftaran berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            
            loadTableData();
            resetForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal menyimpan data!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData() {
        if (txtIdPendaftaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Pilih data dari tabel terlebih dahulu!", 
                "Validasi", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idPendaftaran = Integer.parseInt(txtIdPendaftaran.getText());
            
            String memberStr = cbMember.getSelectedItem().toString();
            int idMember = Integer.parseInt(memberStr.split(" - ")[0]);
            
            String kelasStr = cbKelas.getSelectedItem().toString();
            int idKelas = Integer.parseInt(kelasStr.split(" - ")[0]);
            
            String tanggal = txtTanggalDaftar.getText().trim();
            String catatan = txtCatatan.getText().trim();

            String sql = "UPDATE pendaftaran_kelas SET id_member=?, id_kelas=?, tanggal_daftar=?, catatan=? WHERE id_pendaftaran=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idMember);
            pstmt.setInt(2, idKelas);
            pstmt.setDate(3, Date.valueOf(tanggal));
            pstmt.setString(4, catatan.isEmpty() ? null : catatan);
            pstmt.setInt(5, idPendaftaran);
            
            int result = pstmt.executeUpdate();
            pstmt.close();
            
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, 
                    "Data berhasil diupdate!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                resetForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "Gagal update data!\n" + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteData() {
        if (txtIdPendaftaran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "Pilih data dari tabel terlebih dahulu!", 
                "Validasi", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Yakin ingin menghapus data ini?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idPendaftaran = Integer.parseInt(txtIdPendaftaran.getText());
                
                String sql = "DELETE FROM pendaftaran_kelas WHERE id_pendaftaran=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, idPendaftaran);
                
                int result = pstmt.executeUpdate();
                pstmt.close();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(frame, 
                        "Data berhasil dihapus!", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                    resetForm();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, 
                    "Gagal menghapus data!\n" + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        txtIdPendaftaran.setText("");
        if (cbMember.getItemCount() > 0) cbMember.setSelectedIndex(0);
        if (cbKelas.getItemCount() > 0) cbKelas.setSelectedIndex(0);
        txtTanggalDaftar.setText(java.time.LocalDate.now().toString());
        txtCatatan.setText("");
        table.clearSelection();
    }

    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormPendaftaranKelas());
    }
}