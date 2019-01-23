package oliver.neuron.equation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import oliver.neuron.Cost;
import oliver.neuron.Layer;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
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
				double result = xCoeffs[r] * 2 + yCoeffs[r] * 3;
				if (result > max) {
					max = result;
				}

				results[r] = result;
			}

		}
		public Cost sendinBatch(NeuralNetwork neuralNetwork) {
			Cost theCost = new Cost(1);
	
			for (int r = 0; r < numValues/2; r++) {

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
				// Anything above 0.5 we consider a match
				neuralNetwork.outLayer.handleTopError(new double[] { normalizedResult });
			}
			return theCost;
		}

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		
		SimpleEquation trial = new SimpleEquation();
		NeuralNetwork neuralNetwork = new NeuralNetwork(2,8,8,1); 
		neuralNetwork.runTrial(trial);
	}
	
	
}
