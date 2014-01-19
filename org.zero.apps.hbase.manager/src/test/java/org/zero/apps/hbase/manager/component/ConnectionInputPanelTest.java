package org.zero.apps.hbase.manager.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.zero.apps.hbase.manager.component.ConnectionDialog;
import org.zero.apps.hbase.manager.connector.ConnectionInfo;
import org.zero.apps.hbase.manager.views.tab.TableInfos;

public class ConnectionInputPanelTest extends JFrame implements ActionListener{

	public ConnectionInputPanelTest()
	{
		// Set the frame characteristics
		setTitle( "ConnectionInput Popup Test" );
		setSize( 1024, 768 );
		setBackground( Color.gray );

		// Create a panel to hold all other components
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );
		JButton jButton = new JButton("Test");
		topPanel.add(jButton);
		jButton.addActionListener(this);
	}

	// Main entry point for this example
	public static void main( String args[] )
	{
		// Create an instance of the test application
		ConnectionInputPanelTest mainFrame	= new ConnectionInputPanelTest();
		mainFrame.setVisible( true );
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectionDialog newConnectionPopupPanel = new ConnectionDialog();
		
		newConnectionPopupPanel.setVisible(true);
		ConnectionInfo connectionInfo = newConnectionPopupPanel.getConnectionInfo()	;
		
		System.out.println("???????????? :" +  connectionInfo.toString());
		//JOptionPane.showOptionDialog(null, new NewConnectionPopupPanel(), "", , messageType, icon, options, initialValue)(null, new NewConnectionPopupPanel());
/*		JOptionPane.showInputDialog(null, new NewConnectionPopupPanel(), "Connect",
	             JOptionPane.PLAIN_MESSAGE );*/
	}

}
