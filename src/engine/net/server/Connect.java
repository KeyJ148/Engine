package engine.net.server;

import engine.net.server.readers.ServerReadTCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connect {

    public int id; //����� ���������� � ������� � gameServer
    public boolean disconnect = false; //�������� �� ���� �����
    public int numberSend = 0; //���-�� ������������ ��������� �������

    public String ipRemote; //Ip �������
    public int portUDP; //���� ������� ��� �������� UDP

    public DataInputStream in; //����� ��� ����� TCP
    public DataOutputStream out; //����� ��� �������� TCP

    public volatile MessagePack messagePack; //�������� �������� ���������, ������� �� ���������
    public ServerReadTCP serverReadTCP; //����� ��� ����� ���������

    public Connect(int id, Socket socket, int portUDP) throws IOException{
        this.id = id;
        this.ipRemote = socket.getInetAddress().getHostAddress();
        this.portUDP = portUDP;

        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());

        this.messagePack = new MessagePack(id);
        this.serverReadTCP = new ServerReadTCP(id);
    }

}
