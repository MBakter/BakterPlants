package frontend;

import gamelogic.*;
import javax.swing.*;
import java.awt.*;

public class GameGUI {
    private static JFrame frame;
    private static JPanel fruitPanel;
    private static JPanel topPanel;
    private static JPanel marketPanel;
    private static JLabel fruitCounterLabel;
    private static JButton buyAppleButton;
    private static JButton buyPlotButton;

    private PlantType currentyPlanting = PlantType.NONE;

    private Plot plot;

    private int WIDTH = 1400;
    private int HEIGHT = 1200;

    private void updateFruitCounterLabel() {
        fruitCounterLabel.setText("Fruits: " + plot.getFruits());
    }

    public static void addToFrame(Component component) {
        frame.add(component);
    }
    public static void addToFruitPanel(Component component) {
        fruitPanel.add(component);
    }

    public GameGUI() {
        
        
        frame = new JFrame("Tycoon Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);

        //frame.setLayout(new BorderLayout());

        fruitPanel = new JPanel(new GridLayout(6, 7));
        createPlantingGrid();

        plot = new Plot();
        
        topPanel = new JPanel();
        fruitCounterLabel = new JLabel("Fruits: " + plot.getFruits());
        topPanel.add(fruitCounterLabel);

        marketPanel = new JPanel(new GridLayout(3, 1));
        createMarketPanel();

        frame.add(fruitPanel, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(marketPanel, BorderLayout.EAST);

        

        frame.setVisible(true);
    }

    //Grid paramters: WIDTH: 1400, HEIGHT: 1200. Each grid png is 200*200 
    private void createPlantingGrid() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JLabel plantField = new JLabel();
                ImageIcon plantIcon = new ImageIcon("grass.png");
                plantField.setIcon(plantIcon);
                plantField.setBackground(Color.black);
                fruitPanel.add(plantField);
            }
        }
    }

    private void createMarketPanel() {
        buyAppleButton = new JButton("Buy Apple: " + plot.getApplePrice());
        buyAppleButton.addActionListener(e -> { 
            if(plot.buyApple()) {
                currentyPlanting = PlantType.APPLE;
                buyAppleButton.setText("Buy Apple: " + plot.getApplePrice());
            }
        });
        marketPanel.add(buyAppleButton);

        buyPlotButton = new JButton("Extend Plot: " + plot.getPlotPrice());
        buyPlotButton.addActionListener(e -> { if(plot.buyPlot()) buyPlotButton.setText("Buy Plot: " + plot.getPlotPrice());});
        marketPanel.add(buyPlotButton);
    }

    
}
