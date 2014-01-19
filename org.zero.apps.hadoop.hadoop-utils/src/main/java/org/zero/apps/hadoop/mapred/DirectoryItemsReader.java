package org.zero.apps.hadoop.mapred;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;

public class DirectoryItemsReader implements RecordReader<Text, Path>{

	int idx = 0;
	private Path path;
	private Text currentKey;
	public DirectoryItemsReader(JobConf arg1, InputSplit inputSplit) {
		// File명에 대한 Path 정보를 획득해 놓아야함.
		System.out.println("RecordReader.InputSplit : " + inputSplit.getClass() + "  , "
				+ inputSplit);
		System.out.println("RecordReader.JobConf : " + arg1.getClass() + "  , " + arg1);
		FileSplit fs = (FileSplit) inputSplit;
		path = fs.getPath();
	}

	public boolean next(Text key, Path value) throws IOException {
		// 단일 파일 전달하는 개념이니 다음건은 없음.
		if(idx == 0) {
			idx++;
			return true;
		}
		return false;
	}

	public Text createKey() {
		System.out.println(this + "DirectoryItemsReader.CREATEKEY");
		currentKey = new Text(path.getName());
		return currentKey; 
	}

	public long getPos() throws IOException {
		//무조건 0
		return idx;
	}

	public void close() throws IOException {
		System.out.println(this + "DirectoryItemsReader.CLOSE");
	}

	public float getProgress() throws IOException {
		return 1;
	}

	public Path createValue() {
		System.out.println("DirectoryItemsReader.CREATEVALUE");
		return path;
	}


}
