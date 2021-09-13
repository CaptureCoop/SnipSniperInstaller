package org.snipsniper.installer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {

    public static void copy(InputStream source , String destination) {
        System.out.println("Copying " + source.toString() + " to " + destination);
        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
