package oliver.neuron.neonnumbers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.TrialInfo;
import oliver.neuron.mnist.MnistTrainer;
import oliver.neuron.ui.DrawPanel;

public class NeonTrial extends TrialInfo {
	static List<int[][]> images = new ArrayList<int[][]>();
	static List<Integer> labels= new ArrayList<Integer>();
	static List<double[]> inputData = new ArrayList<double[]>();
	static double[][] tenBitArray;

	public NeonTrial(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {
		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		// TODO Auto-generated constructor stub
	}


	@Override
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		Cost theCost = new Cost(10);
		DrawPanel.waitForUserClick(this, 0, 0);
		for (int y = 0; y < 2000; y++) {
			for (int image = 0; image < this.numValues; image++) {

				double[] input = inputData.get(image);
				// DrawPanel.input = images.get(image);
				neuralNetwork.inputLayer.setvalues(input);
				for (int innerTrial = 0; innerTrial < 1; innerTrial++) {

					neuralNetwork.outLayer.sigmoid();
					double[] expected = tenBitArray[image];
					double[] output = neuralNetwork.outLayer.getvalues();
					double expected2 = labels.get(image);

					this.trialNumber ++;
					double max = 0;
					int maxI = 0;
					for (int x = 0; x < 10; x++) {
						if (output[x] > max) {
							max = output[x];
							maxI = x;
						}

					}
					if (maxI != expected2) {
						theCost.numWrong++;
					}
				
					theCost.addResult(expected, output);
					neuralNetwork.outLayer.handleTopError(expected);
					DrawPanel.setInputImage(images.get(image), 4,DrawPanel.PICTURE_TYPE.BINARY);
					
					DrawPanel.waitForUserClick(this, expected2, maxI);

				}

			}
		}
		System.out.println(theCost.getCost().getAverage() + " numWrong " + theCost.numWrong);
		return theCost;
	}

	public static void main(String[] args) throws Exception {

		NeonTrial trainer = new NeonTrial(1, 1, 10, 1.1);

	
		
		NeuralNetwork neuralNetwork = new NeuralNetwork(10 * 20, 0, 0, 10, false);
		DrawPanel.showNeurons(10, 4);

	

		images = NeonDisplay.drawImages(labels);
		for (int d = 0; d < images.size(); d++) {
			NeonDisplay.number = d;
			int [][] imageData =images.get(d);
			double[] asDouble = asDoubleArray(imageData);
			inputData.add(asDouble);
			
			
		}
		tenBitArray = asTenBitArray(labels);

		

		for (int x = 0; x < 400; x++) {
			trainer.nextTrial(neuralNetwork);
			
		}

		trainer.numValues = 10;

		trainer.sendinBatch(neuralNetwork, false);

	}

	public static double[] asDoubleArray(int[][] image) {

		int height = image.length;
		int width = image[0].length;

		double[] tenBitArray = new double[height * width];
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				tenBitArray[h * width + w] = image[h][w];
			}
		}

		return tenBitArray;
	}

	public static double[][] asTenBitArray(List<Integer>labels) {

		double[][] tenBitArray = new double[labels.size()][10];
		for (int image = 0; image < labels.size(); image++) {
			tenBitArray[image][labels.get(image)] = 1;
		}
		return tenBitArray;
	}
}
