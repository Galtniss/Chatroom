package com.Chat02.Server;
/**
 * ����ཫ�����������������̣߳�Ҳ����˵���Server���ˣ���ô�����߳̽��޷�������ɼ������շ���Ϣ������
 * ��������߳̽����������µ��̣߳����ǵĹ����ǿ�����Ϣ�շ��̣߳�����ע���̣߳����������б�ķ����̣߳�
 * �������߳̽���˾��ְ��
 * �����ﻹ��һ�������������̵߳�һ�����أ�Ŀ����Ϊ�˸������ͳһ�ر��̣߳������õĹرշ�����������һ�������������Եø�ЧһЩ��
 * ����Ŀǰ�������ܻ��ǻ����һЩBUG�����̵߳Ĺر��쳣��
 */
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JTextArea;

public class Server extends Thread {
	private int port = 44444;
	private JTextArea area;
	private ServerSocket server;
	private boolean stop = false;// ��ֹ�߳�
	private RegisterThread registerT;//ע���߳�
	private HashMap<String, ServerThread> userList = new HashMap<>();
	private ListThread listT;

	public Server(int port, JTextArea textArea) {
		this.port = port;
		this.area = textArea;
		
	}

	public void run() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		try {
			server = new ServerSocket(port, 50);
			register(area);
			area.append("���������ƣ�" + ip + " �˿ںţ�" + port + "\r\n");
			area.append("�������Ѿ�����>>>>>>\r\n");
			listT = new ListThread(userList);
			listT.start();
			int i=0;//��¼�̸߳���
			while (!stop) {
				Socket client = server.accept();//��������״̬��ֱ�ӹرս�SocketServer���׳��쳣
				new Thread(new ServerThread(client, userList, area),"ClientThread"+(i++)).start();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void register(JTextArea area) throws IOException, InterruptedException{
		registerT=new RegisterThread(area);
		Thread.sleep(10);
		registerT.start();
		area.append("ע���߳��Ѿ�������Ĭ�϶˿ڣ�34343 ......\r\n");
	}
	
	public void exit() {
		try {
			if (server != null) {
				this.stop = true;
				this.interrupt();
				registerT.interrupt();
				listT.interrupt();
				server.close();
				Set<String> keySet = userList.keySet();
				for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
					userList.get(it.next()).stop();
				}
				area.append("�������ر�......\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
