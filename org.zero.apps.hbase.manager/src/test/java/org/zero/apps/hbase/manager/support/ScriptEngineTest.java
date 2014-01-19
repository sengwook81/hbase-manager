package org.zero.apps.hbase.manager.support;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptEngineTest {

	public static class SciprtResult {
		private String val ;

		/**
		 * @return the val
		 */
		public String getVal() {
			return val;
		}

		/**
		 * @param val the val to set
		 */
		public void setVal(String val) {
			this.val = val;
		}
	}
	
	
	public static void main(String[] args) {
		
		
		SciprtResult rslt = new SciprtResult();
		ScriptEngineManager manager=  new ScriptEngineManager();
		ScriptEngine engineByExtension = manager.getEngineByExtension("js");
		engineByExtension.getContext().setAttribute("rslt", rslt,ScriptContext.ENGINE_SCOPE);
		try {
			engineByExtension.eval("function put(val) { rslt.setVal(JSON.stringify({type:'put',name:val}));}");
			engineByExtension.eval("put('sengwook');");
			System.out.println(rslt.getVal());
			
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
