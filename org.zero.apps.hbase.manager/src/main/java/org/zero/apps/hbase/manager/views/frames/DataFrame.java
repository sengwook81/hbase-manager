package org.zero.apps.hbase.manager.views.frames;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.views.EditorView;

/**
 * Connection 별로 Data 를 표현하는 MDI View
 * @author Administrator
 *
 */
@Component("dataFrame")
@Scope("prototype")
public class DataFrame extends JTabbedPane implements ConnectAware{
	public DataFrame() {
		JPanel panel = new EditorView();
		//this.addTab("New tab", null, panel, null);
	}

protected static final Logger log = LoggerFactory.getLogger(DataFrame.class);
	private static final long serialVersionUID = -3072269191366219176L;
	private HBaseConnector connector;

	
	public void setConnector(HBaseConnector connector) {
		this.connector = connector;
		for(java.awt.Component cpnt : getComponents()) {
			if(cpnt instanceof ConnectAware) {
				ConnectAware connectAware = (ConnectAware) cpnt;
				connectAware.setConnector(connector);
			}	
		}
	}

	@Override
	public void addTab(String title, Icon icon, java.awt.Component component, String tip) {
		log.trace("Add Tab {}", title);
		if(component instanceof ConnectAware) {
			ConnectAware connectAware = (ConnectAware) component;
			connectAware.setConnector(connector);
		}
		/*JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(new BorderLayout(0, 0));
		wrapPanel.add(component,BorderLayout.CENTER);*/
		super.addTab(title, icon, component, tip);
	}


	/* (non-Javadoc)
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon, java.awt.Component)
	 */
	@Override
	public void addTab(String title, Icon icon, java.awt.Component component) {
		// TODO Auto-generated method stub
		addTab(title, icon, component,null);
	}


	/* (non-Javadoc)
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, java.awt.Component)
	 */
	@Override
	public void addTab(String title, java.awt.Component component) {
		// TODO Auto-generated method stub
		addTab(title, null,component,null);
	}
	
	//@Override
	/*
	public java.awt.Component addTab(String Name , java.awt.Component component) { 
		if(component instanceof ConnectAware) {
			ConnectAware connectAware = (ConnectAware) component;
			connectAware.setConnector(connector);
		}
		JPanel wrapPanel = new JPanel();
		wrapPanel.setLayout(new BorderLayout(0, 0));
		wrapPanel.add(component,BorderLayout.CENTER);
		return super.add(wrapPanel);
	}*/

}
