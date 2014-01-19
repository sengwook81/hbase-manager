package org.zero.apps.hbase.manager.views.frames;

import java.awt.BorderLayout;

import javax.annotation.PostConstruct;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.views.EditorView;

/**
 * ConnectFrame -> ContentFrame  -> InfoTabFrame
 * 								 -> DataFrame
 * 접속별 Schema 정보와 Editor Tab을 관리하는 프레임
 * @author Administrator
 *
 */
@Component("contentFrame")
@Scope("prototype")
public class ContentFrame extends JSplitPane implements ApplicationContextAware , ConnectAware{
	
	protected static final Logger log = LoggerFactory.getLogger(ContentFrame.class);
	
	private static final long serialVersionUID = 8381016544138193517L;

	private ApplicationContext applicationContext;
	private HBaseConnector hbaseConnector;
	private JTabbedPane tabFrame;
	private JTabbedPane dataFrame;

	private JPanel editorWrapPanel;

	public ContentFrame() {
		
		editorWrapPanel = new JPanel();
		setRightComponent(editorWrapPanel);
		editorWrapPanel.setLayout(new BorderLayout(0, 0));
		setDividerLocation(300);
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		editorWrapPanel.add(tabbedPane, BorderLayout.CENTER);
	}


	@PostConstruct
	protected void init() {
		tabFrame = applicationContext.getBean("infoTabFrame",
				InfoTabFrame.class);
		tabFrame.setTabPlacement(JTabbedPane.TOP);
		setLeftComponent(tabFrame);
		
		dataFrame = applicationContext.getBean("dataFrame", JTabbedPane.class);
		editorWrapPanel.add(dataFrame, BorderLayout.CENTER);
		setRightComponent(editorWrapPanel);
		
		addNewEditor();
		System.out.println(tabFrame.getParent().getClass());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		this.hbaseConnector = connector;
		log.trace("Set Connector {}",hbaseConnector);
		if (dataFrame instanceof ConnectAware) {
			ConnectAware connectAware = (ConnectAware) dataFrame;
			connectAware.setConnector(hbaseConnector);
			for(java.awt.Component cpnt: dataFrame.getComponents()) { 
				if (cpnt instanceof ConnectAware) {
					ConnectAware connectAware2 = (ConnectAware) cpnt;
					connectAware2.setConnector(connector);
				}
			}
		}
		if (tabFrame instanceof ConnectAware) {
			ConnectAware connectAware = (ConnectAware) tabFrame;
			connectAware.setConnector(hbaseConnector);
		}
	}
	
	public void addNewEditor() { 
		EditorView bean = applicationContext.getBean(EditorView.class);
		dataFrame.addTab("Edit" + (dataFrame.getComponentCount() + 1), bean);
	}


	/**
	 * @return the tabFrame
	 */
	public JTabbedPane getTabFrame() {
		return tabFrame;
	}


	/**
	 * @return the dataFrame
	 */
	public JTabbedPane getDataFrame() {
		return dataFrame;
	}

}
