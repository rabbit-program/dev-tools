package com.king.linux.sftp;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.king.linux.util.ThreadUtil;

public class ExecServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final int BLOCK_SIZE = 2048;

	private SessionServer sessionServer;

	public static ExecServer valueOf(SessionServer sessionServer) {
		return new ExecServer(sessionServer);
	}

	/**
	 * 私有构造
	 */
	private ExecServer() {

	}

	private ExecServer(SessionServer sessionServer) {
		this.sessionServer = sessionServer;
	}

	
	/**
	 * linux上传文件
	 */
	public void command(final String command) {
		command(command,new ExecServerData(){
			@Override
			public void success(Object object) {
			}
			@Override
			public void fail(Object object) {
			}
			@Override
			public void response(String data) {
			}
		});
	}
	
	/**
	 * linux上传文件
	 */
	public void command(final String command,final ExecServerData execServerData) {
		ThreadUtil.execute(new Runnable() {
			public void run() {
				ChannelExec channelExec = null;
				try {
					channelExec = sessionServer.openChannelExec();
					logger.info("execute command [{}]", command);
					executeCommand(command, channelExec,execServerData);
					execServerData.success(null);
				} catch (Exception e) {
					execServerData.fail(e.getMessage());
					logger.error("command [{}] fail", command, e);
				} finally {
					SessionServer.closeChannel(channelExec);
				}
			}
		});
	}

	private void executeCommand(String command, ChannelExec channelExec,ExecServerData execServerData) {
		byte[] bytes = new byte[BLOCK_SIZE];
		try {
			InputStream result = channelExec.getInputStream();
			channelExec.setCommand(command);
			channelExec.connect();
			int len;
			while ((len = result.read(bytes)) != -1) {
				String data = new String(bytes, 0, len);
				if(logger.isDebugEnabled()) 
					logger.debug("read===>{}", data);
				execServerData.response(data);
			}
		} catch (Exception e) {
			logger.error("SocketThreadInput leave", e);
		}
	}
}
