package org.zero.apps.hbase.manager.component;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JTextField;

public class MessagePane extends JPanel{
	private static final long serialVersionUID = 804567399207202585L;
	private JTextArea txtMessage;
	private JTextField textField;

	public MessagePane() {
		setLayout(new GridLayout(0, 1, 0, 0));
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);
		txtMessage = new JTextArea();
		txtMessage.setColumns(3);
		txtMessage.setEditable(false);
		//textPane.setColumns(80);
		txtMessage.setLineWrap(true);
		txtMessage.setWrapStyleWord(true);
		txtMessage.setSize(500, 50);
		add(txtMessage);
	}
	
	public void setText(String text) { 
		textField.setText(text.trim());
	}
	public void setDetail(String text) { 
		txtMessage.setText(text.trim());
	}
	

	
}
