package org.zero.apps.hadoop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;

public class MapWriter {

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
			
			System.out.println("SCAN FINISH");
//			SequenceFile sqf = new SequenceFile.Reader(fs, file, conf)
			
			Path seqPath = new Path("/opensource.map");
			System.out.println("seqPath.getName() : " + seqPath.getName());
			fs.delete(seqPath, true);
			MapFile.Writer writer = new MapFile.Writer(conf, fs ,"/opensource.map",Text.class,BytesWritable.class);
			MapFile.Reader reader = new MapFile.Reader(fs, "/opensource.map",conf);
			for (File item : fileList) {
				java.io.ByteArrayOutputStream bout = new ByteArrayOutputStream();
				//FSDataInputStream open = fs.open(item.getPath());
				FileInputStream open = new FileInputStream(item);
				
				//System.out.println(item.getPath().toString());
				//System.out.println(item.getPath());
				
				while (open.read(buffer, 0, buffer.length) >= 0) {
					bout.write(buffer);
				}
				
				String key = item.getPath().substring(root.length() + 1).replace('\\','/');
				System.out.println("key : " + key);
				
				//writer.(new Text(key),new BytesWritable(bout.toByteArray()));

				open.close();
				bout.close();
			}
			System.out.println("FINISH");
			writer.close();
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
