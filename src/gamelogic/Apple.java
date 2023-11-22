package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    static {
        price = 10; 
    }
    
    Apple(Plot plot) {
        super(plot);
        type = PlantType.APPLE;
        super.time = 5 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 1;
        super.infusionPrice = 30;
        super.fertilizerPrice = 20;
        icon = new ImageIcon("Graphics" + File.separator + "apple.png");
        super.startTimer();
    }

    Apple(Plot plot, long delay) {
        super(plot);
        type = PlantType.APPLE;
        super.time = 5 * 1000;
        super.timerDelay = delay;
        super.produceAmount = 1;
        super.infusionPrice = 30;
        super.fertilizerPrice = 20;
        icon = new ImageIcon("Graphics" + File.separator + "apple.png");
        super.startTimer();
    }

    @Override
    public void upgradePlant() {
        if(levelOfPlant >= getMaxLevel())
            return;

        produceAmount *= 2; //TODO: Ennél jobbat
        infusionPrice *= 2;
        levelOfPlant++;
        icon = new ImageIcon("Apple_lvl" + levelOfPlant + ".png");
    }
    
    @Override
    public void speedUpPlant() {
        if((super.time /= 2) <= minTime)
            return;

        super.time /= 2; //TODO: Ennél jobbat
        fertilizerPrice *= 4;

        timer.schedule(new ProduceFruit(), time);
        timeOfStart = System.currentTimeMillis();
    }
}
