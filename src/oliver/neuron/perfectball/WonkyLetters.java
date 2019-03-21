package oliver.neuron.perfectball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


import oliver.neuron.ui.DrawNeuralNetwork;

/**
 * Draw a pseudo random polygon representing a Letter.
 * Split the panel into 0-7 columns and A-H rows
 * The polygon takes a route through the panel stepping on each square in the route.
 * Example B1-C2-F2  A line from a random place in square B1 to a random place in square C2 to a random place in F2 
 * @author oliver
 *
 */
public class WonkyLetters extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -985471666659201980L;
	
	static WonkyLetters me;

	/**
	 * Current Polygon BeingDisplayed
	 */
	protected Polygon poly;
    /**
     * Routes the polygon can follow
     */
	static String[] routes = new String[] { "A0-A7-B7-B1-C1-D1-D7-E7-E1-F1-G1-G7-H7-H0-A0", // Wonky E
			"A0-A7-B7-B1-C1-D1-D7-E7-E1-F1-G1-H1-H0-A0" 
			, // Wonky E
			"A0-A1-D1-D6-A6-A7-H7-H6-E6-E2-H2-H0-A0" // Wonky H		
	}; // WonkyF 
	
	
	public WonkyLetters() {
	
		this.setLayout(null);
		
	}

	
	

	
	

	/**
	 * Draw one of the ploygons in routes 
	 */
	protected void newPoly() {
		Dimension dimension = this.getSize();
		poly = new Polygon();
		int height = dimension.height ;
		int width = dimension.width;

		// Divide the square in 64 squares 8 across 8 down
		// Draw an E or and F
		// E Goes in squares
		// 1 row 0-6
		// A0-A7-B7-B2-C2-D2-D7-E7-E2-F2-G2-G7-H7-H1-A1
		// 0 6 12 18 24
		int choice = (int) (Math.random() * routes.length);
		
		String route = routes[choice];
		String[] routeArray = route.split("-");
	
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
		WonkyLetters panel = new WonkyLetters();
		frame.add(panel);
		frame.setVisible(true);

	}
}
