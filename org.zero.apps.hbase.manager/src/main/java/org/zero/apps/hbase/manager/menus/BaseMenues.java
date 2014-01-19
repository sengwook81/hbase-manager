package org.zero.apps.hbase.manager.menus;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JSplitPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.component.ConnectionDialog;
import org.zero.apps.hbase.manager.connector.ConnectionInfo;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.support.event.EditorRunEvent;
import org.zero.apps.hbase.manager.support.event.ZVentRunner;
import org.zero.apps.hbase.manager.views.EditorView;
import org.zero.apps.hbase.manager.views.frames.ConnectionFrame;
import org.zero.apps.hbase.manager.views.frames.ContentFrame;

@Component
public class BaseMenues{
	protected static final Logger log = LoggerFactory.getLogger(BaseMenues.class);
	@Component
	@MENU_INFO(groupName = "Main", menuName = "New Connect", order = 0, keyEvent = KeyEvent.VK_N)
	public static class BaseNewConnect extends BaseMenuItem{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ConnectionDialog connectionDialog = getContext().getBean(ConnectionDialog.class);
			connectionDialog.setVisible(true);
			ConnectionInfo connectionInfo = connectionDialog.getConnectionInfo();
			if(connectionInfo != null) {
				ConnectionFrame bean = getContext().getBean("connectionFrame",ConnectionFrame.class);
				JSplitPane bodyFrame = getBodyFrame();
				if(bodyFrame instanceof ConnectAware) {
					ConnectAware connectAware = (ConnectAware) bodyFrame;
					HBaseConnector hBaseConnector = new HBaseConnector();
					hBaseConnector.doConnect(connectionInfo);
					connectAware.setConnector(hBaseConnector);
					
				}
				bean.addTab("Title", bodyFrame); 
			}
		}
		
		private JSplitPane getBodyFrame() {
			return getContext().getBean(ContentFrame.class);
		}
	}
	
	@Component
	@MENU_INFO(groupName = "Main", menuName = "New Editor", order = 1, keyEvent = KeyEvent.VK_N , keyMask = ActionEvent.CTRL_MASK)
	public static class BaseNewEditor extends BaseMenuItem{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			ConnectionFrame bean = getContext().getBean("connectionFrame",ConnectionFrame.class);
			java.awt.Component selectedComponent = bean.getSelectedComponent();
			if(selectedComponent instanceof ContentFrame) {
				ContentFrame contentFrame = (ContentFrame) selectedComponent;
				contentFrame.addNewEditor();
			}
		}
	}
	
	@Component
	@MENU_INFO(groupName = "Excute", menuName = "Run", order = 0, keyEvent = KeyEvent.VK_ENTER , keyMask = ActionEvent.CTRL_MASK)
	public static class ExecuteRun extends BaseMenuItem{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			run();
		}
		
		protected void run() { 
			ConnectionFrame bean = getContext().getBean("connectionFrame",ConnectionFrame.class);
			java.awt.Component selectedComponent = bean.getSelectedComponent();
			if(selectedComponent == null) {
				return ;
			}
			if(selectedComponent instanceof ContentFrame) {
				ContentFrame contentFrame = (ContentFrame) selectedComponent;
				java.awt.Component selectedComponent2 = contentFrame.getDataFrame().getSelectedComponent();
				if(selectedComponent2 instanceof ZVentRunner) {
					
					ZVentRunner zventRunner = (ZVentRunner) selectedComponent2;
					log.trace("Run : " + zventRunner);
					zventRunner.run(new EditorRunEvent());
				}
			}
		}
	}
}
