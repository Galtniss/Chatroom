package com.Chat02.Server;

/**
 * ���ں�̨���ϵ�����ÿ�����µĿͻ��˴�ʱ���ͻῪ���µ��߳�Ϊ�����ע�����
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;

public class RegisterThread extends Thread {
	private ServerSocket register;
	private JTextArea area;

	public RegisterThread() {
		try {
			this.register = new ServerSocket(34343);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RegisterThread(JTextArea area) {
		this();
		this.area = area;
	}

	@Override
	public void run() {
		while (true) {
			try {
				area.append("�ȴ��ͻ���ע��\r\n");
				Socket reC = register.accept();
				area.append(reC.getInetAddress().toString());
				area.append(reC.getLocalAddress() + "���ڳ���ע��\r\n");
				new Thread(new Reg(reC)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class Reg implements Runnable {
		private Socket reC;
		private BufferedReader receive;
		private BufferedReader list;
		private PrintWriter send;
		private boolean exist = false;
		private PrintWriter writerList;

		public Reg(Socket reC) {
			this.reC = reC;
		}

		@Override
		public void run() {
			try {
				receive = new BufferedReader(new InputStreamReader(reC.getInputStream()));
				send = new PrintWriter(reC.getOutputStream(), true);
				area.append("���ڶ�ȡ�û��б�......\r\n");
				File file = new File("d:\\list.txt");////////////////////////////////////////////////////////
				if (!file.exists())
					file.createNewFile();
				area.append("��ȡ���û��б�......\r\n");
				while (true) {
					synchronized (file) {// ��סע���ļ�
						list = new BufferedReader(new FileReader(file));
						area.append("���ڻ�ȡע����Ϣ......");

						String name = receive.readLine();
						String password = receive.readLine();
						String info = null;
						while ((info = list.readLine()) != null) {// ��list.txt�е���Ϣ�Ա�
							String str[] = info.split("::");
							if (str[0].equals(name)) {
								send.println("Exist");
								exist = true;
								area.append(name + "�û��Ѿ�����!\r\n");
								break;
							}
						}
						area.append(name + "::" + password + "\r\n");
						if (!exist) {
							area.append("����д������.....\r\n");
							writerList = new PrintWriter(new FileOutputStream("d:\\list.txt", true), true);//////////////////////////////////
							writerList.println(name + "::" + password);
							send.println("RegisterSuccess");
							area.append("д�����......\r\n");
						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
