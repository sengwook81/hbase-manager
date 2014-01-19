package org.zero.apps.hbase.manager.views.frames;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JTabbedPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zero.apps.hbase.manager.connector.ConnectionInfo;
import org.zero.apps.hbase.manager.connector.HBaseConnector;
import org.zero.apps.hbase.manager.support.ConnectAware;
import org.zero.apps.hbase.manager.views.tab.ZTabView;

/**
 * Main Content Frame에서 Left 스키마 정보를 갖는 Tab
 * @author Administrator
 *
 */
@Component("infoTabFrame")
@Scope("prototype")
public class InfoTabFrame extends JTabbedPane implements ApplicationContextAware , ConnectAware{
	
	protected static final Logger log = LoggerFactory.getLogger(InfoTabFrame.class);
	
	private static final long serialVersionUID = 5532673094789562029L;
	
	private ApplicationContext applicationContext;

	private HBaseConnector connector;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@PostConstruct
	private void init() {
		String[] beanNamesForType = applicationContext.getBeanNamesForType(ZTabView.class);
		
		List<String> asList = Arrays.asList(beanNamesForType);
		Collections.sort(asList,new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		for(String beanName : beanNamesForType) {
			ZTabView tabView = applicationContext.getBean(beanName,ZTabView.class);
			addTab(tabView.getTabName(), (java.awt.Component) tabView);
		}
		
	}

	@Override
	public void setConnector(HBaseConnector connector) {
		log.trace("Set Connector : {} " , connector);
		this.connector = connector;
		for(int idx = 0;idx<getTabCount();idx++) {
			java.awt.Component tabComponentAt = getComponentAt(idx);
			if(tabComponentAt instanceof ConnectAware) {
				ConnectAware connectAware = (ConnectAware) tabComponentAt;
				connectAware.setConnector(connector);
			}
		}
	}

}
