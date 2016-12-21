package com.king.linux.sftp;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.king.linux.util.ThreadUtil;

public class SessionServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected String host;
	protected int port;
	protected String username;
	protected String password;

	private Session session;

	public static SessionServer valueOf(String host, int port, String username, String password) {
		return new SessionServer(host, port, username, password);
	}

	/**
	 * 私有构造
	 */
	private SessionServer() {
		
	}

	/**
	 * @param host
	 *            ip
	 * @param port
	 *            端口
	 * @param username
	 *            账号
	 * @param password
	 *            密码
	 */
	private SessionServer(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	public void openSession(final StatusServer statusServer) {
		ThreadUtil.execute(new Runnable() {
			public void run() {
				try {
					JSch jsch = new JSch();
					jsch.getSession(username, host, port);
					session = jsch.getSession(username, host, port);
					session.setPassword(password);
					Properties sshConfig = new Properties();
					sshConfig.put("StrictHostKeyChecking", "no");
					session.setConfig(sshConfig);
					// 设置登陆超时时间
					session.connect(5000);
					logger.info("{} connect success", host);
					statusServer.success(null);
				} catch (Exception e) {
					statusServer.fail(e.getMessage());
					logger.error("{} connect fail", host, e);
				}
			}
		});
	}

	/**
	 * ChannelSftp
	 */
	public ChannelSftp openChannelSftp() throws JSchException {
		Channel channel = session.openChannel("sftp");
		return ((ChannelSftp) channel);
	}

	/**
	 * ChannelShell
	 */
	public ChannelExec openChannelExec() throws JSchException {
		Channel channel = session.openChannel("exec");
		return ((ChannelExec) channel);
	}
	
	public static void closeChannel(Channel channel) {
		if (channel != null) {
			channel.disconnect();
		}
	}
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Session getSession() {
		return session;
	}
}
