package org.zero.apps.hbase.manager.script;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.component.grid.HbaseSingleRowModel;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.connector.dto.ColumnData;
import org.zero.apps.hbase.manager.connector.dto.PutData;
import org.zero.apps.hbase.manager.connector.dto.ScanData;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.support.DataUtil;
import org.zero.apps.hbase.manager.support.ScanUtil;
import org.zero.apps.hbase.manager.support.ScanUtil.PagingInfo;
import org.zero.apps.utils.DoWith;

@Component
@Scope("prototype")
public class DefaultHBaseScriptOperator implements ConnectAware , ScriptOperator{

	protected static final Logger log = LoggerFactory.getLogger(DefaultHBaseScriptOperator.class);
		
	private HBaseConnector connector;

	public DefaultHBaseScriptOperator() {
	}
	
	private HBaseDataGrid outputGrid;
	

	public void put(final PutData putInfo) throws Exception {
		log.trace("PUT : {}" , putInfo);
		connector.doWithTable(putInfo.getTable(), new DoWith<HTableInterface>() {
			
			@Override
			public HTableInterface doWith(HTableInterface src) {
				Put put = new Put(Bytes.toBytes(putInfo.getRowKey()));
				for(ColumnData data :  putInfo.getColumnInfos()) {
					put.add(data.getColumnFamily().getBytes() , data.getQualifier().getBytes() , DataUtil.convertObjectToBytes(data.getValue()));
				}
				try {
					src.put(put);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return src;
			}
		});
	}
	
	public void scan(final ScanData scanData) {
		log.trace("SCAN : {}" , scanData);
		log.trace("Connector Info : {}",connector);
		connector.doWithTable(scanData.getTable(), new DoWith<HTableInterface>() {
			@Override
			public HTableInterface doWith(HTableInterface src) {
				Scan scan = new Scan();
				scan.setCaching(300);
				if(scanData.getFilter() instanceof FilterList) {
					FilterList filterList = (FilterList) scanData.getFilter();
				}
				scan.setFilter(scanData.getFilter());
				
				try {
					ResultScanner scanner = src.getScanner(scan);
					
					PagingInfo tableModel = ScanUtil.getTableModel(scanner);
					
					if(outputGrid != null)
						outputGrid.setDataModel(tableModel.getDataModel());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return src;
			}
		});
	}
	
	public void count(final ScanData scanData) {
		log.trace("COUNT : {}" , scanData);
		log.trace("Connector Info : {}",connector);
		
		connector.doWithTable(scanData.getTable(), new DoWith<HTableInterface>() {
			long counter = 0;
			@Override
			public HTableInterface doWith(HTableInterface src) {
				Scan scan = new Scan();
				scan.setCaching(1000);
				/*if(scanData.getFilter() instanceof FilterList) {
					FilterList filterList = (FilterList) scanData.getFilter();
				}*/
				scan.setFilter(scanData.getFilter());
				
				try {
					ResultScanner scanner = src.getScanner(scan);
					
					while(scanner.iterator().next() != null) { 
						counter++;
					}
					
					HashMap<String,Object> dMap = new HashMap<String, Object>();
					dMap.put("COUNTER", counter);
					PagingInfo tableModel = new PagingInfo(new HbaseSingleRowModel(dMap), null);
					if(outputGrid != null)
						outputGrid.setDataModel(tableModel.getDataModel());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return src;
			}
		});
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		this.connector = connector;
	}


	@Override
	public String getVarName() {
		return "HBASE";
	}

	@Override
	public void setDataGrid(HBaseDataGrid grid) {
		outputGrid = grid;
		
	}

	@Override
	public String getResourcePath() {
		return "classpath:script/hbase.js";
	}
	
}