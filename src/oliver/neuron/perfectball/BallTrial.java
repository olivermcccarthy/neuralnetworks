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

	static BallDisplay panel = null;
	

	public static void main(String[] args) throws Exception {

		BallTrial trainer = new BallTrial(1, 1, 10000, 1.2);

	
		 panel = new BallDisplay();
		NeuralNetwork neuralNetwork = new NeuralNetwork(16, 0, 0, 2, false);
		DrawNeuralNetwork.showNeurons(neuralNetwork,4, 4);
		DrawNeuralNetwork.getNeuronPanel().setInputPanel(panel);
	
		for (int x = 0; x < 400; x++) {
			trainer.nextTrial(neuralNetwork);
			
		}
	}

	@Override
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		// TODO Auto-generated method stub
		Cost theCost = new Cost(1);
		DrawNeuralNetwork drawPanel = DrawNeuralNetwork.getNeuronPanel();
		drawPanel.setInputPanel(panel);
		
		for(int trial = 0; trial < this.numValues; trial ++) {
				panel.newPoly();
			double [] inputs = Helper.saveImageAsDouble(panel, 4, 4);
		
		    if(inputs != null) {
			neuralNetwork.setInput(inputs);
		    }
		    neuralNetwork.sigmoid();
		    double[] values = neuralNetwork.getOutput();
			double []expected = panel.like(values);
		    
		
			
			neuralNetwork.handleTopError(expected);
			theCost.addResult(expected, values);
			drawPanel.repaint();
			
			drawPanel.waitForUserClick(this, expected, values);
		}
		return theCost;
	}
}
