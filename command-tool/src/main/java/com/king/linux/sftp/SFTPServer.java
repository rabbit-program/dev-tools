package com.king.linux.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;

import jodd.util.StringUtil;

public class SFTPServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private SessionServer sessionServer;

	public static SFTPServer valueOf(SessionServer sessionServer) {
		return new SFTPServer(sessionServer);
	}

	/**
	 * 私有构造
	 */
	private SFTPServer() {

	}

	private SFTPServer(SessionServer sessionServer) {
		this.sessionServer = sessionServer;
	}

	/**
	 * linux上传文件
	 */
	public List<String> ls(String directory) {
		List<String> list = Lists.newArrayList();
		if (StringUtil.isEmpty(directory))
			return list;
		ChannelSftp channelSftp = null;
		try {
			channelSftp = this.sessionServer.openChannelSftp();
			channelSftp.connect();
			Vector<?> vector = channelSftp.ls(directory);
			for (Iterator<?> iterator = vector.iterator(); iterator.hasNext();) {
				ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) iterator.next();
				list.add(lsEntry.getFilename());
			}
			this.logger.info("ls [{}] success", directory);
		} catch (Exception e) {
			this.logger.info("ls [{}] fail", directory, e);
		} finally {
			SessionServer.closeChannel(channelSftp);
		}
		return list;
	}

	/**
	 * linux上传文件
	 */
	public void upload(String directory, File file, StatusServer statusServer) {
		if ((file == null) || (!file.exists()))
			return;
		upload(directory, file, file.getName(), statusServer);
	}

	/**
	 * linux上传文件
	 */
	public void upload(String directory, File file, String fileName, StatusServer statusServer) {
		if (StringUtil.isEmpty(fileName))
			return;
		ChannelSftp channelSftp = null;
		try {
			channelSftp = this.sessionServer.openChannelSftp();
			channelSftp.connect();
			channelSftp.cd(directory);
			this.logger.info("cd {}", directory);
			channelSftp.put(new FileInputStream(file), fileName);
			this.logger.info("upload [{}] success", fileName);
			statusServer.success("success");
		} catch (Exception e) {
			statusServer.fail(e.getMessage());
			this.logger.info("upload [{}] fail", fileName);
		} finally {
			SessionServer.closeChannel(channelSftp);
		}
	}

	/**
	 * linux下载文件
	 */
	/*
	 * public void download(String dir, String downDir) { try { File file = new
	 * File(dir); String parent = getParent(file); channelSftp.cd(parent); File
	 * desc = new File(downDir); FileOutputStream outputStream = new
	 * FileOutputStream(desc);
	 * 
	 * // channelSftp.
	 * 
	 * channelSftp.get(file.getName(), outputStream); logger.info("down %s suc",
	 * dir); outputStream.close(); } catch (Exception e) {
	 * logger.error("download error : {}", dir, e); } finally { // close(sftp);
	 * } }
	 */
}
