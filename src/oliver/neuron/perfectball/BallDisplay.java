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

	
	double roundness =0.5;
	double redness = 0.7;
	
	JButton toggle;
	JButton dont;
	static double perfectRedness = 0.7;
	static double perfectRoundness = 0.8;
    public double getRoundness() {
		return roundness;
	}

	Object waitForMe = "";
	

    public BallDisplay() {
    	toggle = new JButton("like");
    	
    	this.setLayout(null);
    	toggle.addActionListener(new ActionListener() {

    		@Override
    		public void actionPerformed(ActionEvent e) {
    			synchronized (waitForMe) {
    				waitForMe.notifyAll();
    				like = true;
    				
    			}

    		}
    	});
    	toggle.setBounds(0, 0, 60, 20);
    	this.add(toggle);
        dont = new JButton("dont");
    	
    	this.setLayout(null);
    	dont.addActionListener(new ActionListener() {

    		@Override
    		public void actionPerformed(ActionEvent e) {
    			synchronized (waitForMe) {
    				waitForMe.notifyAll();
    				 dontlike = true;
    				
    			}

    		}
    	});
    	dont.setBounds(60, 0, 60, 20);
    	this.add(dont);
    }
    boolean like = false;
    boolean dontlike = false;
    public double [] like(double [] in) {
    	like = false;
    	dontlike = false;
double [] expected = new double[2];
          expected[0]= in[0];
          expected[1]=in[1];
    	  synchronized (waitForMe) {
				try {
					waitForMe.wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    	  
    	if(like) {
    		expected[0]=1;
    	}
    	if(dontlike) {
    		expected[1]=1;
    	}
    	return expected;
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
	
	protected Polygon poly;
	protected void newPoly() {
		Dimension dimension = this.getSize();
		 poly = new Polygon();
		 int height = dimension.height;
			int width = dimension.width;
		
			
			
			// Divide the square in 6 squares 3 across 2 down
			// Randomly pick a point in each square
			// Flip a coin as to whether to add teh point or not
			
			 poly = new Polygon();
			int baseY=0;
			int baseX=0;
			double squareWidth = width/3;
			double squareHeight = height/2;
			for(int y = 0; y < 2; y++ ) {
				if(y == 0) {
				for(int x = 0; x < 3; x++ ) {
					baseY = (int)(y * squareHeight);
					baseX= (int)(x *squareWidth);
					int randX= (int)(Math.random()*squareWidth);
					int randY= (int)(Math.random()*squareHeight);
					poly.addPoint(baseX + randX, baseY + randY);
				}
				}else {
					for(int x = 2; x >=0; x-- ) {
						baseY = (int)(y * squareHeight);
						baseX= (int)(x *squareWidth);
						int randX= (int)(Math.random()*squareWidth);
						int randY= (int)(Math.random()*squareHeight);
						poly.addPoint(baseX + randX, baseY + randY);
					}
				}
				
			}
		
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	
		g.setColor(new Color(255,0,0));
		if(poly == null) {
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
