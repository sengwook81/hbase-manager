package org.zero.apps.hbase.manager.views.tab;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.zero.apps.hbase.manager.views.tab.TableInfos;

public class TableInfosTest  extends 	JFrame{

	public TableInfosTest()
	{
		// Set the frame characteristics
		setTitle( "Simple Tree Application" );
		setSize( 1024, 768 );
		setBackground( Color.gray );

		// Create a panel to hold all other components
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new BorderLayout() );
		getContentPane().add( topPanel );

		
		topPanel.add( new TableInfos(), BorderLayout.CENTER );
	}

	// Main entry point for this example
	public static void main( String args[] )
	{
		// Create an instance of the test application
		TableInfosTest mainFrame	= new TableInfosTest();
		mainFrame.setVisible( true );
	}
}
