package com.king.linux.tool;

import java.io.File;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.king.linux.database.ApplicationVo;
import com.king.linux.sftp.ExecServer;
import com.king.linux.sftp.ExecServerData;
import com.king.linux.sftp.SFTPServer;
import com.king.linux.sftp.SessionServer;
import com.king.linux.sftp.StatusServer;
import com.king.linux.util.PropUtil;
import com.king.linux.util.ShellUtil;

import jodd.util.StringUtil;

public class TestApplication extends Application {

	public TestApplication() {
		super(TestApplication.class.getSimpleName());
	}

	protected Shell shell;

	private File file;
	private String fileName;

	private SessionServer sessionServer;

	private Text ipText;
	private Text userText;
	private Text pwdText;
	private Text portText;
	private Text respText;
	private Text uploadText;

	private Label statusLabel;
	private List historyList;
	private Composite statusComposite;
	private Text runText;
	private Combo getCombo;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TestApplication window = new TestApplication();
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
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.DIALOG_TRIM | SWT.MIN);
		shell.setSize(844, 666);
		shell.setText(fileName == null ? "TestApplication" : fileName);

		// 启动设置屏幕中间
		ShellUtil.setIntermediateWindow(shell);

		Group serverGroup = new Group(shell, SWT.NONE);
		serverGroup.setText("服务器信息");
		serverGroup.setBounds(10, 10, 686, 95);

		Label lblIp = new Label(serverGroup, SWT.NONE);
		lblIp.setBounds(48, 24, 36, 17);
		lblIp.setText("主机：");

		ipText = new Text(serverGroup, SWT.BORDER);
		ipText.setBounds(90, 21, 97, 23);

		Label lblPort = new Label(serverGroup, SWT.NONE);
		lblPort.setText("用户名：");
		lblPort.setBounds(193, 24, 48, 17);

		userText = new Text(serverGroup, SWT.BORDER);
		userText.setBounds(247, 21, 97, 23);

		Label label = new Label(serverGroup, SWT.NONE);
		label.setText("密码：");
		label.setBounds(350, 24, 36, 17);

		pwdText = new Text(serverGroup, SWT.BORDER);
		pwdText.setBounds(392, 21, 97, 23);

		Label label_1 = new Label(serverGroup, SWT.NONE);
		label_1.setText("端口：");
		label_1.setBounds(495, 24, 36, 17);

		portText = new Text(serverGroup, SWT.BORDER);
		portText.setText("22");
		portText.setBounds(537, 21, 53, 23);

		Button ConnectionButton = new Button(serverGroup, SWT.NONE);
		ConnectionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String host = getText(ipText);
				String port = getText(portText);
				String username = getText(userText);
				String password = getText(pwdText);
				sessionServer = SessionServer.valueOf(host, Integer.valueOf(port), username, password);
				sessionServer.openSession(new StatusServer() {
					@Override
					public void success(Object object) {
						asyncExec(new Runnable() {
							public void run() {
								statusLabel.setText("连接成功!");
								statusComposite.setBackground(SWTResourceManager.getColor(153, 255, 153));
							}
						});
					}

					@Override
					public void fail(final Object object) {
						asyncExec(new Runnable() {
							public void run() {
								statusLabel.setText(object.toString());
								statusComposite.setBackground(SWTResourceManager.getColor(255, 102, 102));
							}
						});
					}
				});
			}
		});
		ConnectionButton.setBounds(596, 18, 80, 27);
		ConnectionButton.setText("快速连接");
		// 画圆形
		Region region = new Region();
		region.add(circle(10, 14, 14));
		statusComposite = new Composite(serverGroup, SWT.NONE);
		statusComposite.setBounds(10, 18, 28, 28);
		statusComposite.setRegion(region);
		statusComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

		Label label_2 = new Label(serverGroup, SWT.NONE);
		label_2.setBounds(10, 63, 72, 17);
		label_2.setText("服务器目录：");

		uploadText = new Text(serverGroup, SWT.BORDER);
		uploadText.setBounds(90, 60, 236, 23);

		Button renameCheckButton = new Button(serverGroup, SWT.CHECK);
		renameCheckButton.setBounds(332, 63, 57, 17);
		renameCheckButton.setText("重命名");

		getCombo = new Combo(serverGroup, SWT.READ_ONLY);
		// getCombo.setItems(new String[] {"ddd", "da", "dd", "d"});
		getCombo.setBounds(478, 60, 146, 25);

		Button getButton = new Button(serverGroup, SWT.NONE);
		getButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String directory = getText(uploadText);
				SFTPServer sftpServer = SFTPServer.valueOf(sessionServer);
				java.util.List<String> list = sftpServer.ls(directory);
				/*java.util.List<String> newList = Lists.newArrayList();
				for (String string : list) {
					if (string.endsWith(".sh")) {
						newList.add(string);
					}
				}*/
				getCombo.setItems(list.toArray(new String[0]));
				if (list.size() > 0) {
					getCombo.select(0);
				}
			}
		});
		getButton.setBounds(628, 59, 48, 27);
		getButton.setText("获取");

		Button uploadButton = new Button(serverGroup, SWT.NONE);
		uploadButton.setBounds(392, 59, 80, 27);
		uploadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String directory = getText(uploadText);
				if (renameCheckButton.getSelection()) {
					String command = String.format("cd %s;rm -f %s;", directory, fileName);
					// 执行命令
					ExecServer.valueOf(sessionServer).command(command);
					fileName = PropUtil.getProperty("shellFileName", "run.sh");
				}
				SFTPServer sftpServer = SFTPServer.valueOf(sessionServer);
				sftpServer.upload(directory, file, fileName, new StatusServer() {
					@Override
					public void success(Object object) {
						statusLabel.setText("文件上传成功!");
					}
					@Override
					public void fail(Object object) {
						statusLabel.setText(object.toString());
					}
				});
			}
		});
		uploadButton.setText("文件上传");

		Group responseGroup = new Group(shell, SWT.NONE);
		responseGroup.setText("响应结果");
		responseGroup.setBounds(10, 182, 686, 392);

		respText = new Text(responseGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		respText.setBounds(10, 21, 666, 361);

		Button startButton = new Button(shell, SWT.NONE);
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("start");
			}
		});
		startButton.setBounds(97, 116, 80, 27);
		startButton.setText("启动");

		Button stopButton = new Button(shell, SWT.NONE);
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("stop");
			}
		});
		stopButton.setBounds(183, 116, 80, 27);
		stopButton.setText("停止");

		Button restartButton = new Button(shell, SWT.NONE);
		restartButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("restart");
			}
		});
		restartButton.setBounds(269, 116, 80, 27);
		restartButton.setText("重启");

		Button killButton = new Button(shell, SWT.NONE);
		killButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("kill");
			}
		});
		killButton.setBounds(355, 116, 80, 27);
		killButton.setText("杀死");

		Button statusButton = new Button(shell, SWT.NONE);
		statusButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeCommand("status");
			}
		});
		statusButton.setBounds(441, 116, 80, 27);
		statusButton.setText("状态");

		statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10, SWT.NORMAL));
		statusLabel.setBounds(10, 580, 818, 27);
		statusLabel.setText("这是状态信息栏");

		Menu rootMenu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(rootMenu);

		MenuItem fileSubmenu = new MenuItem(rootMenu, SWT.CASCADE);
		fileSubmenu.setText("File");

		Menu menu_1 = new Menu(fileSubmenu);
		fileSubmenu.setMenu(menu_1);

		MenuItem saveDataItem = new MenuItem(menu_1, SWT.NONE);
		// 保存数据
		saveDataItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = getText(ipText);
				if (StringUtil.isNotBlank(ip)) {
					Map<String, Object> dMap = getFieldData(new Class<?>[] { Text.class });
					mapdbServer.put(ip, ApplicationVo.valueOf(ip, dMap));
					setHistoryList(historyList);
				}
			}
		});
		saveDataItem.setText("保存数据");

		MenuItem removeMenuItem = new MenuItem(menu_1, SWT.NONE);
		removeMenuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 获取选中的行
				if (historyList.getSelectionIndex() > -1) {
					String key = historyList.getItem(historyList.getSelectionIndex());
					mapdbServer.remove(key);
					setHistoryList(historyList);
				}
			}
		});
		removeMenuItem.setText("删除数据");

		MenuItem aboutSubmenu = new MenuItem(rootMenu, SWT.CASCADE);
		aboutSubmenu.setText("关于");

		Menu menu_2 = new Menu(aboutSubmenu);
		aboutSubmenu.setMenu(menu_2);

		MenuItem mntmVersion = new MenuItem(menu_2, SWT.NONE);
		mntmVersion.setText("version1.1");

		Group historyGroup = new Group(shell, SWT.NONE);
		historyGroup.setText("历史信息");
		historyGroup.setBounds(700, 10, 131, 564);

		historyList = new List(historyGroup, SWT.V_SCROLL);
		// 还原数据
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
		historyList.setBounds(5, 23, 121, 531);
		setHistoryList(historyList);

		Button runButton = new Button(shell, SWT.NONE);
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String command = getText(runText);
				logger.info("run command : {}", command);
				ExecServer execServer = ExecServer.valueOf(sessionServer);
				final StringBuffer buffer = new StringBuffer();
				// 执行命令
				execServer.command(command, new ExecServerData() {
					@Override
					public void response(String data) {
						buffer.append(data);
						asyncExec(new Runnable() {
							public void run() {
								respText.setText(buffer.toString());
							}
						});
					}

					@Override
					public void success(Object object) {
						asyncExec(new Runnable() {
							public void run() {
								statusLabel.setText("执行命令完成!");
							}
						});
					}

					@Override
					public void fail(Object object) {
						asyncExec(new Runnable() {
							public void run() {
								statusLabel.setText(object.toString());
							}
						});
					}
				});
			}
		});

		runButton.setBounds(527, 116, 80, 27);
		runButton.setText("运行命令");

		runText = new Text(shell, SWT.BORDER);
		runText.setBounds(97, 149, 510, 27);

		if (historyList.getItemCount() > 0) {
			historyList.select(0);
			String key = historyList.getItem(0);
			setFieldData(key);
			logger.info("设置默认历史key:{}", key);
		}

		if (file == null) {
			uploadButton.setEnabled(false);
			statusLabel.setText("启动错误，文件名称为空!");
		}
	}

	private void executeCommand(String cmd) {
		String directory = getText(uploadText);
		String _fileName = fileName;
		if (getCombo.getSelectionIndex() > -1) {
			_fileName = getCombo.getItem(getCombo.getSelectionIndex());
		}
		ExecServer execServer = ExecServer.valueOf(sessionServer);
		String commandFormart = "cd %s;chmod +x %s;sh %s %s";
		String command = String.format(commandFormart, directory, _fileName, _fileName, cmd);
		final StringBuffer buffer = new StringBuffer();
		// CountDownLatch countDownLatch = new CountDownLatch(1);
		// 执行命令
		execServer.command(command, new ExecServerData() {
			@Override
			public void response(String data) {
				buffer.append(data);
				asyncExec(new Runnable() {
					public void run() {
						respText.setText(buffer.toString());
					}
				});
			}

			@Override
			public void success(Object object) {
				asyncExec(new Runnable() {
					public void run() {
						statusLabel.setText("执行命令完成!");
					}
				});
			}

			@Override
			public void fail(Object object) {
				asyncExec(new Runnable() {
					public void run() {
						statusLabel.setText(object.toString());
					}
				});
			}
		});
	}

	static int[] circle(int r, int offsetX, int offsetY) {
		int[] polygon = new int[8 * r + 4];
		// x^2 + y^2 = r^2
		for (int i = 0; i < 2 * r + 1; i++) {
			int x = i - r;
			int y = (int) Math.sqrt(r * r - x * x);
			polygon[2 * i] = offsetX + x;
			polygon[2 * i + 1] = offsetY + y;
			polygon[8 * r - 2 * i - 2] = offsetX + x;
			polygon[8 * r - 2 * i - 1] = offsetY - y;
		}
		return polygon;
	}

	public void setFile(File file) {
		this.file = file;
		this.fileName = file.getName();
	}
}
