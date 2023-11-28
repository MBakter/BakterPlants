package frontend;

import gamelogic.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameGUI {
    private JFrame frame;
    private JPanel plotPanel;
    private JPanel topPanel;
    private JPanel marketPanel;
    private JLabel fruitCounterLabel;
    private JButton buyPlotButton;
    private JLabel[][] plantLabels;

    private Plot plot;

    private static int WIDTH = Plot.getCOLS()*301 + 105;
    private static int HEIGHT = Plot.getROWS()*300 + 65 + 30;

    /**
     * The constructor creates the frame and its components by calling several helper functions in the class
     */
    public GameGUI() {
        createFrame();        
        createPlotPanel();

        plot = new Plot(this);
        
        createMenuBar();
        createMarketPanel();
        createTopPanel();

        frame.add(plotPanel, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(marketPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    /** 
     * Should be called, when the number of fruit changes in the plot.
     * This changes the label in the topPanel
    */
    public void updateFruitCounterLabel() {
        fruitCounterLabel.setText("Fruits: " + plot.getFruits());
    }

    /**
     * Should be called, when a new plot is bought.
     * This changes the label on the buyPlot button in the marketPanel
    */
    public void updateBuyPlotButtonText() {
        buyPlotButton.setText("Buy Plot: " + plot.getPlotPrice());
    }

    /**
     * Adds the @param component to the frame
     */
    public void addToFrame(Component component) {
        frame.add(component);
    }
    
    /**
     * Adds the @param component to the plotPanel
     */
    public void addToPlotPanel(Component component) {
        plotPanel.add(component);
    }
    
    /**
     * @return JFrame
     */
    public JFrame getFrame() {
        return frame;
    }
    
    /**
     * Should be called if we need to modify the plantlabel in row, col.
     * It returns the JLabel int row, col
     * @param row 
     * @param col
     * @return JLabel
     */
    public JLabel getPlantLabels(int row, int col) {
        if(row >= Plot.getROWS() || col >= Plot.getCOLS())
            return null;
        return plantLabels[row][col];
    }

    /**
     * Creates the frame
     */
    private void createFrame() {
        frame = new JFrame("BakterPlants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        //frame.setLayout(new BorderLayout());
        frame.setIconImage(new ImageIcon("Graphics" + File.separator + "Apple.png").getImage());
    }
 
    /**
     * Creates the plotPanel. This is an n*m grid where the plot can be managed
     */
    private void createPlotPanel() {
        plotPanel = new JPanel(new GridLayout(Plot.getROWS(), Plot.getCOLS()));
        plotPanel.setBackground(new Color(77, 80, 100));
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

    /**
     * Creates the topPanel. Sets the fruit counter to the starting value of plot's number of fruits.
     */
    private void createTopPanel() {
        topPanel = new JPanel(new GridLayout(0, 2));
        topPanel.setBackground(new Color(90, 80, 100));
        fruitCounterLabel = new JLabel(" Fruits: " + plot.getFruits());
        fruitCounterLabel.setFont(fruitCounterLabel.getFont().deriveFont(Font.BOLD, 20));
        fruitCounterLabel.setForeground(Color.WHITE);
        fruitCounterLabel.setPreferredSize(new Dimension(100, 30));

        topPanel.add(fruitCounterLabel, BorderLayout.CENTER);
    }

    /**
     * Creates the top menu bar. Adds a menu called Menu, and to it adds two menuItems, save and load.
     * They can be used to save/load the game. They each call plot's initsave/load function
     */
    private void createMenuBar() {
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
        frame.setJMenuBar(menuBar);
    }

    /**
     * Creates the market panel. It is a grid layout so that it can be easily expanded to more items to buy.
     * In the current state of the game, only a buy plot button is inside. It shows the price of a new plot,
     * initially 0. If it is pressed, calls the plot's buyPlot function and if it returns true, increases the
     * parcelLabels, thus calling the increaseParcelLabel function.
     */
    private void createMarketPanel() {
        marketPanel = new JPanel(new GridLayout(1, 1));
        marketPanel.setBackground(new Color(77, 80, 100));
        buyPlotButton = new JButton("Buy Plot: " + plot.getPlotPrice());
        buyPlotButton.setBackground(new Color(90, 80, 100));
        buyPlotButton.setForeground(Color.WHITE);

        //If a new plot is bought, 
        buyPlotButton.addActionListener(e -> { 
            if(plot.getParcels() < Plot.getROWS()*Plot.getCOLS() && plot.buyPlot()) {
                updateBuyPlotButtonText();
                updateFruitCounterLabel(); //Update number of fruits
                increaseParcelLabel((int)((plot.getParcels()-1) / Plot.getCOLS()), (int)(plot.getParcels()-1) % Plot.getCOLS());
                if(plot.getParcels() >= Plot.getCOLS()*Plot.getROWS())
                    marketPanel.remove(buyPlotButton); //If all plots is bought, buyPlot button should be removed
            }
        });
        marketPanel.add(buyPlotButton);
    }

    /**
     * This is called when a new plot is bought with the buy plot button. It creates an occupied plot by 
     * updating the parcelLabel in location @param currentRow and @param currentCol and adds the
     * functions to it, which can be accessed by right clicking on the component.
     */
    private void increaseParcelLabel(int currentRow, int currentCol) {
        if(currentRow >= Plot.getROWS() || currentCol >= Plot.getCOLS())
            return;

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
            int result = JOptionPane.showOptionDialog(
                null,
                "You pick up the shovel, but stop for a moment...\nDo you really want to start digging?",
                "Digging...",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Start digging", "Put down shovel"},
                "Yes"
            );
            if(result == JOptionPane.YES_OPTION)
                plot.digPlant(currentRow, currentCol);
        });
        popMenu.add(digMI);

        JMenuItem propertiesMI = new JMenuItem("Properties");
        propertiesMI.addActionListener(e -> {
            createPropertiesTable(currentRow, currentCol);
        });
        popMenu.add(propertiesMI);

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

    /**
     * It's a helper function for increase parcel label, creates a menuItem with one PlantType @param pType thus adding an option
     * to plant them. It should be called to all existing plantTypes to ensure that if a new plant is added to the game, 
     * this class is not needed to be modified.
     * Clicking on the name of the plant calls the plot's plantPlant function.
     * @param menu The MenuItem where all all plants are added
     * The plantLabel parameters:
     * @param row
     * @param col
    */
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

    /* public void fruitAnimation(int row, int col) {
        JLabel floatingLabel = new JLabel(new ImageIcon("Graphics" + File.separator + "Fruit.png"));
        plantLabels[row][col].add(floatingLabel, BorderLayout.CENTER);
        
        floatingLabel.setVisible(false);

        Timer timer = new Timer(2000, new ActionListener() {
            int elapsedTime = 0;
            public void actionPerformed(ActionEvent e) {
                if (elapsedTime < 2000) {
                    float percentComplete = (float) elapsedTime / 2000;
                    int newY = (int) (200 * (1 - percentComplete));
                    floatingLabel.setLocation(floatingLabel.getX(), newY);
                    floatingLabel.setVisible(true);
                    elapsedTime += 100;
                } else {
                    ((Timer) e.getSource()).stop();
                    floatingLabel.setVisible(false);
                }
            }
        });
        timer.start();
    } */

    /**
     * Creates a dialog consisting of a table with the properties of a plant on which it is called. 
     * These are queried through the plot and the plant class
     * @param row and @param col parameters are the locations in the plot's array to the current plant
     */
    public void createPropertiesTable(int row, int col) {
        if(!plot.isPlantInPlot(row, col)) {
            showMessage("There are no known properies of grass");
            return;
        }

        Map<String, String> propList = new LinkedHashMap<>();
        plot.fillPropList(propList, row, col);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Name of the property");
        model.addColumn("Value");

        for (Map.Entry<String, String> entry : propList.entrySet()) {
            model.addRow(new String[]{entry.getKey(), entry.getValue()});
        }

        JTable table = new JTable(model);
        
        table.getTableHeader().setReorderingAllowed(false);

        JDialog dialog = new JDialog();
        dialog.setTitle("Properties");
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(new JScrollPane(table));

        dialog.setSize(new Dimension(300, 180));
        dialog.setLocationRelativeTo(null); // Center the dialog
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setVisible(true);
        
    }
    
    /**
     * It gets a Plot @param input and updates the buyPlot price and calls the increaseParcelLabel to each occupies parcels
     * in the input Plot.
     */
    public void initFromLoad(Plot input) {
        //____Set plots____
        updateBuyPlotButtonText();
        for (int i = 1; i <= input.getParcels(); i++) {
            increaseParcelLabel((int)((i-1) / Plot.getCOLS()), (int)(i-1) % Plot.getCOLS());
        }
    } 

    /**
     * Creates an option pane with a @param message String. Should be used to throw a note or a warning to the user.
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
