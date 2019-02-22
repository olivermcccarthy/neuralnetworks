package oliver.neuron.neonnumbers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import oliver.neuron.ui.DrawPanel;

public class NeonDisplay extends JPanel {

	static int[][] neonCodes = new int[10][7];
	static {
		neonCodes[0] = new int[] { 1, 1, 1, 0, 1, 1, 1 };
		neonCodes[1] = new int[] { 0, 0, 1, 0, 0, 0, 1 };
		neonCodes[2] = new int[] { 0, 1, 1, 1, 1, 1, 0 };
		neonCodes[3] = new int[] { 0, 1, 1, 1, 0, 1, 1 };
		neonCodes[4] = new int[] { 1, 0, 1, 1, 0, 0, 1 };
		neonCodes[5] = new int[] { 1, 1, 0, 1, 0, 1, 1 };
		neonCodes[6] = new int[] { 1, 0, 0, 1, 1, 1, 1 };
		neonCodes[7] = new int[] { 0, 1, 1, 0, 0, 0, 1 };
		neonCodes[8] = new int[] { 1, 1, 1, 1, 1, 1, 1 };
		neonCodes[9] = new int[] { 1, 1, 1, 1, 0, 0, 1 };
	}
	static int number = 0;
    static NeonDisplay me;
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dimension = this.getSize();
		int height = dimension.height;
		int width = dimension.width;
		g.setColor(Color.red);
		int rectWidth = 3;

		int[] neonCode = neonCodes[number];
		if (neonCode[0] == 1) {
			g.fillRect(0, 0, rectWidth, height / 2);
		}
		if (neonCode[1] == 1) {
			g.fillRect(0, 0, width, rectWidth);
		}
		if (neonCode[2] == 1) {
			g.fillRect(width - rectWidth, 0, rectWidth, height / 2);
		}
		if (neonCode[3] == 1) {
			g.fillRect(0, height / 2, width, rectWidth);
		}
		if (neonCode[4] == 1) {
			g.fillRect(0, height / 2, rectWidth, height / 2);
		}
		if (neonCode[5] == 1) {
			g.fillRect(0, height - rectWidth, width, rectWidth);
		}
		if (neonCode[6] == 1) {
			g.fillRect(width - rectWidth, height / 2, rectWidth, height / 2);
		}
		for (int x = 0; x < 4000; x++) {
			int value = this.getColorModel().getRGB(x);
			if (value > 0) {
				System.out.println(value);
			}

		}

	}

	public int[] createImage() {

		int w = getWidth();
		int h = getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		paint(g);
		g.dispose();
		int [] rgb = bi.getRGB(0, 0, w, h, null,0,w);
		int [] reds= new int[rgb.length];
		int numReds=0;
		for(int f =0; f < rgb.length; f++) {
			int red = new Color(rgb[f]).getRed();
			if(red > 250) {
		      	reds[f]=1;
		      	numReds++;
			}
		}
		return reds;
	}
	public int[][] saveImage(int sampleRate) {

		int w = getWidth();
		int h = getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		paint(g);
		g.dispose();
				int [][] reds= new int[h/sampleRate][w/sampleRate];
		int numReds=0;
	    int widthSamples = w/sampleRate;
	    int heightSamples= h/sampleRate;
	    for(int h1 = 0; h1 <  heightSamples; h1++) {
		for(int w1 = 0; w1 <  widthSamples; w1++) {
			int redC= new Color(bi.getRGB(w1 * sampleRate, (h1*sampleRate))).getRed();
			if(redC > 240) {
				reds[h1][w1] = 1;
			}
			
		}
	    }
		return reds;
	}
	
	public static List<int[][]> drawImages(List<Integer> labels){
		ArrayList<int[][]> images = new ArrayList<int[][]>();
		JFrame frame = new JFrame();
		frame.setSize(100, 100);

		NeonDisplay panel = new NeonDisplay();
		NeonDisplay.me = panel;
		frame.setVisible(true);
		panel.setBackground(new Color(230, 255, 255));
	
		frame.setLayout(null);
		
		frame.add(panel);
		panel.setBounds(0,0,30,60);
		frame.repaint();
		
		for (int d = 0; d < 10; d++) {
			NeonDisplay.number = d;
			labels.add(d);
			int[][] imageData = NeonDisplay.me.saveImage(3);
			images.add(imageData);
			frame.repaint();
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return images;
	}
	
	public static void main(String[] args) {
		
		drawImages(new ArrayList<Integer>());
		
	}
}