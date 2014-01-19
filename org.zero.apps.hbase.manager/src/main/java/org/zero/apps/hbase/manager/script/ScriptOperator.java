package org.zero.apps.hbase.manager.script;

import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;

public interface ScriptOperator {
	public String getVarName();
	public String getResourcePath();
	public void setDataGrid(HBaseDataGrid grid);
}
