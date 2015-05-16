package main.net;

import java.io.DataOutputStream;
import java.io.IOException;

import main.Game;
import main.Global;
import main.player.Bullet;

public class ClientNetSend extends Thread{
	
	public DataOutputStream out;
	public Game game;
	
	public int sizeData = 0; //bytes 
	
	public ClientNetSend(DataOutputStream out, Game game){
		this.out = out;
		this.game = game;
		Global.clientSend = this;
	}
	
	public void sendData(String str){
		try{
			sizeData += str.length()*2;
			out.flush();
			out.writeUTF(str);
		} catch (IOException e){
			Global.error("Send internet message");
			System.exit(0);
		}
	}
	
	public void sendM2(){//���� �� ������� � �������
		sendData("-2 ");
	}
	
	public void sendM1(){//���������� ��������� �����
		sendData("-1 ");
	}
	
	public void send1(Bullet bull){//������ �������
		sendData("1 " + Math.round(bull.getX()) + " " + Math.round(bull.getY()) + " " 
				+ bull.getDirection() + " " + bull.getSpeed() + " "
				+ bull.getClass().getName() + " " + game.name + " " + Global.idNet);
	}
	
	public void send2(Bullet bull){//����������� ����
		sendData("2 " + bull.getIdNet() + " " + game.name);
	}
	
	public void send3(Bullet bull, String enemyName){//��������� ������
		sendData("3 " + enemyName + " " + bull.getDamage());
	}
	
	public void send4(){//���� ������ ���������
		sendData("4 " + game.name);
	}
	
	public void send5(){//������������ ����� (��� ����������)
		sendData("5 " + game.name);
	}
}