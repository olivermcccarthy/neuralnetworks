package oliver.neuron.perfectball;

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

import oliver.neuron.ui.DrawNeuralNetwork;
import oliver.neuron.ui.HelpPanel;

public class BallDisplay extends JPanel {

	
	double roundness =0.5;
	double redness = 0.7;
    public double getRoundness() {
		return roundness;
	}





	public void setRoundness(double roundness) {
		this.roundness = roundness;
	}





	public double getRedness() {
		return redness;
	}





	public void setRedness(double redness) {
		this.redness = redness;
	}





	static BallDisplay me;
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension dimension = this.getSize();
		int height = dimension.height;
		int width = dimension.width;
		g.setColor(new Color((int)(255*redness),0,0));
		g.fillOval(0, 0, width, (int)(height*roundness));
		int rectWidth = 3;


	}

	
	
	
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("test");
		frame.setSize(200, 200);
		BallDisplay panel = new BallDisplay();
		frame.add(panel);
		frame.setVisible(true);
		
	}
}
