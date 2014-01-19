package org.zero.apps.hbase.manager;

public enum MenuConstant {

	CONNECT("CONNECT")
	,DISCONNECT("DISCONNECT")
	,QUIT("QUIT");
	
	private String menuName;
	
	MenuConstant(String val){ 
		menuName = val;
	}
	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

}
