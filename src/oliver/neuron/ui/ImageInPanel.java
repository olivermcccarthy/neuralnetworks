package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;



public class ImageInPanel extends JPanel{
	/**
	 * We will draw the input Image on the left
	 */
	private int[][] inputImage = null;
	int pictureScale = 1;

	/**
	 * Type of input data. 1s and zeros or greyscale
	 */
	public enum PICTURE_TYPE {
		BINARY, GREYSCALE
	};

	PICTURE_TYPE pictureType;

	boolean repaintImage = false;
	public void setInfo(int[][] inputImage, int pictureScale, PICTURE_TYPE pictureType) {
		this.inputImage = inputImage;
		this.pictureScale = pictureScale;
		this.pictureType = pictureType;
	
		repaintImage =  true;
	}
	BufferedImage img = null;

	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
		int baseY =0;
		int pictureWidth = inputImage[0].length;
		int pictureHeight = inputImage.length;
		if (repaintImage) {
			repaintImage = false;
			img = new BufferedImage(pictureWidth * pictureScale, pictureHeight * pictureScale,
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
