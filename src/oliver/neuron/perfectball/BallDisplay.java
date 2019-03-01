package oliver.neuron.perfectball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import oliver.neuron.ui.DrawNeuralNetwork;
import oliver.neuron.ui.HelpPanel;

public class BallDisplay extends JPanel {

	JButton toggle;
	JButton dont;
	static double perfectRedness = 0.7;
	static double perfectRoundness = 0.8;

	Object waitForMe = "";

	public BallDisplay() {
		toggle = new JButton("E");

		this.setLayout(null);
		toggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					itsAnE = true;
					itsAndF = false;

				}

			}
		});
		toggle.setBounds(0, 0, 50, 20);
		//this.add(toggle);
		dont = new JButton("F");

		this.setLayout(null);
		dont.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					itsAndF = true;
					itsAnE = false;
				}

			}
		});
		dont.setBounds(50, 0, 50, 20);
		//this.add(dont);
	}

	public void setDrawPanel(DrawNeuralNetwork dN) {
		dN.addButton(this.toggle);
		dN.addButton(this.dont);
	}
	boolean itsAnE = false;
	boolean itsAndF = false;

	public double[] like(double[] in) {
		itsAnE = false;
				 itsAndF = false;
		double[] expected = new double[2];
		expected[0] = in[0];
		expected[1] = in[1];
		synchronized (waitForMe) {
			try {
				waitForMe.wait(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (itsAnE) {
			expected[0] = 1;
			expected[1] = 0;
		}
		if (itsAndF) {
			expected[0] = 0;
			expected[1] = 1;
		}
		return expected;
	}

	static BallDisplay me;

	protected Polygon poly;

	static String[] routes = new String[] { "A0-A7-B7-B1-C1-D1-D7-E7-E1-F1-G1-G7-H7-H0-A0",
			"A0-A7-B7-B1-C1-D1-D7-E7-E1-F1-G1-H1-H0-A0" };

	protected void newPoly() {
		Dimension dimension = this.getSize();
		poly = new Polygon();
		int height = dimension.height ;
		int width = dimension.width;

		// Divide the square in 64 squares 8 across 8 down
		// Draw an E or and F
		// Flip a coin as to whether to add the point or not

		// E Goes in squares
		// 1 row 0-6
		// A0-A7-B7-B2-C2-D2-D7-E7-E2-F2-G2-G7-H7-H1-A1
		// 0 6 12 18 24
		int choice = (int) (Math.random() * routes.length);
		if(choice == 0) {
			this.itsAnE = true;
			this.itsAndF = false;
		}else {
			this.itsAnE = false;
			this.itsAndF = true;
		}
		String route = routes[choice];
		String[] routeArray = route.split("-");
		int lastRow = -1;
		int lastCol = -1;
		poly = new Polygon();
		int baseY = 0;
		int baseX = 0;
		double squareWidth = width / 8;
		double squareHeight = height / 8;
		for (String pointStr : routeArray) {

			int row = pointStr.charAt(0) - 'A';
			int col = pointStr.charAt(1) - '0';

			baseY = (int) (row * squareHeight) ;
			baseX = (int) (col * squareWidth);
			int randX = (int) (Math.random() * squareWidth);
			int randY = (int) (Math.random() * squareHeight);
			poly.addPoint(baseX + randX, baseY + randY);
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(new Color(255, 0, 0));
		if (poly == null) {
			newPoly();
		}

		g.fillPolygon(poly);

	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("test");
		frame.setSize(200, 200);
		BallDisplay panel = new BallDisplay();
		frame.add(panel);
		frame.setVisible(true);

	}
}
