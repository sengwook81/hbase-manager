package org.zero.apps.hbase.manager.support;

import java.awt.Window;

import javax.swing.JFrame;

public class FrameUtil {

	private static Window rootFrame = null;
	
	public static Window getRootFrame() { 
		if(rootFrame == null) { 
			Window[] frames = JFrame.getWindows();
			for(Window frame : frames ) {
				if(frame.getParent() == null) {
					rootFrame = frame;
				}
			}
		}
		return rootFrame;
	}
}
