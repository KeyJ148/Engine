package engine.implementation;

public interface GameInterface {

    void init(); //Engine: ������������� ���� ����� �������� �������� �����
    void update(long delta); //Engine: ����������� ������ ���� ����� ����������� ���� ������� ��������
    void render(); //Engine: ����������� ������ ���� ����� ������������ ���� ������� ��������

}
