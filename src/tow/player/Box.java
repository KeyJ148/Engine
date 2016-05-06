package tow.player;

import tow.Global;
import tow.image.TextureHandler;
import tow.obj.Obj;

public class Box extends Obj {
	
	public int idBox;
	public int typeBox;
	public boolean collision = false;//��������� �� ������������?
	
	public Box(double x, double y, int idBox, TextureHandler textureHandler) {
		super(x, y, 0.0, 90.0, 1, false, textureHandler);
		//��������, ����, �������, ������������ �����, ������
		
		this.idBox = idBox;
	}

	public void collisionPlayer(){
		collision = true;
		destroy();
		Global.tcpSend.send13(idBox);
	}
}
