package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Banana extends Plant {
    
    /**
     * The constructor of this plant sets everything according to its properties
     * @param plot Plot parameter is needed to call the ancestor class's constructor 
     */
    public Banana(Plot plot) {
        super(plot);
        type = PlantType.BANANA;
        price = PlantType.BANANA.getPrice(); 
        icon = new ImageIcon("Graphics" + File.separator + "Banana.png");
        super.time = 180 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 49;
        super.infusionPrice = 250;
        super.fertilizerPrice = 225;
        super.startTimer();
    }

    /**
     * The copy constructor is to be used when loading a save.
     * It calls the ancestor's copy constuctor with @param input and @param plot 
     */
    public Banana(Plot plot, Plant input) {
        super(plot, input);
    }
    
}