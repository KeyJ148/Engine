package main;

import java.awt.Color;
import java.util.ArrayList;

import main.image.Animation;
import main.image.DepthVector;
import main.image.Sprite;
import main.net.ClientNetSend;
import main.net.ClientNetThread;
import main.net.LinkCS;
import main.net.Ping;
import main.obj.ObjLight;
import main.player.Player;
import main.player.enemy.Enemy;
import main.player.enemy.EnemyBullet;
import main.setting.SettingStorage;

public class Global {
	
	public static Game game; //������� ������� �����
	
	public static ArrayList<ObjLight> obj; //������ �� ����� ���������
	public static ArrayList<DepthVector> depth; //������ � DepthVector
	
	public static Player player; //������� �����
	public static ClientNetSend clientSend; //���� �������� ������ �� ������
	public static ClientNetThread clientThread; //���� ���������� ������ � �������
	public static LinkCS linkCS; //����� ����� ������� � �������
	
	public static Ping pingCheck;//������ ��� �������� �����
	public static SettingStorage setting;//������ �������� �������� ���������
	
	public static long id = 1;//����. ����� ���������� �������
	public static long idNet = 1; //id ������� ������������ � ���������� ��-�� �������� ������  
	
	public static Enemy[] enemy; //������ ���� �����������
	public static ArrayList<EnemyBullet> enemyBullet; //������ ���� �������� ����������� (EnemyBullet)
	
	public static String name;//��� ������
	public static Color color;//���� ������
	
	public static int heightMap;
	public static int widthMap;//������ �����
	public static int peopleMax;//���-�� ������� �� �����
	
	public static double cameraX;
	public static double cameraY;
	public static double cameraXView;
	public static double cameraYView;
	
	public static Sprite background;
	
	public static Sprite road_g;
	public static Sprite road_g_fork;
	public static Sprite road_g_turn;
	public static Sprite road_g_inter;
	public static Sprite road_a;
	public static Sprite road_a_g;
	public static Sprite road_a_fork;
	public static Sprite road_a_inter_big;
	public static Sprite b_default;
	public static Sprite b_mortar;
	public static Sprite b_massgun;
	public static Sprite b_massgun_small;
	public static Sprite b_gunfury;
	public static Sprite defaultgun;
	public static Sprite doublegun;
	public static Sprite gunfury;
	public static Sprite mortar;
	public static Sprite rocketgun;
	public static Sprite massgun;
	public static Sprite player_color;
	public static Sprite home1;
	public static Sprite home2;
	public static Sprite home3;
	public static Sprite home4;
	public static Sprite home5;
	public static Sprite home6;
	public static Sprite home7;
	public static Sprite home8;
	public static Sprite home9;
	public static Sprite home10;
	public static Sprite home11;
	public static Sprite home12;
	public static Sprite home13;
	public static Sprite grayrock1;
	public static Sprite grayrock2;
	public static Sprite grayrock3;
	public static Sprite grayrock4;
	public static Sprite grayrock5;
	public static Sprite grayrock6;
	public static Sprite tree;
	public static Sprite error;
	public static Sprite player_sys;
	
	public static Animation c_default;
	
	//�������� ������� �� ������� �� id
	public static void delObj(long id){
		for (int i = 0; i<obj.size(); i++){
			if (obj.get(i).getId() == id){
				obj.remove(i);
			}
		}
	}
	
	//����������� ������� �� ������� �� id
	public static ObjLight getObj(long id){
		for (int i = 0; i<obj.size(); i++){
			if (obj.get(i).getId() == id){
				return obj.get(i);
			}
		}
		return null;
	}
	
	//����� ��������� � ������
	public static void error(String s){
		System.out.println("[ERROR] " + s);
	}
}

