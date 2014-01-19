package org.zero.apps.hbase.manager.support.event;

public interface ZVentRunner<T extends ZVent> {
	public void run(T event);
}
