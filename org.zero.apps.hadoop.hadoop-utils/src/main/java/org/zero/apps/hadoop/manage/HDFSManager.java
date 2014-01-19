package org.zero.apps.hadoop.manage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.WritableComparable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zero.apps.utils.DoWith;

public class HDFSManager {
	public static final DoWith<Path> EMPTY_FILTER = new DoWith<Path>() { public Path doWith(Path table) {return null;};} ;
	
	protected static final Logger log = LoggerFactory
			.getLogger(HDFSManager.class);

	private String host = null;
	private String user = null;
	private FileSystem fs = null;

	public HDFSManager(String host, String user) {
		this.host = host;
		this.user = user;
		//System.setProperty("HADOOP_USER_NAME", user);

	}

	public void doConnect() {
		Configuration conf = new Configuration();
		conf.set("HADOOP_USER_NAME", user);
		try {
			System.out.println(host);
			fs = FileSystem.get(new URI(host), conf);
			Iterator<Entry<String, String>> iterator = fs.getConf().iterator();
			while(iterator.hasNext()) {
				Entry<String,String> val = iterator.next();
				System.out.println(val.getKey() + " , " + val.getValue());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public void cleanUp() {
		try {
			fs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getFileLists(String path) {
		Path filePath = new Path(path);

		try {
			FileStatus[] listStatus = fs.listStatus(filePath);
			for (FileStatus fs : listStatus) {
				System.out.println(fs.getPath().getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<FileStatus> getFileList(Path path , DoWith<Path> fileFilter) {
		List<FileStatus> retData = new ArrayList<FileStatus>();
		
		if(fileFilter == null ) { fileFilter = EMPTY_FILTER;}
		
		try {
			FileStatus fileStatus = fs.getFileStatus(path);
			if(fileStatus.isDir()) {
				FileStatus[] listStatus = fs.listStatus(path);
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
	
	public void writeFileToHdfs(File f, String hadoopPath, String baseDir) {
		ArrayList<File> listFiles = new ArrayList<File>();
		listFiles.add(f);
		writeFilesToHdfs(listFiles, hadoopPath, baseDir);
	}

	public void writeFilesToHdfs(List<File> f, String hadoopPath, String baseDir) {
		Path filenamePath = new Path(hadoopPath);
		try {

			for (File item : f) {
				FileInputStream in = new FileInputStream(item);
				log.info("CREATE HDFS FILE : " + hadoopPath + File.separator
						+ item.getName());
				Path dirPath = new Path(hadoopPath
						+ File.separator
						+ item.getParentFile().getPath()
								.substring(baseDir.length()));
				if (!fs.exists(dirPath)) {
					fs.mkdirs(dirPath);
				}
				Path path = new Path(hadoopPath + File.separator
						+ item.getPath().substring(baseDir.length()));
				if (fs.exists(path)) {
					fs.delete(path, true);
				}

				FSDataOutputStream out = fs.create(path);

				org.apache.commons.io.IOUtils.copy(in, out);
				org.apache.commons.io.IOUtils.closeQuietly(in);
				org.apache.commons.io.IOUtils.closeQuietly(out);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	

	public void readSeqFile(String fileName , Class<? extends WritableComparable> keyClass , Class<? extends WritableComparable> valClass, DoWith<SequenceFile.Reader> doReader) {
	
		SequenceFile.Reader reader;
		try {
			reader = new SequenceFile.Reader(fs, new Path(fileName), fs.getConf());
			doReader.doWith(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Create SeqReader Exception [" + fileName + "]" , e);
		}
	}
	public void writeSeqFile( String fileName , Class<? extends WritableComparable> keyClass , Class<? extends WritableComparable> valClass, DoWith<SequenceFile.Writer> doWrite) {
		SequenceFile.Writer writer;
		try {
			
			writer = new SequenceFile.Writer(fs,fs.getConf(),  new Path(fileName), keyClass, valClass);
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
}