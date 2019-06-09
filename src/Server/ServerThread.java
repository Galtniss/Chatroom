package com.Chat02.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * 这是一个任务比较重的线程。它是整个系统收发用户message的主要线程，在这个线程它将承担从登录验证到收发message的功能，
 * 这个线程将是每一个SocketClient都独有的线程，在这里可能会存在一些小的BUG，等下会有测试，
 * 它将为每一个SocketClient核对登录信息，以及收发message，
 * 核对信息的方式是以读取文本的方式，没有使用数据，因为它太繁琐，这只是一个小程序。
 * 
 */
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JTextArea;

public class ServerThread implements Runnable {
	private Socket client = null;
	private HashMap<String, ServerThread> userList = null;
	private JTextArea area = null;
	private BufferedReader receive = null;
	private BufferedReader list = null;// 获取本地用户列表
	private PrintWriter send = null;
	private boolean login = false;// 是否可以登录
	private String user;
	private String password;
	private String message = null;
	private boolean exit = false;// 接收和发送信息的关闭标识

	public ServerThread(Socket client, HashMap<String, ServerThread> userList2, JTextArea area) {
		this.client = client;
		this.userList = userList2;
		this.area = area;
	}

	public void stop() {
		try {
			this.exit = true;

			send.println("服务器已关闭\r\n");
			userList.remove(user);
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {
			InetAddress ip = client.getInetAddress();
			area.append(ip + " 正在连接服务端......\r\n");
			receive = new BufferedReader(new InputStreamReader(client.getInputStream()));
			send = new PrintWriter(client.getOutputStream(), true);
			logIn();// 登录口
			if (login) {
				userList.put(user, this);

				sendToAll("大家好，我上线了！谢谢欢迎:-)");

				final Thread re = new Thread(new Runnable() {// 使用线程来实时读取////////////////////
					public void run() {
						while (!exit) {
							try {

								message = receive.readLine();
								if (message.startsWith("%s:")) {
									sendToP(message);
								} else
									sendToAll(message);
								area.append(user + " 说：" + message + "\r\n");
								if (message.equals("886")) {
									userList.remove(user);
									break;
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				re.start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logIn() throws IOException {
		String info = null;
		while (true) {// 没有设置登录限制
			File file = new File("d:\\list.txt");
			if (!file.exists())
				file.createNewFile();
			list = new BufferedReader(new FileReader(file));////////////////////////////////
			user = receive.readLine();
			password = receive.readLine();
			if (user.length() < 0 || password.length() < 0) {
				area.append("空用户名或密码为空\r\n");
				send.println("Blank");
				continue;
			}
			while ((info = list.readLine()) != null) {// 与list.txt中的信息对比
				String str[] = info.split("::");
				if (user.equals(str[0]) && password.equals(str[1]) && !checkRepetition(user)) {
					area.append(user + "::登录成功\r\n");
					login = true;
					break;
				}
			}
			if (login) {
				send.println("Success");
				return;
			} else {
				if (checkRepetition(user))
					send.println("Repetition");
				else
					send.println("Fialed");
			} // 如果放在循环内部，则每次比较不成功都会发送一次失败！
			area.append(user + "::尝试登录\r\n");
		}
	}

	public String getUser() {
		return user;
	}

	public void exit() {// 退出所需操作
		try {
			exit = true;
			userList.remove(user);
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean checkRepetition(String user) {// 检查是否已经在线
		if (userList.containsKey(user)) {
			return true;
		} else
			return false;
	}

	private void send(String user, String message) {
		send.println(user + " 说：" + message);// Send定义成方法方便使用
	}

	private void sendToP(String message) {
		String pri[] = message.split("%s:");

		Set<String> keySet = userList.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			ServerThread se = userList.get(it.next());
			if (se.user.equals(pri[1])) {
				se.send.println(this.user + "悄悄对你说：" + pri[2]);
				send.println(this.user + "悄悄对" + pri[1] + "说：" + pri[2]);
			}
		}
	}

	public void sendToAll(String message) {// 迭代器 发送给所有人
		Set<String> keySet = userList.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			ServerThread ser = userList.get(it.next());
			if (message.length() > 0)
				ser.send(user, message);
		}

	}

}
