package gamelogic;

import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import frontend.GameGUI;

public class Plot implements Serializable{
    private static final long serialVersionUID = 4443892591982365865L;
    private Plant[][] array;
    private int numOfParcels;
    private int numOfFruits;
    private int applePrice;
    private int plotPrice;
    private int numOfPlants;
    private static int ROWS = 3;
    private static int COLS = 5;

    public static ImageIcon plotIcon = new ImageIcon("Graphics" + File.separator + "Soil.png");
    public static ImageIcon occupiedPlotIcon = new ImageIcon("Graphics" + File.separator + "Grass.png");
    private transient GameGUI game;

    public Plot(GameGUI g) {
        game = g;
        array = new Plant[ROWS][COLS];
        numOfParcels = 0;
        numOfPlants = 0;
        numOfFruits = 2000000000;
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

    public void fertilizePlant(int row, int col) {
        if(isPlantInPlot(row, col) && numOfFruits >= array[row][col].fertilizerPrice){
            decreaseFruit(array[row][col].fertilizerPrice);
            array[row][col].speedUpPlant();
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
            game.getPlantLabels(row, col).setIcon(Plot.occupiedPlotIcon);
            array[row][col] = null;
            numOfPlants--;
        }
    }   

    private void stopTimers() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if(array[i][j] == null)
                    break;
                array[i][j].stopTimer();
            }
        }
    }
    private void startTimers() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if(array[i][j] == null)
                    break;
                array[i][j].startTimer();
            }
        }
    }

    //Initiates saving sequence
    public void initSave() {
        //stopTimers();

        File saveLocation = new File(System.getProperty("user.dir").toString() + File.separator + "saves");
        if(!saveLocation.exists()) 
            saveLocation.mkdir();

        JFileChooser chooser = new JFileChooser(saveLocation);

        if(chooser.showSaveDialog(game.getFrame()) == JFileChooser.APPROVE_OPTION) {            
            try {
                FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                saveElapsedTime();
                oos.writeObject(this);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //startTimers();
    }

    public void saveElapsedTime() {
        for (int i = 0; i < ROWS; i++) 
            for (int j = 0; j < COLS; j++) 
                if(isPlantInPlot(i, j))
                    array[i][j].setTimeAtSave();
    }

    public void initLoad() {
        File saveLocation = new File(System.getProperty("user.dir").toString() + File.separator + "saves");
        if(!saveLocation.exists()){
            game.showMessage("There is no file to load");
            return; //There is nothing to load
        }
        JFileChooser chooser = new JFileChooser(saveLocation);

        if(chooser.showOpenDialog(game.getFrame()) == JFileChooser.APPROVE_OPTION) {            
            try {
                FileInputStream fis = new FileInputStream(chooser.getSelectedFile());
                ObjectInputStream oos = new ObjectInputStream(fis);
                Plot inputPlot = (Plot) oos.readObject();
                oos.close();
                copyDataToThis(inputPlot);
            } catch (Exception e) {
                e.printStackTrace();
            }
            game.updateState();
        }
    }

    //TODO: Timer visszaállítása arra ami volt mentéskor
    public void loadPlants(int row, int col, PlantType plant, long timeAtSave) {
        switch (plant) {
            case APPLE:
                array[row][col] = new Apple(this, timeAtSave);
                updatePlantIcon(row, col);
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

    private void copyDataToThis(Plot input) {
        this.applePrice = input.applePrice;
        this.numOfFruits = input.numOfFruits;
        this.numOfParcels = input.numOfParcels;
        this.numOfPlants = input.numOfPlants;
        this.plotPrice = input.plotPrice;
        this.game.initFromLoad(input);

    }
}
