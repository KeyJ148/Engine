package main.net;

import java.io.IOException;

import main.GameServer;

public class ServerSend extends Thread {
	
	public GameServer gameServer;
	public int id;

	public ServerSend(GameServer gameServer,int id){
		this.gameServer = gameServer;
		this.id = id;
		start();
	}
	
	public void run(){
		try{
			String str;
			long t,numberSend;
			t = System.currentTimeMillis(); //������� �������
			numberSend = 0; //������� �������
			while (true){
				
				if (System.currentTimeMillis() > t+1000){ //������� �������
					t = System.currentTimeMillis(); //������� �������
					System.out.println("ID: " + id + "    MPS:" + numberSend); //������� �������
					numberSend = 0; //������� �������
				} //������� �������
				
				synchronized(gameServer.messagePack[id]) {//������ �� ������������� ������ � ��������
					if (gameServer.messagePack[id].haveMessage()){//���� � ������ ������� ���������				
						str = (String) gameServer.messagePack[id].get();//������ ���������
						for(int j=0;j<gameServer.peopleMax;j++){//���������� ��������� ����
							if (j != id){//����� ������, ����������� ���������
								synchronized(gameServer.out[j]){
									gameServer.out[j].flush();
									gameServer.out[j].writeUTF(str);
								}
								numberSend++; //������� �������
							}
						}							
					}
				}
			}
		} catch (IOException e){
			System.out.println("[ERROR] Send message!");
		}
	}
}
