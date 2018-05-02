package engine.net.server.senders;

import engine.io.Logger;
import engine.net.server.GameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class ServerSendUDP {

    public static void send(int id, int type, String str){
        try {
            byte[] data = (type + " " + str).getBytes();
            InetAddress addr = InetAddress.getByName(GameServer.connects[id].ipRemote);
            int port = GameServer.connects[id].portUDP;

            DatagramPacket packet = new DatagramPacket(data, data.length, addr, port);
            GameServer.socketUDP.send(packet);

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
