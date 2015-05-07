package main.login;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Label;
import java.awt.TextField;

import main.ConfigReader;
import main.WindowMain;


@SuppressWarnings("serial")
public class LoginWindow extends Dialog{
	
	public final int horFrame = 300;
	public final int vertFrame = 150;
	public final String pathSetting = "setting.properties";
	
	public String tf1Default = "";//ip
	public String tf2Default = "";//����
	public String tf3Default = "";//���
	
	public LoginWindow(WindowMain frame, String s, int hor, int vert){
		super(frame,s);
		setLayout(null);
		
		ConfigReader cr = new ConfigReader(pathSetting);
		tf1Default = cr.find("ip");
		tf2Default = cr.find("port");
		tf3Default = cr.find("nickname");
		cr.close();
		
		Label L1 = new Label("Host ip:", Label.RIGHT);
		L1.setBounds(20, 30, 70, 25); 
		add(L1);
		
		Label L2 = new Label("You name:", Label.RIGHT);
		L2.setBounds(20, 60, 70, 25);
		add(L2);
		
		Label L3 = new Label(":", Label.CENTER);
		L3.setBounds(200, 30, 10, 25);
		add(L3);
		
		TextField tf1 = new TextField(tf1Default, 30); 
		tf1.setBounds(100, 30, 100, 25);
		add(tf1);
		
		TextField tf2 = new TextField(tf2Default, 30); 
		tf2.setBounds(210, 30, 50, 25);
		add(tf2); 
		
		TextField tf3 = new TextField(tf3Default,30); 
		tf3.setBounds(100, 60, 160, 25);
		add(tf3); 
		
		Button b1 = new Button("Enter"); 
		b1.setBounds(100, 100, 100, 30);
		add(b1);
		
		LoginListener ll = new LoginListener(tf1,tf2,tf3,frame);
		tf1.addActionListener(ll);
		tf2.addActionListener(ll);
		tf3.addActionListener(ll);
		b1.addActionListener(ll);
		
		setBounds(hor/2-horFrame/2,vert/2-vertFrame/2,horFrame,vertFrame);
		setVisible(true);
	}
	
}