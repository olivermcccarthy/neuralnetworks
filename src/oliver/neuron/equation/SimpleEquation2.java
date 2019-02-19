package oliver.neuron.equation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

import oliver.neuron.Cost;
import oliver.neuron.Layer;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;

public class SimpleEquation2 extends TrialInfo{

	
		
		double[] results = new double[numValues];
		double[] xCoeffs = new double[numValues];
		double[] yCoeffs = new double[numValues];
		double max;
	
		SimpleEquation2() {
			max = 0;
			for (int r = 0; r < numValues; r++) {
				xCoeffs[r] = (int) (Math.random() * 5);
				yCoeffs[r] = (int) (Math.random() * 5);
				double result = xCoeffs[r] * 13 + yCoeffs[r] * 7;
				if (xCoeffs[r] > max) {
					max = xCoeffs[r];
				}
				if (yCoeffs[r] > max) {
					max = yCoeffs[r];
				}
				results[r] = result;
			}

		}
		
		public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
			Cost theCost = new Cost(1);
	
			
			for (int r = 0; r < numValues; r++) {

				double normalizedXCoeff = (xCoeffs[r]) / max;
				double normalizedYCoeff = (yCoeffs[r]) / max;
				double normalizedResultTens = (int)(results[r]/10);
				double normalizedResultUnits= (results[r])%10;
				normalizedResultTens /= 12;
				normalizedResultUnits /= 12;
				normalizedResultTens += 0.05;
				normalizedResultUnits += 0.05;
				
				
				neuralNetwork.inputLayer.setvalues(new double[] { normalizedXCoeff, normalizedYCoeff });
				neuralNetwork.outLayer.sigmoid();

				theCost.addResult(new double[] { normalizedResultTens,normalizedResultUnits }, neuralNetwork.outLayer.getvalues());
				int realResult = (int) (Math.round((neuralNetwork.outLayer.getvalues()[0]  - 0.05)*12));
				realResult *=10;
				 realResult += (int) (Math.round((neuralNetwork.outLayer.getvalues()[1]   - 0.05)*12));
				int expected = (int) results[r];
				if (realResult != expected) {
					
					theCost.numWrong++;
				}
				if(learning) {
				// Anything above 0.5 we consider a match
				neuralNetwork.outLayer.handleTopError(new double[] { normalizedResultTens,normalizedResultUnits });
				}
			}
			return theCost;
		}

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		
		SimpleEquation2 trial = new SimpleEquation2();
		NeuralNetwork neuralNetwork = new NeuralNetwork(2,0,0,2,true); 
		trial.numTrialsBetweenSaves =300;
		trial.numValues = 100;
		neuralNetwork.runTrial(trial);
		trial.numValues = 1000;
		 trial.sendinBatch(neuralNetwork, false);
			System.out.println("Best trial" + trial.bestTrial+ "Best cost" + trial.bestCost  + "  best numWrong "+ trial.bestNumWrong  + " LearningRate" + trial.learningRate + " numValues "+ trial.numValues);
			
		
	}
	
	
}
