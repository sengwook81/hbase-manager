package org.zero.apps.hbase.manager.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.connector.dto.PutData;
import org.zero.apps.hbase.manager.connector.dto.ScanData;

@Component
public class HBaseDataProvider implements ScriptOperator {

	protected static final Logger log = LoggerFactory.getLogger(HBaseDataProvider.class);

	public static ScanData scanData() {
		log.trace("Return ScanData");
		return new ScanData();
	}

	public static PutData putData() {
		log.trace("Return PutData");
		return new PutData();
	}

	@Override
	public String getVarName() {
		return "NEWDATA";
	}

	@Override
	public String getResourcePath() {
		return null;
	}

	@Override
	public void setDataGrid(HBaseDataGrid grid) {
	}
}