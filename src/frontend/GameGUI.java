package frontend;

import gamelogic.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;

public class GameGUI implements Serializable {
    private static final long serialVersionUID = 2225901656906345936L;
    private JFrame frame;
    private JPanel plotPanel;
    private JPanel topPanel;
    private JPanel marketPanel;
    private JLabel fruitCounterLabel;
    private JButton buyPlotButton;
    private JLabel[][] plantLabels;

    private Plot plot;

    private static int WIDTH = Plot.getCOLS()*301 + 105;
    private static int HEIGHT = Plot.getROWS()*300 + 65;

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
    public void updateBuyPlotButtonText() {
        buyPlotButton.setText("Buy Plot: " + plot.getPlotPrice());
    }

    public void addToFrame(Component component) {
        frame.add(component);
    }
    public void addToPlotPanel(Component component) {
        plotPanel.add(component);
    }
    public JFrame getFrame() {
        return frame;
    }
    
    //If we need to modify the plantlabel in x,y
    public JLabel getPlantLabels(int row, int col) {
        return plantLabels[row][col];
    }

    private void createFrame() {
        frame = new JFrame("BakterPlants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        //frame.setLayout(new BorderLayout());
        frame.setIconImage(new ImageIcon("Graphics" + File.separator + "Apple.png").getImage());
    }
 
    private void createPlotPanel() {
        plotPanel = new JPanel(new GridLayout(Plot.getROWS(), Plot.getCOLS()));
        plotPanel.setBackground(new Color(77, 88, 99));
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
        marketPanel = new JPanel(new GridLayout(9, 1));
        buyPlotButton = new JButton("Buy Plot: " + plot.getPlotPrice());

        //If a new plot is bought, 
        buyPlotButton.addActionListener(e -> { 
            if(plot.getParcels() < Plot.getROWS()*Plot.getCOLS() && plot.buyPlot()) {
                updateBuyPlotButtonText();
                updateFruitCounterLabel(); //Update number of fruits
                increaseParcelLabel((int)((plot.getParcels()-1) / Plot.getCOLS()), (int)(plot.getParcels()-1) % Plot.getCOLS());
                if(plot.getParcels() >= Plot.getCOLS()*Plot.getROWS())
                    marketPanel.remove(buyPlotButton); //Ha megvan az összes plot akkor ne legyen ott a buyplots gomb
            }
        });
        marketPanel.add(buyPlotButton);
    }

    public void increaseParcelLabel(int currentRow, int currentCol) {

        //Change texture of the plot to grass
        plantLabels[currentRow][currentCol].setIcon(Plot.occupiedPlotIcon);

        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem plantMenu = new JMenu("Plant the seed");
        popMenu.add(plantMenu);

        //For all plantTypes, add to popMenu
        for (PlantType pType : PlantType.values()) {
            if(pType != PlantType.NONE)
                addItemToPlantMenu(plantMenu, pType, currentRow, currentCol);
        }

        JMenuItem fertilizeMI = new JMenuItem("Fertilize it");
        fertilizeMI.addActionListener(e->{
            plot.fertilizePlant(currentRow, currentCol);
        });
        popMenu.add(fertilizeMI);

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
        JMenuItem item = new JMenuItem(PlantType.convertToString(pType) + ":  " + pType.getPrice() + ".-");

        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plot.plantPlant(row, col, pType);
                //createProgressBar(row, col);
            }  
        });

        menu.add(item);
    }

    /* public void createProgressBar(int row, int col) {
        JProgressBar bar = new JProgressBar(0, 100);
        Timer t = new Timer(col, null)
        plantLabels[row][col].add(bar);
    } */

    //Update every visual element
    public void updateState() {
        //TODO: implement this
        updateFruitCounterLabel();

    }
    
    public void initFromLoad(Plot input) {
        //____Set plots____
        updateBuyPlotButtonText();
        updateFruitCounterLabel();
        for (int i = 1; i <= input.getParcels(); i++) {
            increaseParcelLabel((int)((i-1) / Plot.getCOLS()), (int)(i-1) % Plot.getCOLS());
        }

        //____Set plants___
         for (int i = 0; i < Plot.getROWS(); i++) {
            for (int j = 0; j < Plot.getCOLS(); j++) {
                if(input.isPlantInPlot(i, j))
                    plot.loadPlants(i, j, input);
            }
        }  
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
