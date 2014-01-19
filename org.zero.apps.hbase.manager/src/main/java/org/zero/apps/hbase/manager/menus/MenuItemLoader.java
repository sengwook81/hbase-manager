package org.zero.apps.hbase.manager.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.support.Closer;

/**
 * Menu 정보를 갖는 Bean객체를 이용하여 메뉴 구성.
 * 
 * @author Administrator
 * 
 */
@Component
public class MenuItemLoader implements ApplicationContextAware, ActionListener {

	public static final String QUIT_MENU = "QUIT";

	protected static final Logger log = LoggerFactory.getLogger(MenuItemLoader.class);

	private ApplicationContext applicationContext;

	public static class MenuItem {
		MENU_INFO menuInfo;
		String beanName;

		public MenuItem(MENU_INFO menuInfo, String beanName) {
			super();
			this.menuInfo = menuInfo;
			this.beanName = beanName;
		}

		/**
		 * @return the menuInfo
		 */
		public MENU_INFO getMenuInfo() {
			return menuInfo;
		}

		/**
		 * @param menuInfo
		 *            the menuInfo to set
		 */
		public void setMenuInfo(MENU_INFO menuInfo) {
			this.menuInfo = menuInfo;
		}

		/**
		 * @return the beanName
		 */
		public String getBeanName() {
			return beanName;
		}

		/**
		 * @param beanName
		 *            the beanName to set
		 */
		public void setBeanName(String beanName) {
			this.beanName = beanName;
		}

	}

	public List<JMenu> loadMenuItems() {
		List<String> menuGroupOrder = (List<String>) applicationContext.getBean("menuGroup");
		LinkedHashMap<String, List<MenuItem>> topMenuMap = new LinkedHashMap<String, List<MenuItem>>();
		LinkedHashMap<String, JMenu> retData = new LinkedHashMap<String, JMenu>();
		for (String group : menuGroupOrder) {
			topMenuMap.put(group, new ArrayList<MenuItem>());
		}
		Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(MENU_INFO.class);
		log.debug("Bean With Annotation Menu : {} [{}]", beansWithAnnotation.size(), beansWithAnnotation);
		for (Entry<String, Object> en : beansWithAnnotation.entrySet()) {
			MENU_INFO annotation = en.getValue().getClass().getAnnotation(MENU_INFO.class);
			log.debug("{} Menu Add Try [{}]", new Object[] { en.getValue(), en.getKey() });

			// annotation.
			if (!topMenuMap.containsKey(annotation.groupName())) {
				log.error("UnRegistered Menu Group : {} [{}]", annotation.groupName(), en.getKey());
			} else {
				topMenuMap.get(annotation.groupName()).add(new MenuItem(annotation, en.getKey()));
			}
			log.debug("{} Menu Add Finish", en.getValue());
		}

		for (Entry<String, List<MenuItem>> entry : topMenuMap.entrySet()) {
			retData.put(entry.getKey(), new JMenu(entry.getKey()));
			Collections.sort(entry.getValue(), new Comparator<MenuItem>() {
				@Override
				public int compare(MenuItem o1, MenuItem o2) {
					int rslt = o2.menuInfo.order() - o1.menuInfo.order();
					if (rslt != 0) {
						return rslt;
					}
					return o2.menuInfo.menuName().compareTo(o1.menuInfo.menuName());
				}
			});

			for (MenuItem item : entry.getValue()) {
				JMenuItem menuItem = new JMenuItem(item.getMenuInfo().menuName());
				if (item.getMenuInfo().keyEvent() > 0) {
					menuItem.setMnemonic(item.getMenuInfo().keyEvent());
				}
				menuItem.setAccelerator(KeyStroke.getKeyStroke(item.getMenuInfo().keyEvent(), item.getMenuInfo().keyMask()));
				menuItem.setActionCommand(item.getMenuInfo().menuName());
				retData.get(entry.getKey()).add(menuItem);
				if (!applicationContext.isSingleton(item.getBeanName())) {
					throw new RuntimeException("Event Listener Must Be Singleton Bean");
				}

				ActionListener bean = applicationContext.getBean(item.getBeanName(), ActionListener.class);
				menuItem.addActionListener(bean);

			}
		}

		{
			JMenuItem menuItem = new JMenuItem(QUIT_MENU);
			menuItem.setMnemonic(KeyEvent.VK_Q);
			menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
			menuItem.setActionCommand(QUIT_MENU);
			menuItem.addActionListener(this);
			// menu.add(menuItem);
			retData.get("Main").add(menuItem);
		}

		return new ArrayList<JMenu>(retData.values());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (QUIT_MENU.equals(e.getActionCommand())) {
			close();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void close() {
		Map<String, Closer> beansOfType = applicationContext.getBeansOfType(Closer.class, true, true);
		for (Entry<String, Closer> en : beansOfType.entrySet()) {
			log.debug("{} Close Try", en.getValue());
			en.getValue().close();
			log.debug("{} Close Finish", en.getValue());
		}
		System.exit(0);
	}
}
