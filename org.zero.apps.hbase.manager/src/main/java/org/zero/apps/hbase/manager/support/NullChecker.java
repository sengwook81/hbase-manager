package org.zero.apps.hbase.manager.support;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NullChecker {
	
	public static void CheckNull(String Label, String value) throws EmptyValueException {
		if(value == null || value.trim().length() == 0) {
			throw new EmptyValueException(Label + " Value Is Null");
		}
	}
	
	public static void CheckNullWithMessage(String Label, String value) throws EmptyValueException {
		if(value == null || value.trim().length() == 0) {
			JOptionPane.showMessageDialog(null, Label + " Value Is Null");
			throw new EmptyValueException(Label + " Value Is Null");
		}
	}
	
	public static void CheckNullWithMessage(String Label, JTextField value) throws EmptyValueException {
		try {
			CheckNullWithMessage(Label,value.getText());	
		}
		catch(EmptyValueException e) {
			value.requestFocus();
			throw e;
		}
		
	}

}
