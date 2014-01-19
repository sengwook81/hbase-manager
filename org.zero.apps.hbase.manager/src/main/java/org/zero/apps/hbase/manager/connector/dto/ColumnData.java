package org.zero.apps.hbase.manager.connector.dto;

public class ColumnData {
	public ColumnData(String columnFamily, String qualifier, Object value) {
		super();
		this.columnFamily = columnFamily;
		this.qualifier = qualifier;
		this.value = value;
	}

	String columnFamily;
	String qualifier;
	Object value;

	/**
	 * @return the columnFamily
	 */
	public String getColumnFamily() {
		return columnFamily;
	}

	/**
	 * @param columnFamily
	 *            the columnFamily to set
	 */
	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * @param qualifier
	 *            the qualifier to set
	 */
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ColumnData [columnFamily=" + columnFamily + ", qualifier="
				+ qualifier + ", value=" + value + "]";
	}
}
