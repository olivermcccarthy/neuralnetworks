package oliver.neuron;

import java.io.IOException;

/**
 * Information on a Neural Network trial
 * learningRate
 * numValues in each batch
 * numTrialsBetweenSaves . The Network is saved every x trials
 * @author oliver
 *
 */
public abstract class TrialInfo {
	protected int numTrialsBetweenSaves=100;
	protected double learningRate = 1;
	public int numValues =1000;
	protected  int bestTrial = 0;
	protected  double bestCost = 1;
	protected int trialNumber =-1;
	int savePoint;
	protected  double savePointCost = 1;
	protected  double currentCost = 1;
	double learningRateChange = 1.2;
	public abstract Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) ;
	
	/**
	 * 
	 * @param numTrialsBetweenSaves Save the network every x number of saves 
	 * @param learningRate learningrate of the Network which will be increased or decreased
	 * @param numValues Number of values in the batch 
	 * @param learningRateChange. Amount to increase learning rate if we make it through  numTrialsBetweenSaves trials without an increase in cost
	 *                           Amount to decrease learning rate if do not make it through  numTrialsBetweenSaves trials without an increase in cost
	 */
	public TrialInfo(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super();
		this.numTrialsBetweenSaves = numTrialsBetweenSaves;
		this.learningRate = learningRate;
		this.numValues = numValues;
		this.learningRateChange = learningRateChange;
	}

	public String toString() {
		return String.format("Trial Number %s cost %s Best trial %s Best cost %s  LearningRate %s numValues %s", this.trialNumber,currentCost,bestTrial, bestCost, this.learningRate,  numValues);
	}
	
	public void nextTrial(NeuralNetwork neuralNetwork) throws Exception {
		if(this.currentCost > this.savePointCost || this.currentCost > this.bestCost) {
			// we must run again
			neuralNetwork.load();
			this.trialNumber = this.savePoint;
			learningRate = learningRate/learningRateChange;
			System.out.println(String.format("Reducing learning rate to %s",learningRate));
		
			//learningRateChange = (learningRateChange -1 )*0.5 +1;
			Neuron.learningRate = this.learningRate;	
			if(this.bestTrial > this.savePoint) {
				for(int trial = this.trialNumber; trial < this.bestTrial;trial ++) {
					trialNumber ++;
					System.out.println(this);
					sendinBatch(neuralNetwork, true);
				}
				neuralNetwork.save();
				this.savePoint = this.trialNumber;
				this.savePointCost = this.currentCost;
				System.out.println(String.format("Saving at save point %s learning rate is %s" ,savePoint, learningRate));
			}
			this.savePoint = this.trialNumber+1;
		}
		
		trialNumber ++;
		Neuron.learningRate = this.learningRate;	
		if(trialNumber - savePoint >= numTrialsBetweenSaves) {
			neuralNetwork.save();
			this.savePoint = this.trialNumber -1;
			this.savePointCost = this.currentCost;
			learningRate = learningRate*learningRateChange;
			System.out.println(String.format("Saving at save point %s Increasing learning rate to %s" ,savePoint, learningRate));

		}
		
		Cost cost = sendinBatch(neuralNetwork, true);
		
		cost.getCost().getAverage();
		this.currentCost = cost.getCost().getAverage();
		if (currentCost <	bestCost) {
			bestCost =currentCost;
			bestTrial = trialNumber;
		}
		System.out.println(this);
	}
}
