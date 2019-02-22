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

		res += getBias();
		this.sigMoid = res;
		return res;
	}
	
	public void handleError(double t) {
		

		//
		double sigM = sigMoid;
		setErrorVar((sigM - t));
		for (int wI = 0; wI < this.getWeights().size(); wI++) {
			Neuron inputNeuron = this.getInputs().get(wI);
			double input = inputNeuron.getValue();
			double weight = this.getWeights().get(wI);
			inputNeuron.addWeightedError( weight * getErrorVar());
			weight = weight - learningRate * (getErrorVar() * input);
			this.getWeights().set(wI, weight);
		}
		setBias(getBias() - learningRate * (getErrorVar()));

	}
}
