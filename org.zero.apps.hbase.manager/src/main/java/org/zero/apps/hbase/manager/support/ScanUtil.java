package org.zero.apps.hbase.manager.support;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NavigableMap;

import javax.swing.table.TableModel;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGridModel;

public class ScanUtil {

	public static class PagingInfo { 
		private TableModel dataModel;
		private byte []lastRow;
		
		public PagingInfo( TableModel dataModel,byte[] lastRow) {
			super();
			
			this.dataModel = dataModel;
			this.lastRow = lastRow;
		}
		/**
		 * @return the lastRow
		 */
		public byte[] getLastRow() {
			return lastRow;
		}
		/**
		 * @param lastRow the lastRow to set
		 */
		public void setLastRow(byte[] lastRow) {
			this.lastRow = lastRow;
		}
		/**
		 * @return the dataModel
		 */
		public TableModel getDataModel() {
			return dataModel;
		}
		/**
		 * @param dataModel the dataModel to set
		 */
		public void setDataModel(TableModel dataModel) {
			this.dataModel = dataModel;
		}
	}
	public static PagingInfo getTableModel(ResultScanner scanner) {
		LinkedHashMap<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>> rowList = new LinkedHashMap<byte[], NavigableMap<byte[], NavigableMap<byte[], byte[]>>>();
		Iterator<Result> iterator = scanner.iterator();
		byte [] lastRow = null;
		while (iterator.hasNext()) {
			Result next = iterator.next();
			rowList.put(next.getRow(), next.getNoVersionMap());
			lastRow = next.getRow();
		}
		scanner.close();
		return new PagingInfo(new HBaseDataGridModel(rowList) , lastRow);
	}
	
	
}
