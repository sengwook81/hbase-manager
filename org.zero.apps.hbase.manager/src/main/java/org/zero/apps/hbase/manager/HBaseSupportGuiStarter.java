package org.zero.apps.hbase.manager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zero.apps.hbase.manager.menus.MenuItemLoader;

public class HBaseSupportGuiStarter extends JFrame {

	
	protected static final Logger log = LoggerFactory.getLogger(HBaseSupportGuiStarter.class);
	
	private static final long serialVersionUID = -3735375010508353693L;

	MenuItemLoader menuLoader;

	private ClassPathXmlApplicationContext ctx;
	
	protected JPanel getTopMenus() {
		menuLoader = ctx.getBean(MenuItemLoader.class);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
	
		log.debug("Get Top Menus ");
		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar);
		menuBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		List<JMenu> loadMenuItems = menuLoader.loadMenuItems();
		for(JMenu menuItem : loadMenuItems) {
			menuBar.add(menuItem);
		}
		
		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.add(new JButton("New"));
		toolBar.add(new JButton("Run"));
		panel.add(toolBar, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public HBaseSupportGuiStarter() {
		LoadSpringResources();
		// Make the big window be indented 50 pixels from each edge
		// of the screen.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height
				- inset * 2);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(getTopMenus(), BorderLayout.NORTH);
		getContentPane().add(getConnectionFrame() , BorderLayout.CENTER);
		
		
    }
	
	private JTabbedPane getConnectionFrame() { 
		JTabbedPane bean = ctx.getBean("connectionFrame" ,JTabbedPane.class);
		return bean;
	}

	

	
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		
		try {
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
		}
		
		// Create and set up the window.
		HBaseSupportGuiStarter frame = new HBaseSupportGuiStarter();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	private void LoadSpringResources() {
		ctx = new ClassPathXmlApplicationContext(
				"classpath:spring-config/*-context.xml");
		for (String name : ctx.getBeanDefinitionNames()) {
			log.debug("LOADED BEAN {}[{}]", name , ctx.getBean(name).getClass());
		}
	}
	
}