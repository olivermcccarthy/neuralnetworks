package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.util.HashMap;

import javax.swing.JPanel;

import com.sun.prism.PixelFormat.DataType;

import oliver.neuron.Neuron;

public class NeuronInAPanel extends JPanel {

	int neuronHeight;

	int pictureHeight;
	int ourWidth;
	Image  imageWithInputs;
	Image  imageWeightsOnly;
	Neuron neuron;
	static HashMap<String, NeuronInAPanel> panels = new HashMap<String, NeuronInAPanel>();

	public NeuronInAPanel(Neuron neuron) {
		this.neuron = neuron;
	}

	public void setCords(int neuronHeight) {
		this.neuronHeight = neuronHeight;

	}

	public static void forceRedraw() {
		for(NeuronInAPanel panel : panels.values()) {
			panel.imageWithInputs = null;
		}
	}
	public static void setCords(Neuron neuron, DrawNeuralNetwork parent, int baseX, int baseY, int neuronHeight) {
		NeuronInAPanel existing = panels.get(neuron.getName());
		
		if (existing == null) {
			existing = new NeuronInAPanel(neuron);
			panels.put(neuron.getName(), existing);
			parent.add(existing);
			existing.setBackground(parent.getBackground());
		}
		existing.ourWidth=  neuronHeight*3;
		existing.setBounds(baseX , baseY, existing.ourWidth, neuronHeight);
		existing.setCords(neuronHeight);
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
		int baseX = this.ourWidth - this.neuronHeight;
		int baseY=0;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.getName();
		char[] chararr = textStr.toCharArray();
		g2d.setColor(DrawNeuralNetwork.faderYellowToRed(1, 0, neuron.getValue()));
		float fontSize = neuronHeight / 8;
		// g.setFont(g.getFont().deriveFont(fontSize));
		g2d.fillRect(baseX, baseY, neuronHeight, neuronHeight);

		int textSpace = neuronHeight / 5;
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace);

		;

		textStr = " b " + DrawNeuralNetwork.formatDouble(neuron.getBias());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 2);
		textStr = " err " + DrawNeuralNetwork.formatDouble(neuron.getErrorVar());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 3);

		textStr = " value " + DrawNeuralNetwork.formatDouble(neuron.getValue());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 5, baseY + textSpace * 4);
		// g2d.drawRect(baseX, baseY, neuronHeight, neuronHeight);
		neuron.X = baseX + neuronHeight;
		neuron.Y = baseY + (neuronHeight / 2);

		int numInputs = neuron.getInputs().size();
	
		// g2d.setFont(new Font("Monaco", Font.PLAIN, 10));
		if (numInputs > DrawNeuralNetwork.NUM_NURONS_TODRAW) {
			if (neuron.getName().endsWith("-0")) {
				textStr = " Weights      Weights*input";
				chararr = textStr.toCharArray();
				g2d.drawChars(chararr, 0, chararr.length, baseX - 200, baseY - 10);
			}
			paintInputsInSquare(g, 0, baseY, false);
			paintInputsInSquare(g,neuronHeight +10, baseY, true);
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
			drawFanOut(g2d, min, max, baseX, baseY, neuron);
			return;
		}

	}

	protected void drawFanOut(Graphics g2d, double min, double max, int baseX, int baseY, Neuron neuron) {

		int fanoutXPos = baseX - 60;
		int fandoutYChange = this.neuronHeight / neuron.getInputs().size();
		for (int x = 0; x < neuron.getWeights().size(); x++) {
			double weight = neuron.getWeights().get(x);
			double input = neuron.getInputs().get(x).getValue();
			double value = weight * 1;
			Color rgb = DrawNeuralNetwork.faderYellowToRed(max, min, value);
			g2d.setColor(rgb);
			Polygon poly = new Polygon();
			poly.addPoint(baseX, baseY + this.neuronHeight / 2);
			int yPoint = baseY + (x * fandoutYChange);
			poly.addPoint(fanoutXPos, yPoint);
			yPoint += fandoutYChange;
			poly.addPoint(fanoutXPos, yPoint);
			g2d.drawPolygon(poly);
		}

	}
	int scale;
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
	protected void createImage(int baseX, int baseY, boolean includeInput) {
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

		int sqRoot = (int) Math.sqrt(neuron.getWeights().size());

		pictureHeight = neuron.getWeights().size() / sqRoot;
		if (pictureHeight == 0) {
			int debugME = 0;
		}
		scale = this.neuronHeight / pictureHeight;
	
		BufferedImage img = new BufferedImage(pictureHeight * scale, pictureHeight * scale, BufferedImage.TYPE_INT_RGB);

		
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
				
				if (w1 >= pictureHeight*scale) {
					w1 = 0;
				
					h1+= scale;
				}
			}
		}

		if(includeInput) {
			this.imageWithInputs = img;
		}else {
			this.imageWeightsOnly =img;
		}
		int df = 0;
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
			  this.createImage(baseX, baseY, includeInput);
			}
			g.drawImage(this.imageWithInputs, baseX, baseY, pictureHeight * scale, pictureHeight * scale, new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		if (includeInput == false) {
			if( this.imageWeightsOnly == null) {
				  this.createImage(baseX, baseY, includeInput);
				}
				g.drawImage(this.imageWeightsOnly, baseX, baseY, pictureHeight * scale, pictureHeight * scale, new ImageObserver() {

					@Override
					public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
						// TODO Auto-generated method stub
						return false;
					}
				});
			
		}
		

		int df = 0;
	}

	public void showFullWeights(Graphics g2d, int index, double min, double max, int baseX, int baseY, Neuron neuron) {

		Neuron connNu = neuron.getInputs().get(index);

		double weight = neuron.getWeights().get(index);
		double input = neuron.getInputs().get(index).getValue();
		double value = weight * input;

		g2d.setColor(DrawNeuralNetwork.faderYellowToRed(max, min, value));
		g2d.drawLine(connNu.X, connNu.Y, baseX, baseY + (neuronHeight / 2));

		// print the weight along the sloped line between inout Neuron and this Neuron
		int centerX = (baseX - connNu.X) / 2 + connNu.X;
		int centerY = (baseY + (neuronHeight / 2) - connNu.Y) / 2 + connNu.Y;
		String textStr = " w " + DrawNeuralNetwork.formatDouble(value);
		char[] chararr = textStr.toCharArray();
		Font existing = g2d.getFont();
		AffineTransform affineTransform = new AffineTransform();

		double rise = baseY - connNu.Y;
		rise += (neuronHeight / 2);
		double length = baseX - connNu.X;
		double slope = (rise) / (length);

		// affineTransform.setToScale(1.5, 1.5);
		affineTransform.rotate(Math.atan(slope), 0, 0);

		Font rotatedFont = g2d.getFont().deriveFont(affineTransform);
		g2d.setFont(rotatedFont);
		g2d.setColor(Color.black);
		if (slope <= 0) {
			g2d.drawChars(chararr, 0, chararr.length, centerX - 5, centerY);
		} else {
			g2d.drawChars(chararr, 0, chararr.length, centerX + 5, centerY);
		}
		g2d.setFont(existing);
	}
}
