package oliver.neuron;



public abstract class TrialInfo {
	protected int maxTrials=100;
	protected double learningRate = 1;
	public int numValues =1000;
	protected  int bestTrial = 0;
	protected  double bestCost = 1;
	protected double bestNumWrong = 10;
	double learningRateChange = 1.005;
	public abstract Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) ;
	
	public String toString() {
		return String.format("Best trial %s Best cost %s   best numWrong %s LearningRate %s numValues %s", bestTrial, bestCost, this.learningRate, bestNumWrong, numValues);
	}
}
