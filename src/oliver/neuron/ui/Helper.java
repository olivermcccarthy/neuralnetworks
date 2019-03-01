package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Helper {

	
	static public int[][] saveImage(Component comp, int sampleRate) {

		int w = comp.getWidth();
		int h = comp.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		comp.paint(g);
		g.dispose();
		int[][] reds = new int[h / sampleRate][w / sampleRate];
		int numReds = 0;
		int widthSamples = w / sampleRate;
		int heightSamples = h / sampleRate;
		for (int h1 = 0; h1 < heightSamples; h1++) {
			for (int w1 = 0; w1 < widthSamples; w1++) {
				int redC = new Color(bi.getRGB(w1 * sampleRate, (h1 * sampleRate))).getRed();
				if (redC > 240) {
					reds[h1][w1] = 1;
				}

			}
		}
		return reds;
	}
	
	
	static public double[] saveImageAsDouble(Component comp, int height, int width) {

		int w = comp.getWidth();
		int h = comp.getHeight();
		if(w == 0 || h == 0) {
			return null;
		}
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		comp.paint(g);
		g.dispose();
		
		int sampleRate = w/width;
		if(h < w) {
			sampleRate = h/height;
		}
		
		double[] reds = new double[height * width];
		
		int widthSamples = w / sampleRate;
		int heightSamples = h / sampleRate;
		int index =0;
		for (int h1 = 0; h1 < heightSamples; h1++) {
			for (int w1 = 0; w1 < widthSamples; w1++) {
				int redC = new Color(bi.getRGB(w1 * sampleRate, (h1 * sampleRate))).getRed();
				if (redC > 240) {
					reds[index] = 1;
				}
				else {
					reds[index] = 0;
				}
                index ++;
			}
		}
		return reds;
	}
}
