package main;

public class ObjLight {
	
	public double x;
	public double y;
	public double xView = 0; //��� �������� ������
	public double yView = 0; //���� ������� � ����
	
	public int depth;

	public double directionDraw; //0, 360 - � �����, ������ ������� - ���������
	
	private long id; //���������� �����
	
	protected void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}

}
