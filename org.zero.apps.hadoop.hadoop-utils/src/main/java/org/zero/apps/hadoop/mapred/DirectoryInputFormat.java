package org.zero.apps.hadoop.mapred;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.InvalidInputException;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.security.TokenCache;
import org.apache.hadoop.net.NetworkTopology;
import org.apache.hadoop.util.StringUtils;
import org.zero.apps.hadoop.utils.HDFSUtil;
import org.zero.apps.utils.DoWith;

public class DirectoryInputFormat extends FileInputFormat<Text, Path> {

	public static final Log LOG = LogFactory.getLog(DirectoryInputFormat.class);
	//private static final double SPLIT_SLOP = 1.1; // 10% slop
	static final String NUM_INPUT_FILES = "mapreduce.input.num.files";
	
	//private long minSplitSize = 1;
	private static DoWith<Path> fileFilter;

	@Override
	public RecordReader<Text, Path> getRecordReader(InputSplit inputSplit,
			JobConf arg1, Reporter arg2) throws IOException {

		LOG.error("RecordReader.InputSplit : " + inputSplit.getClass() + "  , "
				+ inputSplit);
		LOG.error("RecordReader.JobConf : " + arg1.getClass() + "  , " + arg1);
		LOG.error("RecordReader.Reporter: " + arg2.getClass() + "  , " + arg2);

		return new DirectoryItemsReader(arg1, inputSplit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.mapred.FileInputFormat#listStatus(org.apache.hadoop
	 * .mapred.JobConf)
	 */
	@Override
	protected FileStatus[] listStatus(JobConf job) throws IOException {

		if(fileFilter == null) { 
			fileFilter = HDFSUtil.EMPTY_FILTER;
		}
		Path[] dirs = getInputPaths(job);
		LOG.error("Config Input Path : " + dirs.length);
		if (dirs.length == 0) {
			throw new IOException("No input paths specified in job"
					+ " Path Count : " + dirs.length);
		}

		// get tokens for all the required FileSystems..
		TokenCache.obtainTokensForNamenodes(job.getCredentials(), dirs, job);

		List<FileStatus> result = new ArrayList<FileStatus>();
		List<IOException> errors = new ArrayList<IOException>();

		final DoWith<Path> inputFilter = // new MultiPathFilter(filters);
		new DoWith<Path>() {
			public Path doWith(Path path) {
				LOG.info("Filter path : " + path.getName());
				return path;
			}
		};

		for (Path p : dirs) {
			FileSystem fs = p.getFileSystem(job);
			FileStatus[] matches = fs.globStatus(p, new PathFilter() {
				
				public boolean accept(Path path) {
					if(inputFilter.doWith(path) == null){
						return false;	
					}
					
					return true;
				}
			});

			if (matches == null) {
				errors.add(new IOException("Input path does not exist: " + p));
			} else if (matches.length == 0) {
				errors.add(new IOException("Input Pattern " + p
						+ " matches 0 files"));
			} else {
				for (FileStatus globStat : matches) {
					if (globStat.isDir()) {
						List<FileStatus> fileList = HDFSUtil.getFileList(globStat.getPath(), inputFilter);
						result.addAll(fileList);
					} else {
						LOG.info("add FileStatus : "
								+ globStat.getPath().getName());
						if(inputFilter.doWith(globStat.getPath()) != null) {
							result.add(globStat);	
						}
						
					}
				}
			}
		}

		if (!errors.isEmpty()) {
			throw new InvalidInputException(errors);
		}
		LOG.info("Total input paths to process : " + result.size());
		return result.toArray(new FileStatus[result.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.mapred.FileInputFormat#isSplitable(org.apache.hadoop
	 * .fs.FileSystem, org.apache.hadoop.fs.Path)
	 */
	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.hadoop.mapred.FileInputFormat#getSplits(org.apache.hadoop.
	 * mapred.JobConf, int)
	 */
	@Override
	public InputSplit[] getSplits(JobConf job, int numSplits)
			throws IOException {

		LOG.info("Directory Input Filter Does Not Split File");

		FileStatus[] files = listStatus(job);

		// Save the number of input files in the job-conf
		job.setLong(NUM_INPUT_FILES, files.length);
		long totalSize = 0; // compute total size

		ArrayList<FileSplit> splits = new ArrayList<FileSplit>(numSplits);
		NetworkTopology clusterMap = new NetworkTopology();
		for (FileStatus file : files) {
			Path path = file.getPath();
			FileSystem fs = path.getFileSystem(job);
			long length = file.getLen();
			BlockLocation[] blkLocations = fs.getFileBlockLocations(file, 0,
					length);
			if ((length != 0) && !isSplitable(fs, path)) {
				if (length != 0) {
					String[] splitHosts = getSplitHosts(blkLocations, 0,
							length, clusterMap);
					LOG.info("SPLITED HOSTS : " + Arrays.deepToString(splitHosts));
					splits.add(new FileSplit(path, 0, length, splitHosts));
				} else {
					// Create empty hosts array for zero length files
					LOG.info("Empty Split");
					splits.add(new FileSplit(path, 0, length, new String[0]));
				}
			}
		}
		LOG.info("Total # of splits: " + splits.size());
		return splits.toArray(new FileSplit[splits.size()]);
	}

	public static void setFileExts(DoWith<Path> filter) {
		DirectoryInputFormat.fileFilter = filter;

	}

	public static void setInputPaths(JobConf conf, String commaSeparatedPaths) {
		setInputPaths(conf,
				StringUtils.stringToPath(getPathStrings(commaSeparatedPaths)));
	}

	/**
	 * Add the given comma separated paths to the list of inputs for the
	 * map-reduce job.
	 * 
	 * @param conf
	 *            The configuration of the job
	 * @param commaSeparatedPaths
	 *            Comma separated paths to be added to the list of inputs for
	 *            the map-reduce job.
	 */
	public static void addInputPaths(JobConf conf, String commaSeparatedPaths) {
		for (String str : getPathStrings(commaSeparatedPaths)) {
			addInputPath(conf, new Path(str));
		}
	}

	/**
	 * Set the array of {@link Path}s as the list of inputs for the map-reduce
	 * job.
	 * 
	 * @param conf
	 *            Configuration of the job.
	 * @param inputPaths
	 *            the {@link Path}s of the input directories/files for the
	 *            map-reduce job.
	 */
	public static void setInputPaths(JobConf conf, Path... inputPaths) {

		Path path = new Path(conf.getWorkingDirectory(), inputPaths[0]);
		StringBuffer str = new StringBuffer(StringUtils.escapeString(path
				.toString()));
		for (int i = 1; i < inputPaths.length; i++) {
			str.append(StringUtils.COMMA_STR);
			path = new Path(conf.getWorkingDirectory(), inputPaths[i]);
			str.append(StringUtils.escapeString(path.toString()));
		}
		conf.set("mapred.input.dir", str.toString());
	}

	// This method escapes commas in the glob pattern of the given paths.
	private static String[] getPathStrings(String commaSeparatedPaths) {
		int length = commaSeparatedPaths.length();
		int curlyOpen = 0;
		int pathStart = 0;
		boolean globPattern = false;
		List<String> pathStrings = new ArrayList<String>();

		for (int i = 0; i < length; i++) {
			char ch = commaSeparatedPaths.charAt(i);
			switch (ch) {
			case '{': {
				curlyOpen++;
				if (!globPattern) {
					globPattern = true;
				}
				break;
			}
			case '}': {
				curlyOpen--;
				if (curlyOpen == 0 && globPattern) {
					globPattern = false;
				}
				break;
			}
			case ',': {
				if (!globPattern) {
					pathStrings
							.add(commaSeparatedPaths.substring(pathStart, i));
					pathStart = i + 1;
				}
				break;
			}
			}
		}
		pathStrings.add(commaSeparatedPaths.substring(pathStart, length));

		return pathStrings.toArray(new String[0]);
	}
}
