package org.zero.apps.hbase.manager.views.tab;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.utils.DoWith;

@Component
@Scope(value = "prototype")
public class TableInfos extends JScrollPane implements ZTabView {

	protected static final Logger log = LoggerFactory
			.getLogger(TableInfos.class);

	private class TableTreeModel extends DefaultTreeModel {
		private static final long serialVersionUID = 1L;

		public TableTreeModel(TreeNode root) {
			super(root);
		}
	}

	private JTree tree;
	private HBaseConnector connector;

	public TableInfos() {
		tree = new JTree();
		setViewportView(tree);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(tree, popupMenu);

		JMenu mnHello = new JMenu("Hello");
		popupMenu.add(mnHello);
	}

	@Override
	public String getTabName() {
		return "SCHEMA";
	}

	@Override
	public void refresh() {
		final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		rootNode.setAllowsChildren(true);
		rootNode.setUserObject(connector.getHbaseHosts());
		final DefaultMutableTreeNode tableList = new DefaultMutableTreeNode(
				"Table List", true);
		final DefaultMutableTreeNode serverList = new DefaultMutableTreeNode(
				"Server List", true);
		//tableList.setParent(rootNode);
		rootNode.add(tableList);
		rootNode.add(serverList);

		connector.doWithAdmin(new DoWith<HBaseAdmin>() {
			@Override
			public HBaseAdmin doWith(HBaseAdmin src) {
				try {
					HTableDescriptor[] listTables = src.listTables();
					for (HTableDescriptor table : listTables) {
						DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode();
						tableNode.setAllowsChildren(true);
						tableNode.setUserObject(table.getNameAsString());
						// Column Family
						DefaultMutableTreeNode tableColumnFamily = getTableColumnFamily(src,table);
						tableNode.add(tableColumnFamily);
						tableList.add(tableNode);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DefaultMutableTreeNode tableRegions = getTableRegions(src);
				rootNode.add(tableRegions);
				return null;
			}
			DefaultMutableTreeNode getTableColumnFamily(HBaseAdmin admin , HTableDescriptor table) {
				DefaultMutableTreeNode cfGroupNode = new DefaultMutableTreeNode("ColumnFamily", true);
				HColumnDescriptor[] columnFamilies = table.getColumnFamilies();
				for (HColumnDescriptor cf : columnFamilies) {
					DefaultMutableTreeNode cfNode = new DefaultMutableTreeNode(
							cf.getNameAsString(), false);
					//cfNode.setParent(cfGroupNode);
					cfGroupNode.add(cfNode);
				}
				
				return cfGroupNode;
			}
			
			DefaultMutableTreeNode getTableRegions(HBaseAdmin admin) {
				DefaultMutableTreeNode regionNode = new DefaultMutableTreeNode("Server List", true);
				try {
					Collection<ServerName> servers = admin.getClusterStatus().getServers();
					for(ServerName sn : servers) {
						DefaultMutableTreeNode rgfNode = new DefaultMutableTreeNode(
								new String(sn.getHostname()),true);
						serverList.add(rgfNode);
					}
					/*
					List<HRegionInfo> tableRegions = admin.getTableRegions(tableName.getBytes());
					
					for(HRegionInfo hri : tableRegions) {
						
						log.trace("RegionServerInfo Name[{}]",new String(hri.getRegionName()));
						
						regionNode.add(rgfNode);
					}*/
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return serverList;
			}
		});
		tree.setModel(new TableTreeModel(rootNode));
		System.out.println("INIT SCHEMA LOAD");
	}

	private static void addPopup(java.awt.Component component,
			final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		this.connector = connector;
		log.trace("Set Connector : {}", connector);
		refresh();
	}
}
