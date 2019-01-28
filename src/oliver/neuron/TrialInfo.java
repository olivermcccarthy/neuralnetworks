package oliver.neuron;



public abstract class TrialInfo {
	protected int numTrialsBetweenSaves=100;
	protected double learningRate = 1;
	public int numValues =1000;
	protected  int bestTrial = 0;
	protected  double bestCost = 1;
	int trialNumber =-1;
	int savePoint;
	protected  double currentCost = 1;
	double learningRateChange = 1.2;
	public abstract Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) ;
	
	public String toString() {
		return String.format("Trial Number %s cost %s Best trial %s Best cost %s  LearningRate %s numValues %s", this.trialNumber,currentCost,bestTrial, bestCost, this.learningRate,  numValues);
	}
}
