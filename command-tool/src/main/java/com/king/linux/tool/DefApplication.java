package com.king.linux.tool;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.king.linux.AppShell;
import com.king.linux.database.ApplicationVo;
import com.king.linux.templet.FreemarkerData;
import com.king.linux.templet.FreemarkerUtil;
import com.king.linux.util.ShellUtil;

import jodd.util.StringUtil;

public class DefApplication extends Application {

	public DefApplication() {
		super(DefApplication.class.getSimpleName());
	}

	protected Shell shlApplication;

	private volatile boolean isExec = true;

	private Text titleText;
	private Text appProtText;
	private Text appStartText;
	private Text appStopText;
	private Text appStatusText;
	private Text appLogText;
	private Text dirNameText;
	private Text dataResultText;
	
	private AppShell appShell;
	
	private List historyList;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DefApplication window = new DefApplication();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlApplication.open();
		shlApplication.layout();
		while (!shlApplication.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlApplication = new Shell(SWT.DIALOG_TRIM | SWT.MIN);
		shlApplication.setSize(796, 631);
		shlApplication.setText("DefApplication");
		
		// 启动设置屏幕中间
		ShellUtil.setIntermediateWindow(shlApplication);

		Group group = new Group(shlApplication, SWT.NONE);
		group.setText("基本配置信息");
		group.setBounds(10, 10, 661, 281);

		Label lblNewLabel = new Label(group, SWT.NONE);
		lblNewLabel.setBounds(10, 24, 85, 17);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setText("title：");

		Label lblNewLabel_1 = new Label(group, SWT.NONE);
		lblNewLabel_1.setAlignment(SWT.RIGHT);
		lblNewLabel_1.setBounds(10, 96, 85, 17);
		lblNewLabel_1.setText("app_port：");

		Label lblNewLabel_2 = new Label(group, SWT.NONE);
		lblNewLabel_2.setAlignment(SWT.RIGHT);
		lblNewLabel_2.setBounds(10, 132, 85, 17);
		lblNewLabel_2.setText("app_start：");

		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setAlignment(SWT.RIGHT);
		lblNewLabel_3.setBounds(10, 168, 85, 17);
		lblNewLabel_3.setText("app_stop：");

		Label lblNewLabel_4 = new Label(group, SWT.NONE);
		lblNewLabel_4.setAlignment(SWT.RIGHT);
		lblNewLabel_4.setBounds(10, 204, 85, 17);
		lblNewLabel_4.setText("app_status：");

		Label lblNewLabel_5 = new Label(group, SWT.NONE);
		lblNewLabel_5.setAlignment(SWT.RIGHT);
		lblNewLabel_5.setBounds(10, 240, 85, 17);
		lblNewLabel_5.setText("app_log：");

		Label lblDirname = new Label(group, SWT.NONE);
		lblDirname.setText("dir_name：");
		lblDirname.setAlignment(SWT.RIGHT);
		lblDirname.setBounds(10, 60, 85, 17);

		titleText = new Text(group, SWT.BORDER);
		titleText.setBounds(101, 19, 550, 23);

		appProtText = new Text(group, SWT.BORDER);
		appProtText.setBounds(101, 93, 73, 23);

		appStartText = new Text(group, SWT.BORDER);
		appStartText.setBounds(101, 130, 550, 23);

		appStopText = new Text(group, SWT.BORDER);
		appStopText.setBounds(101, 167, 550, 23);

		appStatusText = new Text(group, SWT.BORDER);
		appStatusText.setBounds(101, 204, 550, 23);

		appLogText = new Text(group, SWT.BORDER);
		appLogText.setBounds(101, 241, 550, 23);

		dirNameText = new Text(group, SWT.BORDER);
		dirNameText.setBounds(101, 56, 550, 23);
		
		final Button debugButton = new Button(shlApplication, SWT.NONE);
		debugButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TestApplication testApplication = new TestApplication();
				if (appShell != null) {
					testApplication.setFile(new File(appShell.getFilePath()));
				}
				testApplication.open();
			}
		});
		debugButton.setBounds(245, 302, 80, 27);
		debugButton.setText("debug");

		Button button = new Button(shlApplication, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String dirNameTextStr = getText(dirNameText);
				if (isExec && StringUtil.isNotBlank(dirNameTextStr)) {
					isExec = Boolean.FALSE;
					FreemarkerData data = FreemarkerUtil.valueOf();
					data.put(AppShell.APP_TITLE, getText(titleText));// 标题
					data.put(AppShell.APP_PORT, getText(appProtText));// 端口
					data.put(AppShell.DIR_NAME, dirNameTextStr);// 目录名字
					data.put(AppShell.APP_START, getText(appStartText));
					data.put(AppShell.APP_STOP, getText(appStopText));
					data.put(AppShell.APP_STATUS, getText(appStatusText));
					data.put(AppShell.APP_LOG, getText(appLogText));
					try {
						appShell = AppShell.run(data);
						dataResultText.setText(appShell.getData());
					} catch (Exception e1) {
						logger.warn("执行错误", e1);
					} finally {
						isExec = Boolean.TRUE;
					}
					//debugButton.setEnabled(isExec);
				}
			}
		});
		button.setBounds(357, 302, 80, 27);
		button.setText("生成文件");
		
		Group group_1 = new Group(shlApplication, SWT.NONE);
		group_1.setText("输出数据");
		group_1.setBounds(10, 330, 661, 245);
		
		dataResultText = new Text(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		dataResultText.setBounds(10, 23, 641, 212);
		
		Group variableGroup = new Group(shlApplication, SWT.NONE);
		variableGroup.setText("内部变量");
		variableGroup.setBounds(677, 10, 103, 182);
		
		Button APP_PATHButton = new Button(variableGroup, SWT.NONE);
		APP_PATHButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShellUtil.setClipboard("$APP_PATH");
			}
		});
		APP_PATHButton.setBounds(11, 74, 80, 27);
		APP_PATHButton.setText("APP_PATH");
		
		Button BASE_PATHButton = new Button(variableGroup, SWT.NONE);
		BASE_PATHButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShellUtil.setClipboard("$BASE_PATH");
			}
		});
		BASE_PATHButton.setBounds(11, 41, 80, 27);
		BASE_PATHButton.setText("BASE_PATH");
		
		Button APP_PIDButton = new Button(variableGroup, SWT.NONE);
		APP_PIDButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShellUtil.setClipboard("$APP_PID");
			}
		});
		APP_PIDButton.setBounds(11, 107, 80, 27);
		APP_PIDButton.setText("APP_PID");
		
		Button APP_NAMEButton = new Button(variableGroup, SWT.NONE);
		APP_NAMEButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShellUtil.setClipboard("$APP_NAME");
			}
		});
		APP_NAMEButton.setBounds(11, 140, 80, 27);
		APP_NAMEButton.setText("APP_NAME");
		
		Group historyGroup = new Group(shlApplication, SWT.NONE);
		historyGroup.setText("历史信息");
		historyGroup.setBounds(677, 198, 103, 376);
		
		// 还原数据
		historyList = new List(historyGroup, SWT.V_SCROLL);
		historyList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				int index = historyList.getSelectionIndex();
				if (index > -1) {
					String key = historyList.getItem(index);
					setFieldData(key);
					logger.info("selection key => {}", key);
				}
			}
		});
		
		historyList.setBounds(3, 25, 98, 341);
		
		Menu rootMenu = new Menu(shlApplication, SWT.BAR);
		shlApplication.setMenuBar(rootMenu);
		
		MenuItem mntmFile = new MenuItem(rootMenu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu = new Menu(mntmFile);
		mntmFile.setMenu(menu);
		
		// 保存数据
		MenuItem saveDataMenuItem = new MenuItem(menu, SWT.NONE);
		saveDataMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = getText(dirNameText);
				if (StringUtil.isNotBlank(text)) {
					Map<String, Object> dMap = getFieldData(new Class<?>[] { Text.class });
					mapdbServer.put(text, ApplicationVo.valueOf(text, dMap));
					setHistoryList(historyList);
				}
			}
		});
		saveDataMenuItem.setText("保存数据");
		
		MenuItem removeMenuItem = new MenuItem(menu, SWT.NONE);
		removeMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//获取选中的行
				if (historyList.getSelectionIndex() > -1) {
					String key = historyList.getItem(historyList.getSelectionIndex());
					mapdbServer.remove(key);
					setHistoryList(historyList);
				}
			}
		});
		removeMenuItem.setText("删除数据");
		
		MenuItem aboutMenuItem = new MenuItem(rootMenu, SWT.CASCADE);
		aboutMenuItem.setText("关于");
		
		Menu menu_1 = new Menu(aboutMenuItem);
		aboutMenuItem.setMenu(menu_1);
		
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.setText("version1.0");
		
		setHistoryList(historyList);
	}
	

}
