package org.zero.apps.hbase.manager.component;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.BorderLayout;

public class BoardPane extends JPanel{
	private static final long serialVersionUID = 804567399207202585L;
	private JTextArea textPane;

	public BoardPane() {
		setLayout(new BorderLayout(0, 0));
		textPane = new JTextArea();
		textPane.setEditable(false);
		//textPane.setColumns(80);
		textPane.setLineWrap(true);
		textPane.setWrapStyleWord(true);
		textPane.setSize(500, 50);
		add(textPane);
	}
	
	public void setText(String text) { 
		textPane.setText(text.trim());
	}
	
	

	
}
