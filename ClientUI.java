package com.Chat02.Client;
/**
 * ��������潫ʵ�ֵ�¼���������ӷ��������ܣ�
 * ����Ҫ���ڷ�����ȥ�����ӣ�Ϊ��ȷ������Ŀ����ԣ�ÿһ����ť�������������ǰ�ᣬ
 * ͨ�������¼���û���Ϣ���͵�����˽��к˶ԣ�Ȼ����ݷ���˷�������Ϣ���жϵ�ǰ�û��ܷ���룬
 * ���������Ϣ��ʾ���ܽ��룬�ͻ������Ϣ���ж�����һ�����ˣ���һ��ʹ��switch�����Ч�ʣ���Ϊѡ����Ҫ��if else�ж������Ч
 * ͬʱ������滹ʵ������RegDialogUIע��������ת������Ĳ�����������RegDialogUI
 * 
 */
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class ClientUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8542876911761120960L;
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private Socket client;
	private JTextField textField_1;
	private JTextField textField_2;
	private RegDialogUI regDialog;
	private String ip = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientUI frame = new ClientUI();
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
	public ClientUI() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ChatRoomUI.class.getResource("/com/Chat02/Client/Chat_ico.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 524, 206);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setResizable(false);
		setTitle("Client��¼����");
		JLabel label = new JLabel("\u7528\u6237\u540D\uFF1A");

		textField = new JTextField();
		textField.setColumns(10);

		JButton button = new JButton("\u767B\u5F55");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (textField.getText().length() == 0 || passwordField.getPassword().length == 0) {
					Dialog errorLink = new Dialog(ClientUI.this);
					errorLink.setLayout(new FlowLayout());
					errorLink.setTitle("�������");
					errorLink.add(new Label("�������û���������"));
					errorLink.setBounds(600, 200, 200, 100);
					JButton liButton = new JButton("ȷ��");
					errorLink.add(liButton);
					liButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							errorLink.dispose();
						}
					});
					errorLink.setVisible(true);
				} else {
					if (client != null) {
						login(textField.getText(), new String(passwordField.getPassword()));// ��¼����
					} else {
						Dialog errorLink = new Dialog(ClientUI.this);
						errorLink.setLayout(new FlowLayout());
						errorLink.setTitle("���Ӵ���");
						errorLink.add(new Label("��û�����ӷ��������������ӣ�"));
						errorLink.setBounds(600, 200, 200, 100);
						JButton liButton = new JButton("ȷ��");
						errorLink.add(liButton);
						liButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								errorLink.dispose();
							}
						});
						errorLink.setVisible(true);
					}

				}
			}

		});

		passwordField = new JPasswordField();

		JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");

		JButton button_1 = new JButton("\u6CE8\u518C");
		button_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (client != null)
					register(client);// ע�����
				else {
					Dialog errorLink = new Dialog(ClientUI.this);
					errorLink.setLayout(new FlowLayout());
					errorLink.setTitle("���Ӵ���");
					errorLink.add(new Label("��û�����ӷ��������������ӣ�"));
					errorLink.setBounds(600, 200, 200, 100);
					JButton liButton = new JButton("ȷ��");
					errorLink.add(liButton);
					liButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							errorLink.dispose();
						}
					});
					errorLink.setVisible(true);
				}

			}
		});

		textField_1 = new JTextField();
		textField_1.setText("44444");
		textField_1.setColumns(10);

		JLabel label_2 = new JLabel("\u7AEF\u53E3\uFF1A");

		JLabel label_3 = new JLabel("\u670D\u52A1\u5668\uFF1A");

		textField_2 = new JTextField();
		textField_2.setText("192.168.1.100");
		textField_2.setColumns(10);

		JButton button_2 = new JButton("\u8FDE\u63A5");
		button_2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String ip = textField_2.getText();// ��ȡIP �Ͷ˿ڽ�������
				int port = Integer.parseInt(textField_1.getText());
				try {
					client = new Socket(ip, port);
					ClientUI.this.ip = ip;
					setTitle(getTitle() + "...���ӳɹ�");
					button_2.setEnabled(false);
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					JDialog error = new JDialog(ClientUI.this, "���Ӵ���", true);
					error.getContentPane().add(new Label("���������Ϣ����,�������δ����������������"));
					error.setBounds(400, 200, 200, 100);
					error.setVisible(true);
					e1.printStackTrace();
				}
			}
		});

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(label_3)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 122,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(label_2).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(label_1)
										.addComponent(label))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(textField)
										.addComponent(passwordField, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))))
				.addGap(42).addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(button_1)
						.addComponent(button).addComponent(button_2)).addGap(80)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(label_3)
										.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 22,
												GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addComponent(button_2))
				.addGap(37)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(label)
						.addComponent(button).addComponent(textField, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(19)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(label_1)
						.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_1)).addContainerGap(22, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);

	}

	public void login(String name, String password) {
		try {
			PrintWriter send = new PrintWriter(client.getOutputStream(), true);
			BufferedReader receive = new BufferedReader(new InputStreamReader(client.getInputStream()));
			send.println(name);
			send.println(password);
			String info = receive.readLine();
			switch (info) {// ���ݷ�����Ϣ�жϵ�¼���
			case "Success"://��¼�ɹ�
				new ChatRoomUI(client, name ,ip).setVisible(true);
				dispose();
				break;
			case "Fialed":
				Dialog error = new Dialog(this, "��¼����", true);// ʹ��Jdialog�����ֽ�������
				error.add(new Label("    �ʺŻ�������������µ�¼��"));
				error.setBounds(600, 200, 190, 100);
				error.setLayout(new FlowLayout());
				JButton erButton = new JButton("ȷ��");
				error.add(erButton);
				erButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						error.dispose();
					}
				});
				error.setVisible(true);
				break;
			case "Repetition":
				Dialog reError = new Dialog(this, "��¼����", true);
				reError.add(new Label("   ����������ʺ��Ѿ����ߣ��볢�������ʺţ�"));
				reError.setBounds(600, 200, 270, 100);
				reError.setLayout(new FlowLayout());
				JButton reButton = new JButton("ȷ��");
				reError.add(reButton);
				reButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						reError.dispose();
					}
				});
				reError.setVisible(true);
				break;
			case "Blank":
				Dialog blankError = new Dialog(this, "��¼����", true);
				blankError.add(new Label("�������û��������룡"));
				blankError.setBounds(600, 200, 180, 100);
				blankError.setLayout(new FlowLayout());
				JButton blButton = new JButton("ȷ��");
				blankError.add(blButton);
				blButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						blankError.dispose();
					}
				});
				blankError.setVisible(true);
				break;
			default:
				break;
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void register(Socket client) {//Dialogע�������

		regDialog = new RegDialogUI(ip);
		regDialog.setModal(true);
		regDialog.setVisible(true);
	}
}
