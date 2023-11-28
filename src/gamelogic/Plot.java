package gamelogic;

import java.io.*;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import frontend.GameGUI;

public class Plot implements Serializable{
    //private static final long serialVersionUID = 4443892591982365865L;
    private Plant[][] array;
    private int numOfParcels;
    private int numOfFruits;
    private int plotPrice;
    private int numOfPlants;
    private static int ROWS = 3;
    private static int COLS = 5;

    public static ImageIcon plotIcon = new ImageIcon("Graphics" + File.separator + "Soil.png");
    public static ImageIcon occupiedPlotIcon = new ImageIcon("Graphics" + File.separator + "Grass.png");
    public transient GameGUI game;

    /**
     * Creates a new plot for the @param g GameGUI game. Sets the array's elements to null, and everything to 0
     */
    public Plot(GameGUI g) {
        game = g;
        array = new Plant[ROWS][COLS];
        numOfParcels = 0;
        numOfPlants = 0;
        numOfFruits = 10000;
        plotPrice = 0;
    }

    /** @return the max number of rows */
    public static int getROWS() {return ROWS;}

    /** @return the max number of cols */
    public static int getCOLS() {return COLS;}

    /** @return the max number of rows */
    public int getFruits() { return numOfFruits; }

    /** Sets the number of fruits to @param f */
    public void setFruits(int f) { numOfFruits = f; }

    /** @return the price of a new plot */
    public int getPlotPrice() { return plotPrice; }

    /** @return the number of occupied parcels */
    public int getParcels() { return numOfParcels; }

    /** @return the array of Plants */
    public Plant[][] getarray() { return array; }

    /** @return the total number of planted plants */
    public int getPlants() { return numOfPlants; }

    /**
     * Returns true if the array in @param row and @param col is not null, so a plant is
     * planted there
     * @param row
     * @param col
     * @return true or false
     */
    public boolean isPlantInPlot(int row, int col) { 
        if(row >= ROWS || col >= COLS)
            return false;
        return array[row][col] != null; 
    }

    /**
     * Increases the total number of fruits by
     * @param incrementValue
     */
    public void IncreaseFruit(int incrementValue) {
        if(incrementValue <= 0)
            return;
        numOfFruits += incrementValue;
        game.updateFruitCounterLabel();
    }

    /**
     * Decreases the total number of fruits by
     * @param decrementValue
     */
    public void decreaseFruit(int decrementValue) {
        if(decrementValue <= 0)
            return;

        if(numOfFruits - decrementValue >= 0)
            numOfFruits -= decrementValue;

        game.updateFruitCounterLabel();
    }

    /**
     * Increases the number of parcels ergo shows that a new plot is bought.
     * Doubles the price of a new plot
     * @return if it was successful or not. Could be failure if plots are already maxed out or insufficient
     * amount of fruits
     */
    public boolean buyPlot() {
        if(numOfParcels >= ROWS*COLS) 
            return false;
        
        if(numOfFruits < plotPrice){
            game.showMessage("Not enough fruits to buy plot!");
            return false;
        }

        numOfFruits -= plotPrice;
        if(plotPrice == 0)
            plotPrice = 40;
        else
            plotPrice *= 2;
        numOfParcels++;
        return true;
    }

    /**
     * Updates the game's fruit counter label
     */
    public void updateGUI() {
        game.updateFruitCounterLabel();
    }

    /**
     * Updates the game's plantLabels with the icon of the plant that's planted there 
     * in the array at location:
     * @param row
     * @param col
     */
    public void updatePlantIcon(int row, int col) {
        if(row >= ROWS || col >= COLS)
            return;
        game.getPlantLabels(row, col).setIcon(array[row][col].getIcon());
    }

    /**
     * Plants a plant (creates an instance of a Plant) with PlantType @param plant 
     * in @param row @param col of the array if the number of fruits is enough. If number of fruits
     * is smaller than the price of the plant, shows a message
     */
    public void plantPlant(int row, int col, PlantType plant) {
        
        if(isPlantInPlot(row, col)){
            game.showMessage("Plot is occupied");
            return;
        }
        if(numOfParcels < (col+1) * (row+1)){
            System.err.println("Plot not bought");
            return;
        }
        if(row >= ROWS || col >= COLS)
            return;

        switch (plant) {
            case APPLE:
                if(numOfFruits >= PlantType.APPLE.getPrice() || numOfPlants == 0) {
                    if(numOfPlants > 0)
                        decreaseFruit(PlantType.APPLE.getPrice());
                    array[row][col] = new Apple(this);
                    numOfPlants++;
                    updatePlantIcon(row, col);
                }
                else
                    game.showMessage("Not enough fruits for Apple!");
                break;

            case GRAPE:
                if(numOfFruits >= PlantType.GRAPE.getPrice()) {
                    if(numOfPlants > 0)
                        decreaseFruit(PlantType.GRAPE.getPrice());
                    array[row][col] = new Grape(this);
                    numOfPlants++;
                    updatePlantIcon(row, col);
                }
                else
                    game.showMessage("Not enough fruits for Grape!");
                break;

            case BANANA:
                if(numOfFruits >= PlantType.BANANA.getPrice()) {
                    if(numOfPlants > 0)
                        decreaseFruit(PlantType.BANANA.getPrice());
                    array[row][col] = new Banana(this);
                    numOfPlants++;
                    updatePlantIcon(row, col);
                }
                else
                    game.showMessage("Not enough fruits for Banana!");
                break;

            case PINEAPPLE:
                if(numOfFruits >= PlantType.PINEAPPLE.getPrice()) {
                    if(numOfPlants > 0)
                        decreaseFruit(PlantType.PINEAPPLE.getPrice());
                    array[row][col] = new Pineapple(this);
                    numOfPlants++;
                    updatePlantIcon(row, col);
                }
                else
                    game.showMessage("Not enough fruits for Pineapple!");
                break;
            default:
                break;
        }
    }

    /**
     * Calls the speedUpPlant to the plant at @param row @param col of the array if there is a plant,
     * it isn't already at the minimum time and the amount of fruits avaliable is enough. 
     */
    public void fertilizePlant(int row, int col) {
        if(!isPlantInPlot(row, col)) 
            return;
        
        if(array[row][col].time <= Plant.minTime) {
            game.showMessage("This " + PlantType.convertToString(array[row][col].getType()) +  " is very fertile!");
            return;
        }

        if(numOfFruits >= array[row][col].fertilizerPrice){
            decreaseFruit(array[row][col].fertilizerPrice);
            array[row][col].speedUpPlant();
        }
        else
            game.showMessage("Not enough fruits for Fertilization!");
    }

    /**
     * Calls the upgradePlant to the plant at @param row @param col of the array if there is a plant,
     * it isn't already at max level and the amount of fruits avaliable is enough.
     */
    public void infusePlant(int row, int col) {
        if(!isPlantInPlot(row, col))
            return;

        if(array[row][col].levelOfPlant >= array[row][col].getMaxLevel()){
            game.showMessage("This " + PlantType.convertToString(array[row][col].getType()) +  " is already super infused!");
            return;
        }
        if(numOfFruits >= array[row][col].infusionPrice){
            decreaseFruit(array[row][col].infusionPrice);
            array[row][col].upgradePlant();
            updatePlantIcon(row, col);
        }
        else
            game.showMessage("Not enough fruits for Infusion!");
    }

    /**
     * Removes the plant from the array at @param row @param col (sets array[row][col] to null)
     * Decreases the total number of plants
     */
    public void digPlant(int row, int col) {
        if(isPlantInPlot(row, col)){
            array[row][col].stopTimer();
            game.getPlantLabels(row, col).setIcon(Plot.occupiedPlotIcon);
            array[row][col] = null;
            numOfPlants--;
        }
    }   

    /**
     * Fills a @param props Map with the properties of a plant in the @param row and @param col of the array
     * Filling an array happens through calling the getProperties function of the plant 
     */
    public void fillPropList(Map<String, String> props, int row, int col) {
        if(row >= ROWS || col >= COLS)
            return;
        array[row][col].getProperties(props);
    }

    /**
     * Initiates saving sequence. A new JFileChooser is created where the location is already added, only
     * the name of the save should be given. Note that the location could be changed to but it is not
     * adviced to save elsewhere
     * It writes this Plot object with ObjectOutputStream to a new file
     */
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
    }

    /**
     * This is a helper function to initSave to set the time at save to each planted fruit.
     * The time at save is to ensure that when the game is loaded each individual plant continues
     * its growing from the time where it was when the save occured.
     */
    public void saveElapsedTime() {
        for (int i = 0; i < ROWS; i++) 
            for (int j = 0; j < COLS; j++) 
                if(isPlantInPlot(i, j))
                    array[i][j].setTimeAtSave();
    }

    /**
     * Created a JFileChooser at the default location of the saves. It is to load a file that stores the Plot object.
     * If the plot class is modified after a game state is saved, the load will throw an error
     */
    public void initLoad() {
        File saveLocation = new File(System.getProperty("user.dir").toString() + File.separator + "saves");
        if(!saveLocation.exists()){
            game.showMessage("There is no file to load");
            return; 
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
            updateGUI();
        }
    }
    /**
     * This is a helper function for the copyDataToThis function, it should be called to all existing plants in the @param input Plot
     * It calls the Plant's "copy constructor", passing on the input Plot for it to copy all data. 
     * @param row @param col are the location of the plant in array
     */
    public void loadPlants(int row, int col, Plot input) {
        if(row >= ROWS || col >= COLS)
            return;
        switch (input.getarray()[row][col].getType()) {
            case APPLE:
                array[row][col] = new Apple(this, input.getarray()[row][col]);
                updatePlantIcon(row, col);
                break;

            case GRAPE:
                array[row][col] = new Grape(this, input.getarray()[row][col]);
                updatePlantIcon(row, col);
                break;

            case BANANA:
                array[row][col] = new Banana(this, input.getarray()[row][col]);
                updatePlantIcon(row, col);
                break;

            case PINEAPPLE:
                array[row][col] = new Pineapple(this, input.getarray()[row][col]);
                updatePlantIcon(row, col);
                break;
            default:
                break;
        }
    }
    
    /**
     * It copies all the data of the @param input Plot to this instance of the class. 
     * It calls loadPlants and the game's initFromLoad thus updating the graphic elements too.
     */
    private void copyDataToThis(Plot input) {
        this.numOfFruits = input.numOfFruits;
        this.numOfParcels = input.numOfParcels;
        this.numOfPlants = input.numOfPlants;
        this.plotPrice = input.plotPrice;
        this.game.initFromLoad(input);
        //____Set plants___
        for (int i = 0; i < Plot.getROWS(); i++) {
            for (int j = 0; j < Plot.getCOLS(); j++) {
                if(input.isPlantInPlot(i, j))
                    loadPlants(i, j, input);
            }
        } 
    }
}
