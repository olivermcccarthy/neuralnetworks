package oliver.neuron.perfectball;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.TrialInfo;
import oliver.neuron.neonnumbers.NeonTrial;
import oliver.neuron.ui.DrawPanel;

public class BallTrial extends  TrialInfo{

	
	public BallTrial(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		// TODO Auto-generated constructor stub
	}
	static BallDisplay panel = null;
	public static void main(String[] args) throws Exception {

		BallTrial trainer = new BallTrial(1, 1, 10000, 1.2);

	
		 panel = new BallDisplay();
		NeuralNetwork neuralNetwork = new NeuralNetwork(2, 4, 0, 1, false);
		DrawPanel.showNeurons(10, 4);
		DrawPanel.setInputPanel(panel);
		for (int x = 0; x < 400; x++) {
			trainer.nextTrial(neuralNetwork);
			
		}
	}

	@Override
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		// TODO Auto-generated method stub
		Cost theCost = new Cost(1);
		for(int trial = 0; trial < this.numValues; trial ++) {
			double roundness = Math.random()/4 + 0.6;
			double redness = Math.random()/4 + 0.5;
			panel.setRedness(redness);
			panel.setRoundness(roundness);
			double [] inputs = new double[] {roundness,redness};
			neuralNetwork.inputLayer.setvalues(inputs);
			// Human makes ups its mind how much it likes tehball 
			// based on how far off perefection we are (0.7) redness and 0.8 roundness
			double dislike = Math.abs(redness - 0.7);
			dislike = dislike  + Math.abs(roundness - 0.8);
			neuralNetwork.outLayer.sigmoid();
			double expected = 1 - dislike;
			double value = neuralNetwork.outLayer.getvalues()[0];
			neuralNetwork.outLayer.handleTopError(new double[] {expected});
			theCost.addResult(new double[] {expected}, new double[] {value});
			DrawPanel.waitForUserClick(this, expected, value);
		}
		return theCost;
	}
}
