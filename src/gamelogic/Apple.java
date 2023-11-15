package gamelogic;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    static {
        type = PlantType.APPLE;
        price = 0;
        icon = new ImageIcon("appletree.png");
    }
    
    Apple(Plot plot) {
        super(plot);
        super.time = 1;
        super.produceAmount = 1;
        super.startTimer();
    }

    public static int getPrice() {
        return price;
    }

    public static void updatePrice() {
        if(price == 0)
            price = 1;
        price *= 1.5; //Mindig a másfélszeresébe kerül TODO: Kitalálni egy jobb "emelőfüggvényt" az 'y(x) = 1.5x' -nél
    }
    
}
