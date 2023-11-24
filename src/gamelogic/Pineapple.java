package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Pineapple extends Plant {
    Pineapple(Plot plot) {
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

    //To be used when loading a save
    Pineapple(Plot plot, Plant input) {
        super(plot, input);
    }
    
}