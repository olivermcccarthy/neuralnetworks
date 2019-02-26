package oliver.neuron.perfectball;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.TrialInfo;
import oliver.neuron.neonnumbers.NeonTrial;
import oliver.neuron.ui.DrawNeuralNetwork;

public class BallTrial extends  TrialInfo{

	
	public BallTrial(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		// TODO Auto-generated constructor stub
	}
	static BallDisplay panel = null;
	public static void main(String[] args) throws Exception {

		BallTrial trainer = new BallTrial(1, 1, 10000, 1.2);

	
		 panel = new BallDisplay();
		NeuralNetwork neuralNetwork = new NeuralNetwork(2, 2, 0, 1, false);
		DrawNeuralNetwork.showNeurons(neuralNetwork,10, 4);
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
			double roundness = Math.random()/4 + 0.775;
			double redness = Math.random()/4 + 0.675;
			panel.setRedness(redness);
			panel.setRoundness(roundness);
			double [] inputs = new double[] {roundness,redness};
			neuralNetwork.setInput(inputs);
			// Human makes ups its mind how much it likes tehball 
			// based on how far off perefection we are (0.7) redness and 0.8 roundness
			double dislike = Math.abs(redness - 0.7);
			dislike = dislike  + Math.abs(roundness - 0.8);
			neuralNetwork.sigmoid();
			double expected = 1 - dislike;
		
			double value = neuralNetwork.getOutput()[0];
			neuralNetwork.handleTopError(new double[] {expected});
			theCost.addResult(new double[] {expected}, new double[] {value});
			drawPanel.waitForUserClick(this, expected, value);
		}
		return theCost;
	}
}
