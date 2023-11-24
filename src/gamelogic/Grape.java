package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Grape extends Plant {
    Grape(Plot plot) {
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

    //To be used when loading a save
    Grape(Plot plot, Plant input) {
        super(plot);
        type = input.getType();
        price = input.price; 
        icon = input.icon;
        super.time = input.time;
        super.timerDelay = input.getTimeAtSave();
        super.produceAmount = input.produceAmount;
        super.infusionPrice = input.infusionPrice;
        super.fertilizerPrice = input.fertilizerPrice;
        super.startTimer();
    }
}
