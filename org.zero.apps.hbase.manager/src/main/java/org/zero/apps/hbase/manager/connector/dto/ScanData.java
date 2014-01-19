package org.zero.apps.hbase.manager.connector.dto;

import org.apache.hadoop.hbase.filter.Filter;

public class ScanData {

	private String table;
	private Filter filter;
	private String startRow;
	private String endRow;
	private long startTimeStamp;
	private long endTimeStamp;
	
	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
	/**
	 * @param table the table to set
	 */
	public void setTable(String table) {
		this.table = table;
	}
	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
	/**
	 * @return the startRow
	 */
	public String getStartRow() {
		return startRow;
	}
	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(String startRow) {
		this.startRow = startRow;
	}
	/**
	 * @return the endRow
	 */
	public String getEndRow() {
		return endRow;
	}
	/**
	 * @param endRow the endRow to set
	 */
	public void setEndRow(String endRow) {
		this.endRow = endRow;
	}
	/**
	 * @return the startTimeStamp
	 */
	public long getStartTimeStamp() {
		return startTimeStamp;
	}
	/**
	 * @param startTimeStamp the startTimeStamp to set
	 */
	public void setStartTimeStamp(long startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}
	/**
	 * @return the endTimeStamp
	 */
	public long getEndTimeStamp() {
		return endTimeStamp;
	}
	/**
	 * @param endTimeStamp the endTimeStamp to set
	 */
	public void setEndTimeStamp(long endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}
}
