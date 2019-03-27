package oliver.neuron;



/**
 * Information on a Neural Network trial
 * learningRate
 * numValues in each batch
 * numTrialsBetweenSaves . The Network is saved every x trials
 * @author oliver
 *
 */
public abstract class TrialInfo {
	/**
	 * We save the network to memory every X Batches
	 */
	protected int numBatchesBetweenSaves=100;
	
	/**
	 * Rate at which we learn (adjust weights)
	 */
	protected double learningRate = 1;
	
	/**
	 * Number of runs in each batch
	 */
	public int numRunsPerBatch =1000;
	
	/**
	 * Pointer to the batch with the best cost
	 */
	protected  int bestBatch = 0;
	
	/**
	 * Current batch number we are running
	 */
	protected int batchNumber =-1;
	
	/**
	 * Number of teh btach we saved at
	 */
	int savePoint;
	
	/**
	 * Cost of the batch we saved at
	 */
	protected  double savePointCost = 1;
	
	/**
	 * Cost of current batch
	 */
	protected  double currentCost = 1;
	
	/**
	 * If the cost of current is better than the last batch we increase learning rate by this<br>
	 * If the cost of current is worse than last saved batch we load last saved from memory and decrease learning rate by this<br>
	 */
	double learningRateChange = 1.2;
	
	
	/**
	 * Number of batches remaining to be run. counts down from what the user
	 * selects as number of batches before they click run
	 */
	int numBatchesRemaining;

	/**
	 * Overall run counter increment by one every run
	 */
	int runCounter = 0;

	/**
	 * Count number of baches run
	 */
	int batchesRun = 0;
	
	
    int sleepTimeMs;

	/**
	 * Value of the best cost
	 */
	protected  double bestCost = 1;

	public Cost theCost;
	
	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public double getBestCost() {
		return bestCost;
	}

	public void setBestCost(double bestCost) {
		this.bestCost = bestCost;
	}

	
	
	
	public double getCurrentCost() {
		return currentCost;
	}

	public void setCurrentCost(double currentCost) {
		this.currentCost = currentCost;
	}

	/**
	 * Abstract method override to send in batch
	 * @param neuralNetwork
	 * @param learning
	 * @return
	 */
	public abstract Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) ;
	
	/**
	 * Override to return some short help on the trial
	 * @return
	 */
	public abstract String getHelp();
	
	/**
	 * Override to return some short help on the trial
	 * @return
	 */
	public abstract String getHeading();
	
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
		this.numBatchesBetweenSaves = numTrialsBetweenSaves;
		this.learningRate = learningRate;
		this.numRunsPerBatch = numValues;
		this.learningRateChange = learningRateChange;
	}

	public String toString() {
		return String.format("Trial Number %s cost %s Best trial %s Best cost %s  LearningRate %s numValues %s", this.batchNumber,currentCost,bestBatch, bestCost, this.learningRate,  numRunsPerBatch);
	}
	
	/**
	 * Send in a batch. If the cost of this batch is less the the last batch. 
	 * Then  increase learning rate by multiplying it by learningRateChange <br>
	 *  If cost is greater than last batch go back to last save point and decrease learning rate by dividing it by learningRateChange 
	 * @param neuralNetwork
	 * @throws Exception
	 */
	public void nextBatch(NeuralNetwork neuralNetwork) throws Exception {
		if(this.currentCost > this.savePointCost || this.currentCost > this.bestCost) {
			// we must run again
			neuralNetwork.load();
			this.batchNumber = this.savePoint;
			learningRate = learningRate/learningRateChange;
			System.out.println(String.format("Reducing learning rate to %s",learningRate));
		
			//learningRateChange = (learningRateChange -1 )*0.5 +1;
			Neuron.learningRate = this.learningRate;	
			if(this.bestBatch > this.savePoint) {
				for(int trial = this.batchNumber; trial < this.bestBatch;trial ++) {
					batchNumber ++;
					System.out.println(this);
					sendinBatch(neuralNetwork, true);
				}
				neuralNetwork.save();
				this.savePoint = this.batchNumber;
				this.savePointCost = this.currentCost;
				System.out.println(String.format("Saving at save point %s learning rate is %s" ,savePoint, learningRate));
			}
			this.savePoint = this.batchNumber+1;
		}
		
		batchNumber ++;
		Neuron.learningRate = this.learningRate;	
		if(batchNumber - savePoint >= numBatchesBetweenSaves) {
			neuralNetwork.save();
			this.savePoint = this.batchNumber -1;
			this.savePointCost = this.currentCost;
			learningRate = learningRate*learningRateChange;
			System.out.println(String.format("Saving at save point %s Increasing learning rate to %s" ,savePoint, learningRate));

		}
		
		Cost cost = sendinBatch(neuralNetwork, true);
		
		cost.getCost().getAverage();
		this.currentCost = cost.getCost().getAverage();
		if (currentCost <	bestCost) {
			bestCost =currentCost;
			bestBatch = batchNumber;
		}
		System.out.println(this);
	}
	
	
	public String getPackageS() {
		// TODO Auto-generated method stub
		return "ui";
	}

	
	abstract public String getName() ;
	
	public int getNumBatchesRemaining() {
		return numBatchesRemaining;
	}

	public void setNumBatchesRemaining(int numBatchesRemaining) {
		this.numBatchesRemaining = numBatchesRemaining;
	}

	public int getRunCounter() {
		return runCounter;
	}

	public void setRunCounter(int runCounter) {
		this.runCounter = runCounter;
	}

	public int getBatchesRun() {
		return batchesRun;
	}

	public void setBatchesRun(int batchesRun) {
		this.batchesRun = batchesRun;
	}

	public long getSleepTimeMs() {
		// TODO Auto-generated method stub
		return sleepTimeMs;
	}

	public void setSleepTimeMs(int sleepTimeMs) {
		this.sleepTimeMs = sleepTimeMs;
		
	}

	public void setLearningRateChange(double learningRateChange2) {
		this.learningRateChange = learningRateChange2;
		
	}
}
