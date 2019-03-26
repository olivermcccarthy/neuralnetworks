package oliver.neuron.ui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * We depict weights as colors ranging from red ( most positive weights) to dark blue (most negative weights) <br>
 * This panel shows the range of colors and provides static method  weightAsColor to map a weight to a color
 *  <li> >= 50% of max weight bright red
 *  <li> >= 20%  < 50% of max weight dull red
 *  <li> > 0% < 20& of max yellow
 *  
 * @author oliver
 *
 */
public class ColorLegend extends JPanel{

	/**
	 * Positive weights are represented by these red colors.
	 */
	static Color[] shadesPositive = new Color[] { new Color(255, 0, 0), new Color(225, 125, 125),
			new Color(255, 255, 204) };

	/**
	 * Negative weights are represented by these blue colors.
	 */
	static Color[] shadesNegative = new Color[] { new Color(0, 0, 105), new Color(0, 0, 255),
			new Color(153, 255, 255) };

	/**
	 * Map a weight to a color depending on what percentage of max or min value it is
	 *
	 */
	protected static Color weightAsColor(double maxValue, double minValue, double currentValue) {

		if (maxValue == 0 && minValue == 0) {
			return shadesNegative[0];
		}
		if (currentValue < 0) {
			return negativeWeightToColor(minValue * -1, currentValue * -1);
		}
		double fraction = currentValue / maxValue;
		if (fraction > 0.5) {
			return shadesPositive[0];
		}
		if (fraction > 0.2) {
			return shadesPositive[1];
		}
		return shadesPositive[2];
	}

	private static Color negativeWeightToColor(double minValue, double currentValue) {

		double fraction = currentValue / minValue;
		if (fraction > 0.5) {
			return shadesNegative[0];
		}
		if (fraction > 0.2) {
			return shadesNegative[1];
		}
		return shadesNegative[2];
	}

	/**
	 * Show the mapping between weights an color. Bright red is a weight that is 50% or more of Max Weight
	 * Dark blue is a weight that is 50% or more of mon Weight
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int x =20;
		int y =0;
		showLegend(g, x, y, shadesPositive[0], "50 -> 100% of max weight");
		showLegend(g, x, y + 20, shadesPositive[1], "20 -> 50% of max weight ");
		showLegend(g, x, y + 40, shadesPositive[2], "0 -> 20% of max weight ");
		showLegend(g, x, y + 60, shadesNegative[2], "0 -> 20% of min weight ");
		showLegend(g, x, y + 80, shadesNegative[1], "20 -> 50% of min weight");
		showLegend(g, x, y + 100, shadesNegative[0], "50 -> 100% of min weight ");

	}

	/**
	 * Paint one segment of the legend
	 * @param g
	 * @param x
	 * @param y
	 * @param color
	 * @param message
	 */
	private void showLegend(Graphics g, int x, int y, Color color, String message) {

		char[] chararr = message.toCharArray();
		g.setColor(Color.BLACK);
		g.drawChars(chararr, 0, chararr.length, x, y + 15);
		g.setColor(color);
		g.fillRect(x - 20, y, 20, 20);

	}
}
