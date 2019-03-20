package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;



import oliver.neuron.Neuron;

/**
 * Draw Neuron as a square with text showings it's output,bias and error value
 * To the left of the neuron we draw 2 images
 * One representing weights as colors. Red for positive blue for negative
 * The image is broken down into squares one square per input neuron. 
 * The top left hand corner shows the weight for input zero. 
 * ( This is easy to understand if the input is an image)
 * @author oliver
 *
 */
public class NeuronInAPanel extends JPanel {

	/**
	 * Neuron is drawn a square as neuronSizeInPixels*neuronSizeInPixels
	 */
	int neuronSizeInPixels;

	/**
	 * Number of input weights represented in each image row
	 * Example If we have 100 inputs then we would paint a picture of 10*10
	 * But we we had 15 inputs we may set numInputsPerImageRow to 1 and have 15 rows
	 */
	
	
	
	int widthOfPanelInPixels;
	/**
	 * Input weights are drawn as an image. This image is only changed when the weights changed
	 * So we cache it once its computed to save on CPU cycles when drawing it. 
	 */
	Image  imageWithInputs;
	
	Image  imageWeightsOnly;
	
	
	int weightImageWidthPixels;
	int weightImageHeightPixels;
	/**
	 * Neuron we are drawing
	 */
	Neuron neuron;
	
	/**
	 * Save all panels in a static HashMap
	 */
	
	public NeuronInAPanel(Neuron neuron) {
		this.neuron = neuron;
	}

	public void setCords(int neuronHeight) {
		this.neuronSizeInPixels = neuronHeight;

	}

	
	 DrawNeuralNetwork parent;
	public static void placeNuronOnScreen(Neuron neuron, DrawNeuralNetwork parent, int baseX, int baseY, int neuronSizeInPixels) {
		NeuronInAPanel existing = parent.panels.get(neuron.getName());
		
		if (existing == null) {
			existing = new NeuronInAPanel(neuron);
			parent.panels.put(neuron.getName(), existing);
			parent.add(existing);
			existing.setBackground( parent.getBackground());
			//existing.setBorder(BorderFactory.createBevelBorder(1));
		}
		existing.parent = parent;
		existing.neuron = neuron;
		existing.widthOfPanelInPixels=  neuronSizeInPixels*5/2 ;
		existing.setBounds(baseX , baseY, existing.widthOfPanelInPixels, neuronSizeInPixels +30);
		existing.setCords(neuronSizeInPixels);
		existing.repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintNeuron(g);

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
	protected void paintNeuron(Graphics g) {
		int baseX = this.widthOfPanelInPixels - this.neuronSizeInPixels;
		int baseY=15;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.getName();
		char[] chararr = textStr.toCharArray();
		g2d.setColor(DrawNeuralNetwork.faderYellowToRed(1, 0, neuron.getValue()));
	    this.setBackground(Color.LIGHT_GRAY);
		g2d.fillRect(baseX, baseY, neuronSizeInPixels, neuronSizeInPixels);

		int textSpace = neuronSizeInPixels / 5;
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace);

		;
        this.setToolTipText(textStr +" Neuron value = " + neuron.getValue() );
		textStr = " b " + DrawNeuralNetwork.formatDouble(neuron.getBias());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 2);
		textStr = " err " + DrawNeuralNetwork.formatDouble(neuron.getErrorVar());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 3);

		textStr = " value " + DrawNeuralNetwork.formatDouble(neuron.getValue());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 4);
	

		int numInputs = neuron.getInputs().size();
	
		// g2d.setFont(new Font("Monaco", Font.PLAIN, 10));
		if (numInputs > 10) {
			if (neuron.getName().endsWith("-0")) {
				
			}
			paintInputsInSquare(g, 20, baseY, false);
			paintInputsInSquare(g,neuronSizeInPixels*2/3 +20, baseY, true);
			return;
		}
		if (numInputs > 5) {
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
			drawFanOut(g2d, min, max, baseX, baseY,true);
			return;
		}

	}

	/**
	 * Draw a fanout for weights . Brightest red m most positive. Darkest blue most negative  
	 * @param g2d
	 * @param min
	 * @param max
	 * @param baseX
	 * @param baseY
	 * @param neuron
	 */
	protected void drawFanOut(Graphics g2d, double min, double max, int baseX, int baseY, boolean includeInput) {

		int fanoutXPos = baseX - 60;
		int fandoutYChange = this.neuronSizeInPixels / neuron.getInputs().size();
		for (int x = 0; x < neuron.getWeights().size(); x++) {
			double weight = neuron.getWeights().get(x);
			double input = neuron.getInputs().get(x).getValue();
			double value = weight * 1;
			Color rgb = DrawNeuralNetwork.faderYellowToRed(max, min, value);
			g2d.setColor(rgb);
			Polygon poly = new Polygon();
			poly.addPoint(baseX, baseY + this.neuronSizeInPixels / 2);
			int yPoint = baseY + (x * fandoutYChange);
			poly.addPoint(fanoutXPos, yPoint);
			yPoint += fandoutYChange;
			poly.addPoint(fanoutXPos, yPoint);
			g2d.drawPolygon(poly);
		}

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
	protected Image createImage( boolean includeInput) {
		int w = 0;
		int h = 0;
		double maxSize = 0;
		double minSize = 0;
		for (int x = 0; x < neuron.getWeights().size(); x++) {
			double weight = neuron.getWeights().get(x);
			double input = neuron.getInputs().get(x).getValue();

			double mult = weight;
			if (includeInput) {
				mult = weight * input;
			}

			if (mult > maxSize) {
				maxSize = mult;
			}
			if (mult < minSize) {
				minSize = mult;
			}
		}
		double ln = Math.log(maxSize);

		if (ln < 0) {
			ln *= -1;
		}

		

		int numInputsPerImageRow = (int) Math.sqrt(neuron.getWeights().size());
		int scale = this.neuronSizeInPixels / numInputsPerImageRow*2/3;
	
		if (numInputsPerImageRow < 3) {
			scale= scale/numInputsPerImageRow;
			numInputsPerImageRow =1;
		}
		int numRows = neuron.getWeights().size()/numInputsPerImageRow;
		int scaleHeight= this.neuronSizeInPixels/numRows*2/3;
		if(scaleHeight < scale) {
			scale= scaleHeight;
		}
		this.weightImageWidthPixels = numInputsPerImageRow * scale;
		this.weightImageHeightPixels = numRows * scale;
		BufferedImage img = new BufferedImage(weightImageWidthPixels, weightImageHeightPixels, BufferedImage.TYPE_INT_RGB);

		
		int h1 = 0;
		 
		for (int s = 0; s < scale; s++) {
			h1 =s;
			int w1 = 0; 
			for (int x = 0; x < neuron.getWeights().size(); x++) {

				double weight = neuron.getWeights().get(x);
				double input = neuron.getInputs().get(x).getValue();

				
				double currentValue = weight;
				if (includeInput) {
					currentValue = weight * input;
				}

				int rgb = DrawNeuralNetwork.faderYellowToRed(maxSize, minSize, currentValue).getRGB();
               
				for(int z = 0; z < scale; z++) {
				img.setRGB(w1, h1, rgb);
				w1++;
				}
				
				if (w1 >= numInputsPerImageRow*scale) {
					w1 = 0;
				
					h1+= scale;
				}
			}
		}

		return img;
		
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
	protected void paintInputsInSquare(Graphics g, int baseX, int baseY, boolean includeInput) {

		
		if (includeInput) {
			if(this.imageWithInputs == null) {
				this.imageWithInputs=this.createImage(includeInput);
			}
			if(neuron.getName().endsWith("-000")) {
				String textStr = "Weights* input";
				char [] chararr = textStr.toCharArray();
				g.setColor(Color.black);
				g.drawChars(chararr, 0, chararr.length, baseX, baseY );
				}	
			g.drawImage(this.imageWithInputs, baseX, baseY +20, this.weightImageWidthPixels, this.weightImageHeightPixels, new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		if (includeInput == false) {
			if( this.imageWeightsOnly == null) {
				this.imageWeightsOnly=this.createImage( includeInput);
				}
			if(neuron.getName().endsWith("-000")) {
			String textStr = "Weights only";
			char [] chararr = textStr.toCharArray();
			g.setColor(Color.black);
			g.drawChars(chararr, 0, chararr.length, baseX, baseY );
			}
				g.drawImage(this.imageWeightsOnly, baseX, baseY+20, this.weightImageWidthPixels, this.weightImageHeightPixels, new ImageObserver() {

					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			
		}
		

		int df = 0;
	}
}
