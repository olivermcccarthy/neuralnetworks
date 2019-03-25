package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import oliver.neuron.Neuron;

/**
 * Draw Neuron as a square with text showings it's output,bias and error value
 * To the left of the neuron we draw 2 images One representing weights as
 * colors. Red for positive blue for negative The image is broken down into
 * squares one square per input neuron. The top left hand corner shows the
 * weight for input zero. ( This is easy to understand if the input is an image)
 * 
 * @author oliver
 *
 */
public class NeuronInAPanel extends JPanel {

	/**
	 * Neuron is drawn a square as neuronSizeInPixels*neuronSizeInPixels
	 */
	int neuronSizeInPixels;

	
	int widthOfPanelInPixels;
	
	/**
	 * Input weights are drawn as an image. This image is only changed when the
	 * weights changed So we cache it once its computed to save on CPU cycles when
	 * drawing it.
	 */
	Image imageWithInputs;

	Image imageWeightsOnly;

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



	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintNeuron(g);

	}

	/**
	 * Draw a square for a neuron. Showing value and errorValue. Also paint picture of weights
	 * Square
	 * 
	 * @param g
	 * @param neuron
	 * @param baseX
	 * @param baseY
	 */
	protected void paintNeuron(Graphics g) {
		int baseX = this.widthOfPanelInPixels - this.neuronSizeInPixels;
		int baseY = 15;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.getName();
		char[] chararr = textStr.toCharArray();
		g2d.setColor(DrawNeuralNetwork.weightAsColor(1, 0, neuron.getValue()));

		g2d.fillRect(baseX, baseY, neuronSizeInPixels, neuronSizeInPixels);

		int textSpace = neuronSizeInPixels / 5;
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace);

		;
		this.setToolTipText(textStr + " Neuron value = " + neuron.getValue());
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

		
			paintInputsInSquare(g, 0, baseY, false);
			paintInputsInSquare(g, neuronSizeInPixels * 2 / 3 + 10, baseY, true);
			return;
		
		
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
	protected Image showWeightsAsColoredSquare(boolean includeInput) {
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
		int scale = this.neuronSizeInPixels / numInputsPerImageRow ;

		if (numInputsPerImageRow < 3) {
			scale = scale / numInputsPerImageRow;
			numInputsPerImageRow = 1;
		}
		int numRows = neuron.getWeights().size() / numInputsPerImageRow;
		int scaleHeight = this.neuronSizeInPixels / numRows ;
		if (scaleHeight < scale) {
			scale = scaleHeight;
		}
		this.weightImageWidthPixels = numInputsPerImageRow * scale;
		this.weightImageHeightPixels = numRows * scale;
		BufferedImage img = new BufferedImage(weightImageWidthPixels, weightImageHeightPixels,
				BufferedImage.TYPE_INT_RGB);

		int h1 = 0;

		for (int s = 0; s < scale; s++) {
			h1 = s;
			int w1 = 0;
			for (int x = 0; x < neuron.getWeights().size(); x++) {

				double weight = neuron.getWeights().get(x);
				double input = neuron.getInputs().get(x).getValue();

				double currentValue = weight;
				if (includeInput) {
					currentValue = weight * input;
				}

				int rgb = DrawNeuralNetwork.weightAsColor(maxSize, minSize, currentValue).getRGB();

				for (int z = 0; z < scale; z++) {
					img.setRGB(w1, h1, rgb);
					w1++;
				}

				if (w1 >= numInputsPerImageRow * scale) {
					w1 = 0;

					h1 += scale;
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
			if (this.imageWithInputs == null) {
				this.imageWithInputs = this.showWeightsAsColoredSquare(includeInput);
			}

			g.drawImage(this.imageWithInputs, baseX, baseY , this.weightImageWidthPixels,
					this.weightImageHeightPixels, new ImageObserver() {

						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
							// TODO Auto-generated method stub
							return false;
						}
					});
		}
		if (includeInput == false) {
			if (this.imageWeightsOnly == null) {
				this.imageWeightsOnly = this.showWeightsAsColoredSquare(includeInput);
			}

			g.drawImage(this.imageWeightsOnly, baseX, baseY , this.weightImageWidthPixels,
					this.weightImageHeightPixels, new ImageObserver() {

						@Override
						public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
							// TODO Auto-generated method stub
							return false;
						}
					});

		}

		int df = 0;
	}

	public void forceRedraw() {
		this.imageWeightsOnly = null;
		this.imageWithInputs = null;

	}
}
