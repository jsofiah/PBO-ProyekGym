package src;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardGym extends JFrame {

    private final Color PRIMARY_BLUE = new Color(30, 144, 255);
    private final Color DARK_BLUE = new Color(25, 118, 210);
    private final Color LIGHT_BLUE = new Color(100, 181, 246);
    private final Color WHITE = Color.WHITE;
    private final Color BG_COLOR = new Color(245, 248, 255);

    public DashboardGym() {
        setTitle("Gym Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);

        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);

        JPanel footer = createFooter();
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(PRIMARY_BLUE);

        JLabel lblTitle = new JLabel("GYM MANAGEMENT SYSTEM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Kelola Gym");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitle.setForeground(new Color(220, 240, 255));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        titlePanel.add(lblTitle);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(lblSubtitle);

        header.add(titlePanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(createMenuCard("Member Gym", "Kelola data member gym", 
            new Color(30, 144, 255), () -> openForm(new FormMemberGym())), gbc);

        gbc.gridx = 1;
        mainPanel.add(createMenuCard("Instruktur Gym", "Kelola data instruktur", 
            new Color(25, 118, 210), () -> openForm(new FormInstrukturGym())), gbc);

        gbc.gridx = 2;
        mainPanel.add(createMenuCard("Jadwal Kelas", "Atur jadwal kelas gym", 
            new Color(13, 71, 161), () -> openForm(new FormJadwalKelas())), gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(createMenuCard("Pendaftaran Kelas", "Daftarkan member ke kelas", 
            new Color(100, 181, 246), () -> openForm(new FormPendaftaranKelas())), gbc);

        gbc.gridx = 1;
        mainPanel.add(createMenuCard("Keahlian Gym", "Kelola keahlian tersedia", 
            new Color(66, 165, 245), () -> openForm(new FormKeahlianGym())), gbc);

        gbc.gridx = 2;
        mainPanel.add(createMenuCard("Keahlian Instruktur", "Atur keahlian instruktur", 
            new Color(41, 182, 246), () -> openForm(new FormInstrukturPilihKeahlian())), gbc);

        return mainPanel;
    }

    private JPanel createMenuCard(String title, String description, Color color, Runnable action) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
            new EmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(240, 248, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 3),
                    new EmptyBorder(24, 24, 24, 24)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 220, 240), 2),
                    new EmptyBorder(25, 25, 25, 25)
                ));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                action.run();
            }
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(color);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDesc.setForeground(new Color(120, 120, 120));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcon = new JLabel("→");
        lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblIcon.setForeground(color);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblTitle);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblDesc);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(lblIcon);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(DARK_BLUE);
        footer.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblFooter = new JLabel("© 2025 Gym Management System - All Rights Reserved");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(WHITE);

        footer.add(lblFooter);

        return footer;
    }

    private void openForm(JFrame form) {
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        form.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DashboardGym().setVisible(true);
        });
    }
}