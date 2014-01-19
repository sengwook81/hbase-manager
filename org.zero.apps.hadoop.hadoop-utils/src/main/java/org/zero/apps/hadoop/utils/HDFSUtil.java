package org.zero.apps.hadoop.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.WritableComparable;
import org.zero.apps.utils.DoWith;

public class HDFSUtil {
	
	private static FileSystem fs = null;
	private static Map<String, String> hdfsConfig;

	public static final String USER_NAME_PARAM = "HADOOP_USER_NAME";
	public static void initHDFSManager(String hdfsMaster,Map<String,String> property) {
		Configuration conf = new Configuration();
		if(property.containsKey("HADOOP_USER_NAME")) {
			System.setProperty("HADOOP_USER_NAME",property.get("HADOOP_USER_NAME").toString());
		}
		conf.clear();
		for(Entry<String, String> entry : property.entrySet()) { 
			conf.set(entry.getKey(), entry.getValue());	
		}
		try {
			fs  = FileSystem.get(new URI(hdfsMaster), conf);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("HDFS FileSystem Initialize Exception",e);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException("HDFS FileSystem Initialize Exception",e);
		}
		
	}
	
	public static void clear() {
		try {
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fs = null;
		}
	}
	
	public static final DoWith<Path> EMPTY_FILTER = new DoWith<Path>() { public Path doWith(Path table) {return null;};} ;
	
	public static List<FileStatus> getFileList(Path path , DoWith<Path> fileFilter) {
		return getFileList(fs,path,fileFilter);
	}

	private static void checkStaticFileSystem(FileSystem fileSystem) {
		if(fileSystem == null) { 
			throw new RuntimeException("File System is not initialized");
		}
	}
	
	public static List<FileStatus> getFileList(FileSystem fileSystem,Path path , DoWith<Path> fileFilter) {
		checkStaticFileSystem(fileSystem);
		List<FileStatus> retData = new ArrayList<FileStatus>();
		
		if(fileFilter == null ) { fileFilter = EMPTY_FILTER;}
		
		try {
			FileStatus fileStatus = fileSystem.getFileStatus(path);
			if(fileStatus.isDir()) {
				FileStatus[] listStatus = fileSystem.listStatus(path);
				for(FileStatus file : listStatus) {
					if(file.isDir()) {
						List<FileStatus> fileList = getFileList(file.getPath(),fileFilter);
						retData.addAll(fileList);
					}
					else {
						if(fileFilter.doWith(path) != null) {
							retData.add(file);
						}
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("File List Scan Exception", e);
		}
		
		return retData;
	}
		
	public static void writeSeqFile(FileSystem fileSystem , String fileName , Class<? extends WritableComparable> keyClass , Class<? extends WritableComparable> valClass, DoWith<SequenceFile.Writer> doWrite) {
		
		checkStaticFileSystem(fileSystem);
		SequenceFile.Writer writer;
		try {
			
			writer = new SequenceFile.Writer(fileSystem,fileSystem.getConf(),  new Path(fileName), keyClass, valClass);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Create SeqWriter Exception [" + fileName + "]" , e);
		}
		doWrite.doWith(writer);
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void writeSeqFile(String fileName , Class<? extends WritableComparable> keyClass , Class<? extends WritableComparable> valClass, DoWith<SequenceFile.Writer> doWrite) {
		writeSeqFile(fs,fileName,keyClass,valClass,doWrite);
		
	}
}
