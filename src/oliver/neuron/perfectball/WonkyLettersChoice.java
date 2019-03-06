package oliver.neuron.perfectball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	static String [] LETTERS= new String[] {"E","F","H"}; 
	JButton agreeButton;
	JComboBox chosen = new JComboBox();
	
	
	Object waitForMe = "";
	static WonkyLettersChoice me;
	ButtonGroup group = new ButtonGroup();
	String message;
	WonkyLetters innerPanel = new WonkyLetters();
	
	boolean agreeWithNeuron = false;
	static int preferedWidth=200;
	public WonkyLettersChoice() {
		agreeButton = new JButton("Agree or change ");

		
		this.setLayout(null);
		this.add(innerPanel);
		this.setPreferredSize(new Dimension(preferedWidth,260));
		
		
		
		agreeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					agreeWithNeuron=true;

				}

			}
		});
		
		
		
        this.add(agreeButton);
        this.add(chosen);
		this.setLayout(null);
	
		chosen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					
				}

			}
		});
		
		for(String letter : LETTERS) {
			chosen.addItem(letter);
		}
		chosen.setBounds(preferedWidth*2/3, 0, preferedWidth/3, 30);
		agreeButton.setBounds(0, 0, preferedWidth*2/3, 30);
		innerPanel.setBounds(0, 60, preferedWidth, 200);
		
		
		//this.add(dont);
	}

	public void setDrawPanel(DrawNeuralNetwork dN) {
	
		//dN.addButton(this.dont);
	}
	

	/**
	 * User agrees we set expected[neuronChoce] to 1 and all others to 0
	 * User changes choice  we set expected[userChoice] to 1 and all others to 0
	 * User does nothing we set expected to in for all values and nothing is learned
	 * @param overallPanel
	 * @param in
	 * @return
	 */
	public double[] like(DrawNeuralNetwork overallPanel, double[] in) {
		
		this.agreeWithNeuron = false;
		double[] expected = new double[2];
		int neuronChoice =-1;
		double max =0;
		for(int x =0; x < in.length; x++) {
			if(in[x] > max) {
				max = in[x];
				neuronChoice= x;
			}
		}
		this.chosen.setSelectedIndex(neuronChoice);
		
		
		synchronized (waitForMe) {
			try {
				waitForMe.wait(overallPanel.getSleepTime());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(this.agreeWithNeuron) {
			for(int x =0; x < in.length; x++) {
				expected[x]=0;
				if(x == neuronChoice) {
					expected[x]=1;
				}
			}
		}else {
			if(this.chosen.getSelectedIndex() != neuronChoice) {
				neuronChoice=this.chosen.getSelectedIndex() ;
				for(int x =0; x < in.length; x++) {
					expected[x]=0;
					if(x == neuronChoice) {
						expected[x]=1;
					}
				}	
			}else {
				// No choice nothing to learn
				for(int x =0; x < in.length; x++) {
					expected[x]=in[x];
				}
			}
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
