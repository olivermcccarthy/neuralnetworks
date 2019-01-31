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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DrawPanel extends JPanel {

	static JButton button = new JButton("Click");

	public DrawPanel(ActionListener listener) {
		button.addActionListener(listener);
		this.add(button);
	}

	public static int[][] input = null;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int baseX = 0;
		int baseY = 60;

		if (input != null) {
			int scale = 4;
			BufferedImage img = new BufferedImage(pictureWidth * scale, pictureHeight * scale,
					BufferedImage.TYPE_INT_ARGB);

			for (int h = 0; h < pictureHeight*scale; h++) {
				for (int w = 0; w < pictureWidth; w++) {
					int greyScale = 255 - input[h/scale][w];
					for(int z =0; z < scale; z++) {
						img.setRGB(w*scale +z, h, new Color(greyScale,greyScale,greyScale).getRGB());
					}	

				}
			}

		
			g.drawImage(img, baseX, baseY, pictureWidth * scale, pictureHeight * scale, new ImageObserver() {

				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});

		}
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
		int screenWidth = 1000;
		int diffX = screenWidth / Layer.layers.size();
		int shiftY = 0;

		for (Layer layer : Layer.layers) {
			if (layer.neurons.size() > 20) {
				continue;
			}
			int diffY = 0;

			int startLevel = (maxLevelSize - layer.neurons.size()) / 2;
			shiftY = (startLevel * 150);
			diffY += shiftY;
			if (layer.neurons.size() > 20) {

			} else {
				for (Neuron nu : layer.neurons) {

					paintNewronTopX(g, nu, baseX + diffX, baseY + diffY);
					diffY += 150;
				}
			}
			baseX += diffX;

		}

	}

	public static String getDBL(double value) {

		String doubleStr = "" + (Math.floor(value * 1000) / 1000);

		return doubleStr;
	}

	static int neuronWidth = 100;
	static int neuronHeight = 100;

	static int pictureHeight = 28;

	static int pictureWidth = 28;

	/**
	 * Paint a picture of the inputs neurons contribution to this neurons sigmoid If
	 * we have 100 input neurons we can paint a 10 by ten picture where the first
	 * row is neuron 0 to 9. The neurons with the biggest contribution get the
	 * brightest color.
	 * 
	 * @param g
	 * @param neuron
	 * @param baseX
	 * @param baseY
	 */
	protected void paintInputsInSquare(Graphics g, Neuron neuron, int baseX, int baseY, int pictureHeight,
			int pictureWidth, int scale) {
		int w = 0;
		int h = 0;
		double maxSize = 1;

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
		maxSize = maxSize /neuron.getValue();
		g.setColor(Color.WHITE);
		
		BufferedImage img = new BufferedImage(pictureWidth * scale, pictureHeight * scale, BufferedImage.TYPE_INT_RGB);
	
		int x =0;
		for (h = 0; h < pictureHeight * scale; h++) {
			x = (pictureWidth )*(h/scale);
			for (w = 0; w < pictureWidth ; w++) {
				
				double weight = neuron.weights.get(x);
				double input = neuron.inputs.get(x).getValue();
				x++;
				double currentValue = weight*input*neuron.getValue();
				double fractionOfMax = currentValue/maxSize;
				if(fractionOfMax < 0) {
					fractionOfMax *=-1;
				}
				int rgb =  faderYellowToRed(fractionOfMax).getRGB();
				for(int z =0; z < scale; z++) {
					img.setRGB(w*scale+z, h, rgb);
				}
			}
			
		}

		g.drawImage(img, baseX - 150, baseY, pictureWidth * scale, pictureHeight * scale, new ImageObserver() {

			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		int df = 0;
	}
	/**
	 *  if you pass 0 you get yellow. if you pass 1 you get bright red
	 *  Values between fade from yellow to red
	 *
	 */
	private Color faderYellowToRed(double fade){
		
		   fade = fade*255;
		   if(fade > 255) {
			   int debugME =0;
		   }
		   if(fade < 128) {
			   fade = fade *1.3;
			   return new Color(255, 255, 180-(int)fade); 
		   }
	       return new Color(255, 255-(int)fade, 0); 
	    }
	
	
	protected void paintNewronTopX(Graphics g, Neuron neuron, int baseX, int baseY) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		String textStr = "  " + neuron.name;
		char[] chararr = textStr.toCharArray();
		g2d.setColor(faderYellowToRed(neuron.getValue()));
		g2d.fillOval(baseX, baseY, neuronWidth, neuronHeight);
		g2d.setColor(Color.BLACK);
		g2d.drawChars(chararr, 0, chararr.length, baseX + 15, baseY + 25);

		;

		textStr = " b " + getDBL(neuron.bias);
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 15, baseY + 40);
		textStr = " err " + getDBL(neuron.errorVar);
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 15, baseY + 55);

		textStr = " value " + getDBL(neuron.getValue());
		chararr = textStr.toCharArray();
		g2d.drawChars(chararr, 0, chararr.length, baseX + 15, baseY + 70);
		// g2d.drawRect(baseX, baseY, neuronWidth, neuronHeight);
		neuron.X = baseX + neuronWidth;
		neuron.Y = baseY + (neuronHeight / 2);

		int numInputs = neuron.inputs.size();
		int newY = baseY + 15;

		Color[] colors = new Color[] { Color.BLUE, Color.black, Color.RED, new Color(0, 102, 0), Color.MAGENTA,
				new Color(153, 102, 51) };
		int colorIndex = 0;

		// g2d.setFont(new Font("Monaco", Font.PLAIN, 10));
		if (numInputs > 100) {
			paintInputsInSquare(g, neuron, baseX, baseY, 28, 28,4);
			return;
		}
		
		if (numInputs > 0) {

			int nI = 0;
            double max =0;
			for (int x = 0; x < neuron.weights.size(); x++) {
				double weight = neuron.weights.get(x);
				double input = neuron.inputs.get(x).getValue();
				double value = weight*input;
				if(value < 0) {
					value *= -1;
				}
				if(value > max) {
					max = value;
				}
			}
			max = max /neuron.getValue();
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
				double value = weight*input;
				if(value < 0) {
					value *= -1;
				}
				Font existingFont =g2d.getFont();
				if(value > 0.2*max) {
				
				  g2d.setColor(faderYellowToRed(value/max));
				  g2d.drawLine(connNu.X, connNu.Y, baseX , baseY + (neuronHeight / 2));

				  
				  int centerX = (baseX - connNu.X)/2 + connNu.X;
				  int centerY = ( baseY - connNu.Y + neuronHeight)/2 + connNu.Y;
					textStr = " w " + getDBL(inpu);
					chararr = textStr.toCharArray();
					AffineTransform affineTransform = new AffineTransform();
				
					
					double slope = ( baseY - connNu.Y + (neuronHeight/2))/(baseX - connNu.X);
					
					affineTransform.setToScale(1.5, 1.5);
					affineTransform.rotate(Math.atan(slope), 0, 0);
					
					Font rotatedFont = g2d.getFont().deriveFont(affineTransform);
					g2d.setFont(rotatedFont);
					 g2d.setColor(Color.black);
					g2d.drawChars(chararr, 0, chararr.length, centerX -10, centerY);
				}
				
				g2d.setFont(existingFont);
				//g2d.drawLine(baseX - 60, baseY + (neuronHeight / 2), baseX, baseY + (neuronHeight / 2));


				newY += 15;
				nI++;
			}
		}
	}

	static JFrame frame;

	static Object waitForMe = new Object();;
	static boolean dontStop = false;

	public static void stopAMinute(String msg) {
		if (dontStop) {
			return;
		}
		try {
			button.setText(msg);
			frame.repaint();
			synchronized (waitForMe) {
				waitForMe.wait(100000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showNeurons() {
		frame = new JFrame();
		frame.setSize(1000, 1000);

		JPanel panel = new DrawPanel(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
				}

			}
		});
		JScrollPane scroll = new JScrollPane(panel);
		panel.setPreferredSize(new Dimension(1000, 2000));
		frame.setVisible(true);
		panel.setBackground(new Color(230, 255, 255));
		frame.getContentPane().add(scroll);

		frame.repaint();

	}
}