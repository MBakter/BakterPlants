package frontend;
import javax.swing.*;

public class Game {

    /**
     * The main function. It starts the program by creating an instance of GameGUI
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI());
    }
}
