package com.Chat02.Server;
/**
 * 这个类将是整个服务器的主线程，也就是说如果Server挂了，那么所有线程将无法继续完成监听和收发信息的任务，
 * 在这里，主线程将启动三个新的线程，他们的工作是开启信息收发线程，开启注册线程，开启在线列表的反馈线程，
 * 这三个线程将各司其职，
 * 在这里还有一个对于这三个线程的一个开关，目的是为了更方便的统一关闭线程，开更好的关闭服务器，做成一个方法，将会显得高效一些，
 * 但是目前看来可能还是会存在一些BUG导致线程的关闭异常。
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
	private boolean stop = false;// 终止线程
	private RegisterThread registerT;//注册线程
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
			area.append("服务器名称：" + ip + " 端口号：" + port + "\r\n");
			area.append("服务器已经启动>>>>>>\r\n");
			listT = new ListThread(userList);
			listT.start();
			int i=0;//记录线程个数
			while (!stop) {
				Socket client = server.accept();//处于阻塞状态，直接关闭将SocketServer将抛出异常
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
		area.append("注册线程已经启动，默认端口：34343 ......\r\n");
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
				area.append("服务器关闭......\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
