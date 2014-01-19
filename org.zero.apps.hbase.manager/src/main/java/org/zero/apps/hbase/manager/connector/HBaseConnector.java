package org.zero.apps.hbase.manager.connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manage.HBASEManager;
import org.zero.apps.utils.DoWith;

@Component
@Scope(value="prototype")
public class HBaseConnector extends HBASEManager{

	HTablePool pool = null;
	TableInfo tbManager = new TableInfo();
	
	public void doConnect(ConnectionInfo ci) {
		doConnect(ci.getHadoopHost(),ci.getZookeeperHost(),ci.getZookeeperPort());
	}
	
	public void doConnect(String hbaseHost , String zkHost , String zkPort) {
		setHbaseHosts(hbaseHost);
		setZkHosts(zkHost);
		setZkClientPort(zkPort);
		init();
	}

	
	public List<HTableDescriptor> getTableList() {
		final List<HTableDescriptor> retData =new ArrayList<HTableDescriptor>();
		doWithAdmin(new DoWith<HBaseAdmin>() {
			@Override
			public HBaseAdmin doWith(HBaseAdmin src) {
				try {
					System.out.println("Hello");
					System.out.println(src.isMasterRunning()); 
					HTableDescriptor[] listTables = src.listTables();
					retData.addAll(Arrays.asList(listTables));
					System.out.println("FINISH");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
		return retData;
	}
	
	/**
	 * @param tableDesc
	 * @return
	 * @see org.zero.apps.hbase.connector.HBaseConnector.TableInfo#getColumnDescriptor(org.apache.hadoop.hbase.HTableDescriptor)
	 */
	public List<HColumnDescriptor> getColumnDescriptor(
			HTableDescriptor tableDesc) {
		return tbManager.getColumnDescriptor(tableDesc);
	}


	/**
	 * @param tableName
	 * @return
	 * @see org.zero.apps.hbase.connector.HBaseConnector.TableInfo#getColumnDescriptor(java.lang.String)
	 */
	public List<HColumnDescriptor> getColumnDescriptor(String tableName) {
		return tbManager.getColumnDescriptor(tableName);
	}


	/**
	 * @param tableName
	 * @return
	 * @see org.zero.apps.hbase.connector.HBaseConnector.TableInfo#getTableRegions(java.lang.String)
	 */
	public List<HRegionInfo> getTableRegions(String tableName) {
		return tbManager.getTableRegions(tableName);
	}
	
	class TableInfo { 
		public List<HColumnDescriptor> getColumnDescriptor(final HTableDescriptor tableDesc) {
			List<HColumnDescriptor> retData = new ArrayList<HColumnDescriptor>();
			HColumnDescriptor[] columnFamilies = tableDesc.getColumnFamilies();
			retData.addAll(Arrays.asList(columnFamilies));
			return retData;
		}
		
		public List<HColumnDescriptor> getColumnDescriptor(final String tableName) {
			HTableDescriptor tableDescriptor = null;
			try {
				tableDescriptor = getHAdmin().getTableDescriptor(tableName.getBytes());
			} catch (TableNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("Table Not Found : " + tableName,e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Table Process Error : " + tableName,e);
			}
			return getColumnDescriptor(tableDescriptor);
		}
		
		public List<HRegionInfo> getTableRegions(final String tableName) { 
			try {
				List<HRegionInfo> tableRegions = getHAdmin().getTableRegions(tableName.getBytes());
				return tableRegions;
			} catch (IOException e) {
				throw new RuntimeException("Table Process Error : " + tableName,e);
			}
		}
	}
	
	public static void main(String[] args) {
		HBaseConnector con = new HBaseConnector();
		con.doConnect("vmserver", "vmserver", "2181");
		System.out.println("Connected");
		List<HTableDescriptor> tableList = con.getTableList();
		System.out.println("Connect");
		for(HTableDescriptor td : tableList) { 
			System.out.println(td.getNameAsString());
			List<HColumnDescriptor> columnDescriptor = con.getColumnDescriptor(td);
			for(HColumnDescriptor hcd : columnDescriptor) {
				System.out.println("\t" + new String(hcd.getName()));
				for(Entry<ImmutableBytesWritable, ImmutableBytesWritable> entrySet : hcd.getValues().entrySet()) {
					System.out.println("\t\t" + new String(entrySet.getKey().get()));
				}
			}
		}
		System.out.println("Connect Finish");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HBaseConnector [getHbaseHosts()=" + getHbaseHosts()
				+ ", getZkHosts()=" + getZkHosts() + ", getZkClientPort()="
				+ getZkClientPort() + "]";
	}


}
