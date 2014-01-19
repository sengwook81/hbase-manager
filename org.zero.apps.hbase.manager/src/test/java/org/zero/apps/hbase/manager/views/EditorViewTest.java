package org.zero.apps.hbase.manager.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDesktopPane;

import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.TestFrame;

import javax.swing.JPanel;

public class EditorViewTest extends 	TestFrame{
	public EditorViewTest() {
	}

	// Main entry point for this example
	public static void main( String args[] )
	{
		HBaseConnector connector = new HBaseConnector();
		connector.doConnect("name1", "name1", "2181");
		EditorViewTest editorViewTest = new EditorViewTest();
		final EditorView ev = new EditorView();
		ev.setConnector(connector);
		JDesktopPane jDesktopPane = new JDesktopPane();
		jDesktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		jDesktopPane.setLayout(new BorderLayout(100, 0));
		
		editorViewTest.getRootPanel().add(jDesktopPane,BorderLayout.CENTER );
		jDesktopPane.add(ev);
		ev.show();
		JButton jButton = new JButton("Test");
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//ev.scanData("TB_ARTICLE");
			}
		});
		editorViewTest.add(jButton,BorderLayout.SOUTH);
		editorViewTest.executeFrame();
	}
}