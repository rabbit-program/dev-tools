/**
 * FreemarkerUtil.java
 * com.alife.stock.util
 *
 * Function： TODO 
 *
 *   ver         date      	    author
 * ──────────────────────────────────────
 *  Ver 1.1	     2014年10月15日 	    兔先生_
 *
 * Copyright (c) 2014, TNT All Rights Reserved.
 */

package com.king.linux.templet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.king.linux.util.DateUtil;
import com.king.linux.util.PropUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jodd.io.FileUtil;
import jodd.util.SystemUtil;

public class FreemarkerUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerUtil.class);

	private static final String TEMPLATE_LOADING = SystemUtil.userDir() + File.separator + "conf";
	private static final String ENCODING = "UTF-8";
	private static Configuration cfg;

	static {
		File file = new File(TEMPLATE_LOADING);
		if (!FileUtil.isExistingFolder(file)) {
			LOGGER.info("创建文件夹->{}", TEMPLATE_LOADING);
			try {
				FileUtil.mkdirs(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("模板的路径\n{}", TEMPLATE_LOADING);
	}

	public static FreemarkerData valueOf() {
		FreemarkerData map = new FreemarkerData();
		map.put("author", PropUtil.getProperty("author", "king"));
		map.put("dateTime", DateUtil.DateToString(new Date(), DateUtil.DateStyle.YYYY_MM_DD_HH_MM_SS));
		return map;
	}

	public static FreemarkerData valueOf(String key, Object value) {
		FreemarkerData map = valueOf();
		map.put(key, value);
		return map;
	}

	private static void init() throws IOException {
		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_LOADING));
		cfg.setDefaultEncoding(ENCODING);
		cfg.setOutputEncoding(ENCODING);
		cfg.setEncoding(Locale.CHINA, ENCODING);
	}

	private static Configuration getConfiguration() throws IOException {
		if (cfg == null)
			init();
		return cfg;
	}

	public static String process(Map<String, Object> data, String template) {
		StringWriter writer = new StringWriter();
		String strings = "";
		try {
			Configuration conf = getConfiguration();
			Template temp = conf.getTemplate(template);
			temp.setEncoding(ENCODING);
			temp.setOutputEncoding(ENCODING);
			temp.process(data, writer);
			strings = writer.toString();
		} catch (IOException e) {
			LOGGER.warn("读取文件流错误", e);
			try {
				writer.close();
			} catch (Exception localException) {
			}
		} catch (TemplateException e) {
			LOGGER.warn("读取模板错误", e);
			try {
				writer.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				writer.close();
			} catch (Exception localException2) {
			}
		}
		return strings;
	}

	public static void print(Map<String, Object> data, String template) {
		StringWriter writer = new StringWriter();
		try {
			Configuration conf = getConfiguration();
			Template temp = conf.getTemplate(template);
			temp.setEncoding(ENCODING);
			temp.setOutputEncoding(ENCODING);
			temp.process(data, writer);
			LOGGER.info("处理后的内容 \n{}", writer.toString());
		} catch (IOException e) {
			LOGGER.warn("读取文件流错误", e);
			try {
				writer.close();
			} catch (Exception localException) {
			}
		} catch (TemplateException e) {
			LOGGER.warn("读取模板错误", e);
			try {
				writer.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				writer.close();
			} catch (Exception localException2) {
			}
		}
	}

	public static String process(Map<String, Object> data, String template, String savePath) {
		StringWriter stringWriter = new StringWriter();
		OutputStream outputStream = null;
		String strings = "";
		try {
			Configuration conf = getConfiguration();
			Template temp = conf.getTemplate(template);
			File file = new File(savePath);

			outputStream = new FileOutputStream(file);
			temp.process(data, new OutputStreamWriter(outputStream, ENCODING));

			temp.process(data, stringWriter);
			strings = stringWriter.toString();
			LOGGER.info("处理后的内容 \n{}", strings);
		} catch (Exception e) {
			LOGGER.warn("处理错误", e);
			try {
				stringWriter.close();
				outputStream.close();
			} catch (Exception localException1) {
			}
		} finally {
			try {
				stringWriter.close();
				outputStream.close();
			} catch (Exception localException2) {
			}
		}
		return strings;
	}
}