package tow;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;

import org.newdawn.slick.Color;

import tow.cycle.Game;
import tow.input.KeyboardHandler;
import tow.input.MouseHandler;
import tow.map.MapControl;
import tow.net.client.Ping;
import tow.net.client.TCPControl;
import tow.net.client.TCPMapLoader;
import tow.net.client.TCPRead;
import tow.net.client.TCPSend;
import tow.obj.ObjLight;
import tow.player.Player;
import tow.player.enemy.Enemy;
import tow.player.enemy.EnemyBullet;
import tow.setting.SettingStorage;

public class Global {
	
	public static Game game; //������� ������� �����
	public static JFrame mainFrame;//������� ����
	public static MouseHandler mouseHandler;//��������� ����
	public static KeyboardHandler keyboardHandler;//��������� ����������
	
	public static Vector<ObjLight> obj; //������ �� ����� ���������
	public static MapControl mapControl; //������ �� ����� ������� � ���������
	
	public static Player player; //������� �����
	
	public static TCPSend tcpSend; //�������� ������ �� ������
	public static TCPRead tcpRead; //���� ���������� ������ � �������
	public static TCPControl tcpControl; //������ ��������� � ������ ����������
	public static TCPMapLoader tcpMapLoader; //��������� ����� � ��������� � �������
	public static Ping pingCheck;//������ ��� �������� �����
	public static String ip;//ip �������
	public static int port;//���� �������
	
	public static SettingStorage setting;//������ �������� �������� ���������
	
	public static long id = 0;//����. ����� ���������� �������
	public static long idNet = 1; //id ������� ������������ � ���������� ��-�� �������� ������  
	
	public static Enemy[] enemy; //������ ���� �����������
	public static ArrayList<EnemyBullet> enemyBullet; //������ ���� �������� ����������� (EnemyBullet)
	
	public static String name;//��� ������
	public static Color color = new Color((int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));//���� ������
	
	public static int heightMap;
	public static int widthMap;//������ �����
	public static int peopleMax;//���-�� ������� �� �����
	
	public static double cameraX;
	public static double cameraY;
	public static double cameraXView;
	public static double cameraYView;
	
	//��������� ������ �� ��������
	public static String parsString(String s,int numFind){
		int numWord = 0;
		int numSpace = -1;
		int k = 0;
		do{
			if (s.charAt(k) == ' '){
				numWord++;
				if (numWord != numFind){
					numSpace = k;
				} else {
					return s.substring(numSpace+1,k);
				}
			}
			if (k == s.length()-1){
				return s.substring(numSpace+1,k+1);
			}
			k++;
		}while(true);
	}
	
	//���������� ������� � ������ �� id
	public static void addObj(ObjLight objLight){
		obj.add((int) objLight.getId(), objLight);
		mapControl.add(objLight);
	}
	
	//�������� ������� �� ������� �� id
	public static void delObj(long id){
		mapControl.del((int) id);//������������ Global.obj, ��� ��� ������ ���� ������
		obj.set((int) id, null);
	}
	
	//����������� ������� �� ������� �� id
	public static ObjLight getObj(long id){
		return obj.get((int) id);
	}
	
	//����������� ���-�� ��������
	public static int getSize(){
		return obj.size();
	}
	
	//����� ��������� � ������
	public static void error(String s){
		System.out.println("[ERROR] " + s);
	}
	
	//����� ��������������� ���������
	public static void p(String s){
		System.out.println("[INFO] " + s);
	}
	
	public static void p(){
		Global.p("ALARM");
	}
	
	//����� ���������  ����
	public static PrintWriter out;
	public static void log(String s){
		if (out == null){
			try {
				out = new PrintWriter(new FileWriter("res/log.txt"));
			} catch (IOException e) {
				Global.error("Write log file");
			}
		}
		out.println(s);
	}
}

