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
	static HashMap<String, DrawNeuralNetwork> neuronPanels = new HashMap<String, DrawNeuralNetwork>();

	/**
	 * The Network we are drawing
	 */
	NeuralNetwork neuralNetwork = null;

	
	/**
	 * User choice panel. Here they choose batch size etc and click run
	 */
	public ControlPanel controlPanel;

	ColorLegend colorLegend = new ColorLegend();
	/**
	 * Overall frame
	 */
	private static JFrame frame;

	private static int SCREEN_SIZE = 1000;

	


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
	 * We will draw the inputPanel on the left
	 */
	private JPanel inputPanel = null;

	/**
	 * Trial we are displaying
	 */
	TrialInfo trialInfo;

	/**
	 * Info on runs in the batch ( Last 20)
	 */
	JTable runInfoPane = new JTable();

	/**
	 * Info on each batch run ( Last 20)
	 */
	JTable batchInfoPane = new JTable();

	/**
	 * Display some short help
	 */
	JTextPane helpPanel = null;

	/**
	 * Over light blue background color
	 */
	static final Color BGColor = new Color(230, 255, 255);

	/**
	 * Add help page and each Network we are painting as a tab
	 */
	static JTabbedPane tabbed = new JTabbedPane();

	/**
	 * We can draw more than one network at a time. This returns the Panel added by
	 * showNeurons
	 * 
	 * @param name
	 * @return
	 */
	public static DrawNeuralNetwork getNeuronPanel(String name) {
		return neuronPanels.get(name);
	}

	/**
	 * Paint the input panel ( Usually showing a digit or a letter)
	 * 
	 * @param inputPanel
	 */
	public void setInputPanel(JPanel inputPanel) {
		this.inputPanel = inputPanel;
		this.add(this.inputPanel);
		Dimension prefr = inputPanel.getPreferredSize();
		inputPanel.setBounds(0, 330, prefr.width, prefr.height);
		inputPanel.setBackground(this.getBackground());
	}

	public void setInputImage(int[][] inputImage, ImageInPanel.PICTURE_TYPE typeOfPicture) {
		int pictureScale = 100/inputImage.length;
		if(this.inputPanel == null) {
			JPanel inputPanel = new ImageInPanel();
			inputPanel.setPreferredSize(new Dimension(100,100));
			setInputPanel(inputPanel);
			
		}
		((ImageInPanel)this.inputPanel).setInfo(inputImage, pictureScale, typeOfPicture);
		
		

	}

	public DrawNeuralNetwork(TrialInfo trialInfo) {
		this.trialInfo = trialInfo;
		controlPanel = new ControlPanel(trialInfo);
		controlPanel.setBounds(0, 0, 100, 150);
		this.add(controlPanel);
		setBackground(BGColor);

		helpPanel = new JTextPane();
		helpPanel.setText(trialInfo.getHelp());
		// Column Names
		String[] columnNames = { "Run", "Expected", "Got" };

		// Initializing the JTable
		String[][] data = new String[1][3];
		data[0] = new String[] { "1", "1", "1" };
		MyTableModel model = new MyTableModel(columnNames);
		runInfoPane = new JTable(model);

		JScrollPane scroll3 = new JScrollPane(helpPanel);
		scroll3.setBounds(150, 10, SCREEN_SIZE - 200, 100);
		this.add(scroll3);

		JScrollPane scroll2 = new JScrollPane(runInfoPane);
		runInfoPane.setFont(getFont().deriveFont(12.0f));

		helpPanel.setBackground(this.getBackground());
		scroll2.setBounds(200, 120, 300, 80);
		this.setLayout(null);
		String[] batchcolumnNames = { "Batch", "Run", "Correct", "%Correct" };

		// Initializing the JTable

		MyTableModel model2 = new MyTableModel(batchcolumnNames);
		batchInfoPane = new JTable(model2);
		JScrollPane scroll4 = new JScrollPane(batchInfoPane);
		batchInfoPane.setFont(getFont().deriveFont(12.0f));

		batchInfoPane.setBackground(this.getBackground());

		scroll4.setBounds(510, 120, 300, 80);
		this.add(scroll4);
		this.add(scroll2);
		colorLegend.setBounds(10,200,200,200);
		this.add(colorLegend);
	}

	

	public static final int NUM_NURONS_TODRAW = 20;

	/**
	 *  DRaw each layer on the screen. Input Layer on the left. Output Layer on the right<br>
	 *  Also place the input Image on the very left hand side of the screen
	 * the Left.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int baseX = -100;
		int baseY = 220;
		this.setFont(g.getFont().deriveFont(12.0f));
		int maxLevelSize = 0;
		
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
		
		for (Layer layer : layers) {
			if (layer.getNeurons().size() > NUM_NURONS_TODRAW) {
				continue;
			}
			diffY = 0;
			for (Neuron nu : layer.getNeurons()) {
				placeNuronOnScreen(nu, this, baseX + diffX, baseY + diffY, neuronSizeInPixels);
				diffY += neuronSpaceHeight;
			}
			baseX += diffX;

		}

	}

	public static String formatDouble(double value) {

		String doubleStr = "" + (Math.floor(value * 1000) / 1000);

		return doubleStr;
	}

	

	public void doRedraw() {
		getTopLevelAncestor().revalidate();
		getTopLevelAncestor().repaint();
	}



	
	

	/**
	 * Add the message to to runInfoTable. Then sleep ( Allowing user to view results/ make choice)
	 * Once the numBatchesRemaining reaches zero. Stop trial and wait for user to click run again 
	 * @param info
	 * @param message
	 * @param sleep
	 * @param redraw
	 */
	public void waitForUserClick(TrialInfo info, String message, boolean sleep, boolean redraw) {

		info.setRunCounter(info.getRunCounter() +1);
		updateRunInfo(String.format("%s,%s", info.getRunCounter(), message));
		if (!redraw) {
			return;
		}

		frame.getContentPane().revalidate();
		forceRedraw();
		frame.getContentPane().repaint();

		if (sleep) {
			try {
				Thread.sleep(info.getSleepTimeMs());
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (info.getNumBatchesRemaining() <= 0) {
			this.controlPanel.waitForMe();
			

			info.numRunsPerBatch = this.controlPanel.getNumBatches();
			info.setLearningRate(this.controlPanel.getLearningRate());

		}

	}
	/**
	 * Wrapper method 
	 */
	public void waitForUserClick(TrialInfo info, double[] expected, double[] got) {
		this.waitForUserClick(info, expected, got, true, true);
	}

	/**
	 * Wrapper method 
	 */
	public void waitForUserClick(TrialInfo info, double[] expected, double[] got, boolean sleep, boolean redraw) {

		String message = String.format("%s, %s ", Neuron.toString(expected), Neuron.toString(got));
		this.waitForUserClick(info, message, sleep, redraw);
	}
	
	
	/**
	 * Wrapper method 
	 */
	public void waitForUserClick(TrialInfo info, double expected, int got, boolean sleep, boolean redraw) {
		// TODO Auto-generated method stub
		this.waitForUserClick(info, new double[] { expected }, new double[] { got }, sleep, redraw);

	}
	
	/**
	 * Paint a picture of Neurons in all the layers
	 * 
	 * @param pictureWidth
	 * @param pictureScale
	 */
	public static DrawNeuralNetwork showNeurons(TrialInfo trialInfo, NeuralNetwork neuralNetwork, int pictureWidth,
			int pictureScale) {

		DrawNeuralNetwork neuronPanel = new DrawNeuralNetwork(trialInfo);
		JScrollPane scroll = new JScrollPane(neuronPanel);
		neuronPanel.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE * 2));
		tabbed.add(trialInfo.getName(), scroll);
		if (frame == null) {
			frame = new JFrame();
			frame.setSize(SCREEN_SIZE, SCREEN_SIZE);

			frame.setVisible(true);

			HelpPanel helpPanel = new HelpPanel("ui");
			JScrollPane scroll2 = new JScrollPane(helpPanel);

			tabbed.add("Help", scroll2);
			tabbed.add(trialInfo.getName(), scroll);
			frame.getContentPane().add(tabbed);
		}
		neuronPanel.neuralNetwork = neuralNetwork;
		neuronPanels.put(trialInfo.getName(), neuronPanel);
		frame.repaint();

		return neuronPanel;

	}

	

	

	

	/**
	 * Update the Batches info table with batchesRun, NumTuples, numCorrect, %correct
	 * Correct
	 * 
	 * @param theCost
	 * @param update. If true update current batches info. Otherwise add new batch
	 */
	public void updateBatchInfo(Cost theCost, boolean update) {
		if (!update) {
			this.trialInfo.setBatchesRun(this.trialInfo.getBatchesRun() +1);
		}
		if (theCost.numRuns == 0) {
			theCost.numRuns = 1;
		}
		int percent = (((theCost.numRuns - theCost.numWrong) * 10000) / theCost.numRuns);
		double p = percent;
		p = p / 100;
		String messC = String.format("%s, %s, %s, %s ", this.trialInfo.getBatchesRun(), theCost.numRuns, theCost.numRuns - theCost.numWrong,
				p);

		this.addStatsToTable(this.batchInfoPane, messC, update);

	}

	
	public void updateRunInfo(String message) {
		this.addStatsToTable(this.runInfoPane, message, false);
	}

	public void addStatsToTable(JTable infoPane, String message, boolean update) {
		// DefaultTableModel model = new DefaultTableModel();
		// this.runInfoPane.setModel(model);

		MyTableModel model = (MyTableModel) infoPane.getModel();

		if (!message.startsWith("Click")) {

			if (update) {
				model.updateRow(message.split(","));
			} else {
				model.addRow(message.split(","));
			}

			infoPane.repaint();
		}
	}

	/**
	 * Infinite Loop. Waits for user to click run and then runs the selected number of batches
	 * If user click stop during the run. Run will stop and user will have to click run again
	 * @param trainer
	 * @throws Exception
	 */
	public void run(TrialInfo trainer) throws Exception {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					trainer.numRunsPerBatch = 1;
					trainer.nextBatch(neuralNetwork);

					waitForUserClick(trainer, "Click run to start ", false, true);
					while (true) {

						while (trainer.getNumBatchesRemaining() > 0) {
							trainer.nextBatch(neuralNetwork);
							trainer.setNumBatchesRemaining(trainer.getNumBatchesRemaining() -1);

						}
						waitForUserClick(trainer, "Click run to start ", false, true);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}

			}
		};
		Thread t = new Thread(r);
		t.start();

	}

	public void forceRedraw() {
		for (NeuronInAPanel nuPanel : this.nueronPanels.values()) {
			nuPanel.forceRedraw();
		}
	}

	/**
	 * Create a panel for a Neuron showing Neuron output. PLus a color coded image of
	 * its weights
	 * 
	 * @param neuron
	 * @param parent
	 * @param baseX
	 * @param baseY
	 * @param neuronSizeInPixels
	 */
	public static void placeNuronOnScreen(Neuron neuron, DrawNeuralNetwork parent, int baseX, int baseY,
			int neuronSizeInPixels) {
		NeuronInAPanel existing = parent.nueronPanels.get(neuron.getName());

		if (existing == null) {
			existing = new NeuronInAPanel(neuron);
			parent.nueronPanels.put(neuron.getName(), existing);
			parent.add(existing);
			existing.setBackground(parent.getBackground());
			// existing.setBorder(BorderFactory.createBevelBorder(1));
		}
	
		existing.neuron = neuron;
		existing.widthOfPanelInPixels = neuronSizeInPixels * 5 / 2;
		existing.setBounds(baseX, baseY, existing.widthOfPanelInPixels, neuronSizeInPixels + 30);
		existing.setCords(neuronSizeInPixels);
		existing.repaint();
	}
}