package oliver.neuron.perfectball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import oliver.neuron.ui.DrawNeuralNetwork;

/**

 *
 */
public class WonkyLettersChoice extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -985471666659201980L;

	static String[] LETTERS = new String[] { "E", "F", "H" };

	Object waitForMe = "";
	static WonkyLettersChoice me;
	ButtonGroup group = new ButtonGroup();
	String message;
	WonkyLetters innerPanel = new WonkyLetters();
	//JTextPane resultPane = new JTextPane();

	int selectedCoice = -1;
	static int preferedWidth = 200;

	public WonkyLettersChoice() {

		this.setLayout(null);
		this.add(innerPanel);
	//	this.add(resultPane);
		this.setPreferredSize(new Dimension(preferedWidth, 280));

		// this.add(chosen);
		this.setLayout(null);

		int w = 0;
		int id = 0;
		for (String letter : LETTERS) {

			JButton but = new JButton(letter);
			final int oID = id;
			but.setBounds(0, w, 200, 30);
			this.add(but);
			group.add(but);
			but.addActionListener(new ActionListener() {

				int ourID = oID;

				@Override
				public void actionPerformed(ActionEvent e) {
					synchronized (waitForMe) {
						waitForMe.notifyAll();
						selectedCoice = ourID;

					}

				}
			});
			id++;

			w += 30;
		}

		// chosen.setBounds(preferedWidth * 2 / 3, 0, preferedWidth / 3, 30);

		//resultPane.setBounds(0, 0, preferedWidth, 60);
		
		innerPanel.setBounds(0, 90, preferedWidth, 200);

		// this.add(dont);
	}

	public void setDrawPanel(DrawNeuralNetwork dN) {

		// dN.addButton(this.dont);
	}

	/**
	 * User agrees we set expected[neuronChoce] to 1 and all others to 0 User
	 * changes choice we set expected[userChoice] to 1 and all others to 0 User does
	 * nothing we set expected to in for all values and nothing is learned
	 * 
	 * @param overallPanel
	 * @param in
	 * @return
	 */
	public double[] like(BallTrial trial, DrawNeuralNetwork overallPanel, double[] in) {
		//resultPane.setBackground(this.getBackground());
		double[] expected = new double[in.length];
		int neuronChoice = -1;
		double max = 0;
		for (int x = 0; x < in.length; x++) {
			if (in[x] > max) {
				max = in[x];
				neuronChoice = x;
			}
		}
		
		Enumeration<AbstractButton> enumk =group.getElements();
		int y=0;
		while (enumk.hasMoreElements()) {
			if(y == neuronChoice) {
				
			enumk.nextElement().setText(LETTERS[y] + "- Network is correct" );
			}else {
				enumk.nextElement().setText(LETTERS[y] + "- Network is wrong" );
			}
			y ++;
		}
		
        String gotLetter = LETTERS[neuronChoice];
		String text = String.format("Is it a %s? \n  ",
				LETTERS[neuronChoice]);
		//this.resultPane.setText(text);
		//this.resultPane.setFont(this.resultPane.getFont().deriveFont(18.0f));
		selectedCoice = -1;
		synchronized (waitForMe) {
			try {
				waitForMe.wait(overallPanel.getSleepTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (selectedCoice != -1) {
			if (this.selectedCoice != neuronChoice) {
				trial.numWrong++;

			}
			neuronChoice = this.selectedCoice;
			for (int x = 0; x < in.length; x++) {
				expected[x] = 0;
				if (x == neuronChoice) {
					expected[x] = 1;
				}
			}

		} else {
			// No choice nothing to learn
			for (int x = 0; x < in.length; x++) {
				expected[x] = in[x];
			}
		}
		String expectedLetter = LETTERS[neuronChoice];
		overallPanel.waitForUserClick(trial, String.format("Expected %s , Got %s", expectedLetter,gotLetter),false,true);
		return expected;
	}

	protected void newPoly() {
		this.innerPanel.newPoly();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("test");
		frame.setSize(200, 200);
		WonkyLettersChoice panel = new WonkyLettersChoice();
		frame.add(panel);
		frame.setVisible(true);

	}
	
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		Enumeration<AbstractButton> enumk =group.getElements();
		while (enumk.hasMoreElements()) {
			enumk.nextElement().setEnabled(enabled);
		}
		
	}
}
