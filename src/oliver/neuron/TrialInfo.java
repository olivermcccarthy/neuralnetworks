package oliver.neuron;



public class TrialInfo {
	int maxTrials=100;
	double learningRate = 1;
	public int numValues =1000;
	int bestTrial = 0;
	double bestCost = 1;
	double bestNumWrong = 10;
	
	public Cost sendinBatch(NeuralNetwork neuralNetwork) {
		Cost theCost = new Cost(1);

		return theCost;
	}
	
}
