package io.wollinger.installer;


import com.erigir.mslinks.ShellLink;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InstallerWindow extends JFrame {

    public static String userHome = System.getProperty("user.home");

    public static String jarLocation = new File(userHome + "\\.SnipSniper\\").getAbsolutePath();

    public static String overrideDesktop = "\\Desktop\\";
    public static String overrideStartup = "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\";
    public static String overrideMenu = "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\";

    public static JFrame instance;

    public JButton btnChangeLocation = new JButton("Change location");
    public JLabel labelChangeLocation = new JLabel(jarLocation, SwingConstants.RIGHT);
    public JCheckBox cbStartMenu = new JCheckBox();
    public JCheckBox cbAutoStart = new JCheckBox();
    public JCheckBox cbDesktopShortcut = new JCheckBox();
    public JButton installBtn;

    JFileChooser jfc = new JFileChooser(userHome);

    public JPanel mainPanel;
    public JPanel installerPanel;
    public JPanel startPanel;

    public InstallerWindow() {
        instance = this;
        this.setTitle("SnipSniper Installer");
        this.setSize(512,512);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel(new GridLayout(0,1));

        installerPanel = new JPanel(new GridLayout(0,1));
        int margin = 10;

        JPanel row0 = new JPanel(new GridLayout(0,2));
        row0.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        labelChangeLocation.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row0.add(labelChangeLocation);
        row0.add(btnChangeLocation);
        installerPanel.add(row0);

        jfc.setDialogTitle("Choose a directory to place .SnipSniper in");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setAcceptAllFileFilterUsed(false);
        btnChangeLocation.addActionListener(e -> {
            if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                jarLocation = jfc.getSelectedFile().getAbsolutePath();
                labelChangeLocation.setText(jarLocation);
            }
        });

        JPanel row1 = new JPanel(new GridLayout(0,2));
        row1.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label2 = new JLabel("Add to Start Menu", SwingConstants.RIGHT);
        label2.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row1.add(label2);
        row1.add(cbStartMenu);
        installerPanel.add(row1);

        JPanel row2 = new JPanel(new GridLayout(0,2));
        row2.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label3 = new JLabel("Add to Auto Start", SwingConstants.RIGHT);
        label3.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row2.add(label3);
        row2.add(cbAutoStart);
        installerPanel.add(row2);

        JPanel row3 = new JPanel(new GridLayout(0,2));
        row3.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label4 = new JLabel("Add Desktop Shortcut", SwingConstants.RIGHT);
        label4.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row3.add(label4);
        row3.add(cbDesktopShortcut);
        installerPanel.add(row3);

        JPanel row4 = new JPanel(new GridLayout(0,2));
        row4.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JEditorPane label5 = new JEditorPane ("text/html", "<html>I have read the license <a href=\"https://github.com/SvenWollinger/SnipSniper/blob/master/LICENSE\">here</a></html>");
        label5.setEditable(false);
        label5.setOpaque(false);
        label5.setSelectionColor(new Color(0,0,0,0));
        label5.setSelectedTextColor(Color.black);
        label5.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        label5.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        label5.addHyperlinkListener(hle -> {
            try {
                if(hle.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                    Desktop.getDesktop().browse(new URI(hle.getURL().toString()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });

        row4.add(label5);
        JCheckBox cbLicense = new JCheckBox();
        cbLicense.addActionListener(e -> installBtn.setEnabled(cbLicense.isSelected()));
        row4.add(cbLicense);
        installerPanel.add(row4);

        JPanel row5 = new JPanel(new GridLayout(0,1));
        installBtn = new JButton("Start");
        installBtn.setEnabled(false);
        row5.setBorder(BorderFactory.createEmptyBorder(margin, margin*15, margin/2, margin*15));
        installBtn.addActionListener(e -> go());
        row5.add(installBtn);
        installerPanel.add(row5);

        startPanel = new JPanel(new GridLayout(0,1));
        startPanel.setBorder(BorderFactory.createEmptyBorder(margin, margin*15, margin/2, margin*15));
        JButton btnInstall = new JButton("Install");
        btnInstall.addActionListener(e -> {
            instance.setVisible(false);
            mainPanel.remove(startPanel);
            mainPanel.add(installerPanel);
            instance.setVisible(true);
        });

        JButton btnUninstall = new JButton("Uninstall");
        btnUninstall.addActionListener(e -> {
            deleteShellLink(overrideDesktop + "SnipSniper.lnk");
            deleteShellLink(overrideDesktop + "SnipSniperEditor.lnk");
            deleteShellLink(overrideStartup + "SnipSniper.lnk");
            deleteShellLink(overrideMenu + "SnipSniper.lnk");
            deleteShellLink(overrideMenu + "SnipSniperEditor.lnk");
            JOptionPane.showMessageDialog(instance, "SnipSniper has been removed from Desktop/Startup/Menu.\nPlease manually remove it from the Installation directory you originally chose.");
        });
        startPanel.add(btnInstall);
        startPanel.add(btnUninstall);

        BufferedImage splashImg = null;
        try {
            splashImg = ImageIO.read(getClass().getResourceAsStream("/splash.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graphics g = splashImg.getGraphics();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        int width = g.getFontMetrics().stringWidth(Main.version);
        g.drawString(Main.version,splashImg.getWidth()-width,splashImg.getHeight()-5);
        g.dispose();
        JLabel splash = new JLabel(new ImageIcon(splashImg.getScaledInstance((int)(splashImg.getWidth() / 1.5F),(int)(splashImg.getHeight() / 1.5F),5)));
        mainPanel.add(splash);
        mainPanel.add(startPanel);

        this.add(mainPanel);

        this.setVisible(true);
    }

    public void deleteShellLink(String location) {
        File link = new File(userHome + location);
        if(link.exists())
            link.delete();
    }

    public void createShellLink(String location, String original, String icon) {
        try {
            ShellLink sl = ShellLink.createLink(jarLocation + "\\" + original);
            sl.setIconLocation(jarLocation + "\\" + icon);
            sl.saveTo(userHome + location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go() {
        File mainFolder = new File(jarLocation);
        if(!mainFolder.exists())
            mainFolder.mkdirs();

        String downloadJarUrl = "https://github.com/SvenWollinger/SnipSniper/releases/latest/download/SnipSniper.jar";
        String tempDir = System.getProperty("java.io.tmpdir");

        try {
            String file = "SnipSniper.jar";
            InputStream inputStream = new URL(downloadJarUrl).openStream();
            Files.copy(inputStream, Paths.get(tempDir + file), StandardCopyOption.REPLACE_EXISTING);
            Files.move(Paths.get(tempDir + file), Paths.get(jarLocation + "\\" + file), StandardCopyOption.REPLACE_EXISTING);

            Utils.copy(getClass().getResourceAsStream("/SnSn.ico"), jarLocation + "\\SnSn.ico");
            Utils.copy(getClass().getResourceAsStream("/editor.ico"), jarLocation + "\\editor.ico");
            Utils.copy(getClass().getResourceAsStream("/SnipSniper.bat"), jarLocation + "\\SnipSniper.bat");
            Utils.copy(getClass().getResourceAsStream("/SnipSniperEditor.bat"), jarLocation + "\\SnipSniperEditor.bat");

            if(cbDesktopShortcut.isSelected()) {
                createShellLink(overrideDesktop + "SnipSniper.lnk", "SnipSniper.bat", "SnSn.ico");
                createShellLink(overrideDesktop + "SnipSniperEditor.lnk", "SnipSniperEditor.bat", "editor.ico");
            }

            if(cbAutoStart.isSelected())
                createShellLink(overrideStartup + "SnipSniper.lnk", "SnipSniper.bat", "SnSn.ico");

            if(cbStartMenu.isSelected()) {
                createShellLink(overrideMenu + "SnipSniper.lnk", "SnipSniper.bat", "SnSn.ico");
                createShellLink(overrideMenu + "SnipSniperEditor.lnk", "SnipSniperEditor.bat", "editor.ico");
            }

            JOptionPane.showMessageDialog(instance, "Done!");
            setVisible(false);
            mainPanel.remove(installerPanel);
            mainPanel.add(startPanel);
            setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
