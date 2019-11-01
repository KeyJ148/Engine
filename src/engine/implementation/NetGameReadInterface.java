package engine.implementation;

import engine.net.client.Message;

public interface NetGameReadInterface {

    void readTCP(Message message);  //Engine: ��������� �������� � ����������� ��������� � �������� TCP
    void readUDP(Message message); //Engine: ��������� �������� � ����������� ��������� � �������� UDP
}
