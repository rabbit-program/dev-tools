package com.king.linux;

import java.io.File;

import com.king.linux.templet.FreemarkerData;
import com.king.linux.templet.FreemarkerUtil;

import jodd.io.FileUtil;
import jodd.util.SystemUtil;

/**
 * Hello world!
 *
 */
public class AppShell {
	
	public static final String TEMPLETE_RUN = "command.ftl";
	
	public static final String APP_TITLE = "title";
	
	public static final String DIR_NAME = "dir_name";
	public static final String APP_PORT = "app_port";
	public static final String APP_START = "app_start";
	public static final String APP_STOP = "app_stop";
	public static final String APP_STATUS = "app_status";
	public static final String APP_LOG = "app_log";

	private String filePath;
	private String data;
	
	public static void main(String[] args) throws Exception {
		FreemarkerData data = FreemarkerUtil.valueOf();
		
		data.put("title", "mongodb");// 标题
		data.put(APP_PORT, "27017");// 端口
		data.put(DIR_NAME, "mongodb-3.2.6");// 目录名字

		data.put(APP_START, "bin/mongod --bind_ip localhost -f $APP_PATH/mongodb.conf");
		data.put(APP_STOP, "bin/mongo 127.0.0.1:27017/admin --eval \"db.shutdownServer()\"");
		data.put(APP_STATUS, "bin/mongo 127.0.0.1:27017/admin --eval \"db.stats()\"");
		data.put(APP_LOG, "/data/mongodb/logs/mongodb.log");

		run(data);
	}

	public static AppShell run(FreemarkerData data) throws Exception {
		String savePath = SystemUtil.userDir() + File.separator + "shell" + File.separator + data.get(DIR_NAME) + ".sh";
		File savePathFile = new File(savePath);
		if (!FileUtil.isExistingFolder(savePathFile.getParentFile())) {
			FileUtil.mkdirs(savePathFile.getParentFile());
		}
		if (FileUtil.isExistingFile(new File(savePath))) {
			FileUtil.deleteFile(savePath);
		}
		AppShell shell = new AppShell();
		shell.data = FreemarkerUtil.process(data, TEMPLETE_RUN, savePath);
		shell.filePath = savePath;
		return shell;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getData() {
		return data;
	}
	
}
