package org.zero.apps.hbase.manager.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NavigableMap;

import javax.annotation.PostConstruct;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGridModel;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.script.DefaultHBaseScriptOperator;
import org.zero.apps.hbase.manager.script.HbaseCommandScriptManager;
import org.zero.apps.hbase.manager.script.ScriptExcutor;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.support.event.EditorRunEvent;
import org.zero.apps.hbase.manager.support.event.ZVentRunner;
import org.zero.apps.utils.DoWith;

import javax.swing.JLabel;


@Component
@Scope("prototype")
public class EditorView extends JPanel implements ConnectAware , ZVentRunner<EditorRunEvent>{
	
	protected static final Logger log = LoggerFactory.getLogger(EditorView.class);
	
	@Autowired
	HbaseCommandScriptManager scriptManager; 
	
	private HBaseConnector connector;
	private HBaseDataGrid dataGrid;
	private JTextPane textPane;
	
	private ScriptExcutor scriptExcutor;

	private JLabel lbStatus;

	@PostConstruct
	public void init() { 
		scriptExcutor = scriptManager.getJavaScriptEngine();
		scriptExcutor.setDataGrid(dataGrid);
		scriptExcutor.setConnector(connector);
	}
	public EditorView() {
		setLayout(new BorderLayout(0, 0));
		//setResizable(true);
		//setMaximizable(true);
		//setClosable(true);
		JSplitPane splitPane = new JSplitPane();
//		splitPane.setResizeWeight(1.0);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		/*getContentPane().*/add(splitPane);
		
		JPanel pnDataGrid = new JPanel();
		splitPane.setRightComponent(pnDataGrid);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
	    System.out.println("ContentFrameSet");
		pnDataGrid.setLayout(new BorderLayout(0, 0));
		
		dataGrid = new HBaseDataGrid();
		pnDataGrid.add(dataGrid);
		
		JPanel pnTextArea = new JPanel();
		splitPane.setLeftComponent(pnTextArea);
		pnTextArea.setLayout(new BorderLayout(0, 0));
		
		textPane = new JTextPane();
		pnTextArea.add(textPane);
		pnTextArea.setSize((int) pnTextArea.getSize().getWidth(), 300);
		
		lbStatus = new JLabel("New label");
		add(lbStatus, BorderLayout.SOUTH);
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		log.trace("Set Connector : {}",connector);
		this.connector = connector;
		scriptExcutor.setConnector(connector);
	}
	
	@Override
	public void run(EditorRunEvent event) {
		log.debug("Run Event Receive");
		Execute();
	}
	
	protected void Execute() {
		String parse = scriptExcutor.parse(textPane.getText());
		lbStatus.setText(parse);
	}

}

