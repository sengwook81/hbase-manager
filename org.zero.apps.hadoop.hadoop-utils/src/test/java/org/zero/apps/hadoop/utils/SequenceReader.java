package org.zero.apps.hadoop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;
import org.apache.hadoop.io.Text;

public class SequenceReader {

	public static void main(String[] args) {
		Configuration conf = new Configuration();
		String root = "E:/home/user/mavenCrawler/tempFiles";
		try {
			System.setProperty("HADOOP_USER_NAME", "user");
			FileSystem fs = FileSystem.get(new URI("hdfs://name1:9000"), conf);
			
			System.out.println("CONNECTED");
			byte buffer[] = new byte[1024 * 1024];
			//List<FileStatus> fileList = HDFSUtil.getFileList(fs, new Path("/data/srcrepo/xstream/xstream"), null);
			
			List<File> fileList = getFiles(new File(root));
			
			//System.out.println("SCAN FINISH");
//			SequenceFile sqf = new SequenceFile.Reader(fs, file, conf)
			Path seqPath = new Path("/opensource.sequence");
			//fs.delete(seqPath, false);
			
			 Reader reader = new SequenceFile.Reader(fs,seqPath,conf);
			 System.out.println("Reader");
			 TreeMap<Text, Text> metadata = reader.getMetadata().getMetadata();
			 System.out.println("getMetaData");
			 System.out.println(reader.toString());
			 for(Entry<Text, Text> entrySet : metadata.entrySet()) {
				 System.out.println(entrySet.getKey());
			 }
			 
			System.out.println("FINISH");

			reader.close();
			fs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public static List<File> getFiles(File f) { 
		List<File> retData =new ArrayList<File>();
		if(f.isDirectory()) {
			File[] listFiles = f.listFiles();
			for(File inf : listFiles) {
				List<File> files = getFiles(inf);
				retData.addAll(files);
			}
		}
		else { 
			if(f.getName().endsWith(".jar")) {
				retData.add(f);
			}
		}
		
		return retData;
	}
}
