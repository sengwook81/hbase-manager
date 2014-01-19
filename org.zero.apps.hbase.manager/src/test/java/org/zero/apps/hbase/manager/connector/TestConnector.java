package org.zero.apps.hbase.manager.connector;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.zero.apps.utils.DoWith;

public class TestConnector extends HBaseConnector{

	public TestConnector() {
		doConnect("name1", "name1", "2181");
	}
	
	public static void main(String[] args) {
		final TestConnector testConnector = new TestConnector();
		
		testConnector.doWithAdmin(new DoWith<HBaseAdmin>() {
			
			@Override
			public HBaseAdmin doWith(HBaseAdmin src) {
				try {
					HTableDescriptor[] listTables = src.listTables();
					for(HTableDescriptor htd : listTables) {
						HTable table = new HTable(testConnector.getConfigure(), htd.getNameAsString());
						NavigableMap<HRegionInfo, ServerName> regionLocations = table.getRegionLocations();
						System.out.println(htd.getNameAsString());
						for(Entry<HRegionInfo, ServerName> entry : regionLocations.entrySet()) {
							System.out.println("\t"+ entry.getKey().getRegionNameAsString() + " - " + entry.getValue().getServerName());
						}
						table.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}
