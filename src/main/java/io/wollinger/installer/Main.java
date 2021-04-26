package io.wollinger.installer;

import javax.swing.*;

public class Main {

    public static String version = "20210426_1";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        new InstallerWindow();
    }
}
