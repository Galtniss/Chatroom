package com.Chat02.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread {
	private Socket client = null;
	private BufferedReader receive = null;
	private PrintWriter send = null;
	private boolean exit = false;
	private JTextArea reText;
	private ChatRoomUI room;

	public ClientThread() {

	}

	public ClientThread(Socket client, JTextArea reText, ChatRoomUI room) {
		this.client = client;
		this.reText = reText;
		this.room = room;
	}

	public void run() {
		try {
			receive = new BufferedReader(new InputStreamReader(client.getInputStream()));// ��ȡSocket��������
			send = new PrintWriter(client.getOutputStream(), true);// �����Զ�ˢ��

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (!exit) {// �����̣߳��ý��տ�ͣ�Ľ�����Ϣ
			try {
				String message = receive.readLine();
				if (message.equals("�������ѹر�")) {
					new ClientUI().setVisible(true);
					room.dispose();
					break;
				}
				if (message.length() > 0) {
					reText.append(message + "\r\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void send(String message) {
		if (message.length() > 0)
			send.println(message);

	}

	public void Exit() {// �˳��¼�
		try {
			if (client != null) {
				send("886");
				client.close();
				this.exit = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

