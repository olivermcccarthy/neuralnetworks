package oliver.neuron.perfectball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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
	JRadioButton eButton;
	JRadioButton fButton;
	
	boolean itsAnE = false;
	boolean itsAndF = false;
	Object waitForMe = "";
	static WonkyLettersChoice me;
	ButtonGroup group = new ButtonGroup();
	String message;
	WonkyLetters innerPanel = new WonkyLetters();
	JTextPane textPane = new JTextPane();
	public WonkyLettersChoice() {
		eButton = new JRadioButton("E");

		
		this.setLayout(null);
		this.add(innerPanel);
		this.setPreferredSize(new Dimension(200,260));
		innerPanel.setBounds(0, 20, 200, 200);
		textPane.setBounds(0, 220, 200, 40);
		this.add(textPane);
		eButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					itsAnE = true;
					itsAndF = false;

				}

			}
		});
		eButton.setBounds(0, 0, 50, 20);
		
		//this.add(toggle);
		fButton = new JRadioButton("F");
		group.add(eButton);
		group.add(fButton);
        this.add(eButton);
        this.add(fButton);
		this.setLayout(null);
		fButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					itsAndF = true;
					itsAnE = false;
				}

			}
		});
		fButton.setBounds(50, 0, 50, 20);
		//this.add(dont);
	}

	public void setDrawPanel(DrawNeuralNetwork dN) {
	
		//dN.addButton(this.dont);
	}
	

	
	public double[] like(DrawNeuralNetwork overallPanel, double[] in) {
		itsAnE = false;
				 itsAndF = false;
		double[] expected = new double[2];
		if(in[0] > in[1]) {
			this.eButton.setSelected(true);
			this.fButton.setSelected(false);
		}else {
			this.eButton.setSelected(false);
			this.fButton.setSelected(true);
		}
		expected[0] = in[0];
		expected[1] = in[1];
		
		synchronized (waitForMe) {
			try {
				waitForMe.wait(overallPanel.getSleepTime());
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
}
