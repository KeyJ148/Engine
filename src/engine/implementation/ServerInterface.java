package engine.implementation;

public interface ServerInterface {

    void init(); //Engine: ������������� ������� ����� ����� �������� ������ (���� ����������� ��� �� �������, �� ����� ����������)
    void startProcessingData(); //Engine: ������������� ������� ����� �������� �������� �����, ����� ����������� ���� �������
    void update(long delta); //Engine: ����������� ������ ���� ����� ���������� ���������

}
