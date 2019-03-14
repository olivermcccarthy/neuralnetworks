package oliver.neuron.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import oliver.neuron.TrialInfo;

public class ControlPanel extends JPanel {

	/**
	 * Buttons to allow user click through training.
	 */
	JButton button = new JButton("Run ");
	
	/**
	 * Allow user choose number of batches to run
	 */
	JComboBox numBatches = new JComboBox();
	
	/**
	 * Allow user choose number of runs per batch
	 */
	JComboBox numRunsPerBatch = new JComboBox();
	
	/**
	 * Allow user choose sleep time between runs
	 */
	JComboBox sleepTimeBetweenRuns = new JComboBox();
	
	/**
	 * Allow user choose learning rate
	 */
	JComboBox learningRate = new JComboBox();
	
	
	/**
	 * Choice of how many batches to run
	 */
	static int[] numBatchesChoice= new int[] {1,10,100,1000};
	
	/**
	 * Choice of how many batches to run
	 */
	static int[] numRunsPerBatchesChoice= new int[] {100,1000,5000,10000,50000};
	
	/**
	 * Choice of ms to sleep between runs
	 */
	static int[] sleepChoice= new int[] {10000,5000,1000,100,10,1};
	
	
	/**
	 * Choice of learning rate
	 */
	static double[] learningRateChoice= new double[] {1,0.5,0.1,0.05,0.01};
	
	/**
	 * Pointer to Trial Information 
	 */
	TrialInfo trialInfo;
	
	/**
	 * Object use to wait for user to click run/stop
	 */
	Object waitForMe = new String("");
    
	/**
	 * Sleep time between runs
	 */
	int sleepTimeMs;
    
	/**
	 * Counts down umber of batches to run
	 */
	int numBatchesI;
	
	public ControlPanel(TrialInfo trialInfo) {

		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					if (button.getText().equals("RUN")) {
						button.setText("STOP");
						numBatches.setEnabled(false);
						numRunsPerBatch.setEnabled(false);
						sleepTimeBetweenRuns.setEnabled(false);
						learningRate.setEnabled(false);

					} else {
						button.setText("RUN");
						numBatches.setEnabled(true);
						numRunsPerBatch.setEnabled(true);
						sleepTimeBetweenRuns.setEnabled(true);
						learningRate.setEnabled(true);
						trialInfo.numPerBatch = 0;
					}

				}

			}
		};
		button.addActionListener(listener);
		button.setBounds(0, 0, 100, 30);
		numBatches.setBounds(0, 30, 100, 30);
		numRunsPerBatch.setBounds(0, 60, 100, 30);
		sleepTimeBetweenRuns.setBounds(0, 90, 100, 30);
		learningRate.setBounds(0, 120, 100, 30);
		this.setPreferredSize(new Dimension(100, 120));
		button.setFont(this.getFont().deriveFont(20.0f));
		this.add(numRunsPerBatch);
		this.add(learningRate);
        this.setLayout(null);
       
        for (int i :  numRunsPerBatchesChoice) {
        	numRunsPerBatch.addItem(i + " runs per batch");	
		}
		
		numRunsPerBatch.setToolTipText("Number of runs per batch. Cost is calclated at the end of each batch");
		
		learningRate.setToolTipText("Speed at which network learns/adjusts is waits");
		for (int i : numBatchesChoice) {
			numBatches.addItem(i + "batches");	
		}
		
		numBatches.setToolTipText("Number of batches to run");
		numBatches.setSelectedIndex(0);
		  for (int i :  numRunsPerBatchesChoice) {
	        	numRunsPerBatch.addItem(i + "batches");	
			}
		for (int i :   sleepChoice) {
			sleepTimeBetweenRuns.addItem(i + "ms sleep ");	
	     }
		 
		for (double i :   learningRateChoice) {
			learningRate.addItem(i + "  ");	
	     }  
		
		sleepTimeBetweenRuns.setToolTipText("sleep between each run ");
		sleepTimeBetweenRuns.setSelectedIndex(0);
		this.add(button);
		this.add(numBatches);
		this.add(sleepTimeBetweenRuns);
		this.add(learningRate);
	}
	
	public int getSleepTime() {
		int selectedIndex = sleepTimeBetweenRuns.getSelectedIndex();
		sleepTimeMs = sleepChoice[selectedIndex];
		
		
		return sleepTimeMs;
	}
	public int getNumBatches() {
		int selectedIndex = numBatches.getSelectedIndex();
		numBatchesI = numBatchesChoice[selectedIndex];
		return numBatchesI;
	}

	public double getLearningRate() {
		int selectedIndex = this.learningRate.getSelectedIndex();
		double lr = learningRateChoice[selectedIndex];
		
		return lr;
	}

	public int getNumPerBatch() {
		int selectedIndex = numRunsPerBatch.getSelectedIndex();
		int numPerBatch =  numRunsPerBatchesChoice[selectedIndex];
		
		return numPerBatch;
	}
	
	public void waitForMe() {
		numBatches.setEnabled(true);
		numRunsPerBatch.setEnabled(true);
		sleepTimeBetweenRuns.setEnabled(true);
		learningRate.setEnabled(true);
		button.setText("RUN");
		synchronized (waitForMe) {
			try {
				waitForMe.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		numBatches.setEnabled(false);
		numRunsPerBatch.setEnabled(false);
		sleepTimeBetweenRuns.setEnabled(false);
		learningRate.setEnabled(false);
	}
}
