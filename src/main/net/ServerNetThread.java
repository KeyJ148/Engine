package main.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.GameServer;
import main.Global;

public class ServerNetThread extends Thread{
	
	public String name;//��� �������
	public boolean mapDownAll = false;//�����. ��� �� ��������� �����?
	
	
	private GameServer gameServer;
	private int id; //����� ���������� � ������� � gameServer
	
	public ServerNetThread(GameServer gameServer, int id) throws IOException{

		this.gameServer = gameServer;
		this.id = id;
		
		start();
	}
	
	public void run(){
		mapLoading();
		receivingData();
	}
	
	public void mapLoading(){
		//���� ��� ������ ������������� ����� -- ������� ��������� ��������
		gameServer.checkMapDownload();
		
		do{
			try {
				Thread.sleep(0,100);
			} catch (InterruptedException e) {}
		}while(!gameServer.tankGenComplite); //��� ���� ���������� ����� � ������������� ������� ������
		
		//�������� �����
		System.out.println("Loading map start.");
		String pathFull = this.gameServer.pathFull;
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(pathFull));
			String s;
			
			this.gameServer.out[id].writeUTF("6 ");//������ �������� �����
			while (true){ 
				s = fileReader.readLine();
				if (s == null){
					break;
				}
				writeMap(s);
			}
			this.gameServer.out[id].writeUTF("8 ");//��������� �������� �����
			fileReader.close();
		} catch (IOException e) {
			gameServer.error("Load map");
		}
		System.out.println("Loading map end.");		
				
		//���������� ����� ������
		this.name = downloadNick();
		System.out.println("Nickname: " + name);
		
		//�������� ������� ������
		writeMap(this.gameServer.tankX[id] + " " + this.gameServer.tankY[id]);
		writeMap(gameServer.peopleMax + " ");
		
		this.gameServer.connect[id] = true;
		do{
			try {
				Thread.sleep(0,100);
			} catch (InterruptedException e) {}
		}while(!mapDownAll);//��� ���� ��� ����� ������ � ���
	}
	
	public String downloadNick(){
		try {
			String s;
			do {
				s = this.gameServer.in[id].readUTF();
			}while(Integer.parseInt(Global.linkCS.parsString(s,1)) != -3);
			return s.substring(3);
		} catch (IOException e) {
			gameServer.error("Method for download nickname");
			return "ERROR LOAD NAME";
		}
	}

	private void writeMap(String s) {
		try {
			this.gameServer.out[id].writeUTF("7 " + s);
		} catch (IOException e) {
			gameServer.error("Method for writeMap");
		}
	}

	public void receivingData(){
		//���������� ����� ������� (�� TCP)
		//������ ����� ����������� ���� �������
		String str;
		try{
			while (true){
				try{
					str = this.gameServer.in[id].readUTF();
					if (Integer.parseInt(Global.linkCS.parsString(str, 1)) >= 0){//���� ��������� ��� �������
						synchronized(this.gameServer.messagePack[id]) {//������ �� ������������� ������ � ��������
							this.gameServer.messagePack[id].add(str);
						}
					} else {//���� ��������� ��� �������
						switch (Integer.parseInt(Global.linkCS.parsString(str, 1))){
							case -1: take1(); break;
							case -2: take2(); break;
						}
					}
				} catch(NumberFormatException e){
					gameServer.error("Take message not found type");
				}
			}
		} catch (IOException e){
			gameServer.error("Take message");
			if ((gameServer.disconnect+1) == gameServer.peopleMax){
				System.out.println("All user disconnect!");
				System.exit(0);
			} else {
				gameServer.disconnect++;
			}
		}
	}
	
	public void take1(){//������ ����� � ����� ����� (�������)
		gameServer.messagePack[id].clear();
		mapLoading();
	}
	
	public void take2(){//������ ������� ������
		try {
			synchronized(gameServer.out[id]){
				gameServer.out[id].flush();
				gameServer.out[id].writeUTF("9 ");
			}
		} catch (IOException e) {
			gameServer.error("Check ping");
		}
	}
	
}