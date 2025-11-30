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

        // Tab Menu
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // === TAB FORM INSTRUKTUR ===
        tabPane.addTab("Instruktur Gym", createTabPanel("Buka Form Instruktur Gym", () -> {
            new FormInstrukturGym().setVisible(true);
        }));

        // === TAB FORM MEMBER ===
        tabPane.addTab("Member Gym", createTabPanel("Buka Form Registrasi Member", () -> {
            JOptionPane.showMessageDialog(this, 
                    "Form Member belum dibuat.\nTinggal hubungkan ke kelas form kamu.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            // new FormMemberGym().setVisible(true);
        }));

        // === TAB FORM JADWAL ===
        tabPane.addTab("Jadwal Kelas Gym", createTabPanel("Buka Form Jadwal Gym", () -> {
            // new FormJadwalGym().setVisible(true);
        }));

        // === TAB FORM PENDAFTARAN ===
        tabPane.addTab("Pendaftaran Kelas", createTabPanel("Buka Form Pendaftaran Kelas", () -> {
            // new FormPendaftaranKelas().setVisible(true);
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