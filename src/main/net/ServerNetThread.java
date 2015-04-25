package main.net;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.GameServer;

public class ServerNetThread extends Thread{
	
	public String name;
	public boolean mapDownAll = false;//��� �� ������� �����?
	
	private GameServer gameServer;
	
	private int id; //����� ���������� � ������� � gameServer
	private int peopleMax;
	
	public ServerNetThread(GameServer gameServer, int id, int peopleMax) throws IOException{

		this.gameServer = gameServer;
		this.id = id;
		this.peopleMax = peopleMax;
		
		start();
	}
	
	public void run(){
		//������� ����� � ������
		String pathFull = this.gameServer.pathFull;
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(pathFull));
			String s;
			while (true){ 
				s = fileReader.readLine();
				if (s == null){
					break;
				}
				this.gameServer.out[id].writeUTF(s);
			}
			this.gameServer.out[id].writeUTF("-1 ");
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
				
		//���������� ����� ������ � �������� ������
		try{
			this.name = this.gameServer.in[id].readUTF();
			System.out.println("Nickname: " + name + ".");
			this.gameServer.out[id].writeUTF(this.gameServer.tankX[id] + " " + this.gameServer.tankY[id]);
			this.gameServer.out[id].writeUTF(this.peopleMax + " ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.gameServer.connect[id] = true;
		do{
		}while(!mapDownAll);
		
		conMessTake();
	}

	public void conMessTake(){
		//���������� ����� ������� (�� TCP)
		//������ ����� ����������� ���� �������
		String str;
		try{
			while (true){
				str = this.gameServer.in[id].readUTF();
				synchronized(this.gameServer.messagePack[id]) {//������ �� ������������� ������ � ��������
					this.gameServer.messagePack[id].add(str);
				}
			}
		} catch (IOException e){
			System.out.println("[ERROR] Take message!");
			if ((gameServer.disconnect+1) == peopleMax){
				System.out.println("All user disconnect!");
				System.exit(0);
			} else {
				gameServer.disconnect++;
			}
		}
	}
}