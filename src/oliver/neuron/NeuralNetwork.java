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
	public NeuralNetwork(int numInputs, int numHidden,  int numHidden2, int numOutputs){
		inputLayer = new Layer("input", numInputs);
		
		if(numHidden > 0) {
			hiddenLayer = new Layer("hidden2", inputLayer, numHidden);
		}
		if(numHidden2 > 0) {
			hiddenLayer2 = new Layer("hidden2", hiddenLayer, numHidden2);
			outLayer = new Layer("outLayer", hiddenLayer2, numOutputs);
		}else {
			outLayer = new Layer("outLayer", hiddenLayer, numOutputs);
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
			 hiddenLayer = outLayer.childLayer; 
			 inputLayer = hiddenLayer.childLayer; 
		}
		
		
	}
	public double runTrial(TrialInfo trialInfo) throws IOException, ClassNotFoundException {
		int numTrials = trialInfo.maxTrials;
		
	  double bestCost = 1;
	  int numZeroWrong = 0;
		while(true) {
		
			runTrial(trialInfo,trialInfo.maxTrials); 
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
				save();
				bestCost = trialInfo.bestCost;
				if(trialInfo.bestTrial < trialInfo.maxTrials -1) {
					// run again upto that point;
					load();
					runTrial(trialInfo, trialInfo.bestTrial+1);
					System.out.println("Best trial" + trialInfo.bestTrial+ "Best cost" + trialInfo.bestCost);
					// then half teh learning rate;
					trialInfo.learningRate = trialInfo.learningRate/2;
				}else {
					// Double teh learning rate;
					trialInfo.learningRate = trialInfo.learningRate*1.5;
				}
			}else {
				load();
				trialInfo.learningRate = trialInfo.learningRate/1.5;
			}
			
		}
	
	}
	public  double runTrial(TrialInfo trialInfo, int numTrials) throws IOException, ClassNotFoundException {

		
		double lowestCost = 1;
		Neuron.learningRate = trialInfo.learningRate;
		double lastCost = 1;
		int bestNumWrong =20;
		for (int trial = 0; trial < numTrials; trial++) {
			//System.out.println("trial " + trial);
			Cost theCost = trialInfo.sendinBatch(this);
			
			double currentCost = theCost.getCost().getValues()[0];
            if(theCost.numWrong <  bestNumWrong) {
            	 bestNumWrong = theCost.numWrong;
            }
	
			if (currentCost <lowestCost) {
				trialInfo.bestCost = currentCost;
				trialInfo.bestTrial = trial;
			}
			if (currentCost > lastCost) {
				break;
			}
			lowestCost = currentCost;
			lastCost = currentCost;
		}
		
		trialInfo.bestNumWrong =bestNumWrong;	
		return lowestCost;

	}
}
