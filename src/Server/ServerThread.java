package com.Chat02.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/**
 * ����һ������Ƚ��ص��̡߳���������ϵͳ�շ��û�message����Ҫ�̣߳�������߳������е��ӵ�¼��֤���շ�message�Ĺ��ܣ�
 * ����߳̽���ÿһ��SocketClient�����е��̣߳���������ܻ����һЩС��BUG�����»��в��ԣ�
 * ����Ϊÿһ��SocketClient�˶Ե�¼��Ϣ���Լ��շ�message��
 * �˶���Ϣ�ķ�ʽ���Զ�ȡ�ı��ķ�ʽ��û��ʹ�����ݣ���Ϊ��̫��������ֻ��һ��С����
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
	private BufferedReader list = null;// ��ȡ�����û��б�
	private PrintWriter send = null;
	private boolean login = false;// �Ƿ���Ե�¼
	private String user;
	private String password;
	private String message = null;
	private boolean exit = false;// ���պͷ�����Ϣ�Ĺرձ�ʶ

	public ServerThread(Socket client, HashMap<String, ServerThread> userList2, JTextArea area) {
		this.client = client;
		this.userList = userList2;
		this.area = area;
	}

	public void stop() {
		try {
			this.exit = true;

			send.println("�������ѹر�\r\n");
			userList.remove(user);
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		try {
			InetAddress ip = client.getInetAddress();
			area.append(ip + " �������ӷ����......\r\n");
			receive = new BufferedReader(new InputStreamReader(client.getInputStream()));
			send = new PrintWriter(client.getOutputStream(), true);
			logIn();// ��¼��
			if (login) {
				userList.put(user, this);

				sendToAll("��Һã��������ˣ�лл��ӭ:-)");

				final Thread re = new Thread(new Runnable() {// ʹ���߳���ʵʱ��ȡ////////////////////
					public void run() {
						while (!exit) {
							try {

								message = receive.readLine();
								if (message.startsWith("%s:")) {
									sendToP(message);
								} else
									sendToAll(message);
								area.append(user + " ˵��" + message + "\r\n");
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
		while (true) {// û�����õ�¼����
			File file = new File("d:\\list.txt");
			if (!file.exists())
				file.createNewFile();
			list = new BufferedReader(new FileReader(file));////////////////////////////////
			user = receive.readLine();
			password = receive.readLine();
			if (user.length() < 0 || password.length() < 0) {
				area.append("���û���������Ϊ��\r\n");
				send.println("Blank");
				continue;
			}
			while ((info = list.readLine()) != null) {// ��list.txt�е���Ϣ�Ա�
				String str[] = info.split("::");
				if (user.equals(str[0]) && password.equals(str[1]) && !checkRepetition(user)) {
					area.append(user + "::��¼�ɹ�\r\n");
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
			} // �������ѭ���ڲ�����ÿ�αȽϲ��ɹ����ᷢ��һ��ʧ�ܣ�
			area.append(user + "::���Ե�¼\r\n");
		}
	}

	public String getUser() {
		return user;
	}

	public void exit() {// �˳��������
		try {
			exit = true;
			userList.remove(user);
			client.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean checkRepetition(String user) {// ����Ƿ��Ѿ�����
		if (userList.containsKey(user)) {
			return true;
		} else
			return false;
	}

	private void send(String user, String message) {
		send.println(user + " ˵��" + message);// Send����ɷ�������ʹ��
	}

	private void sendToP(String message) {
		String pri[] = message.split("%s:");

		Set<String> keySet = userList.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			ServerThread se = userList.get(it.next());
			if (se.user.equals(pri[1])) {
				se.send.println(this.user + "���Ķ���˵��" + pri[2]);
				send.println(this.user + "���Ķ�" + pri[1] + "˵��" + pri[2]);
			}
		}
	}

	public void sendToAll(String message) {// ������ ���͸�������
		Set<String> keySet = userList.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
			ServerThread ser = userList.get(it.next());
			if (message.length() > 0)
				ser.send(user, message);
		}

	}

}
