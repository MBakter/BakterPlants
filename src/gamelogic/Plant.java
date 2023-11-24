package gamelogic;

import java.io.Serializable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public abstract class Plant implements Serializable{
    private static final long serialVersionUID = 6669682568296767219L;
    
    protected PlantType type;
    protected int price;
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

    public ImageIcon icon;
    Plot plot;
    protected transient Timer timer;

    class ProduceFruit extends TimerTask {
        public void run() {
            produceAmount();
            timeOfStart = System.currentTimeMillis();
            //lot.game.fruitAnimation(0,0);
        }
    }

    Plant(Plot plot) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = 1;
        maxLevel = 5;
    }

    /* Plant(Plot plot, Plant copy) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = copy.levelOfPlant;
        maxLevel = copy.maxLevel;
    } */

    //To be used when loading a save
    Plant(Plot plot, Plant input) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = input.levelOfPlant;
        maxLevel = input.maxLevel;
        type = input.getType();
        price = input.price; 
        icon = input.icon;
        time = input.time;
        timerDelay = input.getTimeAtSave();
        produceAmount = input.produceAmount;
        infusionPrice = input.infusionPrice;
        fertilizerPrice = input.fertilizerPrice;
        startTimer();
    }

    public PlantType getType() { return type; }
    protected int getMaxLevel() { return maxLevel; }
    public int getLevel() { return levelOfPlant; }
    public int getPrice() { return price; }
    public int getInfusionPrice() { return infusionPrice; }
    public int getFertilizerPrice() { return fertilizerPrice; }
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

    public void upgradePlant() {
        if(levelOfPlant >= getMaxLevel())
            return;

        produceAmount *= 2; //TODO: Ennél jobbat
        infusionPrice *= 2;
        levelOfPlant++;
    }

    public boolean speedUpPlant() { 
        if((time / 2) <= minTime) {
            if(time != minTime) {
                time = minTime;
                return true;
            }
            plot.game.showMessage("Plant is very fertile!");
            return false;
        }
        time /= 2; //TODO: Ennél jobbat
        fertilizerPrice *= 2;
        long delay = getElapsedTime();
        timer.cancel();
        timer = new Timer();
        timer.schedule(new ProduceFruit(), delay / 2, time); //A maradék időt már gyorsítva tölti le
        timeOfStart = System.currentTimeMillis();
        return true;
    }

    public void getProperties(Map<String, String> props) {
        props.put("Plant", PlantType.convertToString(type));
        props.put("Level", Integer.toString(levelOfPlant));
        props.put("Produce Amount", Integer.toString(produceAmount) + " fruits");
        props.put("Time to produce fruit", Long.toString(time/1000) + " sec");
        props.put("Price", Integer.toString(price) + " fruits");
        props.put("Infusion Price", Integer.toString(infusionPrice) + " fruits");
        props.put("Fertilizer Price", Integer.toString(fertilizerPrice) + " fruits");
    }    
}
