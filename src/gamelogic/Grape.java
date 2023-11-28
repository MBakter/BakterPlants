package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Grape extends Plant {
    
    /**
     * The constructor of this plant sets everything according to its properties
     * @param plot Plot parameter is needed to call the ancestor class's constructor 
     */
    public Grape(Plot plot) {
        super(plot);
        type = PlantType.GRAPE;
        price = PlantType.GRAPE.getPrice(); 
        icon = new ImageIcon("Graphics" + File.separator + "Grape.png");
        super.time = 30 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 7;
        super.infusionPrice = 80;
        super.fertilizerPrice = 70;
        super.startTimer();
    }

    /**
     * The copy constructor is to be used when loading a save.
     * It calls the ancestor's copy constuctor with @param input and @param plot 
     */
    public Grape(Plot plot, Plant input) {
        super(plot, input);
    }
}
