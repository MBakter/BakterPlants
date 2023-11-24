package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    Apple(Plot plot) {
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

    //To be used when loading a save
    Apple(Plot plot, Plant input) {
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
