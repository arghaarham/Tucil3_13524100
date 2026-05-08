package esngalir;

import esngalir.ui.ConsoleUI;
import esngalir.ui.GUIApp;

public class Main {

    public static void main(String[] args) {
        boolean useGUI = false;
        for (String arg : args) {
            if (arg.equals("--gui")) {
                useGUI = true;
                break;
            }
        }

        if (useGUI) {
            GUIApp.launch(args);
        } else {
            new ConsoleUI().run();
        }
    }
}