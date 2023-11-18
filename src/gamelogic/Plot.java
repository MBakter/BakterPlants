package gamelogic;

import javax.swing.ImageIcon;

import frontend.GameGUI;

public class Plot {
    private Plant[][] array;
    private int numOfParcels;
    private int numOfFruits;
    private int applePrice;
    private int plotPrice;
    private int numOfPlants;
    private static int ROWS = 3;
    private static int COLS = 5;

    public static ImageIcon plotIcon = new ImageIcon("grass.png");
    private GameGUI game;

    public Plot(GameGUI g) {
        game = g;
        array = new Plant[ROWS][COLS];
        numOfParcels = 0;
        numOfPlants = 0;
        numOfFruits = 20;
        plotPrice = 0;
    }

    public static int getROWS() {return ROWS;}
    public static int getCOLS() {return COLS;}

    public int getFruits() {
        return numOfFruits;
    }

    public int getApplePrice() {
        return applePrice;
    }

    public int getPlotPrice() {
        return plotPrice;
    }

    public int getParcels() {
        return numOfParcels;
    }

    public Plant[][] getarray() {
        return array;
    }

    public boolean isPlantInPlot(int row, int col) {
        return array[row][col] != null;
    }
    public void IncreaseFruit(int incrementValue) {
        numOfFruits += incrementValue;
        game.updateFruitCounterLabel();
    }

    public void decreaseFruit(int decrementValue) {
        numOfFruits -= decrementValue;
        game.updateFruitCounterLabel();
    }

    public boolean buyPlot() {
        if(numOfFruits < plotPrice)
            return false;

        numOfFruits -= plotPrice;
        if(plotPrice == 0)
            plotPrice = 10;

        plotPrice *= 2;
        numOfParcels++;
        return true;
    }

    public void updateGUI() {
        game.updateFruitCounterLabel();
    }

    public void updatePlantIcon(int row, int col) {
        game.getPlantLabels(row, col).setIcon(array[row][col].getIcon());
    }

    public void plantPlant(int row, int col, PlantType plant) {
        //Ha van már plant akkor nem lehet ültetni
        if(isPlantInPlot(row, col))
            return;
        switch (plant) {
            case APPLE:
                if(numOfFruits >= Apple.getPrice() || numOfPlants == 0) {
                    if(numOfPlants > 0)
                        decreaseFruit(Apple.getPrice());
                    array[row][col] = new Apple(this);
                    numOfPlants++;
                    updatePlantIcon(row, col);
                }
                else
                    game.showMessage("Not enough fruits for Apple");
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
                System.out.println("Planted pinaapple");
                break;
            default:
                break;
        }
    }

    public void infusePlant(int row, int col) {
        if(isPlantInPlot(row, col) && numOfFruits >= array[row][col].infusionPrice){
            decreaseFruit(array[row][col].infusionPrice);
            array[row][col].upgradePlant();
            updatePlantIcon(row, col);
        }
    }

    public void digPlant(int row, int col) {
        if(isPlantInPlot(row, col)){
            array[row][col].stopTimer();
            game.getPlantLabels(row, col).setIcon(Plot.plotIcon);
            array[row][col] = null;
            numOfPlants--;
        }
    }   

    //Initiates saving sequence
    public void initSave() {

    }

    public void initLoad() {
        
    }
}
