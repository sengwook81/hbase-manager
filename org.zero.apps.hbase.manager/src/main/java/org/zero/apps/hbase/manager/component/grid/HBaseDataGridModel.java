package org.zero.apps.hbase.manager.component.grid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.table.AbstractTableModel;

import org.apache.hadoop.hbase.util.Bytes;
import org.zero.apps.hbase.manager.support.GridColumnBytesComparator;

public class HBaseDataGridModel extends AbstractTableModel {

	private static final long serialVersionUID = 7925641231301903510L;

	private class ColumnInfo {
		public ColumnInfo(byte[] family, byte[] qualifier) {
			super();
			this.family = family;
			this.qualifier = qualifier;
		}

		byte[] family;
		byte[] qualifier;

		public String getColumnInfoAsString() { 
			return new String(family) + ":" + new String(qualifier);
		}
		/**
		 * @return the qualifier
		 */
		public byte[] getQualifier() {
			return qualifier;
		}

		/**
		 * @param qualifier
		 *            the qualifier to set
		 */
		public void setQualifier(byte[] qualifier) {
			this.qualifier = qualifier;
		}

		/**
		 * @return the family
		 */
		public byte[] getFamily() {
			return family;
		}

		/**
		 * @param family
		 *            the family to set
		 */
		public void setFamily(byte[] family) {
			this.family = family;
		}

	}
	public static GridColumnBytesComparator byteColumnComparator = new  GridColumnBytesComparator();
	private LinkedHashMap<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>> rowData;

	List<byte[]> keyList = new ArrayList<byte[]>();
	List<ColumnInfo> colList = new ArrayList<ColumnInfo>();

	public HBaseDataGridModel(
			LinkedHashMap<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>> rowData) {
		this.rowData = rowData;
		TreeMap<byte[],Set<byte[]>> rsltColumnMap = new TreeMap<byte[], Set<byte[]>>(byteColumnComparator);
		for(Entry<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>> rowEntry : rowData.entrySet()) {
			for(Entry<byte[], NavigableMap<byte[], byte[]>> entry : rowEntry.getValue().entrySet()) {
				if(!rsltColumnMap.containsKey(entry.getKey())) {
					TreeSet<byte[]> treeSet = new TreeSet<byte[]>(byteColumnComparator);
					treeSet.addAll(entry.getValue().keySet());
					rsltColumnMap.put(entry.getKey(), treeSet);
				}
				else {
					rsltColumnMap.get(entry.getKey()).addAll(entry.getValue().keySet());
//					System.out.println(rsltColumnMap.get(entry.getKey()));
				}
			}
		}
		
		
		if (rowData.size() > 0) {
			keyList.addAll(rowData.keySet());
			byte[] rowKey = keyList.get(0);
			NavigableMap<byte[], NavigableMap<byte[], byte[]>> navigableMap = rowData
					.get(rowKey);
			for(Entry<byte[], Set<byte[]>> columnFamilyEntry : rsltColumnMap.entrySet()) {
				
				for(byte [] qualifier : columnFamilyEntry.getValue()) {
					colList.add(new ColumnInfo(columnFamilyEntry.getKey(), qualifier));
				}
			}
		}
	}

	@Override
	public int getRowCount() {
		return keyList.size();
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "RowKey"; // RowKey Column Plus
		} else {
			return colList.get(column-1).getColumnInfoAsString();
		}
	}

	@Override
	public int getColumnCount() {
		if (colList.size() > 0) {
			return colList.size() + 1; // RowKey Column Plus
		}
		return colList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		byte[] rowkey = keyList.get(rowIndex);
		if (columnIndex == 0) {
			return new String(rowkey);
		} else {
			ColumnInfo columnInfo = colList.get(columnIndex - 1);
			try {
				byte[] colVal = rowData.get(rowkey).get(columnInfo.getFamily())
						.get(columnInfo.getQualifier());
				if (colVal != null) {
					return new String(colVal);
				}
			} catch (NullPointerException ne) {
				return null;
			}
		}
		return null;
	}
}
