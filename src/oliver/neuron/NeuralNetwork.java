package oliver.neuron;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class NeuralNetwork {
	ByteArrayOutputStream bout;
	public Layer inputLayer =  null;
	Layer hiddenLayer = null;
	Layer hiddenLayer2 = null;
	public Layer outLayer = null;
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
		Layer.getLayers().clear();
		Layer.getLayers().add(inputLayer);
		if(hiddenLayer != null) {
			Layer.getLayers().add(hiddenLayer);
		}
		if(hiddenLayer2 != null) {
			Layer.getLayers().add(hiddenLayer2);
		}
		Layer.getLayers().add(outLayer);
	}
	

}
