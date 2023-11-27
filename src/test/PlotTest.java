package test;

import org.junit.Test;
import org.junit.Assert;

import frontend.*;
import gamelogic.*;

public class PlotTest {
	GameGUI game = new GameGUI();

	@Test
	public void testIncreaseFruit() {
		Plot plot = new Plot(game);
		
		int temp = plot.getFruits();
		plot.IncreaseFruit(100);
		Assert.assertEquals(temp + 100, plot.getFruits());
		
		temp = plot.getFruits();
		plot.IncreaseFruit(0);
		Assert.assertEquals(temp, plot.getFruits());
		
		temp = plot.getFruits();
		plot.IncreaseFruit(-100);
		Assert.assertEquals(temp, plot.getFruits());
	}

	@Test
	public void testDecreaseFruit() {
		Plot plot = new Plot(game);
		plot.setFruits(200);
		
		plot.decreaseFruit(100);
		Assert.assertEquals(100, plot.getFruits());
		
		plot.decreaseFruit(0);
		Assert.assertEquals(100, plot.getFruits());
		
		plot.decreaseFruit(-100);
		Assert.assertEquals(100, plot.getFruits());

		plot.setFruits(10);
		plot.decreaseFruit(30);
		Assert.assertEquals(plot.getFruits(), 10);

	}

	@Test
	public void testBuyPlot() {
		Plot plot = new Plot(game);
		int parcels = plot.getParcels();
		Assert.assertTrue(plot.buyPlot());
		Assert.assertEquals(plot.getParcels(), parcels + 1);
	}

	public void fillPlot(Plot pl) {
		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				pl.buyPlot();
				pl.plantPlant(i, j, PlantType.APPLE);
			}
		}
	}

	@Test
	public void testMaxPlot() {
		Plot pl = new Plot(game);
		
		pl.IncreaseFruit(10000000);
		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				pl.buyPlot();
				pl.plantPlant(i, j, PlantType.APPLE);
			}
		}
		Assert.assertFalse(pl.buyPlot());
	}

	@Test
	public void testPlanting() {
		Plot pl = new Plot(game);
		pl.buyPlot();
		pl.plantPlant(0, 0, PlantType.APPLE);
		Assert.assertNotEquals(null, pl.getarray()[0][0]);

		pl.plantPlant(1, 3, PlantType.APPLE);
		Assert.assertEquals(null, pl.getarray()[1][3]);

		pl.plantPlant(Plot.getROWS()-1, Plot.getCOLS()-1, PlantType.APPLE);
		Assert.assertEquals(null, pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1]);
	}

	public Plant createApple() {
		Plot pl = new Plot(game);
		return new Apple(pl);
	}

	@Test
	public void testInfuse() {
		Plant apple = createApple();
		int infusionPrice = apple.getInfusionPrice();
		apple.upgradePlant();
		Assert.assertEquals(2, apple.getLevel());
		Assert.assertNotEquals(infusionPrice, apple.getInfusionPrice());
	}

	@Test
	public void testFertilization() {
		Plant apple = createApple();
		int fertpr = apple.getFertilizerPrice();
		long t = apple.time;
		apple.speedUpPlant();
		Assert.assertEquals(t/2, apple.time);
		Assert.assertNotEquals(fertpr, apple.getFertilizerPrice());
	}

	@Test
	public void testPlantInfusion() {
		Plot pl = new Plot(game);
		pl.setFruits(1000);
		pl.buyPlot();
		pl.plantPlant(0, 0, PlantType.APPLE);
		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(i+1, pl.getarray()[0][0].getLevel());
			int tempfruits = pl.getFruits();
			pl.infusePlant(0, 0);
			Assert.assertEquals(tempfruits - 20 * (int)Math.pow(2, i), pl.getFruits());
		} 
		Assert.assertEquals(20 * (int)Math.pow(2, 4), pl.getarray()[0][0].getInfusionPrice());
		Assert.assertEquals(5, pl.getarray()[0][0].getLevel());

		int tempfruits = pl.getFruits();
		pl.infusePlant(0, 0);
		Assert.assertEquals(5, pl.getarray()[0][0].getLevel());
		Assert.assertEquals(tempfruits, pl.getFruits());
	}

	@Test
	public void testPlantFertilization() {
		Plot pl = new Plot(game);
		pl.setFruits(1000);
		pl.buyPlot();
		pl.plantPlant(0, 0, PlantType.APPLE);

		long t = pl.getarray()[0][0].time;

		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(t / (int)Math.pow(2, i), pl.getarray()[0][0].time);
			int tempfruits = pl.getFruits();
			pl.fertilizePlant(0, 0);
			Assert.assertEquals(tempfruits - 15 * (int)Math.pow(2, i), pl.getFruits());
		} 
		Assert.assertEquals(15 * (int)Math.pow(2, 4), pl.getarray()[0][0].getFertilizerPrice());
		Assert.assertEquals(Plant.minTime, pl.getarray()[0][0].time);

		pl.fertilizePlant(0, 0);
		Assert.assertEquals(Plant.minTime, pl.getarray()[0][0].time);
	}

	@Test
	public void stressTest() {
		Plot pl = new Plot(game);
		Plant basicApple = createApple(); 

		pl.setFruits(Integer.MAX_VALUE);
		fillPlot(pl);
		

		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				pl.fertilizePlant(i, j);
				pl.infusePlant(i, j);		
			}
		}

		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].getType(), PlantType.APPLE);
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].getLevel(), 2);
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].time, basicApple.time / 2);
			}
		}

		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				pl.fertilizePlant(i, j);
				pl.infusePlant(i, j);		
			}
		}

		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].getType(), PlantType.APPLE);
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].getLevel(), 3);
				Assert.assertEquals(pl.getarray()[Plot.getROWS()-1][Plot.getCOLS()-1].time, basicApple.time / 4);
			}
		}
		
	}

	@Test
	public void notEnoughFruitTest() {
		Plot pl = new Plot(game);
		pl.setFruits(PlantType.BANANA.getPrice()-1);
		pl.buyPlot();
		pl.plantPlant(0, 0, PlantType.BANANA);
		Assert.assertEquals(null, pl.getarray()[0][0]);

		pl.setFruits(PlantType.GRAPE.getPrice());
		pl.plantPlant(0, 0, PlantType.GRAPE);
		Assert.assertEquals(PlantType.GRAPE, pl.getarray()[0][0].getType());
		
		pl.setFruits(pl.getarray()[0][0].getFertilizerPrice()-1);
		pl.fertilizePlant(0, 0);

		pl.setFruits(pl.getarray()[0][0].getInfusionPrice()-1);
		pl.infusePlant(0, 0);

	}

	public void maxOutGame(Plot pl) {
		pl.setFruits(Integer.MAX_VALUE);
		for (int i = 0; i < Plot.getROWS(); i++) {
			for (int j = 0; j < Plot.getCOLS(); j++) {
				pl.buyPlot();
				pl.plantPlant(i, j, PlantType.PINEAPPLE);
				for (int j2 = 0; j2 < 4; j2++) 
					pl.infusePlant(i, j);
				while(pl.getarray()[i][j].time != Plant.minTime)
					pl.fertilizePlant(i, j);
			}
		}

	}

	@Test
	public void saveTest() {
		Plot pl = new Plot(game);
		maxOutGame(pl);
		pl.initSave();
	}
	
}
