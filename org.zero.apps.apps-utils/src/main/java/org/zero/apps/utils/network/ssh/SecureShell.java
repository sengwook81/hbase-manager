package org.zero.apps.utils.network.ssh;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public abstract class SecureShell {
	
	public String getHost() {
		return host;
	}
	public SecureShell setHost(String host) {
		this.host = host;
		return this;
	}
	public int getPort() {
		return port;
	}
	public SecureShell setPort(int port) {
		this.port = port;
		return this;
	}
	public String getUsername() {
		return username;
	}
	public SecureShell setUsername(String username) {
		this.username = username;
		return this;
	}
	public String getPassword() {
		return password;
	}
	public SecureShell setPassword(String password) {
		this.password = password;
		return this;
	}


	protected static final Logger log = LoggerFactory.getLogger(SecureShell.class);
	
	private String host = null;
	private int port = 22;
	private String username = null;
	private String password = null;
	
	public SecureShell() {
	}

	
    protected Session getSession() throws JSchException {
    	
    	if(host == null || username == null) { 
    		throw new RuntimeException("Host or UserName is Empty");
    	}
		JSch jsch = new JSch();
    	  Session session = jsch.getSession(username, host, port);
          // 3. 패스워드를 설정한다.
          session.setPassword(password);
          
          java.util.Properties config = new java.util.Properties();
          // 4-1. 호스트 정보를 검사하지 않는다.
          config.put("StrictHostKeyChecking", "no"); 
          session.setConfig(config);
          
          log.debug("Ssh Try To Connect {}",host);
          // 5. 접속한다.       
          session.connect();
          log.debug("Ssh Connect Success {}",host);
          
          return session;
    }
}
