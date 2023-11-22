package gamelogic;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public abstract class Plant implements Serializable{
    private static final long serialVersionUID = 6669682568296767219L;
    
    protected PlantType type;
    protected static int price;
    protected int produceAmount;
    protected int infusionPrice;
    protected int fertilizerPrice;
    protected int levelOfPlant;
    private int maxLevel;

    public long time;        //In millisec
    public static long minTime = 500; //In milliseconds
    protected long timerDelay;
    protected long timeOfStart;
    private long timeAtSave;

    public static ImageIcon icon;
    Plot plot;
    protected transient Timer timer;

    class ProduceFruit extends TimerTask {
        public void run() {
            produceAmount();
            timeOfStart = System.currentTimeMillis();
        }
    }

    Plant(Plot plot) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = 1;
        maxLevel = 5;
    }

    Plant(Plot plot, Plant copy) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = copy.levelOfPlant;
        maxLevel = copy.maxLevel;
    }

    public PlantType getType() { return type; }
    protected int getMaxLevel() { return maxLevel; }
    public static int getPrice() { return price; }
    public int getInfusionPrice() { return infusionPrice; }
    public ImageIcon getIcon() { return icon; }
    public void setTimeAtSave() { timeAtSave = getElapsedTime(); }
    public long getTimeAtSave() { return timeAtSave; }
    

    protected void produceAmount() {
        plot.IncreaseFruit(produceAmount);
        plot.updateGUI();
    }

    protected void startTimer() {
        timer.schedule(new ProduceFruit(), timerDelay, time);
        timeOfStart = System.currentTimeMillis();
    }

    public void stopTimer() {
        timer.cancel();
    }

    //In millisec
    public long getElapsedTime() {
        return System.currentTimeMillis() - timeOfStart;
    }

    public void progressBar() {

    }

    public void upgradePlant() {}

    public boolean speedUpPlant() { return false; }

    
}
