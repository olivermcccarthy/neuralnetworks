package oliver.neuron.mnist;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oliver.neuron.Cost;
import oliver.neuron.Layer;
import oliver.neuron.NeuralNetwork;
import oliver.neuron.Neuron;
import oliver.neuron.TrialInfo;

public class MnistTrainer extends TrialInfo {

	public MnistTrainer() {
		images = MnistReader.getImages("train-images.idx3-ubyte");
		labels=MnistReader.getLabels("train-labels.idx1-ubyte");
		inputData = normalize(images, 28, 28);

		tenBitArray = asTenBitArray(labels);
	}

	List<int[][]> images;
	int[] labels;
	List<double[]> inputData;
	double[][] tenBitArray;

	public List<double[]> normalize(List<int[][]> images, int numRows, int numCols) {

		List<double[]> normalized = new ArrayList<double[]>();
		for (int[][] image : images) {
			double[] newImage = new double[numRows * numCols];
			int index = 0;
			for (int[] row : image) {
				for (int c = 0; c < row.length; c++) {
					newImage[index] = row[c];
					newImage[index] /= 256;
					index++;
				}
			}
			normalized.add(newImage);
		}
		return normalized;

	}

	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		Cost theCost = new Cost(10);

		for (int image = 0; image < this.numValues; image++) {

			double[] input = inputData.get(image);
			// DrawPanel.input = images.get(image);
			neuralNetwork.inputLayer.setvalues(input);
			for (int innerTrial = 0; innerTrial < 1; innerTrial++) {

				neuralNetwork.outLayer.sigmoid();
				double[] expected = tenBitArray[image];
				double[] output = neuralNetwork.outLayer.getvalues();
				double expected2 = labels[image];

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
			

			}

		}
		System.out.println( theCost.getCost().getAverage() +" numWrong " + theCost.numWrong);
		return theCost;
	}

	/**
	 * Run each image through and compare actual with expected Expected will
	 * converted into 10 bit array. Where only one bit is set. For example if
	 * expected is 9 only bit 9 is set For example if expected is 0 only bit 0 is
	 * set
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {

		MnistTrainer trainer = new MnistTrainer();
        trainer.numTrialsBetweenSaves = 4;
        trainer.learningRate= 0.1;
        trainer.numValues = 5000;
        NeuralNetwork neuralNetwork = new NeuralNetwork(28 * 28,15,0,10,false); 
        
        neuralNetwork.runTrial(trainer);
        trainer.numValues = 60000;
        trainer.sendinBatch(neuralNetwork, false);
        
	}

	public int bufAsInt(byte[] buffer, int startIndex, int length) {
		int i2 = 0;

		for (int t = 0; t < startIndex + length; t++) {
			int it = buffer[t] & 0xff;
			i2 = i2 << 8;
			i2 = i2 | it;
		}

		return i2;
	}

	public static double[] asDoubleArray(int[] labels) {

		double[] doubleArray = new double[labels.length];
		for (int image = 0; image < labels.length; image++) {
			doubleArray[image] = labels[image];
			doubleArray[image] = doubleArray[image] / 9;
		}
		return doubleArray;
	}

	public static double[][] asTenBitArray(int[] labels) {

		double[][] tenBitArray = new double[labels.length][10];
		for (int image = 0; image < labels.length; image++) {
			tenBitArray[image][labels[image]] = 1;
		}
		return tenBitArray;
	}
}
