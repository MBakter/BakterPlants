package gamelogic;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    static {
        type = PlantType.APPLE;
        price = 10; 
    }
    
    Apple(Plot plot) {
        super(plot);
        super.time = 5;
        super.produceAmount = 1;
        super.infusionPrice = 30;
        icon = new ImageIcon("appletree.png");
        super.startTimer();
    }

    public static int getPrice() {
        return price;
    }

    @Override
    public void upgradePlant() {
        if(levelOfPlant >= getMaxLevel())
            return;

        produceAmount *= 2; //TODO: Ennél jobbat
        infusionPrice *= 2;
        levelOfPlant++;
        icon = new ImageIcon("appletree_lvl" + levelOfPlant + ".png");
    }
    
}
