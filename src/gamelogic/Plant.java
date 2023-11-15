package gamelogic;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public abstract class Plant {
    public static PlantType type;
    public int time;        //In seconds
    protected static int price;
    protected int produceAmount;
    public static ImageIcon icon;
    Plot plot;
    protected Timer timer;

    class ProduceFruit extends TimerTask {
        public void run() {
            produceAmount();
        }
    }

    Plant(Plot plot) {
        this.plot = plot;
        timer = new Timer();
    }

    protected void produceAmount() {
        plot.IncreaseFruit(produceAmount);
        plot.updateGUI();
    }

    protected void startTimer() {
        timer.schedule(new ProduceFruit(), 0, (long)time*1000);
    }
}
