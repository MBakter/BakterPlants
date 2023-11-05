package frontend;
import javax.swing.*;

public class Game {

    public static String formatTime(int sec) {
        if(sec < 0)
            return new String("Going backwards in time...");

        int hours = (int)(sec / 3600);
        sec = sec - (hours * 3600);

        int minutes = (int)(sec/60);
        sec = sec - (minutes * 60);

        if(hours > 0) 
            return new String(hours + " Hours, " + minutes + " Minutes, " + sec + " Seconds");
        if(minutes > 0)
            return new String(minutes + " Minutes, " + sec + " Seconds");
        return new String(sec + " Seconds");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI());
    }
}
