package engine.implementation;

import engine.net.server.MessagePack;

public interface NetServerReadInterface {

    void readTCP(MessagePack.Message message); //Engine: ���������� ������ ��� ��� ��������� �������� ��������� �� ��������� TCP
    void readUDP(MessagePack.Message message); //Engine: ���������� ������ ��� ��� ��������� �������� ��������� �� ��������� UDP
}
