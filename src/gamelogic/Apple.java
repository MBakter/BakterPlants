package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Apple extends Plant {

    /**
     * The constructor of this plant sets everything according to its properties
     * @param plot Plot parameter is needed to call the ancestor class's constructor 
     */
    public Apple(Plot plot) {
        super(plot);
        type = PlantType.APPLE;
        price = PlantType.APPLE.getPrice(); 
        icon = new ImageIcon("Graphics" + File.separator + "Apple.png");
        super.time = 5 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 1;
        super.infusionPrice = 20;
        super.fertilizerPrice = 15;
        super.startTimer();
    }

    /**
     * The copy constructor is to be used when loading a save.
     * It calls the ancestor's copy constuctor with @param input and @param plot 
     */
    public Apple(Plot plot, Plant input) {
        super(plot, input);
    }
    
}
