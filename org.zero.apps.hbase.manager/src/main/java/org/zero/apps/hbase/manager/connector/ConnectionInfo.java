package org.zero.apps.hbase.manager.connector;

public class ConnectionInfo {

	private String hadoopHost;
	private String zookeeperHost;
	private String zookeeperPort;
	public ConnectionInfo(String hadoopHost, String zookeeperHost,String zookeeperPort) {
		super();
		this.hadoopHost = hadoopHost;
		this.zookeeperHost = zookeeperHost;
		this.zookeeperPort = zookeeperPort;
	}
	/**
	 * @return the hadoopHost
	 */
	public String getHadoopHost() {
		return hadoopHost;
	}
	/**
	 * @param hadoopHost the hadoopHost to set
	 */
	public void setHadoopHost(String hadoopHost) {
		this.hadoopHost = hadoopHost;
	}
	/**
	 * @return the zookeeperHost
	 */
	public String getZookeeperHost() {
		return zookeeperHost;
	}
	/**
	 * @param zookeeperHost the zookeeperHost to set
	 */
	public void setZookeeperHost(String zookeeperHost) {
		this.zookeeperHost = zookeeperHost;
	}
	/**
	 * @return the zookeeperPort
	 */
	public String getZookeeperPort() {
		return zookeeperPort;
	}
	/**
	 * @param zookeeperPort the zookeeperPort to set
	 */
	public void setZookeeperPort(String zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectionInfo [hadoopHost=" + hadoopHost + ", zookeeperHost="
				+ zookeeperHost + ", zookeeperPort=" + zookeeperPort + "]";
	}
}