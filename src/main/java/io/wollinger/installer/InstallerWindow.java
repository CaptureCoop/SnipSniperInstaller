package io.wollinger.installer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InstallerWindow extends JFrame {

    public static String jarLocation = new File(System.getProperty("user.home") + "\\.SnipSniper\\").getAbsolutePath();

    public InstallerWindow() {
        this.setTitle("SnipSniper Installer");
        this.setSize(512,256);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0,1));

        int margin = 10;

        JPanel row0 = new JPanel(new GridLayout(0,2));
        row0.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label1 = new JLabel(jarLocation, SwingConstants.RIGHT);
        label1.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row0.add(label1);
        row0.add(new JButton("Change location"));
        mainPanel.add(row0);

        JPanel row1 = new JPanel(new GridLayout(0,2));
        row1.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label2 = new JLabel("Add to Start Menu", SwingConstants.RIGHT);
        label2.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row1.add(label2);
        row1.add(new JCheckBox());
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new GridLayout(0,2));
        row2.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label3 = new JLabel("Add to Auto Start", SwingConstants.RIGHT);
        label3.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row2.add(label3);
        row2.add(new JCheckBox());
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new GridLayout(0,2));
        row3.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        JLabel label4 = new JLabel("Add Desktop Shortcut", SwingConstants.RIGHT);
        label4.setBorder(BorderFactory.createEmptyBorder(0, margin, 0, margin));
        row3.add(label4);
        row3.add(new JCheckBox());
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new GridLayout(0,1));
        JButton goBtn = new JButton("Start");
        row4.setBorder(BorderFactory.createEmptyBorder(margin, margin*15, margin/2, margin*15));
        row4.add(goBtn);
        mainPanel.add(row4);

        this.add(mainPanel);

        this.setVisible(true);
    }

}
