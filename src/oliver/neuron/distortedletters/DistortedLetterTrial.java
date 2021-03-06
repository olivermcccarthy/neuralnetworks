package oliver.neuron.distortedletters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;
import oliver.neuron.neonnumbers.NeonTrial;
import oliver.neuron.ui.DrawNeuralNetwork;
import oliver.neuron.ui.Helper;

public class DistortedLetterTrial extends  TrialInfo{

	
	public DistortedLetterTrial(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		// TODO Auto-generated constructor stub
	}

	static DistortedLettersChoice panel = null;
	

	public static void main(String[] args) throws Exception {

		DistortedLetterTrial trainer = new DistortedLetterTrial(1, 1, 10000, 1.2);

	
		 panel = new DistortedLettersChoice();
		NeuralNetwork neuralNetwork = new NeuralNetwork(100, 0, 0, 3, false);
		DrawNeuralNetwork.showNeurons(trainer,neuralNetwork,10, 10);
		DrawNeuralNetwork.getNeuronPanel(trainer.getName()).setInputPanel(panel);
		DrawNeuralNetwork nP = DrawNeuralNetwork.getNeuronPanel(trainer.getName());
		
		nP.run(trainer);
		
	}

	@Override
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		// TODO Auto-generated method stub
		Cost theCost = new Cost(1);
		DrawNeuralNetwork drawPanel = DrawNeuralNetwork.getNeuronPanel(getName());
		drawPanel.setInputPanel(panel);
	
		boolean updateBatchInfo = false;
		for(int run = 0; run < this.numRunsPerBatch; run ++) {
				panel.newLetter();
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
		  
		    panel.setEnabled(true);
		    if(this.numRunsPerBatch ==1) {
		    	panel.setEnabled(false);
		    	return theCost;
		    }
			double []expected = panel.like(theCost,this,drawPanel,values);
		    
		
			panel.setEnabled(false);
			neuralNetwork.handleTopError(expected);
			theCost.addResult(expected, values);
			drawPanel.repaint();
			drawPanel.updateBatchInfo(theCost,updateBatchInfo);
			updateBatchInfo = true;
		}
	
		return theCost;
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Train a network to recognize Distorted letters. "
				+ "The network learns each time you click the correct letter. "
				+ "The input panel is broken into 10 by 10 squares.  \n  100 input Neurons that are connected to each of the output Neurons\n"
				+ "Weights are shown in a sqaure to teh left of the neuron. Red for positive weights,blue for negative weights  "
				+ "Watch as the weights change color as your train the network"
				;
		
	}
	public String getHeading() {
		return "Train a network to recognize Distorted letters";
	}
	public String getPackageS() {
		// TODO Auto-generated method stub
		return "perfectball";
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DistortedLetters";
	}
}
