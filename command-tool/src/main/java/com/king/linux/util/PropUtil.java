package com.king.linux.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jodd.io.watch.DirWatcher;
import jodd.io.watch.DirWatcherListener;
import jodd.io.watch.DirWatcher.Event;
import jodd.util.PropertiesUtil;
import jodd.util.SystemUtil;
/**
 * @ClassName PropUtil <br/>
 * @Description TODO <span>资源文件加载</span>
 * @Author Qing
 * @Date 2016年12月20日 下午4:40:54 <br/>
 */
public class PropUtil {

	final static Logger LOGGER = LoggerFactory.getLogger(PropUtil.class);

	private final static String PROP_FILE_DIR = SystemUtil.userDir() + File.separator + "conf" + File.separator;

	private final static String PROP_FILE_NAME = "command.properties";
	
	private final static String PROP_FILE = PROP_FILE_DIR + PROP_FILE_NAME;
	
	private final static Properties PROPERTIES = new Properties();
	
	private static void load(){
		PROPERTIES.clear();
		try {
			PropertiesUtil.loadFromFile(PROPERTIES, new File(PROP_FILE));
		} catch (IOException e) {
			LOGGER.warn("读取文件出错:{}", PROP_FILE, e);
		}
	}
	
	static {
		load();
		DirWatcher dirWatcher = new DirWatcher(PROP_FILE_DIR).useWatchFile(PROP_FILE_NAME);
		dirWatcher.register(new DirWatcherListener() {
			@Override
			public void onChange(File file, Event event) {
				if (event == Event.MODIFIED){
					synchronized(PROPERTIES){
						load();
					}
				}
				LOGGER.info("文件被[{}] - [{}]重新加载",event.toString(), file.getName());
			}
		});
		dirWatcher.start(1000);
	}

	public static String getProperty(String key, String defaultValue) {
		synchronized(PROPERTIES){
			return PropertiesUtil.getProperty(PROPERTIES, key, defaultValue);
		}
	}

}
