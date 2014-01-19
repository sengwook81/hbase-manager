package org.zero.apps.hbase.manager.menus;

import java.awt.event.ActionListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class BaseMenuItem implements ActionListener , ApplicationContextAware{

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
				this.applicationContext = applicationContext;
	}

	/**
	 * @return the applicationContext
	 */
	protected ApplicationContext getContext() {
		return applicationContext;
	}

}
