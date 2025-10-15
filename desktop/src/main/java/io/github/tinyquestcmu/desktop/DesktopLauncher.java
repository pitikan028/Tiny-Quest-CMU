package io.github.tinyquestcmu.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.tinyquestcmu.screens.TinyQuestCMUGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        // Log any uncaught exceptions to a file so it's easy to share
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {
                java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.File("TinyQuestCMU_error.txt"));
                e.printStackTrace(pw);
                pw.flush();
                pw.close();
                System.err.println(">>> Uncaught error written to TinyQuestCMU_error.txt");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        try {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setTitle("TinyQuestCMU");
            config.setWindowedMode(800, 480);
            config.useVsync(true);
            new Lwjgl3Application(new TinyQuestCMUGame(), config);
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.File("TinyQuestCMU_error.txt"));
                e.printStackTrace(pw);
                pw.flush();
                pw.close();
                System.err.println(">>> Error written to TinyQuestCMU_error.txt");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
