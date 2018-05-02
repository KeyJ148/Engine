package engine.net.server.senders;

import engine.io.Logger;
import engine.net.server.GameServer;

import java.io.IOException;

public class ServerSendTCP {

    public static void send(int id, int type, String str){
        try {
            synchronized (GameServer.connects[id].out) {
                GameServer.connects[id].out.writeUTF(type + " " + str);
                GameServer.connects[id].out.flush();
            }
            GameServer.connects[id].numberSend++; //���-�� ������������ �������
        } catch (IOException e) {
            if (!GameServer.connects[id].disconnect) Logger.print("Send message failed", Logger.Type.SERVER_ERROR);
        }
    }

    public static void sendAllExceptId(int id, int type, String str){
        for(int i = 0; i< GameServer.peopleMax; i++){//���������� ��������� ����
            if (i != id){//����� ������, ����������� ���������
                send(i, type, str);
            }
        }
    }

    public static void sendAll(int type, String str){
        for(int i = 0; i< GameServer.peopleMax; i++){//���������� ��������� ����
            send(i, type, str);
        }
    }

}
