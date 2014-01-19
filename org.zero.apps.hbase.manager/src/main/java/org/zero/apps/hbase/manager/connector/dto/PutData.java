package org.zero.apps.hbase.manager.connector.dto;

import java.util.ArrayList;
import java.util.List;

public class PutData {
	String table;
	String rowKey;
	long timeStamp;
	List<ColumnData> columnInfos = new ArrayList<ColumnData>();

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	/**
	 * @param table
	 *            the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the rowKey
	 */
	public String getRowKey() {
		return rowKey;
	}

	/**
	 * @param rowKey
	 *            the rowKey to set
	 */
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the columnInfos
	 */
	public List<ColumnData> getColumnInfos() {
		return columnInfos;
	}

	/**
	 * @param columnInfos
	 *            the columnInfos to set
	 */
	public void setColumnInfos(List<ColumnData> columnInfos) {
		this.columnInfos = columnInfos;
	}

	public void addColumnInfo(String columnFamily, String qualifier,
			Object value) {
		columnInfos.add(new ColumnData(columnFamily, qualifier, value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PutInfo [table=" + table + ", rowKey=" + rowKey
				+ ", timeStamp=" + timeStamp + ", columnInfos=" + columnInfos
				+ "]";
	}
}