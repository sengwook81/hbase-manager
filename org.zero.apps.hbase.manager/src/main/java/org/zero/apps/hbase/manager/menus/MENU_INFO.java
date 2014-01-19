package org.zero.apps.hbase.manager.menus;

import java.awt.event.ActionEvent;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MENU_INFO {
	public String groupName() default "";
	public String menuName();
	public int order() default 0;
	/**
	 * java.awt.event.KeyEvent;
	 * @return
	 */
	public int keyEvent() default -1;
	public int keyMask() default ActionEvent.ALT_MASK;
}
