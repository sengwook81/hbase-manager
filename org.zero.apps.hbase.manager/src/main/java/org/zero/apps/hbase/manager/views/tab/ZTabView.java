package org.zero.apps.hbase.manager.views.tab;

import org.zero.apps.hbase.manager.support.ConnectAware;


public interface ZTabView  extends ConnectAware{
	public abstract String getTabName();
	public abstract void refresh();
/*	public ConnectionInfo getConnectionInfo() ;
	public void setConnectionInfo(ConnectionInfo connectionInfo) ;*/
}
