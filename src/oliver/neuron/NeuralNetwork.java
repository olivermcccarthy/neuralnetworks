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
		
			boolean reachedBatchSize = runTrial(trialInfo,trialInfo.numTrialsBetweenSaves, false); 
			System.out.println(trialInfo);
			 if(trialInfo.bestCost < bestCost) {
				 double improvement = bestCost/trialInfo.bestCost;
				 
				 System.out.println("improvment" + improvement);
				 if ( improvement < 1.0001 ) {
					 return trialInfo.bestCost;
				 }
			 }
			if(reachedBatchSize == false) {
				numZeroWrong ++;
				if(numZeroWrong > 2) {
					return trialInfo.bestCost;
				}
				// run again upto that point;
				System.out.println(String.format("Reloading from save point %s and playing as far as trial %s", trialInfo.savePoint, trialInfo.bestTrial));
				 
				load();
				
				trialInfo.trialNumber= trialInfo.savePoint; 
				runTrial(trialInfo, trialInfo.bestTrial -trialInfo.savePoint, true);
				save();
				trialInfo.savePoint = trialInfo.trialNumber;
				System.out.println("Replayed as far as " + trialInfo.bestTrial+ "Best cost" + trialInfo.bestCost);
				// then half teh learning rate;
				trialInfo.learningRate = trialInfo.learningRate/trialInfo.learningRateChange;
				System.out.println(String.format("Reducing learning rate to %s",trialInfo.learningRate));
				bestCost = trialInfo.bestCost;
				trialInfo.learningRateChange = (trialInfo.learningRateChange -1 )*0.5 +1;
			}else {
				numZeroWrong = 0;
			    if(trialInfo.bestCost < bestCost) {
			    	trialInfo.savePoint = trialInfo.trialNumber;
				     bestCost = trialInfo.bestCost;
					save();
					// Double teh learning rate					trialInfo.savePoint = trialInfo.trialNumber;
					trialInfo.learningRate = trialInfo.learningRate*trialInfo.learningRateChange;
					System.out.println(String.format("Saving at save point %s Increasing learning rate to %s" , trialInfo.savePoint, trialInfo.learningRate));
				
			
			}
			}
		}
	
	}
	public  boolean runTrial(TrialInfo trialInfo, int numTrials, boolean replay) throws IOException, ClassNotFoundException {

		
	
		Neuron.learningRate = trialInfo.learningRate;
	
		int bestNumWrong =trialInfo.numValues;
		int maxtrial = trialInfo.trialNumber+numTrials;
		for (int trial = trialInfo.trialNumber; trial <  maxtrial; trial++) {
			trialInfo.trialNumber ++;
			//System.out.println("trial " + trial);
			Cost theCost = trialInfo.sendinBatch(this, true);
			
			trialInfo.currentCost = theCost.getCost().getAverage();
            if(theCost.numWrong <  bestNumWrong) {
            	 bestNumWrong = theCost.numWrong;
            }
	
			if (trialInfo.currentCost <	trialInfo.bestCost) {
				trialInfo.bestCost = trialInfo.currentCost;
				trialInfo.bestTrial = trialInfo.trialNumber;
			}else if(!replay) {
				
				System.out.println(trialInfo);
				System.out.println(String.format("Found increase in cost without running batch of trials %s",trialInfo.numTrialsBetweenSaves));
				return false;
			}
			
		
			
			System.out.println(trialInfo);
		}
		
		
		return true;

	}
}
