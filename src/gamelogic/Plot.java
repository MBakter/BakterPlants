package gamelogic;

import javax.swing.JButton;

public class Plot {
    private Plant[][] array;
    private int avaliableParcels;
    private int numberOfFruits;
    private int applePrice;
    private int plotPrice;

    public Plot() {
        array = new Plant[7][6];
        avaliableParcels = 1;
        numberOfFruits = 0;
        applePrice = 0;
        plotPrice = 10;
    }

    public int getFruits() {
        return numberOfFruits;
    }

    public int getApplePrice() {
        return applePrice;
    }

    public int getPlotPrice() {
        return plotPrice;
    }

    public int getParcels() {
        return avaliableParcels;
    }

    public void IncreaseFruit(int incrementValue) {
        numberOfFruits += incrementValue;
    }

    //This adds a new button to the grid
    /*private void increaseParcels() {
        JButton plantButton = new JButton();

        plantButton.addActionListener(e -> {
            if(currentyPlanting != PlantType.NONE){
                currentyPlanting = PlantType.NONE;
            }
            plot.IncreaseFruit(1); 
            updateFruitCounterLabel();
        });
    }*/

    public boolean buyApple() {
        if(numberOfFruits < applePrice && avaliableParcels <= 0)
            return false;
        
        //TODO: Initiate buy apple sequence: Choose a plot

        if(applePrice == 0)
            applePrice = 1;
        applePrice *= 1.5; //Mindig a másfélszeresébe kerül TODO: Kitalálni egy jobb "emelőfüggvényt" az 'y(x) = 1.5x' -nél
        return true;
    }

    public boolean buyPlot() {
        if(numberOfFruits < plotPrice)
            return false;

        //TODO: Initiate buy plot sequence

        plotPrice *= 2;
        return true;
    }

    //Ide lehetne, hogy PlantType típust kap és úgy hív meg egy static fv-t pl. Apple.plant();
    public void plantPlant(int row, int col) {
        
    }
}
