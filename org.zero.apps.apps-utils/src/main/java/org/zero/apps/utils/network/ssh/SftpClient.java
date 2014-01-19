package org.zero.apps.utils.network.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpClient extends SecureShell {
	public static enum FTP_OP {
		upload, delete
	}

	public static class FtpFileInfo {
		private String path;
		private FTP_OP op;
		private String fileName;
		private File file;

		public FtpFileInfo(String path, FTP_OP op, String fileName) {
			super();
			this.path = path;
			this.op = op;
			this.fileName = fileName;
		}

		public FtpFileInfo(String path, FTP_OP op, File file) {
			super();
			this.path = path;
			this.op = op;
			this.file = file;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public FTP_OP getOp() {
			return op;
		}

		public void setOp(FTP_OP op) {
			this.op = op;
		}

		@Override
		public String toString() {
			return "FtpFileInfo [path=" + path + ", op=" + op + ", fileName="
					+ fileName + ", file=" + file + "]";
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}
	}

	public void upload(String path, String f) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload,
				new File(f)) });
	}

	public void upload(String path, File f) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.upload, f) });
	}

	public void delete(String path, String f) throws JSchException, IOException {
		execute(new FtpFileInfo[] { new FtpFileInfo(path, FTP_OP.delete, f) });
	}

	protected void execute(FtpFileInfo[] files) throws JSchException {
		Session session = getSession();
		try {

			for (FtpFileInfo command : files) {
				String remoteFileName = "";
				ChannelSftp channelExec = (ChannelSftp) session
						.openChannel("sftp");

				channelExec.connect();
				if (log.isDebugEnabled()) {
					log.debug("{} Excute Command : {} ", getHost(), command);
				}

				try {
					log.debug("{} SFTP Change Directory {}",getHost(),command.getPath());
					channelExec.cd(command.getPath());
				} catch (SftpException e) {
					e.printStackTrace();
					throw new RuntimeException(
							"SFTP Change Directory Exception", e);
				}

				if (command.getOp() == FTP_OP.upload) {
					remoteFileName = command.getFile().getName();
					FileInputStream in = null;
					try {
						in = new FileInputStream(command.getFile());
						channelExec.put(in, remoteFileName);
						if (log.isDebugEnabled()) {
						log.debug("{} SFTP UPLOAD FINISH {}",getHost(),command);
						}
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					} catch (SftpException e) {
						throw new RuntimeException(e);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				} else if (command.getOp() == FTP_OP.delete) {

					remoteFileName = command.getPath();
					if (remoteFileName.endsWith("/")) {
						remoteFileName = remoteFileName + command.getFileName();
					} else {
						remoteFileName = remoteFileName + "/"
								+ command.getFileName();
					}

					try {
						channelExec.rm(remoteFileName);
					} catch (SftpException e) {
						throw new RuntimeException(e);
					}
				} else {
					throw new RuntimeException("Unkown File Operation Type");
				}

				if (log.isDebugEnabled()) {
					log.debug("{} \n Sftp Finish : {}", new Object[] {
							getHost(), command });
				}

				channelExec.disconnect();
				log.debug("Ssh DisConnect Success {}", getHost());
			}
			session.disconnect();
		} catch (RuntimeException e) {
			session.disconnect();
			throw e;
		}
	}
	
	
	public static void main(String[] args) {
		SftpClient sc = new SftpClient();
		sc.setHost("10.10.75.131");
		sc.setUsername("user");
		sc.setPassword("hadoop");
		
		try {
			sc.upload("/home/user/", new File("C:/Patrol.sql"));
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
