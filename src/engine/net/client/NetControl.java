package engine.net.client;

public abstract class NetControl {

    //���������� �������� � ��������� ���������
    public int sizeDataSend = 0, sizeDataRead = 0;//� ������
    public int countPackageSend = 0, countPackageRead = 0;//� ���-�� ���������

    public void analyzeSend(int byteCount){
        sizeDataSend += byteCount;
        countPackageSend++;
    }

    public synchronized void analyzeRead(int byteCount) {
        sizeDataRead += byteCount;
        countPackageRead++;
    }

    public void analyzeClear(){
        sizeDataSend = 0;
        sizeDataRead = 0;
        countPackageSend = 0;
        countPackageRead = 0;
    }

    public abstract void send(int type, String str);
    public abstract String read();
}
