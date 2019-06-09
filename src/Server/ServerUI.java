package com.Chat02.Server;

/**
 * 这里是ServerUI,它的将实现，服务端的界面，以及开启和收发功能，
 * 这是整个小程序里唯一一块净土，
 * 简洁的代码，目的性极强的两个功能按钮，造就了这个服务端只能用来显示当前的运行信息，
 * 在稍后将实现保存服务端运行信息的功能
 */
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class ServerUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1780849261675807781L;
	private JPanel contentPane;
	private JTextField textField;
	private Server server = null;
	private JButton button_1;
	private JButton button;
	private JLabel label;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JButton button_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerUI frame = new ServerUI();
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
	public ServerUI() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(ServerUI.class.getResource("/com/Chat02/Server/Chat_ico.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 424, 440);
		setTitle("Server");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setContentPane(contentPane);

		label = new JLabel("\u7AEF\u53E3\uFF1A");

		textField = new JTextField();
		textField.setText("44444");
		textField.setColumns(10);
		button_1 = new JButton("\u5173\u95ED\u670D\u52A1");
		button_1.setEnabled(false);
		button = new JButton("\u5F00\u542F\u670D\u52A1");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});

		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopServer();
			}
		});

		scrollPane = new JScrollPane();

		button_2 = new JButton("\u4FDD\u5B58\u8FD0\u884C\u65E5\u5FD7");
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
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addComponent(label)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, 50,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(button)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_1)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_2))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(label)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(button).addComponent(button_1).addComponent(button_2))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)));

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setCaretPosition(textArea.getText().length());
		textArea.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				textArea.setSelectionStart(textArea.getText().length());// 设置自动换行到最后一行，有BUG
			}
		});
		scrollPane.setViewportView(textArea);// SrollPane画布会让TextArea具有滚动条效果

		contentPane.setLayout(gl_contentPane);
	}

	public void startServer() {
		if (server == null) {
			server = new Server(Integer.parseInt(textField.getText()), textArea);
			server.start();
			button.setEnabled(false);
			button_1.setEnabled(true);
		}
	}

	public void stopServer() {
		if (server != null) {
			server.exit();
			server = null;
			button.setEnabled(true);
			button_1.setEnabled(false);

		}
	}

	public void save() throws IOException {
		FileDialog fileS = new FileDialog(this, "聊天记录", FileDialog.SAVE);
		fileS.setFile("Service.log");

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
