package org.zero.apps.hbase.manager.support;

import org.zero.apps.hbase.manager.connector.HBaseConnector;

public interface ConnectAware {

	public void setConnector(HBaseConnector connector);
}
