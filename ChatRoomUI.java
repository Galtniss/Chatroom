package com.Chat02.Client;

/**
 * 聊天窗口的主界面将实现聊天系统的重要功能，再这个界面，同时创建了两个SocketClient，
 * 一个是从登录界面获取的Socket，它主要用来发送和接收消息，聊天窗口的message接收与发送，都转移到ClientThread中
 * 另一个Socket主要被用来接收当前的在线人数，这是我迫不得已的做法，因为目前看来，引用一个新的Socket连接是获取列表最为方便的做法
 * 
 * 还将实现的功能是，与某人特定聊天。。。。
 */
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class ChatRoomUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -607104815605494438L;
	private JPanel contentPane;
	private ClientThread client;
	private JTextArea textArea_1;
	private JTextArea textArea;
	private DefaultListModel<String> model;
	private JList<String> jList;
	private Socket listSocket;
	private String ip;
	private JLabel onlineNum;
	private String user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatRoomUI frame = new ChatRoomUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatRoomUI(Socket client, String name, String ip) {
		this();
		this.user = name;
		setTitle(user + " 的聊天室");
		this.client = new ClientThread(client, textArea, this);
		this.client.start();// 开启线程
		this.ip = ip;
		try {
			this.listSocket = new Socket(ip, 34344);// 在线用户列表线程
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ChatRoomUI() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ChatRoomUI.class.getResource("/com/Chat02/Client/Chat_ico.png")));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 580, 445);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();

		JScrollPane scrollPane_1 = new JScrollPane();

		JButton button = new JButton("\u53D1\u9001");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = textArea_1.getText();
				client.send(message);// 客户端发送信息
				textArea_1.setText("");
				textArea.setCaretPosition(textArea.getText().length());// 控制光标下移
			}
		});

		JButton button_1 = new JButton("\u79BB\u5F00");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (client != null) {
					client.Exit();
					System.exit(0);
				}
			}
		});

		JScrollPane scrollPane_2 = new JScrollPane();

		JLabel label = new JLabel("\u5728\u7EBF\u4EBA\u6570\uFF1A");

		onlineNum = new JLabel("0");

		JButton btnNewButton = new JButton("\u5E2E\u52A9");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog help = new JDialog(ChatRoomUI.this, "帮助", true);
				help.setLayout(new FlowLayout());
				JLabel helpContent = new JLabel("通过下方的聊天窗口，");
				JLabel helpContent5 = new JLabel("与聊天室的好友交流。");
				JLabel helpContent2 = new JLabel("左侧可以查看当前在线人数。");
				JLabel helpContent3 = new JLabel("双击列表中的姓名，");
				JLabel helpContent4 = new JLabel("可以单独向其发送信息。");
				JLabel helpContent6 = new JLabel("Ctrl+Enter快捷键可以快速发送信息。");
				help.setBounds(550, 250, 240, 180);
				help.setResizable(false);
				help.add(helpContent);
				help.add(helpContent5);
				help.add(helpContent2);
				help.add(helpContent3);
				help.add(helpContent4);
				help.add(helpContent6);
				help.setVisible(true);
			}
		});

		JButton button_2 = new JButton("\u4FDD\u5B58");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(scrollPane, 0, 0, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup().addGap(2).addComponent(scrollPane_1,
										GroupLayout.PREFERRED_SIZE, 417, GroupLayout.PREFERRED_SIZE)))
				.addGap(6)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnNewButton))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(onlineNum)
										.addComponent(button_2)))
						.addComponent(button_1, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 311, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup().addGap(33)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
														.addComponent(label).addComponent(onlineNum))
												.addGap(13))
										.addGroup(gl_contentPane.createSequentialGroup()
												.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
														.addComponent(button_2).addComponent(btnNewButton))
												.addGap(38)))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(scrollPane_2,
										GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
								.addComponent(button, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		addWindowListener(new WindowAdapter() {// 防止直接点关闭后台线程一直异常
			public void windowClosing(WindowEvent e) {
				if (client != null) {
					client.Exit();
					dispose();
				}
			}
		});

		textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		// textArea_1.setCaretPosition(textArea.getText().length());BUG
		textArea_1.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (e.getKeyCode() == KeyEvent.VK_ENTER)) {// 快捷键发送
					String message = textArea_1.getText();
					client.send(message);// 客户端发送信息
					textArea_1.setText("");
					textArea.setCaretPosition(textArea.getText().length());// 控制光标下移
				}
			}
		});

		scrollPane_1.setViewportView(textArea_1);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		model = new DefaultListModel<>();
		jList = new JList<>(model);
		scrollPane_2.setViewportView(jList);

		jList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = jList.locationToIndex(e.getPoint());
					String obj = model.getElementAt(index);
					if (!obj.equals(user)) {
						textArea_1.append("%s:" + obj + "%s:");
					}
				}
			}
		});

		contentPane.setLayout(gl_contentPane);
		new Thread(new innerRoomThread()).start();

	}

	private class innerRoomThread implements Runnable {// 获取在线列表，原理和RegDialog，使用新的线程，但设置为1S接收一次在线数据，这样看来不断的使用新线程将是一件危险低效的事情
		// private final Socket listSocket;//这样将出现空指针BUG！！！
		// public innerRoomThread(Socket listSocket){
		// this.listSocket=listSocket;
		// }
		private BufferedReader receive;

		public void run() {
			try {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				System.out.println(listSocket.getInetAddress() + "正在获取此处的在线列表......");
				receive = new BufferedReader(new InputStreamReader(listSocket.getInputStream()));
				String info = null;
				String list[] = null;
				while ((info = receive.readLine()) != null) {
					list = info.split(":");
					// DefaultListModel<String> newModel=new
					// DefaultListModel<>();
					model.clear();
					for (String name : list) {
						model.add(model.size(), name);
					}
					jList.setModel(model);
					onlineNum.setText(list.length + "");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void save() throws IOException {
		FileDialog fileS = new FileDialog(this, "聊天记录", FileDialog.SAVE);
		fileS.setFile("chat.log");

		fileS.setVisible(true);
		String path = fileS.getDirectory();
		String filename = fileS.getFile();
		FileWriter filewriter = new FileWriter(new File(path, filename), true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		filewriter.write(dateFormat.format(System.currentTimeMillis()) + "\r\n");
		filewriter.write(textArea.getText());
		filewriter.close();
	}
}
