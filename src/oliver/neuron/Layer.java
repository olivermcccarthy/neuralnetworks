package oliver.neuron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A layer of neurons. Sigmoid is called on this layer and it calculates sigmoid
 * for each neuron in the Layer. Sigmoid is a function that returns a value
 * between 0 and 1  means the neuron is fully on
 * 
 * @author oliver
 *
 */
public class Layer implements Serializable{

	static double getRandNum(double upperLevel) {
	   
	    
		double res = (Math.random() * 2 * upperLevel) - upperLevel;
		return res;
	}

	private List<Neuron> neurons = new ArrayList<Neuron>();

	String layerName;

	public Layer childLayer;

	private  List<Layer> layers = new ArrayList();

	public String getLayerName() {
		return layerName;
	}



	/*
	 * sqrt(d) where d is the number of inputs to the neuron. The resulting weights
	 * are normally distributed between [−1/d√,1/d√] s
	 */
	public Layer(String layerName, Layer childLayer, int numNeurons) {
		
		this.layerName = layerName;
		this.childLayer = childLayer;
		//
		double upperLevel = 1 / (Math.sqrt(numNeurons));
		
		for (int x = 0; x < numNeurons; x++) {
			Neuron newNu = new Neuron(layerName + "-" + x, 0);

			for (Neuron child : childLayer.getNeurons()) {

				newNu.addInput(child, getRandNum(upperLevel));

			}

			this.getNeurons().add(newNu);
		}
	}

	
	
	public Layer(String layerName, Layer childLayer, int numNeurons, boolean isLinear) {
		
		this.layerName = layerName;
		this.childLayer = childLayer;
		//
		double upperLevel = 1 / (Math.sqrt(childLayer.getNeurons().size()));
		
		for (int x = 0; x < numNeurons; x++) {
			Neuron newNu = new Neuron(layerName + "-" + x, 0);

			for (Neuron child : childLayer.getNeurons()) {

				newNu.addInput(child, getRandNum(upperLevel));

			}

			this.getNeurons().add(newNu);
		}
	}
	/**
	 * InputLayer
	 * 
	 * @param layerName
	 * @param numNeurons
	 */
	public Layer(String layerName, int numNeurons) {
	
		for (int x = 0; x < numNeurons; x++) {
			Neuron newNu = new Neuron(layerName + "-" + x, 0, true);
			this.getNeurons().add(newNu);
		}

		this.layerName = layerName;
	}

	public void setvalues(double[] inVals) {
		int index = 0;
		for (Neuron nu : this.getNeurons()) {
			nu.sigMoid = inVals[index];
			index ++;
		}
		
	}

	public double[] getvalues() {
		double [] values = new double[this.getNeurons().size()];
		int index = 0;
		for (Neuron nu : this.getNeurons()) {
			values[index]=nu.getValue();
		    index ++;
		}
		
		return values;
	}

	public void sigmoid() {
		

		if (this.childLayer != null) {
			this.childLayer.sigmoid();
		} 

		
		for (Neuron nu : this.getNeurons()) {
			nu.sigmoid();
		
		}
		
	}

	public void clearWeightedError() {
		for (Neuron nu : this.getNeurons()) {
			nu.clearWeightedError();
		}
	}

	/**
	 * Handle the error from outside. This method is called only on the outLayer
	 * handlerError is called for each Neuron in the outputlayer
	 * 
	 * @param args
	 *            Expected values. arg[x] is the expected value for Neuron(x)
	 */
	public void handleTopError(double[] args) {

		this.childLayer.clearWeightedError();
		int index =0;
		for (Neuron nu : this.getNeurons()) {
			nu.handleError(args[index]);
			index ++;
		
		}

		if (this.childLayer != null) {
			this.childLayer.handleError();
		}
	}

	public void handleError() {

		if (this.childLayer != null) {
			// DrawPanel.stopAMinute("Handled layer" + this.layerName);
			this.childLayer.clearWeightedError();
		}

		// Each neuorn adds a little bit of weighted error to each of the
		// childWeigthedErrors
		// we then use this array to pass onto teh child layer.
		for (Neuron nu : this.getNeurons()) {
			nu.handleError();
		}

		if (this.childLayer != null) {
			// DrawPanel.stopAMinute("Handled layer" + this.layerName);
			this.childLayer.handleError();
		}
	}



	public List<Neuron> getNeurons() {
		return neurons;
	}



	public void setNeurons(List<Neuron> neurons) {
		this.neurons = neurons;
	}



	
}
