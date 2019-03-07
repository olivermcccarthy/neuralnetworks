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
	JComboBox numBatches = new JComboBox();
	JComboBox numPerBatch = new JComboBox();
	JComboBox sleepTime = new JComboBox();
	JComboBox learningRate = new JComboBox();
	TrialInfo trialInfo;
	Object waitForMe = new String("");
    int sleepTimeMs;
    int numBatchesI;
	public ControlPanel(TrialInfo trialInfo) {

		ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				synchronized (waitForMe) {
					waitForMe.notifyAll();
					if (button.getText().equals("RUN")) {
						button.setText("STOP");

					} else {
						button.setText("RUN");
						trialInfo.numPerBatch = 0;
					}

				}

			}
		};
		button.addActionListener(listener);
		button.setBounds(0, 0, 100, 30);
		numBatches.setBounds(0, 30, 100, 30);
		numPerBatch.setBounds(0, 60, 100, 30);
		sleepTime.setBounds(0, 90, 100, 30);
		learningRate.setBounds(0, 120, 100, 30);
		this.setPreferredSize(new Dimension(100, 120));
		button.setFont(this.getFont().deriveFont(20.0f));
		this.add(numPerBatch);
		this.add(learningRate);
        this.setLayout(null);
		numPerBatch.addItem("100  per batch");
		numPerBatch.addItem("1000  per batch");
		numPerBatch.addItem("5000  per batch");
		numPerBatch.addItem("10000 per batch");
		numPerBatch.setToolTipText("Number of runs per batch. Cost is calclated at teh end of each batch");
		learningRate.addItem("1");
		learningRate.addItem(".5");
		learningRate.addItem(".1");
		learningRate.addItem(".05");
		learningRate.addItem(".01");
		learningRate.setToolTipText("Speed at which network learns/adjusts is waits");
		numBatches.addItem("1 batches");
		numBatches.addItem("10  batches");
		numBatches.addItem("100  batches");
		numBatches.addItem("1000  batches");
		numBatches.addItem("10000  batches");
		numBatches.addItem("100000  batches");
		numBatches.setToolTipText("Number of batches to run. 100 batches ");
		numBatches.setSelectedIndex(0);
		sleepTime.addItem("10000ms sleep ");
		sleepTime.addItem("5000ms sleep ");
		sleepTime.addItem("2000ms sleep");
		sleepTime.addItem("1000ms sleep");
		sleepTime.addItem("100ms sleep");
		sleepTime.addItem("10ms sleep");
		sleepTime.addItem("1ms sleep");
		sleepTime.setToolTipText("sleep between each run ");
		sleepTime.setSelectedIndex(0);
		this.add(button);
		this.add(numBatches);
		this.add(sleepTime);
		this.add(learningRate);
	}
	
	public int getSleepTime() {
		int selectedIndex = sleepTime.getSelectedIndex();
		
		if (selectedIndex == 0) {
			sleepTimeMs = 10000;
		}
		if (selectedIndex == 1) {
			sleepTimeMs = 5000;
		}
		if (selectedIndex == 2) {
			sleepTimeMs = 2000;
		}
		if (selectedIndex == 3) {
			sleepTimeMs = 1000;
		}
		if (selectedIndex == 4) {
			sleepTimeMs = 100;
		}
		if (selectedIndex == 5) {
			sleepTimeMs = 10;
		}
		if (selectedIndex == 6) {
			sleepTimeMs = 1;
		}
		
		return sleepTimeMs;
	}
	public int getNumBatches() {
		int selectedIndex = numBatches.getSelectedIndex();
		if (selectedIndex == 0) {
			numBatchesI = 1;
		}
		if (selectedIndex == 1) {
			numBatchesI = 10;
		}
		if (selectedIndex == 2) {
			numBatchesI = 100;
		}
		if (selectedIndex == 3) {
			numBatchesI = 1000;
		}
		if (selectedIndex == 4) {
			numBatchesI = 10000;
		}
		
		return numBatchesI;
	}

	public double getLearningRate() {
		int selectedIndex = this.learningRate.getSelectedIndex();
		double lr = 1;
		if (selectedIndex == 0) {
			lr = 1;
		}
		if (selectedIndex == 1) {
			lr = 0.5;
		}
		if (selectedIndex == 2) {
			lr = 0.1;
		}
		if (selectedIndex == 3) {
			lr = 0.05;

		}
		if (selectedIndex == 4) {
			lr = 0.01;

		}
		return lr;
	}

	public int getNumPerBatch() {
		int selectedIndex = numPerBatch.getSelectedIndex();
		int numPerBatch = 100;
		if (selectedIndex == 0) {
			numPerBatch = 500;
		}
		if (selectedIndex == 1) {
			numPerBatch = 1000;
		}
		if (selectedIndex == 2) {
			numPerBatch = 5000;
		}
		if (selectedIndex == 3) {
			numPerBatch = 10000;
		}
		return numPerBatch;
	}
	
	public void waitForMe() {
		button.setText("RUN");
		synchronized (waitForMe) {
			try {
				waitForMe.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
