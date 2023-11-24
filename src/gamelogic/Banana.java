package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Banana extends Plant {
    Banana(Plot plot) {
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

    //To be used when loading a save
    Banana(Plot plot, Plant input) {
        super(plot, input);
    }
    
}