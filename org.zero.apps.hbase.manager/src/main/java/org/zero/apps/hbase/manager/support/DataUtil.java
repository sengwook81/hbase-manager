package org.zero.apps.hbase.manager.support;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUtil {
	
	protected static final Logger log = LoggerFactory.getLogger(DataUtil.class);
	public static byte[] convertObjectToBytes(Object obj) {
		if(obj instanceof String) { 
			return ((String) obj).getBytes();
		}
		log.trace("Convert {}[{}] to Byte[]",obj,obj.getClass());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Value Convert Byte[] Fail");
		}
		return out.toByteArray();
	}
}
