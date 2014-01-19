package org.zero.apps.hbase.manage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.apps.utils.DoWith;

public class HBASEManager {

	private boolean isAvailable = false;
	public static enum EX_TYPE {
		CONNECTION_FAIL
	}

	public static class HBaseConnectException extends RuntimeException {

		private static final long serialVersionUID = 8729957642903112348L;
		private EX_TYPE type;

		public HBaseConnectException(EX_TYPE type) {
			this.type = type;
		}

		/**
		 * @return the type
		 */
		public EX_TYPE getType() {
			return type;
		}

	}

	protected static final Logger log = LoggerFactory
			.getLogger(HBASEManager.class);
	private Configuration configure = HBaseConfiguration.create();

	protected int DEFAULT_POOL_SIZE = 12;
	protected HBaseAdmin hAdmin = null;
	protected HTablePool tablePool = null;
	
	/*
	 * 
	 * private static String HBASE_HOST = ""; private static String ZK_HOST =
	 * ""; private static String ZK_CLIENT_PORT = "";
	 */
	/***
	 * ex: )HBASEManager.initHBASEManager("name1", "name1", "2181");
	 * 
	 * @param hbaseHosts
	 *            HBase Host
	 * @param zkHosts
	 *            ZooKeeper Host
	 * @param zkClientPort
	 *            ZooKeeper Host Port
	 */
	private String hbaseHosts, zkHosts, zkClientPort;

	protected void init() {
		if(hAdmin != null) {
			try {
				hAdmin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			hAdmin = null;
		}
		
		
		if(tablePool != null) {
			try {
				tablePool.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tablePool = null;
		}
		
		testConnection();
	}

	public HBASEManager() {

	}

	public void testConnection() {
		
		if(isAvailable == false) {
			configure.set("hbase.master", hbaseHosts);
			configure.set("hbase.zookeeper.quorum", zkHosts);
			configure.set("hbase.zookeeper.property.clientPort", zkClientPort);
			
			HConnection connection;
			try {
				//connection = HConnectionManager.createConnection(configure);
				System.out.println("connect");
				connection = HConnectionManager.getConnection(configure);
				System.out.println(connection .getMaster());
				
				try {
					connection.close();
					isAvailable = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.debug("HBase Connnect Success HOST : {} , ZKHOST : {} , ZKPORT : {}",new Object[] {hbaseHosts , zkHosts, zkClientPort});
			} catch (ZooKeeperConnectionException e) {
				e.printStackTrace();
				throw new HBaseConnectException(EX_TYPE.CONNECTION_FAIL);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new HBaseConnectException(EX_TYPE.CONNECTION_FAIL);
			}
		}

	}

	public HTableInterface getTable(String tableName) {
		HTableInterface table = getTablePool().getTable(tableName);
		return table;
	}

	public void doWithTable(String tableName, DoWith<HTableInterface> doWith) {
		HTableInterface table = getTable(tableName);
		doWith.doWith(table);
		try {
			table.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Table Close Exception [" + tableName
					+ "]", e);
		}

	}

	public void doWithAdmin(DoWith<HBaseAdmin> doWith) {
		HBaseAdmin admin = getHAdmin();
		doWith.doWith(admin);
	}

	/**
	 * @return the hbaseHosts
	 */
	public String getHbaseHosts() {
		return hbaseHosts;
	}

	/**
	 * @param hbaseHosts
	 *            the hbaseHosts to set
	 */
	public void setHbaseHosts(String hbaseHosts) {
		this.hbaseHosts = hbaseHosts;
		isAvailable = false;
	}

	/**
	 * @return the zkHosts
	 */
	public String getZkHosts() {
		return zkHosts;
	}

	/**
	 * @param zkHosts
	 *            the zkHosts to set
	 */
	public void setZkHosts(String zkHosts) {
		this.zkHosts = zkHosts;
		isAvailable = false;
	}

	/**
	 * @return the zkClientPort
	 */
	public String getZkClientPort() {
		return zkClientPort;
		
	}

	/**
	 * @param zkClientPort
	 *            the zkClientPort to set
	 */
	public void setZkClientPort(String zkClientPort) {
		this.zkClientPort = zkClientPort;
		isAvailable = false;
	}

	/**
	 * @return the configure
	 */
	public Configuration getConfigure() {
		/*Iterator<Entry<String, String>> iterator2 = configure.iterator();
		while(iterator2.hasNext()){
		Entry<String, String> next = iterator2.next();
		System.out.println("\t" +next.getKey() + "\t" + next.getValue());
		}
		*/
		return configure;
	}

	/**
	 * @param configure
	 *            the configure to set
	 */
	public void setConfigure(Configuration configure) {
		this.configure = configure;
		isAvailable = false;
	}

	/**
	 * @return the hAdmin
	 */
	public HBaseAdmin getHAdmin() {
		if (hAdmin == null) {
			try {
				init();
				System.out.println("Try Get HBaseAdmin");
				hAdmin = new HBaseAdmin(getConfigure());
				
				System.out.println("Get HBaseAdmin " + hAdmin.isAborted());
				System.out.println("Get HBaseAdmin " + hAdmin.isMasterRunning());
			} catch (MasterNotRunningException e) {
				e.printStackTrace();
				throw new RuntimeException("Get HBase Admin Fail ", e);
			} catch (ZooKeeperConnectionException e) {
				e.printStackTrace();
				throw new RuntimeException("Get HBase Admin Fail ", e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Get HBase Admin Fail ", e);
			}
		}
		return hAdmin;
	}

	/**
	 * @return the tablePool
	 */
	protected HTablePool getTablePool() {
		if (tablePool == null) {
			init();
			tablePool = new HTablePool(getConfigure(), DEFAULT_POOL_SIZE);
		}
		return tablePool;
	}

}
