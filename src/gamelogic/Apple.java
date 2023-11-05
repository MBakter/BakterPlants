package gamelogic;

import javax.swing.ImageIcon;

public class Apple extends Plant {
    static {
        type = PlantType.APPLE;
        price = 0;
        icon = new ImageIcon("apple.png");
    }
    
    Apple() {
        super.time = 10;
    }
}
