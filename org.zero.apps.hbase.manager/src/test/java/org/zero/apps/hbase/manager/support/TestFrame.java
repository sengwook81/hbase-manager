package org.zero.apps.hbase.manager.support;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class TestFrame extends JFrame{

	JPanel rootPanel = null;
	protected JPanel getRootPanel() 
	{ 
		if(rootPanel == null) {
			rootPanel = new JPanel();
			rootPanel.setLayout(new BorderLayout() );
		}
		return rootPanel;
	}
	
	private void createAndShowGUI() {
		// Make sure we have nice window decorations.
	/*	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		JPanel rootPanel2 = getRootPanel();
		setSize( 1024, 768 );
		setBackground( Color.gray );
		getContentPane().add(rootPanel2, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Display the window.
		this.setVisible(true);
	}
	
	protected void executeFrame() { 
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
