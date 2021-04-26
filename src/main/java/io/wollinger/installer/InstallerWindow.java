package io.wollinger.installer;


import com.erigir.mslinks.ShellLink;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InstallerWindow extends JFrame {

    public static String userHome = System.getProperty("user.home");

    public static String jarLocation = new File(userHome + "\\.SnipSniper\\").getAbsolutePath();

    public JButton btnChangeLocation = new JButton("Change location");
    public JLabel labelChangeLocation = new JLabel(jarLocation, SwingConstants.RIGHT);;
    public JCheckBox cbStartMenu = new JCheckBox();
    public JCheckBox cbAutoStart = new JCheckBox();
    public JCheckBox cbDesktopShortcut = new JCheckBox();

    JFileChooser jfc = new JFileChooser(userHome);

    public InstallerWindow() {
        this.setTitle("SnipSniper Installer");
        this.setSize(512,256);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0,1));
        int margin = 10;

        JPanel row0 = new JPanel(new GridLayout(0,2));
        row0.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        labelChangeLocation.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row0.add(labelChangeLocation);
        row0.add(btnChangeLocation);
        mainPanel.add(row0);

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
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new GridLayout(0,2));
        row2.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label3 = new JLabel("Add to Auto Start", SwingConstants.RIGHT);
        label3.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row2.add(label3);
        row2.add(cbAutoStart);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new GridLayout(0,2));
        row3.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label4 = new JLabel("Add Desktop Shortcut", SwingConstants.RIGHT);
        label4.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row3.add(label4);
        row3.add(cbDesktopShortcut);
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new GridLayout(0,1));
        JButton goBtn = new JButton("Start");
        row4.setBorder(BorderFactory.createEmptyBorder(margin, margin*15, margin/2, margin*15));
        goBtn.addActionListener(e -> go());
        row4.add(goBtn);
        mainPanel.add(row4);

        this.add(mainPanel);

        this.setVisible(true);
    }

    public void createShellLink(String location) {
        try {
            ShellLink sl = ShellLink.createLink(jarLocation + "\\SnipSniper.bat");
            sl.setIconLocation(jarLocation + "\\SnSn.ico");
            sl.saveTo(userHome + location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go() {
        String downloadJarUrl = "https://github.com/SvenWollinger/SnipSniper/releases/latest/download/SnipSniper.jar";
        String tempDir = System.getProperty("java.io.tmpdir");

        try {
            String file = "SnipSniper.jar";
            InputStream inputStream = new URL(downloadJarUrl).openStream();
            Files.copy(inputStream, Paths.get(tempDir + file), StandardCopyOption.REPLACE_EXISTING);
            Files.move(Paths.get(tempDir + file), Paths.get(jarLocation + "\\" + file), StandardCopyOption.REPLACE_EXISTING);

            Utils.copy(getClass().getResourceAsStream("/SnSn.ico"), jarLocation + "\\SnSn.ico");
            Utils.copy(getClass().getResourceAsStream("/SnipSniper.bat"), jarLocation + "\\SnipSniper.bat");

            if(cbDesktopShortcut.isSelected())
                createShellLink("\\Desktop\\SnipSniper.lnk");

            if(cbAutoStart.isSelected())
                createShellLink("\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\SnipSniper.lnk");

            if(cbStartMenu.isSelected())
                createShellLink("\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\SnipSniper.lnk");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
