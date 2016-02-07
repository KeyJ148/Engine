package tow.net.client;

import tow.Global;
import tow.player.Bullet;

public class TCPSend{
	
	public void sendM2(){//���� �� ������� � �������
		Global.tcpControl.send("-2 ");
	}
	
	public void sendM1(){//���������� ��������� �����
		Global.tcpControl.send("-1 ");
	}
	
	public void send0(String str){
		Global.tcpControl.send("0 " + str);
	}
	
	public void send1(Bullet bull){//������ �������
		Global.tcpControl.send("1 " + Math.round(bull.getX()) + " " + Math.round(bull.getY()) + " " 
				+ bull.getDirection() + " " + bull.getSpeed() + " "
				+ bull.getClass().getName() + " " + Global.name + " " + Global.idNet);
	}
	
	public void send2(Bullet bull){//����������� ����
		Global.tcpControl.send("2 " + bull.getIdNet() + " " + Global.name);
	}
	
	public void send3(Bullet bull, String enemyName){//��������� ������
		Global.tcpControl.send("3 " + enemyName + " " + bull.getDamage());
	}
	
	public void send4(){//���� ������ ���������
		Global.tcpControl.send("4 " + Global.name);
	}
	
	public void send5(){//������������ ����� (��� ����������)
		Global.tcpControl.send("5 " + Global.name);
	}
	
	public void send10(String nameEnemy){//��������� ������ �� �����
		Global.tcpControl.send("10 " + nameEnemy);
	}
	
	public void send11(){//��������� ���� ������ ������ �������
		int red = Global.color.getRed();
		int green = Global.color.getGreen();
		int blue = Global.color.getBlue();
		Global.tcpControl.send("11 " + Global.name + " " + red + " " + green + " " + blue);
	}
	
	public void send13(int idBox){//� �������� ����
		Global.tcpControl.send("13 " + idBox);
	}
	
	public void send14(String nameArmor){//� ������ �����
		Global.tcpControl.send("14 " + Global.name + " " + nameArmor);
	}
	
	public void send15(String nameGun){//� ������ �����
		Global.tcpControl.send("15 " + Global.name + " " + nameGun);
	}
}