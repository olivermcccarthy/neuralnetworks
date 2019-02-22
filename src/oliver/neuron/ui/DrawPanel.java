package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import oliver.neuron.Layer;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;

/**
 * Draw neurons showing input weights and output values.
 * 
 * @author oliver
 *
 */
public class DrawPanel extends JPanel {

	/**
	 * Blutton to allow user click through training.
	 */
	static JButton button = new JButton("Run ");
	static JComboBox numTrials = new JComboBox();
	static JComboBox sleepTime = new JComboBox();
	public static JFrame frame;

	/**
	 * Type of input data. 1s and zeros or greyscale
	 */
	public enum PICTURE_TYPE {
		BINARY, GREYSCALE
	};

	/**
	 * Allow user click through training
	 */
	static Object waitForMe = new Object();

	static boolean dontStop = false;

	/**
	 * How large to paint each Neuron
	 */
	static int neuronWidth = 100;
	static int neuronHeight = 100;

	/**
	 * Type of input data
	 */
	static PICTURE_TYPE pictureType;

	/**
	 * When there are more than 20 inputs to a neuron . The weights will be painted
	 * as a picture. Brightest color being highest weight This value will be the
	 * width of the picture.
	 */
	static int pictureWidth = 28;

	/**
	 * How many times to blow up the image.
	 */
	static int pictureScale = 1;

	// We will draw the input Image in the top corner;
	private static int[][] inputImage = null;

	public static int[][] getInputImage() {
		return inputImage;
	}

	public static void setInputImage(int[][] inputImage, int pictureScale, PICTURE_TYPE typeOfPicture) {
		DrawPanel.inputImage = inputImage;
		DrawPanel.pictureType = typeOfPicture;
	}

	public DrawPanel(ActionListener listener) {
		button.addActionListener(listener);
		button.setBounds(300, 30, 100, 30);
		numTrials.setBounds(200, 30, 100, 30);
		sleepTime.setBounds(400, 30, 300, 30);
		button.setFont(this.getFont().deriveFont(20.0f));

		this.setLayout(null);
		 numTrials.addItem("1 trial");
		 numTrials.addItem("10 trials");
		 numTrials.addItem("100 trials");
		 numTrials.addItem("1000 trials");
		 numTrials.addItem("10000 trials");
		 numTrials.addItem("100000 trials");
		 numTrials.setSelectedIndex(0);
		 
		 sleepTime.addItem("1000ms sleep between trials");
		 sleepTime.addItem("100ms sleep between trials");
		 sleepTime.addItem("10ms sleep between trials");
		 sleepTime.addItem("1ms sleep between trials");
		
		 
		
		 sleepTime.setSelectedIndex(0);
		this.add(button);
		this.add(numTrials);
		this.add(sleepTime);
	}

	protected void paintInputImage(Graphics g) {
		
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

			g.drawImage(img, 0, 0, pictureWidth * pictureScale, pictureHeight * pictureScale, new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});

		}
	}

	/**
	 * Draw input Image in top corner Draw each layer. Starting with input layer on
	 * the Left.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int baseX = 0;
		int baseY = 100;

		
		paintInputImage(g);
		Font existing = g.getFont();
		g.setFont(g.getFont().deriveFont(20.0f));
		
		String textStr =  message;
		char[] chararr = textStr.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, 300, 25);
		g.setFont(existing);
		int maxLevelSize = 0;
		
		showLegend( g, this.getWidth() -200 , 20);
		
		for (Layer layer : Layer.getLayers()) {
			if (layer.getNeurons().size() > 20) {
				continue;
			}
			int size = layer.getNeurons().size();
			if (size > maxLevelSize) {
				maxLevelSize = size;
			}
		}
		
		int screenWidth = this.getWidth();
		int screenHeight = this.getHeight();
		
		int diffX = screenWidth / Layer.getLayers().size();
	    int shiftY = 0;

	    // Space 
	    int neuronSpaceHeight=  screenHeight/(maxLevelSize+1);
	    neuronHeight = (neuronSpaceHeight*9 )/10;
	     neuronWidth =  neuronHeight;
		for (Layer layer : Layer.getLayers()) {
			if (layer.getNeurons().size() > 20) {
				continue;
			}
			
			int startLevel = (maxLevelSize - layer.getNeurons().size()) / 2;
			shiftY = (startLevel * neuronSpaceHeight);
			int diffY  = shiftY;
			if (layer.getNeurons().size() > 20) {

			} else {
				for (Neuron nu : layer.getNeurons()) {

					paintNeuron(g, nu, baseX + diffX, baseY + diffY);
					diffY += neuronSpaceHeight;
				}
			}
			baseX += diffX;

		}

	}

	public static String getDBL(double value) {

		String doubleStr = "" + (Math.floor(value * 1000) / 1000);

		return doubleStr;
	}

	/**
	 * Paint a picture of the inputs neurons contribution to this neurons sigmoid If
	 * we have 100 input neurons we can paint a ten by ten picture where the first
	 * row is neuron 0 to 9. The neurons with the biggest contribution get the
	 * brightest color.
	 * 
	 * @param g
	 * @param neuron
	 * @param baseX
	 * @param baseY
	 */
	protected void paintInputsInSquare(Graphics g, Neuron neuron, int baseX, int baseY, boolean includeInput) {
		int w = 0;
		int h = 0;
		double maxSize = 0;
		double minSize = 0;
		for (int x = 0; x < neuron.getWeights().size(); x++) {
			double weight = neuron.getWeights().get(x);
			double input = neuron.getInputs().get(x).getValue();

			double mult = weight ;
			if(includeInput) {
				mult = weight * input;
			}
			
			

			if (mult > maxSize) {
				maxSize = mult;
			}
			if (mult < minSize) {
				minSize = mult;
			}
		}
        double ln =  Math.log(maxSize);
        
        if(ln < 0) {
        	ln *= -1;
        }
        
        
		g.setColor(Color.WHITE);
		int pictureHeight = neuron.getWeights().size() / pictureWidth;
		pictureScale = neuronHeight/pictureHeight +1;
		BufferedImage img = new BufferedImage(pictureWidth * pictureScale, pictureHeight * pictureScale,
				BufferedImage.TYPE_INT_RGB);

		int x = 0;
		//showLegend(maxSize,minSize,g, baseX - 60,baseY);
		for (h = 0; h < pictureHeight * pictureScale; h++) {
			x = (pictureWidth) * (h / pictureScale);
			for (w = 0; w < pictureWidth; w++) {

				double weight = neuron.getWeights().get(x);
				double input = neuron.getInputs().get(x).getValue();

				x++;
				double currentValue = weight;
				if(includeInput) {
					 currentValue = weight * input;
				}
				

			
				int rgb = faderYellowToRed(maxSize, minSize,currentValue).getRGB();
				for (int z = 0; z < pictureScale; z++) {
					img.setRGB(w * pictureScale + z, h, rgb);
				}
			}

		}

		g.drawImage(img, baseX , baseY, pictureWidth * pictureScale, pictureHeight * pictureScale,
				new ImageObserver() {

					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});

		int df = 0;
	}


	
	static Color[] shadesPositive = new Color[] {new Color(255,0,0), new Color(225,125,125),new Color(255,255,204)};
	static Color[] shadesNegative = new Color[] {new Color(0,0,105), new Color(0,0,255),new Color(153,255,255)};
	/**
	 * Fade colour based of difference of LOg
	 * fade from yellow to red
	 *
	 */
	private Color faderYellowToRed(double maxValue, double minValue, double currentValue) {

		if(maxValue == 0 &&  minValue == 0) {
			return new Color(255, 255, 0);
		}
		if(currentValue < 0) {
			return faderYellowToRedMinSide(minValue *-1, currentValue*-1);
		}
		double fraction = currentValue/maxValue;
		if(fraction > 0.5) {
			return shadesPositive[0];
		}
		if(fraction > 0.2) {
			return shadesPositive[1];
		}
		return shadesPositive[2];
	}

	private Color faderYellowToRedMinSide(double minValue, double currentValue) {

		
		double fraction = currentValue/minValue;
		if(fraction > 0.5) {
			return shadesNegative[0];
		}
		if(fraction > 0.2) {
			return shadesNegative[1];
		}
		return shadesNegative[2];
	}
	  private void showLegend( Graphics g, int x, int y) { 
		  
		  showLegend(g,x,y, shadesPositive[0], "50 -> 100% of max weight");
		  showLegend(g,x,y+20, shadesPositive[1], "20 -> 50% of max weight ");
		  showLegend(g,x,y+40, shadesPositive[2], "0 -> 20% of max weight ");
		  showLegend(g,x,y+60, shadesNegative[2], "0 -> 20% of min weight ");
		  showLegend(g,x,y+80, shadesNegative[1], "20 -> 50% of min weight");
		  showLegend(g,x,y+100, shadesNegative[0], "50 -> 100% of min weight ");
		  
		  
	  }
       private void showLegend( Graphics g, int x, int y,  Color color, String message) {

		
		
		char[] chararr =  message.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, x, y+15);
		g.setColor(color);
		g.fillRect(x -20, y, 20, 20);
		
	}

	/**
	 * Draw a circle for a neuron. Showing value and errorValue. Also Draw links to
	 * input Neurons. If there are more than 20 inputs we just paint them as a
	 * Square
	 * 
	 * @param g
	 * @param neuron
	 * @param baseX
	 * @param baseY
	 */
	protected void paintNeuron(Graphics g, Neuron neuron, int baseX, int baseY) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.getName();
		char[] chararr = textStr.toCharArray();
		g2d.setColor(faderYellowToRed(1,0,neuron.getValue()));
		float fontSize = neuronHeight/8;
		//g.setFont(g.getFont().deriveFont(fontSize));
		g2d.fillRect(baseX, baseY, neuronWidth, neuronHeight);
		
		int textSpace = neuronHeight/5;
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace);

		;

		textStr = " b " + getDBL(neuron.getBias());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*2);
		textStr = " err " + getDBL(neuron.getErrorVar());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*3);

		textStr = " value " + getDBL(neuron.getValue());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*4);
		// g2d.drawRect(baseX, baseY, neuronWidth, neuronHeight);
		neuron.X = baseX + neuronWidth;
		neuron.Y = baseY + (neuronHeight / 2);

		int numInputs = neuron.getInputs().size();
		int newY = baseY + 15;

		Color[] colors = new Color[] { Color.BLUE, Color.black, Color.RED, new Color(0, 102, 0), Color.MAGENTA,
				new Color(153, 102, 51) };
		int colorIndex = 0;

		// g2d.setFont(new Font("Monaco", Font.PLAIN, 10));
		if (numInputs > 20) {
			if(neuron.getName().endsWith("-0")) {
				textStr = " Weights      Weights*input"; 
				chararr = textStr.toCharArray();
				g2d.drawChars(chararr, 0, chararr.length, baseX -200 , baseY -10 );
			}
			paintInputsInSquare(g, neuron, baseX -100, baseY,true);
			paintInputsInSquare(g, neuron, baseX -200, baseY,false);
			return;
		}

		if (numInputs > 0) {

			int nI = 0;
			double max = 0;
			double min = 0;
			for (int x = 0; x < neuron.getWeights().size(); x++) {
				double weight = neuron.getWeights().get(x);
				double input = neuron.getInputs().get(x).getValue();
				double value = weight * input;
				
				if (value > max) {
					max = value;
				}
				if (value < min) {
					min = value;
				}
			}
			max = max / neuron.getValue();
			for (int n = 0; n < neuron.getInputs().size(); n++) {
				colorIndex++;
				if (colorIndex >= colors.length) {
					colorIndex = 0;
				}
				Neuron connNu = neuron.getInputs().get(n);
				Double inpu = neuron.getWeights().get(n);
				// System.out.println(connNu.X + ":" + connNu.Y + " to" + baseX + ":" + newY);
				g2d.setColor(colors[colorIndex]);
				double weight = neuron.getWeights().get(n);
				double input = neuron.getInputs().get(n).getValue();
				double value = weight * input;
				
				if(value < 0) {
					if(value/min > 0.5) {
						g2d.setColor(faderYellowToRed(max,min,value));
						g2d.drawLine(connNu.X, connNu.Y, baseX, baseY + (neuronHeight / 2));

					}
				}
				if(value > 0) {
					if(value/max > 0.5) {
						g2d.setColor(faderYellowToRed(max,min,value));
						g2d.drawLine(connNu.X, connNu.Y, baseX, baseY + (neuronHeight / 2));

					}
				}	
					/*int centerX = (baseX - connNu.X) / 2 + connNu.X;
					int centerY = (baseY - connNu.Y + neuronHeight) / 2 + connNu.Y;
					textStr = " w " + getDBL(inpu);
					chararr = textStr.toCharArray();
					AffineTransform affineTransform = new AffineTransform();

					double slope = (baseY - connNu.Y + (neuronHeight / 2)) / (baseX - connNu.X);

					affineTransform.setToScale(1.5, 1.5);
					affineTransform.rotate(Math.atan(slope), 0, 0);

					Font rotatedFont = g2d.getFont().deriveFont(affineTransform);
					g2d.setFont(rotatedFont);
					g2d.setColor(Color.black);
					g2d.drawChars(chararr, 0, chararr.length, centerX - 10, centerY);
			*/
				// g2d.drawLine(baseX - 60, baseY + (neuronHeight / 2), baseX, baseY +
				// (neuronHeight / 2));

				newY += 15;
				nI++;
			}
		}
	}

	static String message = "About to start Training";

	static int numTrialsToRun =0;
	static int numTrialsRun =0;
	static int sleepTimeMs =100;
	public static void waitForUserClick(TrialInfo info, double expected, double got) {
		numTrialsRun++;
		message = String.format("Trial %d  . Expected %s  Got %s ",numTrialsRun,expected, got);
		numTrialsToRun --;
		frame.repaint();
		try {
			Thread.sleep(sleepTimeMs);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(numTrialsToRun <=0) {
			button.setText("RUN");
			synchronized (waitForMe) {
				try {
					waitForMe.wait(100000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int selectedIndex = sleepTime.getSelectedIndex();
			if(selectedIndex == 0) {
				sleepTimeMs =1000;
			}
			if(selectedIndex == 1) {
				sleepTimeMs =100;
			}
			if(selectedIndex == 2) {
				sleepTimeMs = 10;
			}
			if(selectedIndex == 3) {
				sleepTimeMs = 1;
			}
			selectedIndex = numTrials.getSelectedIndex();
			if(selectedIndex == 0) {
				numTrialsToRun =1;
			}
			if(selectedIndex == 1) {
				numTrialsToRun =10;
			}
			if(selectedIndex == 2) {
				numTrialsToRun = 100;
			}
			if(selectedIndex == 3) {
				numTrialsToRun = 1000;
			}
			if(selectedIndex == 4) {
				numTrialsToRun = 10000;
			}
			if(selectedIndex == 5) {
				numTrialsToRun = 100000;
			}
			
		}
	}
	/**
	 * Call this method when you want to stop your training. It will wait until user clicks continue
	 * . Returns the number of trials to run before Stopping again.
	 * @param msg
	 */
	public static int waitForUserClick(String msg , int trialNumber) {
		if (dontStop) {
			return 1000000;
		}
		try {
			message = String.format("Trial %d  %s",trialNumber,msg);
			
			frame.repaint();
			synchronized (waitForMe) {
				waitForMe.wait(100000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int selectedIndex = numTrials.getSelectedIndex();
		if(selectedIndex == 0) {
			return 1;
		}
		if(selectedIndex == 1) {
			return 10;
		}
		if(selectedIndex == 2) {
			return 100;
		}
		return 1000;
		
	}

	private static DrawPanel neuronPanel;

	/**
	 * Paint a picture of Neurons in all the layers
	 * 
	 * @param pictureWidth
	 * @param pictureScale
	 */
	public static void showNeurons(int pictureWidth, int pictureScale) {

		DrawPanel.pictureWidth = pictureWidth;
		DrawPanel.pictureScale = pictureScale;

		frame = new JFrame();
		frame.setSize(1000, 1000);

		neuronPanel = new DrawPanel(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					button.setText("STOP");
					numTrialsToRun=0;
				}

			}
		});
		JScrollPane scroll = new JScrollPane(neuronPanel);
		neuronPanel.setPreferredSize(new Dimension(1000, 1000));
		frame.setVisible(true);
		neuronPanel.setBackground(new Color(230, 255, 255));
		frame.getContentPane().add(scroll);
	
		frame.repaint();

	}
}