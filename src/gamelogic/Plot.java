package gamelogic;

import frontend.GameGUI;

public class Plot {
    private Plant[][] array;
    private int avaliableParcels;
    private int numberOfFruits;
    private int applePrice;
    private int plotPrice;

    private GameGUI game;

    public Plot(GameGUI g) {
        game = g;
        array = new Plant[7][6];
        avaliableParcels = 0;
        numberOfFruits = 0;
        plotPrice = 0;
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

    public boolean buyPlot() {
        if(numberOfFruits < plotPrice)
            return false;

        numberOfFruits -= plotPrice;
        if(plotPrice == 0)
            plotPrice = 10;

        plotPrice *= 2;
        avaliableParcels++;
        return true;
    }

    public void updateGUI() {
        game.updateFruitCounterLabel();
    }

    public void plantPlant(int row, int col, PlantType plant) {
        switch (plant) {
            case APPLE:
                if(numberOfFruits >= Apple.getPrice()) {
                    numberOfFruits -= Apple.getPrice();
                    Apple.updatePrice();
                    array[row][col] = new Apple(this); //TODO Implement these!!!!!!!!
                    game.getPlantLabels(row, col).setIcon(Apple.icon);
                }
                else
                    System.out.println("Not enough fruits to buy apple!");
                break;

            case GRAPE:
                array[row][col] = new Grape(this);
                System.out.println("Planted grape");
                break;

            case BANANA:
                array[row][col] = new Banana(this);
                System.out.println("Planted banana");
                break;

            case PINEAPPLE:
                array[row][col] = new Pineapple(this);
                System.out.println("Planted pineapple");
                break;
            default:
                break;
        }
         
    }
}
