package org.zero.apps.hbase.manager.support;

import javax.swing.JOptionPane;

import org.zero.apps.hbase.manager.component.MessagePane;

public class SimpleMessageException extends RuntimeException {

	public SimpleMessageException(String message) { 
		super(message);
		showMessageBox(message,"");
	}
	
	public SimpleMessageException(String message,Throwable ex) { 
		super(message,ex);
		showMessageBox(message,ex.getMessage());
	}
	
	private void showMessageBox(String text , String detail) {
		
		MessagePane msg = new MessagePane();
		msg.setText(text);
		msg.setDetail(detail);;
		JOptionPane.showMessageDialog(null, msg);
	}
}
