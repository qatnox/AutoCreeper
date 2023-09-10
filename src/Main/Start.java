package Main;

import javax.swing.*;

public class Start {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch(Exception ignored){}

        AutoClicker autoClicker = new AutoClicker();
    }
}
