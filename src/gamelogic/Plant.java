package gamelogic;

import java.io.Serializable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

public abstract class Plant implements Serializable{
    //private static final long serialVersionUID = 6669682568296767219L;
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
    private Plot plot;
    protected transient Timer timer;

    /**
     * This class is a TimerTask for the timer that calls produceAmount every time the timer 
     */
    class ProduceFruit extends TimerTask {
        public void run() {
            produceAmount();
            timeOfStart = System.currentTimeMillis();
            //lot.game.fruitAnimation(0,0);
        }
    }

    /**
     * The constuctor of the plant. It gets a @param plot Plot and creates a new timer
     */
    public Plant(Plot plot) {
        this.plot = plot;
        timer = new Timer();
        levelOfPlant = 1;
        maxLevel = 5;
    }

    /**
     * The copy constructor is to be used when loading a save. The plot of the @param input Plant is
     * not copied, it should be added separately through @param plot
     * It copies all values of the input Plant and starts the timer from the timeAtSave of the input Plant
     */
    public Plant(Plot plot, Plant input) {
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

    /** @return Type of plant */
    public PlantType getType() { return type; }

    /** @return max level of plant */
    protected int getMaxLevel() { return maxLevel; }

    /** @return level of plant */
    public int getLevel() { return levelOfPlant; }

    /** @return price of plant */
    public int getPrice() { return price; }

    /** @return infusion price of plant */
    public int getInfusionPrice() { return infusionPrice; }

    /** @return fertilization price of plant */
    public int getFertilizerPrice() { return fertilizerPrice; }

    /** @return icon of plant */
    public ImageIcon getIcon() { return icon; }

    /** Sets the time at save to the elapsed time of the timer */
    public void setTimeAtSave() { timeAtSave = getElapsedTime(); }

    /** @return The time at save */
    public long getTimeAtSave() { return timeAtSave; }
    
    /**
     * Increases the number of fruits in plot by the amount that a plant produces
     */
    protected void produceAmount() {
        plot.IncreaseFruit(produceAmount);
        plot.updateGUI();
    }

    /**
     * Starts the timer of the plant
     */
    protected void startTimer() {
        timer.schedule(new ProduceFruit(), timerDelay, time);
        timeOfStart = System.currentTimeMillis();
    }

    /**
     * Cancels the timer of the plant
     */
    public void stopTimer() {
        timer.cancel();
    }

    /**
     * @return The elapsed time of the timer
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - timeOfStart;
    }

    /**
     * Upgrades a plant if it is not at max level. The production amount and the price of infusion doubles
     */
    public void upgradePlant() {
        if(levelOfPlant >= getMaxLevel()){
            return;
        }

        produceAmount *= 2; 
        infusionPrice *= 2;
        levelOfPlant++;
    }

    /**
     * Speeds up the plant by halving the timer. Creates a new timer so that the remaining amount 
     * of time could be sped up.
     */
    public void speedUpPlant() {
        if(time == minTime)
            return;

        if((time / 2) < minTime) {
            time = minTime;
        }
        else
            time /= 2;
        fertilizerPrice *= 2;
        long delay = getElapsedTime();
        timer.cancel();
        timer = new Timer();
        timer.schedule(new ProduceFruit(), delay / 2, time);
        timeOfStart = System.currentTimeMillis();
        return;
    }

    /**
     * Fills a @param props Map to all properties of the plant. 
     * If new properties are added to a plant, this should be expanded
     */
    public void getProperties(Map<String, String> props) {
        props.put("Plant", PlantType.convertToString(type));
        props.put("Level", Integer.toString(levelOfPlant));
        props.put("Produce Amount", Integer.toString(produceAmount) + " fruits");
        props.put("Time to produce fruit", Long.toString(time/1000) + " sec");
        props.put("Price", Integer.toString(price) + " fruits");
        if(levelOfPlant != maxLevel)
            props.put("Infusion Price", Integer.toString(infusionPrice) + " fruits");
        if(time > minTime)
            props.put("Fertilizer Price", Integer.toString(fertilizerPrice) + " fruits");
    }    
}
