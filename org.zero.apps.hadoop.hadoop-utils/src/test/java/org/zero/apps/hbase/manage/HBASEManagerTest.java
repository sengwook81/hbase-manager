package org.zero.apps.hbase.manage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.junit.Test;
import org.zero.apps.utils.DoWith;

public class HBASEManagerTest {

	@Test
	public void test() {
		HBASEManager manager = new HBASEManager();
		manager .setHbaseHosts("name1");
		manager .setZkHosts("name1");
		manager .setZkClientPort("2181");
		manager.init();
		
		manager.doWithTable("TB_ARTICLE", new DoWith<HTableInterface>() {
			
			public HTable doWith(HTableInterface table) {
				
				try {
					ResultScanner scanner = table.getScanner(new Scan());
					Iterator<Result> iterator = scanner.iterator();
					System.out.println("Read Start");
					while(iterator.hasNext()) {
						Result next = iterator.next();
						System.out.println(new String(next .getRow()));
						
					}
					System.out.println("Read Finish");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
	}

}
