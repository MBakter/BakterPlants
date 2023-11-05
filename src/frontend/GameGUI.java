package frontend;

import gamelogic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameGUI {
    private JFrame frame;
    private JPanel plotPanel;
    private JPanel topPanel;
    private JPanel marketPanel;
    private JLabel fruitCounterLabel;
    private JButton buyPlotButton;
    private JLabel[][] plantLabels;

    private Plot plot;

    private int WIDTH = 1400;
    private int HEIGHT = 1200;

    public void updateFruitCounterLabel() {
        fruitCounterLabel.setText("Fruits: " + plot.getFruits());
    }

    public void addToFrame(Component component) {
        frame.add(component);
    }
    public void addToPlotPanel(Component component) {
        plotPanel.add(component);
    }
    
    //If we need to modify the plantlabel in x,y
    public JLabel getPlantLabels(int row, int col) {
        return plantLabels[row][col];
    }
    
    public GameGUI() {
        createFrame();        
        createPlotPanel();

        plot = new Plot();
        
        createTopPanel();
        createMarketPanel();

        frame.add(plotPanel, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(marketPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private void createFrame() {
        frame = new JFrame("BakterPlants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        //frame.setLayout(new BorderLayout());
    }

    //Grid paramters: WIDTH: 1400, HEIGHT: 1200. Each grid png is 200*200 
    //TODO: Lehetne, hogy a plantlabels-t a plot classból hívjuk. Mondjuk a plotban van egy 6*7-es plant és egy 6*7-es plantlabel attribútum
    private void createPlotPanel() {
        plotPanel = new JPanel(new GridLayout(6, 7));
        plantLabels = new JLabel[6][7];
        ImageIcon plantIcon = new ImageIcon("grass.png");

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                plantLabels[row][col] = new JLabel();
                plantLabels[row][col].setIcon(plantIcon);
                plantLabels[row][col].setBackground(Color.black);
                plotPanel.add(plantLabels[row][col]);
            }
        }
    }

    private void createTopPanel() {
        topPanel = new JPanel();
        fruitCounterLabel = new JLabel("Fruits: " + plot.getFruits());
        topPanel.add(fruitCounterLabel);
    }

    private void createMarketPanel() {
        marketPanel = new JPanel(new GridLayout(3, 1));
        buyPlotButton = new JButton("Extend Plot: " + plot.getPlotPrice());

        //If a new plot is bought, 
        buyPlotButton.addActionListener(e -> { 
            if(plot.buyPlot()) {
                buyPlotButton.setText("Buy Plot: " + plot.getPlotPrice());
                updateFruitCounterLabel(); //Update number of fruits
                increaseParcelLabel();
            }
        });
        marketPanel.add(buyPlotButton);
    }

    public void increaseParcelLabel() {

        //plot.parcels is already increased by one when this is called
        int currentRow = (int)((plot.getParcels()-1) / 7);
        int currentCol = (int)(plot.getParcels()-1) % 7;

        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem plantMenu = new JMenu("Plant the seed");
        popMenu.add(plantMenu);

        //For all plantTypes, add to popMenu
        for (PlantType pType : PlantType.values()) {
            if(pType != PlantType.NONE)
                addItemToPlantMenu(plantMenu, pType, currentRow, currentCol);
        }

        popMenu.add(new JMenuItem("Dig up the ground"));

        plantLabels[currentRow][currentCol].addMouseListener(new MouseListener() {
            
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    popMenu.show(plantLabels[currentRow][currentCol], e.getX(), e.getY());
                }
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) { /* TODO:Highlight the object  */}
            public void mouseExited(MouseEvent e){}
        });

    }

    private void addItemToPlantMenu(JMenuItem menu, PlantType pType, int row, int col) {
        JMenuItem item = new JMenuItem(PlantType.convertToString(pType));

        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plot.plantPlant(row, col, pType);
            }
            
        });

        menu.add(item);

    }
}
