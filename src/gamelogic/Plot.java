package gamelogic;

public class Plot {
    private Plant[][] array;
    private int avaliableParcels;
    private int numberOfFruits;
    private int applePrice;
    private int plotPrice;

    public Plot() {
        array = new Plant[7][6];
        avaliableParcels = 0;
        numberOfFruits = 10000000;
        applePrice = 0; //TODO: Make this a Set collection so it iterates through every plantType thus gaining the price from all Plants
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

    //Make this method buyFruit, and the type in its argument
    public boolean buyApple() {
        if(numberOfFruits < applePrice && avaliableParcels <= 0)
            return false;
        
        //TODO: Initiate buy apple sequence: Choose a plot

        if(applePrice == 0)
            applePrice = 1;
        numberOfFruits -= applePrice;
        applePrice *= 1.5; //Mindig a másfélszeresébe kerül TODO: Kitalálni egy jobb "emelőfüggvényt" az 'y(x) = 1.5x' -nél
        return true;
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

    //Ide lehetne, hogy PlantType típust kap és úgy hív meg egy static fv-t pl. Apple.plant();
    public void plantPlant(int row, int col, PlantType plant) {
        switch (plant) {
            case APPLE:
                array[row][col] = new Apple(); //TODO Implement these!!!!!!!!
                break;
            case GRAPE:
                array[row][col] = new Grape();
                break;
            case BANANA:
                array[row][col] = new Banana();
                break;
            case PINEAPPLE:
                array[row][col] = new Pineapple();
                break;
            default:
                break;
        }
         
    }
}
