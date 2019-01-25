package oliver.neuron.equation;


import java.io.IOException;


import oliver.neuron.Cost;

import oliver.neuron.NeuralNetwork;

import oliver.neuron.TrialInfo;

public class SimpleEquation extends TrialInfo{

	
		
		double[] results = new double[numValues];
		double[] xCoeffs = new double[numValues];
		double[] yCoeffs = new double[numValues];
		double max;
	
		SimpleEquation() {
			max = 0;
			for (int r = 0; r < numValues; r++) {
				xCoeffs[r] = (int) (Math.random() * 5);
				yCoeffs[r] = (int) (Math.random() * 5);
				double result = xCoeffs[r] * 13 + yCoeffs[r] * 7;
				if (result > max) {
					max = result;
				}

				results[r] = result;
			}

		}
		
		public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
			Cost theCost = new Cost(1);
	
			
			for (int r = 0; r < numValues; r++) {

				double normalizedXCoeff = (xCoeffs[r]) / max;
				double normalizedYCoeff = (yCoeffs[r]) / max;
				double normalizedResult = (results[r]) / max;
				neuralNetwork.inputLayer.setvalues(new double[] { normalizedXCoeff, normalizedYCoeff });
				neuralNetwork.outLayer.sigmoid();

				theCost.addResult(new double[] { normalizedResult }, neuralNetwork.outLayer.getvalues());
				int realResult = (int) (Math.round(neuralNetwork.outLayer.getvalues()[0] * max));
				int expected = (int) results[r];
				if (realResult != expected) {
					
					theCost.numWrong++;
				}
				if(learning) {
				// Anything above 0.5 we consider a match
				neuralNetwork.outLayer.handleTopError(new double[] { normalizedResult });
				}
			}
			return theCost;
		}

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		
		SimpleEquation trial = new SimpleEquation();
		NeuralNetwork neuralNetwork = new NeuralNetwork(2,8,8,1,true); 
		trial.maxTrials =3000;
		trial.numValues = 100;
		neuralNetwork.runTrial(trial);
		trial.numValues = 1000;
		trial.learningRate = 1.2;
		 trial.sendinBatch(neuralNetwork, false);
			System.out.println("Best trial" + trial.bestTrial+ "Best cost" + trial.bestCost  + "  best numWrong "+ trial.bestNumWrong  + " LearningRate" + trial.learningRate + " numValues "+ trial.numValues);
			
		
	}
	
	
}
