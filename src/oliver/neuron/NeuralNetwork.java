package oliver.neuron;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;



public class NeuralNetwork {
	ByteArrayOutputStream bout;
	private Layer inputLayer =  null;
	Layer hiddenLayer = null;
	Layer hiddenLayer2 = null;
	private Layer outLayer = null;
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
	
	public List<Layer> getLayers(){
		List<Layer> layers = new ArrayList<Layer>();
		layers.add(this.inputLayer);
		if(this.hiddenLayer != null) {
			layers.add(hiddenLayer);
		}
		if(this.hiddenLayer2 != null) {
			layers.add(hiddenLayer2);
		}
		layers.add(outLayer);
		return layers;
	}
	public void sigmoid() {
		this.outLayer.sigmoid();
	}
	
	public double[] getOutput() {
		return this.outLayer.getvalues();
	}
	public  void setInput(double[] values) {
	this.inputLayer.setvalues(values);
	}
	public  void handleTopError(double[] values) {
		this.outLayer.handleTopError(values);
		}
}
