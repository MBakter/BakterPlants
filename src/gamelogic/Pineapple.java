package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Pineapple extends Plant {
    
    /**
     * The constructor of this plant sets everything according to its properties
     * @param plot Plot parameter is needed to call the ancestor class's constructor 
     */
    public Pineapple(Plot plot) {
        super(plot);
        type = PlantType.PINEAPPLE;
        price = PlantType.PINEAPPLE.getPrice(); 
        icon = new ImageIcon("Graphics" + File.separator + "Pineapple.png");
        super.time = 600 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 210;
        super.infusionPrice = 700;
        super.fertilizerPrice = 650;
        super.startTimer();
    }

    /**
     * The copy constructor is to be used when loading a save.
     * It calls the ancestor's copy constuctor with @param input and @param plot 
     */
    public Pineapple(Plot plot, Plant input) {
        super(plot, input);
    }
    
}