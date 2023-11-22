package gamelogic;

import java.io.File;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    static {
        price = PlantType.APPLE.getPrice(); 
        icon = new ImageIcon("Graphics" + File.separator + "apple.png");
    }
    
    Apple(Plot plot) {
        super(plot);
        type = PlantType.APPLE;
        super.time = 5 * 1000;
        super.timerDelay = super.time;
        super.produceAmount = 1;
        super.infusionPrice = 30;
        super.fertilizerPrice = 20;
        super.startTimer();
    }

    //To be used when loading a save
    Apple(Plot plot, Plant input) {
        super(plot);
        type = PlantType.APPLE;
        super.time = input.time;
        super.timerDelay = input.getTimeAtSave();
        super.produceAmount = input.produceAmount;
        super.infusionPrice = input.infusionPrice;
        super.fertilizerPrice = input.fertilizerPrice;
        super.startTimer();
    }

    @Override
    public void upgradePlant() {
        if(levelOfPlant >= getMaxLevel())
            return;

        produceAmount *= 2; //TODO: Ennél jobbat
        infusionPrice *= 2;
        levelOfPlant++;
        //icon = new ImageIcon("Apple_lvl" + levelOfPlant + ".png");
    }
    
    @Override
    public boolean speedUpPlant() {
        if((super.time / 2) <= minTime) {
            if(super.time != minTime) {
                super.time = minTime;
                return true;
            }
            plot.game.showMessage("Plant is very fertile!");
            return false;
        }
        super.time /= 2; //TODO: Ennél jobbat
        fertilizerPrice *= 4;
        long delay = getElapsedTime();
        timer.schedule(new ProduceFruit(), delay, time);
        timeOfStart = System.currentTimeMillis();
        return true;
    }
}
