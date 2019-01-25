package oliver.neuron;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class NeuralNetwork {
	ByteArrayOutputStream bout;
	public Layer inputLayer =  null;
	Layer hiddenLayer = null;
	Layer hiddenLayer2 = null;
	public Layer outLayer = null;
	public NeuralNetwork(int numInputs, int numHidden,  int numHidden2, int numOutputs, boolean linearOutput){
		inputLayer = new Layer("input", numInputs);
		
		if(numHidden > 0) {
			hiddenLayer = new Layer("hidden2", inputLayer, numHidden);
			if(numHidden2 > 0) {
				hiddenLayer2 = new Layer("hidden2", hiddenLayer, numHidden2);
				outLayer = new Layer("outLayer", hiddenLayer2, numOutputs,linearOutput);
			}else {
				outLayer = new Layer("outLayer", hiddenLayer, numOutputs, linearOutput);
			}
		}else {
			outLayer = new Layer("outLayer", inputLayer, numOutputs, linearOutput);
		}
		
		try {
			save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save() throws IOException {
		bout = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bout);
		oout.writeObject(outLayer);
		 
	}
	public void load() throws IOException, ClassNotFoundException {
		ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
		ObjectInputStream oin = new ObjectInputStream(bin);
		outLayer =(Layer)oin.readObject();
		if(hiddenLayer2 != null) {
			 hiddenLayer2 = outLayer.childLayer;
			 hiddenLayer = hiddenLayer2.childLayer;
			 inputLayer = hiddenLayer.childLayer; 
		}else {
			if(hiddenLayer != null) {
				 hiddenLayer = outLayer.childLayer; 
				 inputLayer = hiddenLayer.childLayer; 
			}else {
				 inputLayer = outLayer.childLayer;
			}
			
		}
		
		
	}
	public double runTrial(TrialInfo trialInfo) throws IOException, ClassNotFoundException {
		int numTrials = trialInfo.numTrialsBetweenSaves;
		
	  double bestCost = 1;
	  int numZeroWrong = 0;
		while(true) {
		
			runTrial(trialInfo,trialInfo.numTrialsBetweenSaves); 
			System.out.println("Best trial" + trialInfo.bestTrial+ "Best cost" + trialInfo.bestCost  + "  best numWrong "+ trialInfo.bestNumWrong  + " LearningRate" + trialInfo.learningRate + " numValues "+ trialInfo.numValues);
			if(trialInfo.bestNumWrong == 0) {
				numZeroWrong ++;
				if(numZeroWrong  >=10) {
					return bestCost;
				}
			}else {
				numZeroWrong = 0;
			}
			if(trialInfo.bestCost < bestCost) {
			
				bestCost = trialInfo.bestCost;
				if(trialInfo.bestTrial < trialInfo.savePoint +trialInfo.numTrialsBetweenSaves -1) {
					// run again upto that point;
					System.out.println("Reloading from save point and playing as far as trial" + trialInfo.bestTrial);
					
					load();
					trialInfo.trialNumber= trialInfo.savePoint; 
					runTrial(trialInfo, trialInfo.bestTrial+1);
					save();
					trialInfo.savePoint = trialInfo.trialNumber;
					System.out.println("Replayed as far as " + trialInfo.bestTrial+ "Best cost" + trialInfo.bestCost);
					// then half teh learning rate;
					trialInfo.learningRate = trialInfo.learningRate/trialInfo.learningRateChange;
					System.out.println("Reducing learning rate to " + trialInfo.learningRate);
				}else {
					save();
					// Double teh learning rate;
					trialInfo.savePoint = trialInfo.trialNumber;
					trialInfo.learningRate = trialInfo.learningRate*trialInfo.learningRateChange;
					System.out.println("Doubling learning rate" + trialInfo.learningRate);
				}
			}else {
				load();
				
				trialInfo.trialNumber= trialInfo.savePoint; 
				trialInfo.learningRate = trialInfo.learningRate/trialInfo.learningRateChange;
				System.out.println("Reducing learning rate to " + trialInfo.learningRate);
			}
			
		}
	
	}
	public  double runTrial(TrialInfo trialInfo, int numTrials) throws IOException, ClassNotFoundException {

		
		double lowestCost = 1;
		Neuron.learningRate = trialInfo.learningRate;
		double lastCost = 1;
		int bestNumWrong =trialInfo.numValues;
		int maxtrial = trialInfo.trialNumber+numTrials;
		for (int trial = trialInfo.trialNumber; trial <  maxtrial; trial++) {
			//System.out.println("trial " + trial);
			Cost theCost = trialInfo.sendinBatch(this, true);
			
			double currentCost = theCost.getCost().getAverage();
            if(theCost.numWrong <  bestNumWrong) {
            	 bestNumWrong = theCost.numWrong;
            }
	
			if (currentCost <	trialInfo.bestCost) {
				trialInfo.bestCost = currentCost;
				trialInfo.bestTrial = trialInfo.trialNumber;
			}
			System.out.println(trialInfo);
			if (currentCost > lastCost) {
				break;
			}
			lowestCost = currentCost;
			lastCost = currentCost;
		
			trialInfo.trialNumber ++;
		}
		
		trialInfo.bestNumWrong =bestNumWrong;	
		return lowestCost;

	}
}
