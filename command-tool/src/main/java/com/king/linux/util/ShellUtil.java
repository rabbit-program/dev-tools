package com.king.linux.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public final class ShellUtil {

	protected final Logger logger = Logger.getLogger(ShellUtil.class);
	
	/**
	 * 将窗口显示在屏幕中间
	 * 
	 * @param shell
	 */
	public static void setIntermediateWindow(Shell shell) {
		// 启动设置屏幕中间
		Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
		Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
		int screenWidth = screenSize.width / 2; // 获取屏幕的宽
		int screenHeight = screenSize.height / 2; // 获取屏幕的高
		shell.setLocation(screenWidth - shell.getSize().y / 2, screenHeight - shell.getSize().x / 2);
	}

	/**
	 * 将内容防止剪贴板
	 * 
	 * @param text
	 */
	public static void setClipboard(String text) {
		// Get the toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// Get the clipboard
		Clipboard clipboard = toolkit.getSystemClipboard();
		// The setContents method of the Clipboard instance takes a Transferable
		// as first parameter. The StringSelection class implements the
		// Transferable
		// interface.
		StringSelection stringSel = new StringSelection(text);
		// We specify null as the clipboard owner
		clipboard.setContents(stringSel, null);
	}

	public static void setTextKeyCombination(final Control contr) {
		contr.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if(contr instanceof StyledText){
					StyledText text = (StyledText) contr;
					if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
						text.selectAll();
					} else if (e.stateMask == SWT.CTRL && e.keyCode == 'c') {
						// todo
						//ShellUtil.setClipboard(contr.getSelectionText());
					} else if (e.stateMask == SWT.CTRL && e.keyCode == 'v') {
						// todo
						
					} else if (e.stateMask == SWT.CTRL && e.keyCode == 'x') {
						// todo
					}
				}
			}
		});
	}

}
