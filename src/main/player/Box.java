package main.player;

import main.Global;
import main.image.Sprite;
import main.obj.Obj;

public class Box extends Obj {
	
	public int idBox;
	public int typeBox;
	public boolean collision = false;//��������� �� ������������?
	
	public Box(double x, double y, int idBox, Sprite s) {
		super(x, y, 0.0, 90.0, 1, false, s);
		//��������, ����, �������, ������������ �����, ������
		
		this.idBox = idBox;
	}

	public void collisionPlayer(){
		collision = true;
		destroy();
		Global.clientSend.send13(idBox);
	}
}
