package org.zero.apps.hbase.manager.component;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.connector.ConnectionInfo;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.EmptyValueException;
import org.zero.apps.hbase.manager.support.NullChecker;

@Component
@Scope("prototype")
public class ConnectionDialog extends JDialog{

	private JTextField txtHadoopHost;
	private final JTextField txtZKHost = new JTextField();
	private JTextField txtZKPort;
	private JButton btnConfirm;
	private JButton btnCancel;
	private ConnectionInfo connectionInfo = null;
	ConnectionDialog thisDialog;
	
	@Autowired
	HBaseConnector connector;
	public ConnectionDialog() {
		setTitle("Connect");
		setType(Type.POPUP);
		setResizable(false);
		setModal(true);
		//setAlwaysOnTop(true);
		getContentPane().setLayout(new GridLayout(0, 2, 5, 3));
		
		JLabel lblNewLabel = new JLabel(" Hadoop Host");
		getContentPane().add(lblNewLabel);
		
		txtHadoopHost = new JTextField();
		lblNewLabel.setLabelFor(txtHadoopHost);
		getContentPane().add(txtHadoopHost);
		txtHadoopHost.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel(" Zookeeper Host");
		lblNewLabel_1.setLabelFor(txtZKHost);
		getContentPane().add(lblNewLabel_1);
		getContentPane().add(txtZKHost);
		txtZKHost.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel(" Zookeeper Client Port");
		getContentPane().add(lblNewLabel_2);
		
		txtZKPort = new JTextField();
		lblNewLabel_2.setLabelFor(txtZKPort);
		getContentPane().add(txtZKPort);
		txtZKPort.setColumns(10);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.TRAILING);
		getContentPane().add(panel);
		
		btnConfirm = new JButton("확인");
		DialogEventListener dialogEventListener = new DialogEventListener() ;
		btnConfirm.addActionListener(dialogEventListener);
		
		panel.add(btnConfirm);
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEADING);
		getContentPane().add(panel_1);
		
		btnCancel = new JButton("취소");
		btnCancel.setHorizontalAlignment(SwingConstants.LEADING);
		btnCancel.addActionListener(dialogEventListener);
		panel_1.add(btnCancel);
		setLocationRelativeTo(getParent());
		setSize(350, 140);
		thisDialog = this;
		
	}
	
	
	
	public ConnectionInfo showDialog() {
		int result = JOptionPane.showConfirmDialog(null, new ConnectionDialog(), "Connect",
	            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(result == JOptionPane.OK_OPTION) {
				try {
					NullChecker.CheckNullWithMessage("HADOOP HOST", txtHadoopHost);
					NullChecker.CheckNullWithMessage("ZOOKEEPER HOST", txtZKHost);
					NullChecker.CheckNullWithMessage("ZOOKEEPER CLIENT PORT", txtZKPort);
				} catch (EmptyValueException e) {
					return null;
				}
			return new ConnectionInfo(txtHadoopHost.getText(), txtZKHost.getText() ,txtZKPort.getText());
		}
		
		return null;
		
	}

	public class DialogEventListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(btnConfirm)) {
				
				try {
					NullChecker.CheckNullWithMessage("HadoopHost", txtHadoopHost);
					NullChecker.CheckNullWithMessage("ZookeeperHost", txtZKHost);
					NullChecker.CheckNullWithMessage("ZookeeperPort", txtZKPort);
					connectionInfo = new ConnectionInfo(txtHadoopHost.getText(), txtZKHost.getText(), txtZKPort.getText());
					try {
					connector.doConnect(connectionInfo );
					}catch(Exception connectEx) {
						
					}
					thisDialog.dispose();
					
				}catch(EmptyValueException ex) {
				}
				 
			}
			else if(e.getSource().equals(btnCancel)) {
				connectionInfo = null;
				thisDialog.dispose();
			}
		}
	}

	
	/**
	 * @return the connectionInfo
	 */
	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	/**
	 * @param connectionInfo the connectionInfo to set
	 */
	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
}
