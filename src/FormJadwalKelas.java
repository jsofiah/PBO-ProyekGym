package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

// koneksi database

class DBConnection {
    // URL database postgreSQL lokal
    private static final String URL = "jdbc:postgresql://localhost:5432/db_gym";
    private static final String USER = "postgres";
    private static final String PASS = "1234"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver"); 
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL tidak ditemukan. Pastikan file .jar ada di classpath.", e);
        } catch (SQLException e) {
            // Ini akan menangkap FATAL: password authentication failed
            JOptionPane.showMessageDialog(null, 
                "Error Koneksi! Gagal koneksi database! Kemungkinan password salah.\nDetail: " + e.getMessage(), 
                "Error Koneksi", 
                JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }
}

// MODEL

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
        return namaInstruktur;
    }
}

class JadwalKelas {
    // ID Kelas string untuk fleksibilitas input manual
    private String idKelas; 
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

    public String getIdKelas() { 
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

// DAO (DATA ACCESS OBJECT)

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
            // Pesan error sudah ditangani di DBConnection
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
                String idKelas = rs.getString("id_kelas"); 
                String namaKelas = rs.getString("nama_kelas");
                String hari = rs.getString("hari");
                Time jam = rs.getTime("jam_kelas");
                int idInstruktur = rs.getInt("id_instruktur");
                String namaInstruktur = rs.getString("nama");

                list.add(new JadwalKelas(idKelas, namaKelas, hari, jam, idInstruktur, namaInstruktur));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Pesan error sudah ditangani di DBConnection
        }

        return list;
    }

    // Menambahkan String idKelas ke parameter dan query INSERT
    public void insertJadwal(String idKelas, String namaKelas, String hari, Time jamKelas, int idInstruktur) throws SQLException {
        String sql = "INSERT INTO jadwal_kelas(id_kelas, nama_kelas, hari, jam_kelas, id_instruktur) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKelas); 
            ps.setString(2, namaKelas);
            ps.setString(3, hari);
            ps.setTime(4, jamKelas);
            ps.setInt(5, idInstruktur);

            ps.executeUpdate();
        }
    }

    // Mengubah tipe data idKelas ke String di parameter dan query UPDATE
    public void updateJadwal(String idKelas, String namaKelas, String hari,
            Time jamKelas, int idInstruktur) throws SQLException {
        String sql = "UPDATE jadwal_kelas SET nama_kelas=?, hari=?, jam_kelas=?, id_instruktur=? " +
                "WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaKelas);
            ps.setString(2, hari);
            ps.setTime(3, jamKelas);
            ps.setInt(4, idInstruktur);
            ps.setString(5, idKelas); 

            ps.executeUpdate();
        }
    }

    // Mengubah tipe data idKelas ke String di parameter dan query DELETE
    public void deleteJadwal(String idKelas) throws SQLException {
        String sql = "DELETE FROM jadwal_kelas WHERE id_kelas=?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKelas); 
            ps.executeUpdate();
        }
    }
}

// 4. form string utama

public class FormJadwalKelas extends JFrame {

    private JTextField txtIdKelas;
    private JTextField txtNamaKelas;
    private JTextField txtJamKelas;
    private JComboBox<String> cbHari;
    private JComboBox<Instruktur> cbInstruktur; 
    private JTable tableJadwal;
    private DefaultTableModel tableModel;

    private JButton btnSimpan, btnEdit, btnHapus, btnReset, btnRefresh, btnKeluar;

    private JadwalKelasDAO dao = new JadwalKelasDAO();
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color DARK_BLUE = new Color(52, 73, 94);

    public FormJadwalKelas() {
        setTitle("Form Jadwal Kelas Gym");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initComponents();
        loadInstrukturToCombo();
        loadDataJadwalToTable();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(LIGHT_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("MANAJEMEN JADWAL KELAS GYM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblSubtitle = new JLabel("Kelola jadwal kelas dan instruktur");
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

        // id kelas: diaktifkan untuk input
        txtIdKelas = createTextField();
        formPanel.add(createFormRow("ID Kelas:", txtIdKelas));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        txtNamaKelas = createTextField();
        formPanel.add(createFormRow("Nama Kelas:", txtNamaKelas));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        cbHari = new JComboBox<>(new String[] {
                "Pilih Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
        });
        cbHari.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbHari.setBackground(WHITE);
        cbHari.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbHari.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(createFormRow("Hari:", cbHari));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        txtJamKelas = createTextField();
        formPanel.add(createFormRow("Jam Kelas (HH:mm):", txtJamKelas));
        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        cbInstruktur = new JComboBox<>();
        cbInstruktur.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbInstruktur.setBackground(WHITE);
        cbInstruktur.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbInstruktur.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(createFormRow("Instruktur:", cbInstruktur));

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

        tableModel = new DefaultTableModel(new Object[] {
                "ID Kelas", "Nama Kelas", "Hari", "Jam Kelas", "Instruktur"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableJadwal = new JTable(tableModel);
        tableJadwal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableJadwal.setRowHeight(35);
        tableJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableJadwal.setSelectionBackground(new Color(52, 152, 219));
        tableJadwal.setSelectionForeground(WHITE);
        tableJadwal.setGridColor(new Color(189, 195, 199));
        tableJadwal.setShowGrid(true);
        tableJadwal.setIntercellSpacing(new Dimension(1, 1));
        
        JTableHeader header = tableJadwal.getTableHeader();
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

        tableJadwal.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableJadwal.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableJadwal.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(4).setPreferredWidth(200);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableJadwal.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableJadwal.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tableJadwal.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        JScrollPane scrollTable = new JScrollPane(tableJadwal);
        scrollTable.setBorder(new LineBorder(new Color(189, 195, 199), 1));
        scrollTable.getViewport().setBackground(WHITE);
        
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(LIGHT_BG);
        
        JLabel lblTableTitle = new JLabel("Data Jadwal Kelas");
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
        splitPane.setDividerLocation(380);
        
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
        label.setPreferredSize(new Dimension(180, 25));
        
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
        btnRefresh.addActionListener(e -> {
            loadInstrukturToCombo();
            loadDataJadwalToTable();
        });
        
        btnReset.addActionListener(e -> {
            txtIdKelas.setText("");
            txtNamaKelas.setText("");
            cbHari.setSelectedIndex(0);
            txtJamKelas.setText("");
            if (cbInstruktur.getItemCount() > 0)
                cbInstruktur.setSelectedIndex(0);
            tableJadwal.clearSelection();
        });
        
        btnKeluar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Yakin ingin keluar dari aplikasi?", 
                    "Konfirmasi Keluar", 
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        tableJadwal.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableJadwal.getSelectedRow();
                if (row >= 0) {
                    txtIdKelas.setText(tableModel.getValueAt(row, 0).toString());
                    txtNamaKelas.setText(tableModel.getValueAt(row, 1).toString());
                    cbHari.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    
                    String jamStr = tableModel.getValueAt(row, 3).toString();
                    if (jamStr.length() > 5) {
                        jamStr = jamStr.substring(0, 5);
                    }
                    txtJamKelas.setText(jamStr);

                    String namaInstruktur = tableModel.getValueAt(row, 4).toString();
                    
                    // memastikan instruktur yang dipilih sesuai dengan nama di tabel
                    for (int i = 0; i < cbInstruktur.getItemCount(); i++) {
                        Instruktur ins = cbInstruktur.getItemAt(i);
                        // cek null safety sebelum memanggil getNamaInstruktur
                        if (ins != null && ins.getNamaInstruktur().equals(namaInstruktur)) {
                            cbInstruktur.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void loadInstrukturToCombo() {
        cbInstruktur.removeAllItems();
        cbInstruktur.addItem(null); // Tambahkan item null/kosong di indeks 0 untuk validasi
        List<Instruktur> list = dao.getAllInstruktur();
        for (Instruktur i : list) {
            cbInstruktur.addItem(i);
        }
    }

    private void loadDataJadwalToTable() {
        tableModel.setRowCount(0);
        List<JadwalKelas> list = dao.getAllJadwal();
        for (JadwalKelas j : list) {
            String jamStr = j.getJamKelas().toString();
            if (jamStr.length() > 5) {
                jamStr = jamStr.substring(0, 5);
            }
            
            tableModel.addRow(new Object[] {
                    j.getIdKelas(),
                    j.getNamaKelas(),
                    j.getHari(),
                    jamStr,
                    j.getNamaInstruktur()
            });
        }
    }

    private boolean validateInput() {
        String idKelas = txtIdKelas.getText().trim();
        String namaKelas = txtNamaKelas.getText().trim();
        String hari = (String) cbHari.getSelectedItem();
        String jamText = txtJamKelas.getText().trim();
        Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

        // Validasi ID Kelas
        if (idKelas.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "ID Kelas tidak boleh kosong!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtIdKelas.requestFocus();
            return false;
        }

        if (namaKelas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama kelas tidak boleh kosong!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNamaKelas.requestFocus();
            return false;
        }

        if (hari == null || hari.equals("Pilih Hari")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih hari!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            cbHari.requestFocus();
            return false;
        }

        if (jamText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jam kelas tidak boleh kosong!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtJamKelas.requestFocus();
            return false;
        }

        try {
            // format jam "08:30"
            if (jamText.contains(".")) {
                 jamText = jamText.replace('.', ':');
                 txtJamKelas.setText(jamText); 
            }
            LocalTime.parse(jamText);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format jam harus HH:mm (contoh: 08:30)!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtJamKelas.requestFocus();
            return false;
        }

        // Validasi Instruktur dipilih
        if (ins == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih instruktur!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            cbInstruktur.requestFocus();
            return false;
        }

        return true;
    }

    private Time parseTime(String jamText) {
        String full = jamText + ":00"; 
        return Time.valueOf(full);
    }

    private void simpanData() {
        if (!validateInput()) return;

        try {
            String idKelas = txtIdKelas.getText().trim(); 
            String namaKelas = txtNamaKelas.getText().trim();
            String hari = (String) cbHari.getSelectedItem();
            String jamText = txtJamKelas.getText().trim();
            Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

            Time jam = parseTime(jamText);
            
            dao.insertJadwal(idKelas, namaKelas, hari, jam, ins.getIdInstruktur()); 

            JOptionPane.showMessageDialog(this, 
                    "Data jadwal berhasil disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            loadDataJadwalToTable();
            btnReset.doClick();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan data! Kemungkinan ID Kelas sudah ada atau SQL Error lain.\nDetail: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void editData() {
        int row = tableJadwal.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diedit!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;

        int konfirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mengupdate data ini?",
                "Konfirmasi Update",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (konfirm == JOptionPane.YES_OPTION) {
            try {
                String idKelas = txtIdKelas.getText().trim(); 
                String namaKelas = txtNamaKelas.getText().trim();
                String hari = (String) cbHari.getSelectedItem();
                String jamText = txtJamKelas.getText().trim();
                Instruktur ins = (Instruktur) cbInstruktur.getSelectedItem();

                Time jam = parseTime(jamText);
                
                dao.updateJadwal(idKelas, namaKelas, hari, jam, ins.getIdInstruktur()); 

                JOptionPane.showMessageDialog(this, 
                    "Data jadwal berhasil diupdate!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                loadDataJadwalToTable();
                btnReset.doClick();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Gagal update data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void hapusData() {
        int row = tableJadwal.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String namaKelas = tableModel.getValueAt(row, 1).toString();
        
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus data ini?\n" +
                "Kelas: " + namaKelas,
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (konfirm == JOptionPane.YES_OPTION) {
            try {
                String idKelas = txtIdKelas.getText().trim(); 
                
                dao.deleteJadwal(idKelas); 
                
                JOptionPane.showMessageDialog(this, 
                    "Data jadwal berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                loadDataJadwalToTable();
                btnReset.doClick();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus data!\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
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
            new FormJadwalKelas().setVisible(true);
        });
    }
}