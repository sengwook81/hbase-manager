package org.zero.apps.hbase.manager.script;

import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.support.DataUtil;

@Component
public class FilterProvider implements ScriptOperator {
	// final byte [] family, final byte [] qualifier, final CompareOp compareOp,
	// final byte[] value
	public static SingleColumnValueFilter SingleColumnValue(String family, String qualifier, String compareOp, Object value) {

		byte[] convertObjectToBytes = DataUtil.convertObjectToBytes(value);
		return new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), CompareOp.valueOf(compareOp.toUpperCase()),
				convertObjectToBytes);
	}

/*	public static SingleColumnValueFilter SingleColumnValue(String family, String qualifier, String compareOp, WritableByteArrayComparable value) {
		return new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), CompareOp.valueOf(compareOp.toUpperCase()), value);
	}*/
	
	public static SingleColumnValueFilter SingleColumnValue(String family, String qualifier, String compareOp, ByteArrayComparable value) {
		return new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), CompareOp.valueOf(compareOp.toUpperCase()), value);
	}
	

	public static PrefixFilter Prefix(String prefix) {
		return new PrefixFilter(Bytes.toBytes(prefix));
	}

	public static PageFilter Page(int size) {
		PageFilter pageFilter = new PageFilter(size);
		return pageFilter;
	}
	
	/*public static RowFilter Row(String compareOp, WritableByteArrayComparable value) {
		return new RowFilter(CompareOp.valueOf(compareOp.toUpperCase()), value);
	}*/
	

	public static RowFilter Row(String compareOp, ByteArrayComparable value) {
		return new RowFilter(CompareOp.valueOf(compareOp.toUpperCase()), value);
	}

	
	public static FilterList FilterList() {
		return new FilterList();
	}

	@Override
	public String getVarName() {
		
		return "FILTER";
	}

	@Override
	public String getResourcePath() {
		return null;
	}

	@Override
	public void setDataGrid(HBaseDataGrid grid) {
	}
}