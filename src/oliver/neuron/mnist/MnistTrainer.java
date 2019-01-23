package oliver.neuron.mnist;

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
import oliver.neuron.Neuron;

public class MnistTrainer {

	public MnistTrainer() {

	}

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

	/**
	 * Run each image through and compare actual with expected Expected will
	 * converted into 10 bit array. Where only one bit is set. For example if
	 * expected is 9 only bit 9 is set For example if expected is 0 only bit 0 is
	 * set
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		MnistTrainer bmpFile = new MnistTrainer();
		String testDate = "2018-12-12T05:30:23.838-06:00".substring(0, 19);
		SimpleDateFormat df;
		java.time.LocalDateTime dg = java.time.LocalDateTime.parse(testDate,java.time.format.DateTimeFormatter.ISO_DATE_TIME);
		try {
			
			java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(testDate);
			System.out.println(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<double[]> inpuData = bmpFile.normalize(images, 28, 28);
	
		Layer inputLayer = new Layer("input", 28 * 28);
		Layer hiddenLayer = new Layer("hidden2", inputLayer, 40);

		double[][] tenBitArray = asTenBitArray(labels);
		double[] doubleArray = asDoubleArray(labels);
		Layer outputLayer = new Layer("output", hiddenLayer, 10);
		Neuron.learningRate = .2;
		// DrawPanel.showNeurons();

		for (int trial = 0; trial < 10; trial++) {
			Cost theCost = new Cost(10);
			int numWrong = 0;
			long startTime = System.currentTimeMillis();
			for (int image = 0; image < 60000; image++) {

				double[] input = inpuData.get(image);
				// DrawPanel.input = images.get(image);
				inputLayer.setvalues(input);
				for (int innerTrial = 0; innerTrial < 1; innerTrial++) {

					outputLayer.sigmoid();
					double[] expected = tenBitArray[image];
					double[] output = outputLayer.getvalues();
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
						numWrong++;
					}

					theCost.addResult(expected, output);
					outputLayer.handleTopError(expected);
					outputLayer.sigmoid();

				}
				
			}

			long diff = System.currentTimeMillis() - startTime;
			System.out.println("Finished trial " + trial + " Taking ms " + diff);
			System.out.println(" Num wrong " + numWrong);
			System.out.println(" The cost is " + Neuron.toString(theCost.getCost().getValues()));
		}

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
