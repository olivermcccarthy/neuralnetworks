package oliver.neuron.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class RunAndBatachInfoPane extends JPanel{

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
	public  RunAndBatachInfoPane () {
		MyTableModel model = new MyTableModel(columnNames);
		runInfoPane = new JTable(model);
		JScrollPane scroll2 = new JScrollPane(runInfoPane);
		runInfoPane.setFont(getFont().deriveFont(12.0f));
		
		scroll2.setBounds(0, 0, 250, 80);
		this.setLayout(null);
		

		// Initializing the JTable

		MyTableModel model2 = new MyTableModel(batchcolumnNames);
		batchInfoPane = new JTable(model2);
		JScrollPane scroll4 = new JScrollPane(batchInfoPane);
		batchInfoPane.setFont(getFont().deriveFont(12.0f));
		scroll4.setBounds(300, 0, 250, 80);
		batchInfoPane.setBackground(this.getBackground());
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
