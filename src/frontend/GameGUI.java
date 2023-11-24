package frontend;

import gamelogic.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

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

    public void addItemToPlantMenu(JMenuItem menu, PlantType pType, int row, int col) {
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

    public void fruitAnimation(int row, int col) {
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

    }

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
    
    public void initFromLoad(Plot input) {
        //____Set plots____
        updateBuyPlotButtonText();
        updateFruitCounterLabel();
        for (int i = 1; i <= input.getParcels(); i++) {
            increaseParcelLabel((int)((i-1) / Plot.getCOLS()), (int)(i-1) % Plot.getCOLS());
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
