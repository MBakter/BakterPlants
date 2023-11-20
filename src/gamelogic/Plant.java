package gamelogic;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public abstract class Plant implements Serializable{
    public static PlantType type;
    public int time;        //In seconds
    protected static int price;
    protected int produceAmount;
    protected int infusionPrice;
    protected int levelOfPlant;
    private int maxLevel;

    public ImageIcon icon;
    Plot plot;
    protected transient Timer timer;

    class ProduceFruit extends TimerTask {
        public void run() {
            produceAmount();
        }
    }

    Plant(Plot plot) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = 1;
        maxLevel = 5;
    }

    protected int getMaxLevel() { return maxLevel; }
    public int getInfusionPrice() { return infusionPrice; }
    protected void produceAmount() {
        plot.IncreaseFruit(produceAmount);
        plot.updateGUI();
    }

    protected void startTimer() {
        timer.schedule(new ProduceFruit(), (long)time*1000, (long)time*1000);
    }

    public void stopTimer() {
        timer.cancel();
    }

    public void upgradePlant() {}

    public ImageIcon getIcon() { return icon; }

    public void copyData(Plant input) {
        this.icon = input.icon;
                                                    //static tagokat is valahogy át kéne másolni
        this.infusionPrice = input.infusionPrice;
        this.levelOfPlant = input.levelOfPlant;
        this.maxLevel = input.maxLevel;
        this.produceAmount = input.produceAmount;
        this.plot = input.plot;                     //EZ lehet, hogy veszélyes
        this.time = input.time;
        timer = new Timer();
        startTimer();
    }
}
