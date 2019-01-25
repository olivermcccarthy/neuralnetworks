package oliver.neuron;

public class LinearNeuron extends Neuron{

	public LinearNeuron(String name, double bias) {
		super(name, bias);
		// TODO Auto-generated constructor stub
	}

	
	public double sigmoid() {

		if (this.input) {
			return sigMoid;
		}
		double res = getSigmoidWeightedValue();

		res += bias;
		this.sigMoid = res;
		return res;
	}
	
	public void handleError(double t) {
		// (y-t)*y*(1-y)* (in1) Dont ask

		//
		double sigM = sigMoid;
		errorVar = (sigM - t);
		for (int wI = 0; wI < this.weights.size(); wI++) {
			Neuron inputNeuron = this.inputs.get(wI);
			double input = inputNeuron.getValue();
			double weight = this.weights.get(wI);
			inputNeuron.addWeightedError( weight * errorVar);
			weight = weight - learningRate * (errorVar * input);
			this.weights.set(wI, weight);
		}
		bias = bias - learningRate * (errorVar);

	}
}
