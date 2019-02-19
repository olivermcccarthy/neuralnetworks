package oliver.neuron;

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
	static JButton button = new JButton("Run X Training intervals");
	static JTextArea numTrials = new JTextArea();
	static JFrame frame;

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
		button.setBounds(300, 30, 200, 30);
		numTrials.setBounds(500, 30, 30, 30);
		
		// Sets JTextArea font and color.
        Font font = new Font("Segoe Script", Font.BOLD, 40);
        numTrials.setFont(font);
        numTrials.setForeground(Color.BLUE);
		button.setFont(this.getFont().deriveFont(20.0f));
		numTrials.setText("1");
		numTrials.getText();
		this.setLayout(null);
		this.add(numTrials);
		this.add(button);
	}

	protected void paintInputImage(Graphics g) {
		numTrials.setFont(g.getFont().deriveFont(100));
		
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
		for (Layer layer : Layer.layers) {
			if (layer.neurons.size() > 20) {
				continue;
			}
			int size = layer.neurons.size();
			if (size > maxLevelSize) {
				maxLevelSize = size;
			}
		}
		
		int screenWidth = this.getWidth();
		int screenHeight = this.getHeight();
		
		int diffX = screenWidth / Layer.layers.size();
	    int shiftY = 0;

	    // Space 
	    int neuronSpaceHeight=  screenHeight/(maxLevelSize+1);
	    neuronHeight = (neuronSpaceHeight*9 )/10;
	     neuronWidth =  neuronHeight;
		for (Layer layer : Layer.layers) {
			if (layer.neurons.size() > 20) {
				continue;
			}
			
			int startLevel = (maxLevelSize - layer.neurons.size()) / 2;
			shiftY = (startLevel * neuronSpaceHeight);
			int diffY  = shiftY;
			if (layer.neurons.size() > 20) {

			} else {
				for (Neuron nu : layer.neurons) {

					paintNewronTopX(g, nu, baseX + diffX, baseY + diffY);
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
	protected void paintInputsInSquare(Graphics g, Neuron neuron, int baseX, int baseY) {
		int w = 0;
		int h = 0;
		double maxSize = 0;

		for (int x = 0; x < neuron.weights.size(); x++) {
			double weight = neuron.weights.get(x);
			double input = neuron.inputs.get(x).getValue();

			double mult = weight * input;
			if (mult < 0) {
				mult = mult * -1;
			}

			if (mult > maxSize) {
				maxSize = mult;
			}

		}

		g.setColor(Color.WHITE);
		int pictureHeight = neuron.weights.size() / pictureWidth;
		pictureScale = neuronHeight/pictureHeight;
		BufferedImage img = new BufferedImage(pictureWidth * pictureScale, pictureHeight * pictureScale,
				BufferedImage.TYPE_INT_RGB);

		int x = 0;
		
		for (h = 0; h < pictureHeight * pictureScale; h++) {
			x = (pictureWidth) * (h / pictureScale);
			for (w = 0; w < pictureWidth; w++) {

				double weight = neuron.weights.get(x);
				double input = neuron.inputs.get(x).getValue();

				x++;
				double currentValue = weight * input;
				double fractionOfMax = currentValue / maxSize;
				if (fractionOfMax < 0) {
					fractionOfMax *= -1;
				}
				int rgb = faderYellowToRed(fractionOfMax).getRGB();
				for (int z = 0; z < pictureScale; z++) {
					img.setRGB(w * pictureScale + z, h, rgb);
				}
			}

		}

		g.drawImage(img, baseX - 150, baseY, pictureWidth * pictureScale, pictureHeight * pictureScale,
				new ImageObserver() {

					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});

		int df = 0;
	}

	/**
	 * if you pass 0 you get yellow. if you pass 1 you get bright red Values between
	 * fade from yellow to red
	 *
	 */
	private Color faderYellowToRed(double fade) {

		fade = fade * 255;
		if (fade > 255) {
			int debugME = 0;
		}
		if (fade < 128) {
			fade = fade * 1.3;
			return new Color(255, 255, 180 - (int) fade);
		}
		return new Color(255, 255 - (int) fade, 0);
	}

	/**
	 * Dra a circle for a neuron. Showing value and errorValue. Also Draw links to
	 * input Neurons. If there are more than 20 inputs we just paint them as a
	 * Square
	 * 
	 * @param g
	 * @param neuron
	 * @param baseX
	 * @param baseY
	 */
	protected void paintNewronTopX(Graphics g, Neuron neuron, int baseX, int baseY) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.name;
		char[] chararr = textStr.toCharArray();
		g2d.setColor(faderYellowToRed(neuron.getValue()));
		float fontSize = neuronHeight/5;
		g.setFont(g.getFont().deriveFont(fontSize));
		g2d.fillRect(baseX, baseY, neuronWidth, neuronHeight);
		
		int textSpace = neuronHeight/5;
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace);

		;

		textStr = " b " + getDBL(neuron.bias);
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*2);
		textStr = " err " + getDBL(neuron.errorVar);
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*3);

		textStr = " value " + getDBL(neuron.getValue());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace*4);
		// g2d.drawRect(baseX, baseY, neuronWidth, neuronHeight);
		neuron.X = baseX + neuronWidth;
		neuron.Y = baseY + (neuronHeight / 2);

		int numInputs = neuron.inputs.size();
		int newY = baseY + 15;

		Color[] colors = new Color[] { Color.BLUE, Color.black, Color.RED, new Color(0, 102, 0), Color.MAGENTA,
				new Color(153, 102, 51) };
		int colorIndex = 0;

		// g2d.setFont(new Font("Monaco", Font.PLAIN, 10));
		if (numInputs > 20) {
			paintInputsInSquare(g, neuron, baseX, baseY);
			return;
		}

		if (numInputs > 0) {

			int nI = 0;
			double max = 0;
			for (int x = 0; x < neuron.weights.size(); x++) {
				double weight = neuron.weights.get(x);
				double input = neuron.inputs.get(x).getValue();
				double value = weight * input;
				if (value < 0) {
					value *= -1;
				}
				if (value > max) {
					max = value;
				}
			}
			max = max / neuron.getValue();
			for (int n = 0; n < neuron.inputs.size(); n++) {
				colorIndex++;
				if (colorIndex >= colors.length) {
					colorIndex = 0;
				}
				Neuron connNu = neuron.inputs.get(n);
				Double inpu = neuron.weights.get(n);
				// System.out.println(connNu.X + ":" + connNu.Y + " to" + baseX + ":" + newY);
				g2d.setColor(colors[colorIndex]);
				double weight = neuron.weights.get(n);
				double input = neuron.inputs.get(n).getValue();
				double value = weight * input;
				if (value < 0) {
					value *= -1;
				}
				Font existingFont = g2d.getFont();
				if (value > 0.2 * max) {

					g2d.setColor(faderYellowToRed(value / max));
					g2d.drawLine(connNu.X, connNu.Y, baseX, baseY + (neuronHeight / 2));

					int centerX = (baseX - connNu.X) / 2 + connNu.X;
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
				}

				g2d.setFont(existingFont);
				// g2d.drawLine(baseX - 60, baseY + (neuronHeight / 2), baseX, baseY +
				// (neuronHeight / 2));

				newY += 15;
				nI++;
			}
		}
	}

	static String message = "About to start Training";

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
		int value =1;
		try {
		  value = Integer.valueOf(DrawPanel.numTrials.getText());
		}
		catch (Throwable t) {
			System.out.println("Please enter number" + t.getMessage());
		}
		
		return value;
		
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
				}

			}
		});
		JScrollPane scroll = new JScrollPane(neuronPanel);
		//neuronPanel.setPreferredSize(new Dimension(1000, 1000));
		frame.setVisible(true);
		neuronPanel.setBackground(new Color(230, 255, 255));
		frame.getContentPane().add(scroll);

		frame.repaint();

	}
}