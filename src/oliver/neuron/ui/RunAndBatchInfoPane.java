package oliver.neuron.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

public class RunAndBatchInfoPane extends JTextPane{

	/**
	 * Info on runs in the batch ( Last 20)
	 */
	JTable runInfoPane = new JTable();

	/**
	 * Info on each batch run ( Last 20)
	 */
	JTable batchInfoPane = new JTable();
	// Column Names
	String[] columnNames = { "Batch" ,"Run", "Expected", "Got" };
	String[] batchcolumnNames = { "Batch", "Run", "Correct", "%Correct" };
	DrawNeuralNetwork parent;
	public  RunAndBatchInfoPane (DrawNeuralNetwork parent) {
		this.parent = parent;
		MyTableModel model = new MyTableModel(columnNames);
		runInfoPane = new JTable(model);
		JScrollPane scroll2 = new JScrollPane(runInfoPane);
		runInfoPane.setFont(getFont().deriveFont(12.0f));
		this.setFont(getFont().deriveFont(14.0f));
		this.setText(" Info on runs" + "                   Info on batches");
		scroll2.setBounds(0, 20, 250, 80);
		this.setLayout(null);
		

		// Initializing the JTable

		MyTableModel model2 = new MyTableModel(batchcolumnNames);
		batchInfoPane = new JTable(model2);
		JScrollPane scroll4 = new JScrollPane(batchInfoPane);
		batchInfoPane.setFont(getFont().deriveFont(12.0f));
		scroll4.setBounds(300, 20, 250, 80);
		batchInfoPane.setBackground(parent.getBackground());
		this.setBackground(parent.getBackground());
		this.add(scroll2);
		this.add(scroll4);
		
	}
	
	public void addRunInfoStats(String message, boolean update) {
		// DefaultTableModel model = new DefaultTableModel();
		// this.runInfoPane.setModel(model);

		MyTableModel model = (MyTableModel) runInfoPane.getModel();

		if (!message.startsWith("Click")) {

			if (update) {
				model.updateRow(message.split(","));
			} else {
				model.addRow(message.split(","));
			}

			runInfoPane.repaint();
		}
	}
	public void addBatchInfoStats(String message, boolean update) {
		// DefaultTableModel model = new DefaultTableModel();
		// this.runInfoPane.setModel(model);

		MyTableModel model = (MyTableModel) batchInfoPane.getModel();

		if (!message.startsWith("Click")) {

			if (update) {
				model.updateRow(message.split(","));
			} else {
				model.addRow(message.split(","));
			}

			runInfoPane.repaint();
		}
	}
}
