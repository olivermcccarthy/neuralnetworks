package oliver.neuron.perfectball;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;
import oliver.neuron.neonnumbers.NeonTrial;
import oliver.neuron.ui.DrawNeuralNetwork;
import oliver.neuron.ui.Helper;

public class BallTrial extends  TrialInfo{

	
	public BallTrial(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		// TODO Auto-generated constructor stub
	}

	static WonkyLettersChoice panel = null;
	

	public static void main(String[] args) throws Exception {

		BallTrial trainer = new BallTrial(1, 1, 10000, 1.2);

	
		 panel = new WonkyLettersChoice();
		NeuralNetwork neuralNetwork = new NeuralNetwork(100, 0, 0, 2, false);
		DrawNeuralNetwork.showNeurons(trainer,neuralNetwork,10, 10);
		DrawNeuralNetwork.getNeuronPanel().setInputPanel(panel);
		DrawNeuralNetwork nP = DrawNeuralNetwork.getNeuronPanel();
		
		nP.run(trainer);
		
	}

	@Override
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		// TODO Auto-generated method stub
		Cost theCost = new Cost(1);
		DrawNeuralNetwork drawPanel = DrawNeuralNetwork.getNeuronPanel();
		drawPanel.setInputPanel(panel);
		int sleepTimeMs = drawPanel.getSleepTime();
	
		for(int trial = 0; trial < this.numPerBatch; trial ++) {
				panel.newPoly();
			double [] inputs = Helper.saveImageAsDouble(panel.innerPanel, 10, 10);
		
		    if(inputs != null) {
			neuralNetwork.setInput(inputs);
		    }else {
		    	try {
					Thread.sleep(5000);
					 drawPanel.repaint();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	continue;
		    }
		    neuralNetwork.sigmoid();
		    double[] values = neuralNetwork.getOutput();
		    drawPanel.repaint();
		    this.numRun ++;
		    panel.setEnabled(true);
			double []expected = panel.like(this,drawPanel,values);
		    
		
			panel.setEnabled(false);
			neuralNetwork.handleTopError(expected);
			theCost.addResult(expected, values);
			drawPanel.repaint();
			
			drawPanel.waitForUserClick(this, expected, values,false,true);
		}
		return theCost;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Train a network to recognize wonky letters. "
				+ "The network learns each time you click the correct letter. "
				+ "The input panel is broken into 10 by 10 squares.  100 input Neurons that are connected to each of the output Neurons "
				+ "Weights are shown in a sqaure. Red for positive weights,blue for negative weights  "
				+ "Watch as the weights change color as the network learns"
				;
		
	}
	
	public String getPackageS() {
		// TODO Auto-generated method stub
		return "perfectball";
	}
}
