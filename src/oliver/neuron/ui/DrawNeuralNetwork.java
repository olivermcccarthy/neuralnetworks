package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import oliver.neuron.Cost;
import oliver.neuron.Layer;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;
import oliver.neuron.neonnumbers.NeonTrial;

/**
 * Draw neurons showing input weights and output values.
 * 
 * @author oliver
 *
 */
public class DrawNeuralNetwork extends JPanel {

	/**
	 * We can draw more than one network at a time
	 */
	static HashMap<String,DrawNeuralNetwork> neuronPanels = new HashMap<String,DrawNeuralNetwork>();

	/**
	 * The Network we are drawing
	 */
	NeuralNetwork neuralNetwork = null;
	
	/**
	 * Nuumber of batches remaining to be run. counst dont from what the user selects as number of batches before they click run 
	 */
	int numBatchesRemaining;
	
	/**
	 * Overall trialNumber increment by one every run
	 */
	int trialNumber =0;
    
	/**
     * User choice panel. Here they choose batch size etc and click run 
     */
	ControlPanel panel ;
	
	/**
	 * Overall frame
	 */
	private static JFrame frame;

	private static int SCREEN_SIZE=1000; 
	
	
	/**
	 * Type of input data. 1s and zeros or greyscale
	 */
	public enum PICTURE_TYPE {
		BINARY, GREYSCALE
	};

	/**
	 * Positive weights are represented by these red colors. 
	 */
	static Color[] shadesPositive = new Color[] { new Color(255, 0, 0), new Color(225, 125, 125),
			new Color(255, 255, 204) };
	
	/**
	 * Negative weights are represented by these blue colors. 
	 */
	static Color[] shadesNegative = new Color[] { new Color(0, 0, 105), new Color(0, 0, 255),
			new Color(153, 255, 255) };
	
	
	/**
	 * A panel for each Neuron being drawn
	 */
	HashMap<String, NeuronInAPanel> nueronPanels = new HashMap<String, NeuronInAPanel>();

	/**
	 * How large to paint each Neuron
	 */
	static final int neuronSizeInPixels = 80;
	
	static final int neuronSpaceHeight = neuronSizeInPixels + 40;
	/**
	 * Type of input dataFTrials are b
	 */
	PICTURE_TYPE pictureType;

	
	/**
	 * When there are more than 20 inputs to a neuron . The weights will be painted
	 * as a picture. Brightest color being highest weight This value will be the
	 * width of the picture.
	 */
	int pictureWidth = 28;

	/**
	 * How many times to blow up the image.
	 */
	int pictureScale = 1;

	// We will draw the input Image in the top corner;
	private int[][] inputImage = null;

	// Place the input panel on the left hand side
	private JPanel inputPanel = null;

	/**
	 * Trial we are displaying
	 */
	TrialInfo trialInfo;
	
	/**
	 * Info on runs in the batch ( Last 20) 
	 */
	JTable runInfoPane= new JTable();
	
	/**
	 * Info on each batch run ( Last 20) 
	 */
	JTable batchInfoPane= new JTable();
	
	/**
	 * Display some short help
	 */
	JTextPane helpPanel= null;
	
	/**
	 * Over light blue background color
	 */
	static final Color BGColor = new Color(230, 255, 255);
	
	/**
	 * Add help page and each Network we are painting as a tab
	 */
	static JTabbedPane tabbed = new JTabbedPane();
	
	
	/**
	 * We can draw more than one network at a time. This returns the Panel added by showNeurons
	 * @param name
	 * @return
	 */
	public static DrawNeuralNetwork getNeuronPanel(String name) {
		return neuronPanels.get(name);
	}
	
	
	/**
	 * Paint the input panel ( Usually showing a digit or a letter) 
	 * @param inputPanel
	 */
	public void setInputPanel(JPanel inputPanel) {
		this.inputPanel = inputPanel;
		this.add(this.inputPanel);
		Dimension prefr = inputPanel.getPreferredSize();
		inputPanel.setBounds(0, 330, prefr.width, prefr.height);
		inputPanel.setBackground(this.getBackground());
	}

	public void setInputImage( int[][] inputImage, int pictureScale, PICTURE_TYPE typeOfPicture) {
		this.inputImage = inputImage;
		pictureType = typeOfPicture;

	}
	
	public DrawNeuralNetwork(TrialInfo trialInfo) {
		this.trialInfo = trialInfo;
		panel = new ControlPanel(trialInfo);
		panel.setBounds(0,0,100,150);
		this.add(panel);
		setBackground(BGColor);
		
		helpPanel= new JTextPane();
		helpPanel.setText(trialInfo.getHelp());
		 // Column Names 
        String[] columnNames = { "Run", "Expected", "Got" }; 
  
        // Initializing the JTable 
        String [][] data = new String[1][3];
        data[0]= new String[]{"1","1","1"};
        MyTableModel model = new  MyTableModel(columnNames);
        runInfoPane= new JTable(model);
		
		JScrollPane scroll3 = new JScrollPane(helpPanel);
		scroll3.setBounds(150, 10, SCREEN_SIZE -200 , 100);
		this.add(scroll3);
		
		JScrollPane scroll2 = new JScrollPane(runInfoPane);
		runInfoPane.setFont(getFont().deriveFont(12.0f));
		
		
		helpPanel.setBackground(this.getBackground());
		scroll2.setBounds(200, 120,300 , 80);
		this.setLayout(null);
		String[] batchcolumnNames = { "Batch", "Run", "Correct", "%Correct" }; 
		  
        // Initializing the JTable 
     
        MyTableModel model2 = new MyTableModel(batchcolumnNames);
        batchInfoPane= new JTable(model2);
		JScrollPane scroll4 = new JScrollPane(batchInfoPane);
		batchInfoPane.setFont(getFont().deriveFont(12.0f));
		
		batchInfoPane.setBackground(this.getBackground());
		
		scroll4.setBounds(450, 120,300 , 80);
		this.add(scroll4);
		this.add(scroll2);
		

	}

	/**
	 * Paint the input image or the input panel on the left hand side of the screen. This shows the user the input image that is being evaluated
	 * @param g
	 * @param baseY
	 */
	protected void paintInputImage(Graphics g, int baseY) {

		
		if (inputPanel != null) {
			inputPanel.setBackground(this.getBackground());

			return;
		}
		if (inputImage != null) {
			int pictureHeight = inputImage.length;
			BufferedImage img = new BufferedImage(pictureWidth * pictureScale, pictureHeight * pictureScale,
					BufferedImage.TYPE_INT_ARGB);

			for (int h = 0; h < pictureHeight * pictureScale; h++) {
				for (int w = 0; w < pictureWidth; w++) {
					if (pictureType == PICTURE_TYPE.GREYSCALE) {
						int greyScale = 255 - inputImage[h / pictureScale][w];
						for (int z = 0; z < pictureScale; z++) {
							img.setRGB(w * pictureScale + z, h, new Color(greyScale, greyScale, greyScale).getRGB());
						}
					} else if (pictureType == PICTURE_TYPE.BINARY) {
						int black = inputImage[h / pictureScale][w];
						if (black == 1) {
							for (int z = 0; z < pictureScale; z++) {
								img.setRGB(w * pictureScale + z, h, Color.RED.getRGB());
							}
						}
					}

				}
			}

			g.drawImage(img, 0, baseY, pictureWidth * pictureScale, pictureHeight * pictureScale, new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});

		}
	}

	public static final int NUM_NURONS_TODRAW=20;
	
	
	/**
	 * Draw input Image in top corner Draw each layer. Starting with input layer on
	 * the Left.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int baseX = -100;
		int baseY = 220;

		Font existing = g.getFont();
		//g.setFont(g.getFont().deriveFont(12.0f));
         this.setFont(g.getFont().deriveFont(12.0f));
		
        
		int maxLevelSize = 0;

		showLegend(g, 20, 200);
		List<Layer> layers = this.neuralNetwork.getLayers();
		for (Layer layer : layers) {
			if (layer.getNeurons().size() > NUM_NURONS_TODRAW) {
				continue;
			}
			int size = layer.getNeurons().size();
			if (size > maxLevelSize) {
				maxLevelSize = size;
			}
		}

		

		int diffX = 350;
	
	   
		
		

		

		int diffY = 0;
		
		paintInputImage(g, 400);
		for (Layer layer : layers) {
			if (layer.getNeurons().size() > NUM_NURONS_TODRAW) {
				continue;
			}

			

			diffY = 0;
			
				for (Neuron nu : layer.getNeurons()) {

					NeuronInAPanel.placeNuronOnScreen(nu, this,  baseX + diffX,  baseY + diffY, neuronSizeInPixels);
					
					diffY += neuronSpaceHeight;
				}

			
			baseX += diffX;

		}

	}

	public static String formatDouble(double value) {

		String doubleStr = "" + (Math.floor(value * 1000) / 1000);

		return doubleStr;
	}

	

	

	/**
	 * Fade colour based of difference of LOg fade from yellow to red
	 *
	 */
	protected static Color faderYellowToRed(double maxValue, double minValue, double currentValue) {

		if (maxValue == 0 && minValue == 0) {
			return shadesNegative[0];
		}
		if (currentValue < 0) {
			return faderYellowToRedMinSide(minValue * -1, currentValue * -1);
		}
		double fraction = currentValue / maxValue;
		if (fraction > 0.5) {
			return shadesPositive[0];
		}
		if (fraction > 0.2) {
			return shadesPositive[1];
		}
		return shadesPositive[2];
	}

	private static Color faderYellowToRedMinSide(double minValue, double currentValue) {

		double fraction = currentValue / minValue;
		if (fraction > 0.5) {
			return shadesNegative[0];
		}
		if (fraction > 0.2) {
			return shadesNegative[1];
		}
		return shadesNegative[2];
	}

	private void showLegend(Graphics g, int x, int y) {

		showLegend(g, x, y, shadesPositive[0], "50 -> 100% of max weight");
		showLegend(g, x, y + 20, shadesPositive[1], "20 -> 50% of max weight ");
		showLegend(g, x, y + 40, shadesPositive[2], "0 -> 20% of max weight ");
		showLegend(g, x, y + 60, shadesNegative[2], "0 -> 20% of min weight ");
		showLegend(g, x, y + 80, shadesNegative[1], "20 -> 50% of min weight");
		showLegend(g, x, y + 100, shadesNegative[0], "50 -> 100% of min weight ");

	}

	private void showLegend(Graphics g, int x, int y, Color color, String message) {

		char[] chararr = message.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, x, y + 15);
		g.setColor(color);
		g.fillRect(x - 20, y, 20, 20);

	}

	
	

	public void doRedraw() {
		getTopLevelAncestor().revalidate();
		getTopLevelAncestor().repaint();
	}

	
	
    int sleepTimeMs;
	public int getSleepTime() {
		sleepTimeMs = this.panel.getSleepTime();
		return this.panel.getSleepTime();
	}

	public int getNumBatches() {
		numBatchesRemaining = this.panel.getNumBatches();
		return numBatchesRemaining;
	}

	public double getLearningRate() {
		return this.panel.getLearningRate();
	}

	public int getNumPerBatch() {
		
		return this.panel.getNumPerBatch();
	}

	public void waitForUserClick(TrialInfo info, double[] expected, double[] got) {
		this.waitForUserClick(info, expected, got, true, true);
	}
	
	public void waitForUserClick(TrialInfo info, double[] expected, double[] got, boolean sleep, boolean redraw) {
		
		
		String message= String.format("%s, %s ", 
				Neuron.toString(expected), Neuron.toString(got));
		this.waitForUserClick(info,message,sleep,redraw);
	}
	public void waitForUserClick(TrialInfo info, String message, boolean sleep, boolean redraw) {

		trialNumber ++;
		setMessage(String.format("%s,%s", trialNumber,message));
        if(!redraw) {
        	return;
        }
	
		frame.getContentPane().revalidate();
		forceRedraw();
		frame.getContentPane().repaint();
		
		if (sleep) {
			try {
				Thread.sleep(sleepTimeMs);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (numBatchesRemaining <= 0) {
			this.panel.waitForMe();
			getSleepTime();
			getNumBatches();

			info.numPerBatch = getNumPerBatch();
			info.setLearningRate(getLearningRate());
		
		}

	}

	/**
	 * Paint a picture of Neurons in all the layers
	 * 
	 * @param pictureWidth
	 * @param pictureScale
	 */
	public static DrawNeuralNetwork showNeurons(TrialInfo trialInfo,NeuralNetwork neuralNetwork, int pictureWidth, int pictureScale) {
	
		DrawNeuralNetwork neuronPanel = new DrawNeuralNetwork(trialInfo);
		JScrollPane scroll = new JScrollPane(neuronPanel);
		neuronPanel.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE*2));
		tabbed.add(trialInfo.getName(),scroll);
		if (frame == null) {
			frame = new JFrame();
			frame.setSize(SCREEN_SIZE, SCREEN_SIZE);
            
			
			

			
			frame.setVisible(true);
		
			
			HelpPanel helpPanel = new HelpPanel("ui");
			JScrollPane scroll2 = new JScrollPane( helpPanel);
			
			
			tabbed.add("Help",scroll2);
            tabbed.add(trialInfo.getName(), scroll);
			frame.getContentPane().add(tabbed);
		}
		neuronPanel.neuralNetwork = neuralNetwork;
		neuronPanels.put(trialInfo.getName(), neuronPanel);
		frame.repaint();

		return neuronPanel;

	}

	public void waitForUserClick(TrialInfo info, double expected, int got, boolean sleep, boolean redraw) {
		// TODO Auto-generated method stub
		this.waitForUserClick(info, new double[] { expected }, new double[] { got }, sleep,redraw);

	}

	
	
	int batchesRun =0;
	public void addCost(Cost theCost) {
		this.addCost(theCost,false);
	}
	public void addCost(Cost theCost, boolean update) {
		if(!update) {
		   batchesRun ++;
		}
		if(theCost.numTuples == 0) {
			theCost.numTuples =1;
		}
		int percent = (((theCost.numTuples-theCost.numWrong) * 10000)/theCost.numTuples);
		double p = percent;
		p = p/100;
		String messC = String.format("%s, %s, %s, %s ", batchesRun,theCost.numTuples, theCost.numTuples-theCost.numWrong,p);
		
		
		this.setMessage(this.batchInfoPane, messC, update);
	
	}
	public  void setMessage(String message) {
		this.setMessage(this.runInfoPane, message,false);
	}
	public  void setMessage(JTable infoPane,String message, boolean update) {
		// DefaultTableModel model = new DefaultTableModel();
	//	 this.runInfoPane.setModel(model);
		 
		MyTableModel model =(MyTableModel)infoPane.getModel();
		
		if(!message.startsWith("Click")) {
			
			if(update) {
				model.updateRow(message.split(","));
			}else {
				model.addRow(message.split(","));
			}
				
			
			
			 
			infoPane.repaint();
		}
	}
	
	public void run(TrialInfo trainer) throws Exception {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
				trainer.numPerBatch =1;
				trainer.nextBatch(neuralNetwork);
				
				waitForUserClick( trainer, "Click run to start ", false,true);
				while(true) {
					
					
					while(numBatchesRemaining > 0) {
						trainer.nextBatch(neuralNetwork);
						numBatchesRemaining --;
						
					}
					waitForUserClick( trainer, "Click run to start ", false,true);
				}
				}catch (Throwable t) {
					t.printStackTrace();
				}
				
			}};
		Thread t = new Thread(r);
		t.start();

	}
	public void forceRedraw() {
		for(NeuronInAPanel nuPanel: this.nueronPanels.values()) {
			nuPanel.forceRedraw();
		}
	}
}