package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// koneksi database
class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_gym";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL tidak ditemukan.", e);
        }
    }
}

// model instruktur
class Instruktur {
    private int idInstruktur;
    private String namaInstruktur;

    public Instruktur(int idInstruktur, String namaInstruktur) {
        this.idInstruktur = idInstruktur;
        this.namaInstruktur = namaInstruktur;
    }

    public int getIdInstruktur() {
        return idInstruktur;
    }

    public String getNamaInstruktur() {
        return namaInstruktur;
    }

    @Override
    public String toString() {
        // yang tampil di ComboBox
        return namaInstruktur;
    }
}

// model jadwal kelas
class JadwalKelas {
    private String idKelas;  // UBAH KE STRING
    private String namaKelas;
    private String hari;
    private Time jamKelas;
    private int idInstruktur;
    private String namaInstruktur;

    public JadwalKelas(String idKelas, String namaKelas, String hari,
                       Time jamKelas, int idInstruktur, String namaInstruktur) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.hari = hari;
        this.jamKelas = jamKelas;
        this.idInstruktur = idInstruktur;
        this.namaInstruktur = namaInstruktur;
    }

    public String getIdKelas() { return idKelas; }
    public String getNamaKelas() { return namaKelas; }
    public String getHari() { return hari; }
    public Time getJamKelas() { return jamKelas; }
    public int getIdInstruktur() { return idInstruktur; }
    public String getNamaInstruktur() { return namaInstruktur; }
}

// dao jadwal kelas akses database 
class JadwalKelasDAO {

    public List<Instruktur> getAllInstruktur() {
        List<Instruktur> list = new ArrayList<>();
        String sql = "SELECT id_instruktur, nama FROM instruktur_gym ORDER BY nama";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_instruktur");
                String nama = rs.getString("nama");
                list.add(new Instruktur(id, nama));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal load instruktur: " + e.getMessage());
        }

        return list;
    }

    public List<JadwalKelas> getAllJadwal() {
        List<JadwalKelas> list = new ArrayList<>();

        String sql = "SELECT j.id_kelas, j.nama_kelas, j.hari, j.jam_kelas, " +
                "j.id_instruktur, i.nama " +
                "FROM jadwal_kelas j " +
                "JOIN instruktur_gym i ON j.id_instruktur = i.id_instruktur " +
                "ORDER BY j.id_kelas";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String idKelas = rs.getString("id_kelas");  // AMBIL SEBAGAI STRING
                String namaKelas = rs.getString("nama_kelas");
                String hari = rs.getString("hari");
                Time jam = rs.getTime("jam_kelas");
                int idInstruktur = rs.getInt("id_instruktur");
                String namaInstruktur = rs.getString("nama");

                list.add(new JadwalKelas(idKelas, namaKelas, hari, jam, idInstruktur, namaInstruktur));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal load jadwal: " + e.getMessage());
        }

        return list;
    }

    // UBAH PARAMETER ID KELAS JADI STRING
    public void insertJadwal(String idKelas, String namaKelas, String hari, Time jamKelas, int idInstruktur) {
        String sql = "INSERT INTO jadwal_kelas(id_kelas, nama_kelas, hari, jam_kelas, id_instruktur) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKelas);  // SET STRING
            ps.setString(2, namaKelas);
            ps.setString(3, hari);
            ps.setTime(4, jamKelas);
            ps.setInt(5, idInstruktur);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal tambah jadwal: " + e.getMessage());
        }
    }

    // UBAH PARAMETER ID KELAS JADI STRING
    public void updateJadwal(String idKelas, String namaKelas, String hari,
                             Time jamKelas, int idInstruktur) {
        String sql = "UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam_kelas=?, id_instruktur=? " +
                "WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaKelas);
            ps.setString(2, hari);
            ps.setTime(3, jamKelas);
            ps.setInt(4, idInstruktur);
            ps.setString(5, idKelas);  // SET STRING

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal update jadwal: " + e.getMessage());
        }
    }

    // UBAH PARAMETER ID KELAS JADI STRING
    public void deleteJadwal(String idKelas) {
        String sql = "DELETE FROM jadwal_kelas WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKelas);  // SET STRING
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal hapus jadwal: " + e.getMessage());
        }
    }
}

// form jadwal kelas (swing)
public class FormJadwalKelas extends JFrame {

    private JTextField txtIdKelas;
    private JTextField txtNamaKelas;
    private JTextField txtJamKelas;
    private JComboBox<String> cbHari;
    private JComboBox<Instruktur> cbInstruktur;
    private JTable tableJadwal;
    private DefaultTableModel tableModel;

    private JadwalKelasDAO dao = new JadwalKelasDAO();

    public FormJadwalKelas() {
        setTitle("Form Jadwal Kelas Gym");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadInstrukturToCombo();
        loadDataJadwalToTable();
    }

    private void initComponents() {
        JPanel panelInput = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblIdKelas = new JLabel("ID Kelas:");
        JLabel lblNamaKelas = new JLabel("Nama Kelas:");
        JLabel lblHari = new JLabel("Hari:");
        JLabel lblJamKelas = new JLabel("Jam Kelas (HH:mm):");
        JLabel lblInstruktur = new JLabel("Instruktur:");

        txtIdKelas = new JTextField(10);
        txtIdKelas.setEditable(true); // BISA DIEDIT UNTUK INPUT MANUAL
        txtNamaKelas = new JTextField(20);
        txtJamKelas = new JTextField(10);

        cbHari = new JComboBox<>(new String[]{
                "Pilih Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        });

        cbInstruktur = new JComboBox<>();

        // baris 0
        gbc.gridx = 0; gbc.gridy = 0;
        panelInput.add(lblIdKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtIdKelas, gbc);

        // baris 1
        gbc.gridx = 0; gbc.gridy = 1;
        panelInput.add(lblNamaKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtNamaKelas, gbc);

        // baris 2
        gbc.gridx = 0; gbc.gridy = 2;
        panelInput.add(lblHari, gbc);
        gbc.gridx = 1;
        panelInput.add(cbHari, gbc);

        // baris 3
        gbc.gridx = 0; gbc.gridy = 3;
        panelInput.add(lblJamKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtJamKelas, gbc);

        // baris 4
        gbc.gridx = 0; gbc.gridy = 4;
        panelInput.add(lblInstruktur, gbc);
        gbc.gridx = 1;
        panelInput.add(cbInstruktur, gbc);

        // tombol
        JPanel panelButton = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");

        panelButton.add(btnTambah);
        panelButton.add(btnUbah);
        panelButton.add(btnHapus);
        panelButton.add(btnBersih);

        // tabel
        tableModel = new DefaultTableModel(new Object[]{
                "ID Kelas", "Nama Kelas", "Hari", "Jam Kelas", "Instruktur"
        }, 0);
        tableJadwal = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(tableJadwal);

        // layout utama
        setLayout(new BorderLayout());
        add(panelInput, BorderLayout.NORTH);
        add(panelButton, BorderLayout.CENTER);
        add(scrollTable, BorderLayout.SOUTH);

        // event listener
        btnTambah.addActionListener(e -> tambahJadwal());
        btnUbah.addActionListener(e -> ubahJadwal());
        btnHapus.addActionListener(e -> hapusJadwal());
        btnBersih.addActionListener(e -> bersihkanForm());

        tableJadwal.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableJadwal.getSelectedRow();
                if (row >= 0) {
                    // ISI ID KELAS
                    txtIdKelas.setText(tableModel.getValueAt(row, 0).toString());
                    
                    // ISI NAMA KELAS
                    txtNamaKelas.setText(tableModel.getValueAt(row, 1).toString());
                    
                    // ISI HARI
                    cbHari.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    
                    // ISI JAM KELAS (Format jam dari tabel - potong detik jika ada)
                    String jamStr = tableModel.getValueAt(row, 3).toString();
                    if (jamStr.length() > 5) {
                        jamStr = jamStr.substring(0, 5);
                    }
                    txtJamKelas.setText(jamStr);

                    // ISI INSTRUKTUR - pilih instruktur yang sesuai di combobox
                    String namaInstruktur = tableModel.getValueAt(row, 4).toString();
                    boolean instrukturDitemukan = false;
                    
                    for (int i = 0; i < cbInstruktur.getItemCount(); i++) {
                        Instruktur ins = cbInstruktur.getItemAt(i);
                        // CEK NULL DULU SEBELUM AKSES METHOD
                        if (ins != null && ins.getIdInstruktur() != -1 && 
                            ins.getNamaInstruktur().equals(namaInstruktur)) {
                            cbInstruktur.setSelectedIndex(i);
                            instrukturDitemukan = true;
                            break;
                        }
                    }
                    
                    // Jika instruktur tidak ditemukan, tetap di placeholder
                    if (!instrukturDitemukan) {
                        cbInstruktur.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(FormJadwalKelas.this, 
                            "Instruktur '" + namaInstruktur + "' tidak ditemukan di daftar.\n" +
                            "Silakan pilih instruktur yang tersedia.", 
                            "Peringatan", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
    }

    // PERBAIKAN: load instruktur ke combobox dengan placeholder DAN DATA DEFAULT
    private void loadInstrukturToCombo() {
        cbInstruktur.removeAllItems();
        // Tambah placeholder sebagai Instruktur object dengan ID -1
        cbInstruktur.addItem(new Instruktur(-1, "-- Pilih Instruktur --"));
        
        List<Instruktur> list = dao.getAllInstruktur();
        
        // Jika tidak ada data dari database, tambahkan data default
        if (list.isEmpty()) {
            cbInstruktur.addItem(new Instruktur(1, "Fearent"));
            cbInstruktur.addItem(new Instruktur(2, "Sofiah"));
            cbInstruktur.addItem(new Instruktur(3, "Ubaydilah"));
            cbInstruktur.addItem(new Instruktur(4, "Tersiqo"));
        } else {
            // Jika ada data dari database, gunakan data tersebut
            for (Instruktur i : list) {
                cbInstruktur.addItem(i);
            }
        }
    }

    // load data jadwal ke tabel
    private void loadDataJadwalToTable() {
        tableModel.setRowCount(0);
        List<JadwalKelas> list = dao.getAllJadwal();
        for (JadwalKelas j : list) {
            // Format jam tanpa detik
            String jamStr = j.getJamKelas().toString();
            if (jamStr.length() > 5) {
                jamStr = jamStr.substring(0, 5);
            }
            
            tableModel.addRow(new Object[]{
                    j.getIdKelas(),
                    j.getNamaKelas(),
                    j.getHari(),
                    jamStr,
                    j.getNamaInstruktur()
            });
        }
    }

    // validasi input form 
    private boolean validateInput() {
        String idKelas = txtIdKelas.getText().trim();  // TAMBAH VALIDASI ID KELAS
        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        // VALIDASI ID KELAS
        if (idKelas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Kelas tidak boleh kosong!");
            txtIdKelas.requestFocus();
            return false;
        }

        if (namaKelas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kelas tidak boleh kosong");
            txtNamaKelas.requestFocus();
            return false;
        }

        if (hari == null || hari.equals("Pilih Hari")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih hari");
            cbHari.requestFocus();
            return false;
        }

        if (jamText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jam kelas tidak boleh kosong");
            txtJamKelas.requestFocus();
            return false;
        }

        // validasi format jam HH:mm
        try {
            // Otomatis replace titik jadi titik dua
            if (jamText.contains(".")) {
                jamText = jamText.replace('.', ':');
                txtJamKelas.setText(jamText);
            }
            LocalTime.parse(jamText);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format jam harus HH:mm (contoh: 08:30)");
            txtJamKelas.requestFocus();
            return false;
        }

        // PERBAIKAN: CEK APAKAH INSTRUKTUR NULL ATAU ID = -1 (PLACEHOLDER)
        if (ins == null || ins.getIdInstruktur() == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih instruktur");
            cbInstruktur.requestFocus();
            return false;
        }

        return true;
    }

    // konversi jam text ke java.sql.Time
    private Time parseTime(String jamText) {
        // jamText format "HH:mm", tambahkan detik ":00"
        String full = jamText + ":00";
        return Time.valueOf(full);
    }

    // aksi tambah
    private void tambahJadwal() {
        if (!validateInput()) return;

        String idKelas = txtIdKelas.getText().trim();  // AMBIL ID KELAS
        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        Time jam = parseTime(jamText);
        dao.insertJadwal(idKelas, namaKelas, hari, jam, ins.getIdInstruktur());

        JOptionPane.showMessageDialog(this, "Data jadwal berhasil ditambahkan");
        loadDataJadwalToTable();
        bersihkanForm();
    }

    // aksi ubah
    private void ubahJadwal() {
        if (txtIdKelas.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang akan diubah");
            return;
        }
        if (!validateInput()) return;

        String idKelas = txtIdKelas.getText().trim();  // AMBIL SEBAGAI STRING
        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        Time jam = parseTime(jamText);
        dao.updateJadwal(idKelas, namaKelas, hari, jam, ins.getIdInstruktur());

        JOptionPane.showMessageDialog(this, "Data jadwal berhasil diubah");
        loadDataJadwalToTable();
        bersihkanForm();
    }

    // aksi hapus
    private void hapusJadwal() {
        if (txtIdKelas.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data di tabel yang akan dihapus");
            return;
        }
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus data ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (konfirm == JOptionPane.YES_OPTION) {
            String idKelas = txtIdKelas.getText().trim();  // AMBIL SEBAGAI STRING
            dao.deleteJadwal(idKelas);
            JOptionPane.showMessageDialog(this, "Data jadwal berhasil dihapus");
            loadDataJadwalToTable();
            bersihkanForm();
        }
    }

    // bersihkan form
    private void bersihkanForm() {
        txtIdKelas.setText("");
        txtNamaKelas.setText("");
        cbHari.setSelectedIndex(0);
        txtJamKelas.setText("");
        cbInstruktur.setSelectedIndex(0);  // Kembali ke placeholder
    }

    // main 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormJadwalKelas().setVisible(true);
        });
    }
}