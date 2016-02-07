package tow.net.server;

import java.io.IOException;

import tow.Global;

public class ServerSend extends Thread {
	
	public GameServer gameServer;
	public int id;
	
	public volatile int x = 0;//������� �����
	public volatile int y = 0;//��������� �������� ����������
	
	public int numberSend = 0; //����� �������

	public ServerSend(GameServer gameServer,int id){
		this.gameServer = gameServer;
		this.id = id;
		start();
	}
	
	public void run(){
		try{
			String str;
			boolean haveMessage;
			while (true){
				
				haveMessage = false;
				str = "synchronized ServerSend";
				
				synchronized(gameServer.messagePack[id]) {//������ �� ������������� ������ � ��������
					if (gameServer.messagePack[id].haveMessage()){//���� � ������ ������� ���������				
						str = (String) gameServer.messagePack[id].get();//������ ���������
						haveMessage = true;
						
						if (Integer.parseInt(Global.parsString(str, 1)) == 0){
							x = Integer.parseInt(Global.parsString(str, 2));
							y = Integer.parseInt(Global.parsString(str, 3));
						}
					}
				}
				
				if (haveMessage){
					for(int j=0;j<gameServer.peopleMax;j++){//���������� ��������� ����
						if (j != id){//����� ������, ����������� ���������
							synchronized(gameServer.out[j]){
								gameServer.out[j].flush();
								gameServer.out[j].writeUTF(str);
							}
							numberSend++; //���-�� ������������ �������
						}
					}							
				} else {
					if (!gameServer.maxPower)
						try {Thread.sleep(0,1);} catch (InterruptedException e) {}
				}
					
			}
		} catch (IOException e){
			GameServer.error("Send message");
		}
	}
}
