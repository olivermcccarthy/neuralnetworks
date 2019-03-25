package oliver.neuron.mnist;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

import oliver.neuron.Cost;

import oliver.neuron.NeuralNetwork;

import oliver.neuron.TrialInfo;
import oliver.neuron.ui.DrawNeuralNetwork;
import oliver.neuron.ui.ImageInPanel;

/**
 * Load Mnist Images and run them through Network For each image we know the
 * expected output
 * 
 * @author oliver
 *
 */
public class MnistTrainer extends TrialInfo {

	/**
	 * Images as integer array
	 */
	List<int[][]> images;

	/**
	 * tells us what each image is (0-9)
	 */
	int[] labels;

	/**
	 * Image data as a double array ( value between 0 and 1)
	 */
	List<double[]> inputData;

	/**
	 * Each label (0-9) gets mapped to a ten bit array. For label 5 bit 5 is 1 and
	 * others are 0
	 */
	double[][] labelsAsBits;

	public MnistTrainer(int numTrialsBetweenSaves, double learningRate, int numValues, double learningRateChange) {

		super(numTrialsBetweenSaves, learningRate, numValues, learningRateChange);
		images = MnistReader.getImages("train-images.idx3-ubyte");
		labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		inputData = normalize(images, 28, 28);

		labelsAsBits = asTenBitArray(labels);
	}

	/**
	 * Convert images into double arrays. Thats one double array for each of the
	 * 6000 plus images
	 * 
	 * @param images
	 * @param numRows
	 * @param numCols
	 * @return
	 */
	public List<double[]> normalize(List<int[][]> images, int numRows, int numCols) {

		List<double[]> normalized = new ArrayList<double[]>();
		for (int[][] image : images) {
			double[] newImage = new double[numRows * numCols];
			int index = 0;
			for (int[] row : image) {
				for (int c = 0; c < row.length; c++) {
					newImage[index] = row[c];
					newImage[index] /= 256;
					// newImage[index] = newImage[index] * 2 -1;
					index++;
				}
			}
			normalized.add(newImage);
		}
		return normalized;

	}

	/**
	 * Send in a batch of images. For each image we correct the Network. The Network
	 * might say thr image is 5 ( output 5 is highest). But we know the image is 6
	 * So we pass 1 as expected value to output 6 and 0 as expected value to all
	 * other outputs. After 1000s of iterations the Network learns
	 */
	public Cost sendinBatch(NeuralNetwork neuralNetwork, boolean learning) {
		theCost = new Cost(10);
		DrawNeuralNetwork drawPanel = DrawNeuralNetwork.getNeuronPanel(this.getName());
		
		int sleepTime = drawPanel.controlPanel.getSleepTime();
		boolean updateBatchInfo = false;
		for (int u = 0; u < this.numRunsPerBatch; u++) {

			int image = (int) (Math.random() * images.size());
			double[] input = inputData.get(image);
			// DrawPanel.input = images.get(image);
			neuralNetwork.setInput(input);

			neuralNetwork.sigmoid();
			double[] expected = labelsAsBits[image];
			double[] output = neuralNetwork.getOutput();
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

			boolean redraw = true;
			if (sleepTime == 1 && numRunsPerBatch > 1000) {
				if (u % 100 != 0) {
					redraw = false;
				}
			}
			 if(this.numRunsPerBatch ==1) {
			    	
			    	return theCost;
			    }
			drawPanel.setInputImage(images.get(image), ImageInPanel.PICTURE_TYPE.GREYSCALE);
			drawPanel.waitForUserClick(this, expected2, maxI, true, redraw);

			theCost.addResult(expected, output);
			neuralNetwork.handleTopError(expected);
			drawPanel.updateBatchInfo(theCost,updateBatchInfo);
			updateBatchInfo = true;
		}
		System.out.println(theCost.getCost().getAverage() + " numWrong " + theCost.numWrong);
		
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
	public static void main(String[] args) throws Exception {

		MnistTrainer trainer = new MnistTrainer(1, 0.1, 1000, 1.1);

		NeuralNetwork neuralNetwork = new NeuralNetwork(28 * 28, 15, 0, 10, false);
		DrawNeuralNetwork.showNeurons(trainer, neuralNetwork, 28, 4);

		DrawNeuralNetwork.getNeuronPanel(trainer.getName()).run(trainer);
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

	public String getPackageS() {
		// TODO Auto-generated method stub
		return "mnist";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Train a network to recogize hadn written digits from the mnist training set \n"
				+ "The network learns each time a digit is passed through as we know the expected digit for each image\n"
				+ "The input image contains 28*28 pixels.  \n"
				+ "784 input Neurons that are connected to each of the Hidden Neurons \n"
				+ "Each of the Hidden Neurons is connected to each of the output Neurons\n"
				+ "Weights are shown in a sqaure. Red for positive weights,blue for negative weights \n"
				+ "Watch as the weights change color as the network learns \n"
				+ "It will take many iterations for teh nwtrok to learn with any degree of accuracy.\n"
				+ " Set Batch size to 10000 and run through X number of batches\n"
				+ " Watch as the number of wrong digits decreases per batch\n";
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "HandWritten digits";
	}
}
