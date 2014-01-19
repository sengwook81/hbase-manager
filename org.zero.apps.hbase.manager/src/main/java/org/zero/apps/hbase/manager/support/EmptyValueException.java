package org.zero.apps.hbase.manager.support;

import java.awt.Component;

public class EmptyValueException extends Exception
{
	
	Component component;

	public EmptyValueException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public EmptyValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public EmptyValueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public EmptyValueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3426600966453412274L;

}
