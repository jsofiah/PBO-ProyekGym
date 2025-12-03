package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// koneksi database
class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_gym";
    private static final String USER = "postgres";
    private static final String PASS = "hione";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
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
class JadwalKelas extends JFrame{
    private int idKelas;
    private String namaKelas;
    private String hari;
    private Time jamKelas;
    private int idInstruktur;
    private String namaInstruktur;

    public JadwalKelas(int idKelas, String namaKelas, String hari,
            Time jamKelas, int idInstruktur, String namaInstruktur) {
        this.idKelas = idKelas;
        this.namaKelas = namaKelas;
        this.hari = hari;
        this.jamKelas = jamKelas;
        this.idInstruktur = idInstruktur;
        this.namaInstruktur = namaInstruktur;
    }

    public int getIdKelas() {
        return idKelas;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public String getHari() {
        return hari;
    }

    public Time getJamKelas() {
        return jamKelas;
    }

    public int getIdInstruktur() {
        return idInstruktur;
    }

    public String getNamaInstruktur() {
        return namaInstruktur;
    }
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
                int idKelas = rs.getInt("id_kelas");
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

    public void insertJadwal(String namaKelas, String hari, Time jamKelas, int idInstruktur) {
        String sql = "INSERT INTO jadwal_kelas(nama_kelas, hari, jam_kelas, id_instruktur) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaKelas);
            ps.setString(2, hari);
            ps.setTime(3, jamKelas);
            ps.setInt(4, idInstruktur);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal tambah jadwal: " + e.getMessage());
        }
    }

    public void updateJadwal(int idKelas, String namaKelas, String hari,
            Time jamKelas, int idInstruktur) {
        String sql = "UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam_kelas=?, id_instruktur=? " +
                "WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaKelas);
            ps.setString(2, hari);
            ps.setTime(3, jamKelas);
            ps.setInt(4, idInstruktur);
            ps.setInt(5, idKelas);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal update jadwal: " + e.getMessage());
        }
    }

    public void deleteJadwal(int idKelas) {
        String sql = "DELETE FROM jadwal_kelas WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idKelas);
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
        txtIdKelas.setEditable(false); // ID auto dari database
        txtNamaKelas = new JTextField(20);
        txtJamKelas = new JTextField(10);

        cbHari = new JComboBox<>(new String[] {
                "Pilih Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        });

        cbInstruktur = new JComboBox<>();

        // baris 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInput.add(lblIdKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtIdKelas, gbc);

        // baris 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInput.add(lblNamaKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtNamaKelas, gbc);

        // baris 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInput.add(lblHari, gbc);
        gbc.gridx = 1;
        panelInput.add(cbHari, gbc);

        // baris 3
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelInput.add(lblJamKelas, gbc);
        gbc.gridx = 1;
        panelInput.add(txtJamKelas, gbc);

        // baris 4
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        tableModel = new DefaultTableModel(new Object[] {
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
                    txtIdKelas.setText(tableModel.getValueAt(row, 0).toString());
                    txtNamaKelas.setText(tableModel.getValueAt(row, 1).toString());
                    cbHari.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    txtJamKelas.setText(tableModel.getValueAt(row, 3).toString());

                    String namaInstruktur = tableModel.getValueAt(row, 4).toString();
                    // pilih instruktur di combobox
                    for (int i = 0; i < cbInstruktur.getItemCount(); i++) {
                        Instruktur ins = cbInstruktur.getItemAt(i);
                        if (ins.getNamaInstruktur().equals(namaInstruktur)) {
                            cbInstruktur.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    // load instruktur ke combobox
    private void loadInstrukturToCombo() {
        cbInstruktur.removeAllItems();
        List<Instruktur> list = dao.getAllInstruktur();
        for (Instruktur i : list) {
            cbInstruktur.addItem(i);
        }
    }

    // load data jadwal ke tabel
    private void loadDataJadwalToTable() {
        tableModel.setRowCount(0);
        List<JadwalKelas> list = dao.getAllJadwal();
        for (JadwalKelas j : list) {
            tableModel.addRow(new Object[] {
                    j.getIdKelas(),
                    j.getNamaKelas(),
                    j.getHari(),
                    j.getJamKelas().toString(),
                    j.getNamaInstruktur()
            });
        }
    }

    // validasi input form
    private boolean validateInput() {
        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        if (namaKelas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kelas tidak boleh kosong");
            return false;
        }

        if (hari == null || hari.equals("Pilih Hari")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih hari");
            return false;
        }

        if (jamText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jam kelas tidak boleh kosong");
            return false;
        }

        // validasi format jam HH:mm
        try {
            LocalTime.parse(jamText);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format jam harus HH:mm (contoh: 08:30)");
            return false;
        }

        if (ins == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih instruktur");
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
        if (!validateInput())
            return;

        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        Time jam = parseTime(jamText);
        dao.insertJadwal(namaKelas, hari, jam, ins.getIdInstruktur());

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
        if (!validateInput())
            return;

        int idKelas = Integer.parseInt(txtIdKelas.getText().trim());
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
            int idKelas = Integer.parseInt(txtIdKelas.getText().trim());
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
        if (cbInstruktur.getItemCount() > 0)
            cbInstruktur.setSelectedIndex(0);
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormJadwalKelas().setVisible(true);
        });
    }
}
