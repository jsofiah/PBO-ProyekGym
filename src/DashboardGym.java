package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardGym extends JFrame {

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color WHITE = Color.WHITE;

    public DashboardGym() {
        setTitle("Aplikasi Manajemen Gym");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initUI();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("APLIKASI MANAJEMEN GYM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSub = new JLabel("Dashboard Utama");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(WHITE);
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(PRIMARY_COLOR);
        titleContainer.add(lblTitle, BorderLayout.CENTER);
        titleContainer.add(lblSub, BorderLayout.SOUTH);

        header.add(titleContainer);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabPane.addTab("Member Gym", createTabPanel("Buka Form Registrasi Member", () -> {
            FormMemberGym f = new FormMemberGym();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }));

        tabPane.addTab("Instruktur Gym", createTabPanel("Buka Form Instruktur Gym", () -> {
            FormInstrukturGym frm = new FormInstrukturGym();
            frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frm.setVisible(true);
        }));

        tabPane.addTab("Jadwal Kelas", createTabPanel("Buka Form Jadwal Gym", () -> {
            FormJadwalKelas f = new FormJadwalKelas();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }));

        tabPane.addTab("Pendaftaran Kelas", createTabPanel("Buka Form Pendaftaran Kelas", () -> {
            FormPendaftaranKelas f = new FormPendaftaranKelas();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }));

        tabPane.addTab("Keahlian Gym", createTabPanel("Buka Form Keahlian Gym", () -> {
            FormKeahlianGym f = new FormKeahlianGym();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }));

        tabPane.addTab("Keahlian Instruktur", createTabPanel("Buka Form Keahlian Instruktur", () -> {
            FormInstrukturPilihKeahlian f = new FormInstrukturPilihKeahlian();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        }));

        add(header, BorderLayout.NORTH);
        add(tabPane, BorderLayout.CENTER);
    }

    private JPanel createTabPanel(String buttonText, Runnable runAction) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));

        JButton btn = new JButton(buttonText);
        btn.setPreferredSize(new Dimension(260, 50));
        btn.setBackground(new Color(39, 174, 96));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> runAction.run());

        panel.add(btn);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardGym().setVisible(true);
        });
    }
}