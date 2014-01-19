package org.zero.apps.utils.network.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshClient extends SecureShell {

	   public String execCommand(String command) throws JSchException, IOException {
	    	return execCommand(new String[] { command}).get(0);
	    }
	    public List<String> execCommand(String [] commands) throws JSchException, IOException { 
	    	List<String> retData = new ArrayList<String>();
	    	Session session = getSession();
	    	for(String command : commands) {
	    		
		    	ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
		    	channelExec.setPty(true);
		    	
		    	if(log.isDebugEnabled())  { log.debug("{} Excute Command : {} " , getHost() , command); }
		    	
		    	channelExec.setCommand(command);
		    	
		    	InputStream err = channelExec.getErrStream();
		    	channelExec.connect(3000);
		    	
		    	StringBuffer sb = new StringBuffer();
		    	BufferedReader br = new BufferedReader(new InputStreamReader(channelExec.getInputStream())); 
		    	String line = null;
		    	while((line = br.readLine()) != null) { 
		    		sb.append(line +"\n");
		    	}
		    	retData.add(sb.toString());
		    	
		    	if(log.isDebugEnabled())  { log.debug("{} \nExcute Command : {}\n result : {}\n err :{}" , new Object[] {getHost() , command , sb.toString(),err}); }
		    	
		    	channelExec.disconnect();
		    	log.debug("Ssh DisConnect Success {}",getHost());
	    	}
	    	session.disconnect();
	    	
	    	return retData;
	    	
	    }
	    
	    
	    public static void main(String[] args) {
			SshClient sc = new SshClient();
			
			sc.setHost("10.10.75.131");
			sc.setUsername("user");
			sc.setPassword("hadoop");
			
			try {
				String execCommand = sc.execCommand("whoami");
				System.out.println(execCommand);
				execCommand = sc.execCommand("cat /etc/hosts");
				System.out.println(execCommand);
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			StringBuffer sb = new StringBuffer();
			System.out.println("[" + sb.toString() +"]");
		}
	    
}
