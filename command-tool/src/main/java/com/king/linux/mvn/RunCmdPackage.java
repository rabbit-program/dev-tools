package com.king.linux.mvn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jodd.io.FileUtil;
import jodd.util.StringUtil;

public class RunCmdPackage {

	public final static String curr_dir = System.getProperty("user.dir");
		
	public static String fileName = "GenCode-1.0.1.jar";
	
	public static void main(String[] args) throws IOException {
		FileUtil.deleteFile(curr_dir + "/target/" + fileName);
		System.out.println(curr_dir);
		int flag = execCmd("mvn assembly:assembly");
		System.out.println("flag -->" + flag);
		FileUtil.deleteFile(curr_dir + "/" + fileName);
		FileUtil.copyFileToDir(curr_dir + "/target/" + fileName, curr_dir);
		System.out.println(fileName);
	}
	
	public static int execCmd(String commod) {
		String[] cmd = new String[] { "cmd.exe", "/C", commod };
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if(line.startsWith("with assembly file")){
						fileName = StringUtil.substring(line, line.lastIndexOf("\\") + 1, line.length());
						//: D:\yunsvn\GenCode\target\GenCode-1.0.1.jar
					}
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
	
}
