package org.zero.apps.hbase.manager.support;

import org.springframework.stereotype.Component;

@Component
public interface Closer {
	public void close();
}
