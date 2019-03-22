package oliver.neuron.ui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class MyTableModel extends AbstractTableModel{

	ArrayList<String[]> messages= new ArrayList<String[]>();
	String[] colNames;
	int colCount;
	public MyTableModel(int colCount) {
		this.colCount = colCount;
		
	}
	 public String getColumnName(int column) {
		 return this.colNames[column];
	 }
	public MyTableModel(String[] batchcolumnNames) {
		
		this.colNames = batchcolumnNames;
		this.colCount = batchcolumnNames.length;
		 messages= new ArrayList<String[]>();
	}
	@Override
	public int getRowCount() {
		if(messages == null) {
			return 0;
		}
		// TODO Auto-generated method stub
		return messages.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colCount;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(rowIndex > 0) {
			int debugME =0;
		}
		try {
		 return messages.get(rowIndex )[columnIndex];	
			
		}catch (Throwable t) {
			
		}
		// TODO Auto-generated method stub
		return "";
	}
	
	public void addRow(String[] data) {
		
		try {
			while(messages.size() > 20) {
				messages.remove(messages.size() -1);
			}
			messages.add(data);
		}catch (Throwable t) {
			
		}
		
	}
	public void updateRow(String[] data) {
		try {
			if(messages.size() > 0) {
				messages.set(0,data);
			}else {
			  messages.add(data);
			}
		}catch (Throwable t) {
			
		}
		
	}
}
