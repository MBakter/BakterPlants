package frontend;

import gamelogic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI {
    private JFrame frame;
    private JPanel plotPanel;
    private JPanel topPanel;
    private JPanel marketPanel;
    private JLabel fruitCounterLabel;
    private JButton buyPlotButton;
    private JLabel[][] plantLabels;

    private Plot plot;

    private static int WIDTH = Plot.getCOLS()*200 + 150;
    private static int HEIGHT = Plot.getROWS()*200 + 75;

    public GameGUI() {
        createFrame();        
        createPlotPanel();

        plot = new Plot(this);
        
        createTopPanel();
        createMarketPanel();

        frame.add(plotPanel, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(marketPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

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

    private void createFrame() {
        frame = new JFrame("BakterPlants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(true);
        //frame.setLayout(new BorderLayout());
    }

    //Each grid png is 200*200 
    private void createPlotPanel() {
        plotPanel = new JPanel(new GridLayout(Plot.getROWS(), Plot.getCOLS()));
        plantLabels = new JLabel[Plot.getROWS()][Plot.getCOLS()];

        for (int row = 0; row < Plot.getROWS(); row++) {
            for (int col = 0; col < Plot.getCOLS(); col++) {
                plantLabels[row][col] = new JLabel();
                plantLabels[row][col].setIcon(Plot.plotIcon);
                plantLabels[row][col].setBackground(Color.black);
                plotPanel.add(plantLabels[row][col]);
            }
        }
    }

    private void createTopPanel() {
        topPanel = new JPanel(new GridLayout(1, 3));

        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Menu");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        save.addActionListener(e -> {
                plot.initSave();
            });
        load.addActionListener(e -> {
                plot.initLoad();
            });
        mainMenu.add(save);
        mainMenu.add(load);
        menuBar.add(mainMenu);

        fruitCounterLabel = new JLabel("Fruits: " + plot.getFruits());

        topPanel.add(menuBar);
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
        int currentRow = (int)((plot.getParcels()-1) / Plot.getCOLS());
        int currentCol = (int)(plot.getParcels()-1) % Plot.getCOLS();

        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem plantMenu = new JMenu("Plant the seed");
        popMenu.add(plantMenu);

        //For all plantTypes, add to popMenu
        for (PlantType pType : PlantType.values()) {
            if(pType != PlantType.NONE)
                addItemToPlantMenu(plantMenu, pType, currentRow, currentCol);
        }

        JMenuItem infuseMI = new JMenuItem("Infuse it");
        infuseMI.addActionListener(e->{
            plot.infusePlant(currentRow, currentCol);
        });
        popMenu.add(infuseMI);

        JMenuItem digMI = new JMenuItem("Dig up the ground");
        digMI.addActionListener(e->{
            plot.digPlant(currentRow, currentCol);
        });
        popMenu.add(digMI);

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

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
