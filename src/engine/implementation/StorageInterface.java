package engine.implementation;

public interface StorageInterface {

    //Engine: ��� ������ �������, ��� ������ �������� ����� �������������� ������
    //Engine: ������� �������������� � ������� ���������� �������

    default String getPathImagesRoot(){ return "res/image/"; } //Engine: ���� � ����� ����� � ����������
    String[][] getImages();  //Engine: �������� �������� (���� �� �����, ��� �������, �������)

    default String getPathAnimationsRoot(){ return "res/animation/"; }//Engine: ���� � ����� ����� � ���������
    String[][] getAnimations();//�������� �������� (���� �� �����, ��� �������, �������)

    int[][] getFonts(); //Engine: �������� ������ (������, ���)
    String[] getAudios(); //Engine: ���� � ����� ������ (.wav)

}
