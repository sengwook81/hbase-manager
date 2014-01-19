package org.zero.apps.hbase.manager.component.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class HbaseSingleRowModel extends AbstractTableModel {

	private Map<String, Object> input;
	private List<Object> valueList = new ArrayList<Object>();
	private List<String> keyList = new ArrayList<String>();

	public HbaseSingleRowModel(Map<String,Object> val) {
		this.input = val;
		valueList.addAll(input.values());
		keyList.addAll(input.keySet());
	}
	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount() {
		return input.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return valueList.get(columnIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return keyList.get(column);
	}

	
}
