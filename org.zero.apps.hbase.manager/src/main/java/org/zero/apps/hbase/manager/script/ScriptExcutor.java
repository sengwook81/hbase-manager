package org.zero.apps.hbase.manager.script;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.grid.HBaseDataGrid;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.support.SimpleMessageException;

@Component
@Scope("prototype")
public class ScriptExcutor implements ConnectAware {
	ScriptEngine engine;
	
	public List<ScriptOperator> operators = new ArrayList<ScriptOperator>();

	private HBaseDataGrid grid;
	
	public String parse(String text) { 
		try {
			StringWriter stringWriter = new StringWriter();
			engine.getContext().setWriter(stringWriter);
			engine.eval(text);
			IOUtils.closeQuietly(stringWriter);
			return stringWriter.getBuffer().toString();
		} catch (ScriptException e) {
			throw new SimpleMessageException("스크립트 분석 오류 ",e);
		}
	}
	
	public void addOperator(ScriptOperator operator) { 
		operators.add(operator);
	}
	public void setDataGrid(HBaseDataGrid grid){
		this.grid = grid; 
		for(ScriptOperator sc : operators) {
			sc.setDataGrid(grid);
		}
	}

	/**
	 * @param engine the engine to set
	 */
	public void setEngine(ScriptEngine engine) {
		this.engine = engine;
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		for(ScriptOperator sc : operators) {
			if(sc instanceof ConnectAware) {
				ConnectAware connectAware = (ConnectAware) sc;
				connectAware.setConnector(connector);
			}
		}
	}
}
