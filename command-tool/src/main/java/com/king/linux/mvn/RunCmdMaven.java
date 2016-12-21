package com.king.linux.mvn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import jodd.util.StringUtil;
import jodd.util.SystemUtil;


public class RunCmdMaven {
	// mvn install:install-file
	// -Dfile=D:/org.eclipse.swt.win32.win32.x86_64_3.104.1.v20150825-0743.jar
	// -DgroupId=org.eclipse.swt
	// -DartifactId=org.eclipse.swt.win32.win32.x86_64
	// -Dversion=3.104.1.v20150825-0743
	// -Dpackaging=jar
	final static String FORMART_MAVEN = "mvn install:install-file -Dfile=%s -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=jar";

	final static Map<String, Boolean> execCmdMap = new LinkedHashMap<>();

	public static void main(String[] args) {
		String lib = SystemUtil.userDir() + File.separator + "libs";
		File libFile = new File(lib);
		File[] files = libFile.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".jar");
			}
		});
		for (File fff : files) {
			installMaven(fff);
		}
		System.out.println("\n\n=====================================成功========================================\n\n");
		for(Map.Entry<String, Boolean> entry : execCmdMap.entrySet()){
			if(entry.getValue()){
				System.out.println(entry.getKey());
			}
		}
		System.out.println("\n\n=====================================失败========================================\n\n");
		for(Map.Entry<String, Boolean> entry : execCmdMap.entrySet()){
			if(!entry.getValue()){
				System.out.println(entry.getKey());
			}
		}
	}

	public static int execCmd(String commod) {
		String[] cmd = new String[] { "cmd.exe", "/C", commod };
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			process.getOutputStream().close();
			int exitValue = process.waitFor();
			return exitValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void installMaven(File file) {
		String fileName = file.getName();
		fileName = StringUtil.substring(fileName, 0, fileName.lastIndexOf("."));
		if (fileName.indexOf("_") > -1) {
			String Dfile = StringUtil.replace(file.getPath(), "\\", "/");
			String DgroupId = "org.eclipse.software";
			String DartifactId = StringUtil.substring(fileName, 0, fileName.indexOf("_"));
			String Dversion = StringUtil.substring(fileName, fileName.indexOf("_") + 1, fileName.length());
			StringBuffer buffer = new StringBuffer();
			buffer.append("<dependency>\n");
			buffer.append("	<groupId>" + DgroupId + "</groupId>\n");
			buffer.append("	<artifactId>" + DartifactId + "</artifactId>\n");
			buffer.append("	<version>" + Dversion + "</version>\n");
			buffer.append("</dependency>\n");
			String cmdCommon = String.format(FORMART_MAVEN, Dfile, DgroupId, DartifactId, Dversion);
			System.out.println("执行命令：" + cmdCommon);
			int flag = execCmd(cmdCommon);
			execCmdMap.put(buffer.toString(), flag == 0);
			System.out.println("\n\n");
		} else {
			System.out.println(fileName);
		}
	}

}
