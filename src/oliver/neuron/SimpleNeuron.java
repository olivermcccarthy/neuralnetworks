package oliver.neuron;

public class SimpleNeuron {

	
	public static void main (String [] args) {
		Neuron simpleNU = new Neuron("test",0);
		Neuron input1 = new Neuron("yh",0,true);
		Neuron input2 = new Neuron("yh",0,true);
		simpleNU.addInput(input1, .4);
		simpleNU.addInput(input2, .8);
		Neuron.learningRate =1;
		System.out.println(Math.exp(0.036003090822005604));
		
		for (int trial = 0; trial < 200000; trial++) {
			
			double X = Math.random();
			double Y = Math.random();
			
			double expected = X*0.9 - Y*0.1 + 0.005;
			expected = expected/100 +0.5 ;
			
			input1.sigMoid = X;
			input2.sigMoid = Y;
			double res = simpleNU.sigmoid();
			simpleNU.handleError(expected);
			
		}
		System.out.println(simpleNU.getWeights());
		System.out.println(simpleNU.getBias());
	}
}
