package org.zero.apps.hbase.manager.script;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class HbaseCommandScriptManager extends ScriptEngineManager implements ApplicationContextAware {

	protected static final Logger log = LoggerFactory.getLogger(HbaseCommandScriptManager.class);
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {

	}

	public ScriptExcutor getJavaScriptEngine() {
		ScriptEngine engine = getEngineByName("JavaScript");

		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		// ScriptExcutor PrototypeBean.
		ScriptExcutor scriptExcutor = applicationContext.getBean(ScriptExcutor.class);
		String[] beanNamesForType = applicationContext.getBeanNamesForType(ScriptOperator.class);
		for (String scriptOp : beanNamesForType) {
			log.trace("ScriptOperator Bean [{}]",scriptOp);
			ScriptOperator so = applicationContext.getBean(scriptOp, ScriptOperator.class);
			String jsResourcePath = so.getResourcePath();
			Resource resource = null;
			if (jsResourcePath != null && jsResourcePath.length() > 0) {
				resource = pathMatchingResourcePatternResolver.getResource(jsResourcePath);
				if (resource == null) {
					log.error("ScriptOperator Resource Not Found : {}[{}]", scriptOp, jsResourcePath);
				}
			}
			else {
				engine.put(so.getVarName(), so);	
				scriptExcutor.addOperator(so);
			}
			
			if (resource != null) {
				// engine.put("hbase", bean);
				InputStreamReader isr = null;
				try {
					isr = new InputStreamReader(resource.getInputStream());
					engine.eval(isr);
				} catch (ScriptException e) {
					e.printStackTrace();
					throw new RuntimeException("JavaScript Resource Script EngineLoad Fail : " + jsResourcePath, e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("JavaScript Resource Stream Load Fail : " + jsResourcePath, e);
				}
				log.trace("Add Script Module {}" , so.getVarName());
				IOUtils.closeQuietly(isr);
				engine.put(so.getVarName(), so);	
				scriptExcutor.addOperator(so);
			}

		}

		log.trace("GetJavaScriptEngine : {} ", engine.getBindings(ScriptContext.ENGINE_SCOPE));
		scriptExcutor.setEngine(engine);

		return scriptExcutor;
	}

	/*
	 * public static void main(String[] args) { HbaseCommandScriptManager
	 * hbaseCommandScriptManager = new HbaseCommandScriptManager();
	 * DefaultHBaseScriptOperator javaScriptEngine =
	 * hbaseCommandScriptManager.getJavaScriptEngine(); HBaseConnector connector
	 * = new HBaseConnector(); connector.setHbaseHosts("name1");
	 * connector.setZkHosts("name1"); connector.setZkClientPort("2181");
	 * javaScriptEngine.setConnector(connector); try {
	 * //javaScriptEngine.getScriptEngine().put("hbase", javaScriptEngine);
	 * javaScriptEngine.getScriptEngine().eval(
	 * "scan('TEST',{filter:[FILTER.Page(10),FILTER.SingleColumnValue('cf','val','EQUAL',COMPARE.SubString('test'))]});"
	 * ); } catch (ScriptException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}
}
