package org.zero.apps.hbase.manager.script;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.support.DataUtil;

@Component
public  class CompareProvider implements ScriptOperator {
		public static SubstringComparator SubString(String string) {
			return new SubstringComparator(string);
		}
		public static RegexStringComparator RegexString(String expr) {
			return new RegexStringComparator(expr);
		}
		public static BinaryPrefixComparator BinaryPrefix(Object obj) { 
			byte[] convertObjectToBytes = DataUtil.convertObjectToBytes(obj);
			return new BinaryPrefixComparator(convertObjectToBytes);
		}
		public static BinaryComparator Binary(Object obj) { 
			byte[] convertObjectToBytes = DataUtil.convertObjectToBytes(obj);
			return new BinaryComparator(convertObjectToBytes);
		}
		@Override
		public String getVarName() {
			return "COMPARE";
		}
		@Override
		public String getResourcePath() {
			return null;
		}
		@Override
		public void setDataGrid(HBaseDataGrid grid) {
		}

		/*추후에 생각해보자..*/
		/*		
			public static BitComparator Bit(Object obj) { 
			byte[] convertObjectToBytes = convertObjectToBytes(obj);
			return new BitComparator(convertObjectToBytes);
		}*/
	}