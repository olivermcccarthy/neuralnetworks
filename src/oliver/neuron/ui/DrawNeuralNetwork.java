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
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

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

	static DrawNeuralNetwork neuronPanel = null;

	NeuralNetwork neuralNetwork = null;

	public static DrawNeuralNetwork getNeuronPanel() {
		return neuronPanel;
	}

	ControlPanel panel ;
	
	private static JFrame frame;

	private static int SCREEN_SIZE=1000;
	/**
	 * Type of input data. 1s and zeros or greyscale
	 */
	public enum PICTURE_TYPE {
		BINARY, GREYSCALE
	};

	/**
	 * Allow user click through training
	 */
	


	/**
	 * How large to paint each Neuron
	 */
	static final int neuronSizeInPixels = 80;
	
	static final int neuronSpaceHeight = neuronSizeInPixels + 20;
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

	public void setInputPanel(JPanel inputPanel) {
		this.inputPanel = inputPanel;
		this.add(this.inputPanel);
		Dimension prefr = inputPanel.getPreferredSize();
		inputPanel.setBounds(0, 300, prefr.width, prefr.height);
	}

	public void setInputImage(int[][] inputImage, int pictureScale, PICTURE_TYPE typeOfPicture) {
		neuronPanel.inputImage = inputImage;
		pictureType = typeOfPicture;

	}
	TrialInfo trialInfo;
	public DrawNeuralNetwork(TrialInfo trialInfo) {
		this.trialInfo = trialInfo;
		panel = new ControlPanel(trialInfo);
		panel.setBounds(0,0,100,150);
		this.add(panel);
		JTextPane heading = new JTextPane();
		//heading.setBackground(new Color(230, 255, 255));
		heading.setFont(this.getFont().deriveFont(15.0f));
		heading.setText(trialInfo.getHelp());
		heading.setFont(getFont().deriveFont(12.0f));
		
		
		JScrollPane scroll = new JScrollPane(heading);
		scroll.setBounds(150, 10, SCREEN_SIZE -500 , 100);
	
		this.setLayout(null);
		this.add(scroll);
		

	}

	protected void paintInputImage(Graphics g, int baseY) {

		String textStr = "Input Image ";
		char[] chararr = textStr.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, 0, baseY - 5);

		if (inputPanel != null) {
			inputPanel.setBackground(new Color(230, 255, 255));

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
	
	private static int maxRedraws =10;
	int numRedraws =0;
	/**
	 * Draw input Image in top corner Draw each layer. Starting with input layer on
	 * the Left.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.numRedraws++;
		if(numRedraws > 10) {
		//	return;
		}
		int baseX = 0;
		int baseY = 160;

		//Font existing = g.getFont();
		//g.setFont(g.getFont().deriveFont(12.0f));
         this.setFont(g.getFont().deriveFont(12.0f));
		String textStr = getMessage();
		char[] chararr = textStr.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, 150, 130);
		//g.setFont(existing);
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

		int screenWidth = this.getWidth();
		int screenHeight = this.getHeight();

		int diffX = screenWidth / layers.size();
		diffX = 250;
	
		int shiftY = 0;

		// Space
		
	   
		
		
		int startLevel = (maxLevelSize - 1);
		shiftY = (startLevel * neuronSpaceHeight / 3);

		int diffY = shiftY;
		paintInputImage(g, baseY + diffY);
		for (Layer layer : layers) {
			if (layer.getNeurons().size() > NUM_NURONS_TODRAW) {
				continue;
			}

			startLevel = (maxLevelSize - layer.getNeurons().size());
			shiftY = (startLevel * neuronSpaceHeight / 2);

			diffY = shiftY;
			
				for (Neuron nu : layer.getNeurons()) {

					NeuronInAPanel.placeNuronOnScreen(nu, this,  baseX + diffX,  baseY + diffY, this.neuronSizeInPixels);
					
					diffY += neuronSpaceHeight;
				}

			
			baseX += diffX;

		}

	}

	public static String formatDouble(double value) {

		String doubleStr = "" + (Math.floor(value * 1000) / 1000);

		return doubleStr;
	}

	

	static Color[] shadesPositive = new Color[] { new Color(255, 0, 0), new Color(225, 125, 125),
			new Color(255, 255, 204) };
	static Color[] shadesNegative = new Color[] { new Color(0, 0, 105), new Color(0, 0, 255),
			new Color(153, 255, 255) };

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

	private static String message = "About to start Training";

	
    int sleepTimeMs;
	public int getSleepTime() {
		sleepTimeMs = this.panel.getSleepTime();
		return this.panel.getSleepTime();
	}

	public int getNumBatches() {
		numBatchesI = this.panel.getNumBatches();
		return numBatchesI;
	}

	public double getLearningRate() {
		return this.panel.getLearningRate();
	}

	public int getNumPerBatch() {
		
		return this.panel.getNumPerBatch();
	}

	public void waitForUserClick(TrialInfo info, double[] expected, double[] got) {
		this.waitForUserClick(info, expected, got, true);
	}
	int numBatchesI;
	public void waitForUserClick(TrialInfo info, double[] expected, double[] got, boolean sleep) {
		
		String message= String.format("Trial %d  . Expected %s  Got %s Cost of last Batch %s LearningRate %s", this.numBatchesI,
				Neuron.toString(expected), Neuron.toString(got), formatDouble(info.getBestCost()),
				formatDouble(info.getLearningRate()));
		this.waitForUserClick(info,message,sleep);
	}
	public void waitForUserClick(TrialInfo info, String message, boolean sleep) {


		setMessage(message);

	
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
		if (numBatchesI <= 0) {
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

		if (frame == null) {
			frame = new JFrame();
			frame.setSize(SCREEN_SIZE, SCREEN_SIZE);

			neuronPanel = new DrawNeuralNetwork(trialInfo);
			neuronPanel.pictureWidth = pictureWidth;
			neuronPanel.pictureScale = pictureScale;

			JScrollPane scroll = new JScrollPane(neuronPanel);
			neuronPanel.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE*2));
			frame.setVisible(true);
			HelpPanel helpPanel = new HelpPanel();
			
			neuronPanel.setBackground(new Color(230, 255, 255));

			frame.getContentPane().add(scroll);
		}
		neuronPanel.neuralNetwork = neuralNetwork;

		frame.repaint();

		return neuronPanel;

	}

	public void waitForUserClick(TrialInfo info, double expected, int got) {
		// TODO Auto-generated method stub
		this.waitForUserClick(info, new double[] { expected }, new double[] { got });

	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		DrawNeuralNetwork.message = message;
	}
	
	public void run(TrialInfo trainer) throws Exception {
		this.waitForUserClick( trainer, "Cick run to start ", false);
		while(true) {
			
			
			while(numBatchesI > 0) {
				trainer.nextBatch(neuralNetwork);
				numBatchesI --;
				
			}
			this.waitForUserClick( trainer, "Cick run to start ", false);
		}
	}
	public void forceRedraw() {
		NeuronInAPanel.forceRedraw();
	}
}