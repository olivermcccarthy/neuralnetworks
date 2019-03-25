package oliver.neuron.neonnumbers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import oliver.neuron.Cost;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.TrialInfo;
import oliver.neuron.mnist.MnistTrainer;
import oliver.neuron.ui.DrawNeuralNetwork;

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
		DrawNeuralNetwork drawPanel = DrawNeuralNetwork.getNeuronPanel(this.getName());
	
	
			for (int y = 0; y < this.numRunsPerBatch; y++) {

				int image = (int)(Math.random()*10);
				double[] input = inputData.get(image);
				// DrawPanel.input = images.get(image);
				neuralNetwork.setInput(input);
				for (int innerTrial = 0; innerTrial < 1; innerTrial++) {

					neuralNetwork.sigmoid();
					double[] expected = tenBitArray[image];
					double[] output = neuralNetwork.getOutput();
					double expected2 = labels.get(image);
					NeonDisplay.number = (int)expected2;
					this.batchNumber ++;
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
					neuralNetwork.handleTopError(expected);
				//	drawPanel.setInputImage(images.get(image), 4,DrawNeuralNetwork.PICTURE_TYPE.BINARY);
					
					drawPanel.waitForUserClick(this, expected2, maxI, true,true);

				}

			}
		
		System.out.println(theCost.getCost().getAverage() + " numWrong " + theCost.numWrong);
		return theCost;
	}

	public static void main(String[] args) throws Exception {

		NeonTrial trainer = new NeonTrial(1, 1, 10, 1.1);

	
		
		NeuralNetwork neuralNetwork = new NeuralNetwork(10 * 20, 0, 0, 10, false);
		DrawNeuralNetwork.showNeurons(trainer,neuralNetwork,10, 4);

	
		NeonDisplay display = new NeonDisplay();
		DrawNeuralNetwork.getNeuronPanel(trainer.getName()).setInputPanel(display);
	
		
		images =display.drawImages(labels);
		for (int d = 0; d < images.size(); d++) {
			NeonDisplay.number = d;
			int [][] imageData =images.get(d);
			double[] asDouble = asDoubleArray(imageData);
			inputData.add(asDouble);
			
			
		}
		tenBitArray = asTenBitArray(labels);

		
		DrawNeuralNetwork.getNeuronPanel(trainer.getName()).run(trainer);
		

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


	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "NeonNumbers";
	}
}
