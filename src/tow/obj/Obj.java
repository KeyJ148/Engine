package tow.obj;

import java.awt.Graphics2D;

import tow.Global;
import tow.image.Animation;
import tow.image.Mask;
import tow.image.Rendering;

public class Obj extends ObjLight{
	
	public double speed; //�� ������� �������� ������ ��������� �� 1 �������
	public double direction; //0, 360 - � �����, ������ ������� - ��������
	
	public String[] collObj;//������ �������� � �������� ���� ��������� ������������
	private boolean collHave = false;//���� �� ������������
	
	public double xPrevious;//���� ������� � ���������� ����
	public double yPrevious;//(��� ������������)
	public double directionPrevious;//��������� ������� � ���������� ���� (��� ������������)
	
	public Mask mask;
	
	public Obj(double x, double y, double speed, double direction, int depth, boolean maskDynamic, Rendering image){
		super(x,y,direction,depth,image);
		try{
			this.mask = image.getMask().clone();
		} catch (CloneNotSupportedException e) {
			Global.error("Failed with clone object. Id = " + getId());
		}
		
		this.speed = speed;
		this.direction = direction;
		mask.dynamic = maskDynamic;
		
		mask.calc(this.x,this.y,this.directionDraw);//������ �����
		
		if (Global.setting.DEBUG_CONSOLE) System.out.println("Object \"" + image.getPath() + "\" create. Id = " + getId());
	}

	public void draw(Graphics2D g){
		//��� ��������� ������� � ��������� �� direction
		directionDrawEqulas();
		
		//��� �������� ������
		xView = Global.cameraXView - (Global.cameraX - x);
		yView = Global.cameraYView - (Global.cameraY - y);
	
		image.draw(g,(int) Math.round(xView),(int) Math.round(yView), Math.toRadians(directionDraw));
		if (Global.setting.MASK_DRAW) mask.draw(g);
	}
	
	public void update(long delta) {
		updateChildStart(delta);
		
		//������ ���� ������ updateChildMid, ��� �� ���� �� ��� ��������� � �����
		xPrevious = x;
		yPrevious = y;
		directionPrevious = direction;
		
		updateChildMid(delta);//step � �������� ��������
		
		x = x + speed * Math.cos(Math.toRadians(direction)) * ((double) delta/1000000000);
		y = y - speed * Math.sin(Math.toRadians(direction)) * ((double) delta/1000000000);
		Global.mapControl.update(this);
		
		directionDrawEqulas();
		
		image.update(delta);
		
		mask.calcInThisStep = false;
		if (!mask.bullet){
			if(collHave){
				mask.collCheck(x, y, directionDraw, collObj, this);
			} else {
				if (mask.dynamic) mask.calc(x,  y, directionDraw);
			}
		} else {
			mask.collCheckBullet(x, y, directionDraw, collObj, this);
		}
		
		updateChildFinal(delta);//step � �������� ��������
		
		if (destroy){
			Global.delObj(getId());
		}
	}
	
	public void directionDrawEqulas(){
		this.directionDraw = this.direction;
	}
	
	public boolean isLight(){
		return false;
	}
	
	/*
	 * 
	 * ������ ������ SET, GET
	 * � ������ ���������������� � ����������� (updateChild, collReport);
	 * ��� �� ������ P(����� ��������� � �������)
	 * 
	 */
	
	public void setXcenter(double x){
		this.x=x-this.mask.width/2;
	}
	
	public void setYcenter(double y){
		this.y=y-this.mask.height/2;
	}
	
	public void setXView(double xView){
		this.xView = xView;
	}
	
	public void setYView(double yView){
		this.yView = yView;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public void setDirection(double direction){
		this.direction = direction;
	}
	
	public void setCollObj(String[] collObj){
		this.collObj = collObj;
		this.collHave = true;
	}
	
	
	
	public double getXcenter(){
		return x+this.mask.width/2;
	}
	
	public double getYcenter(){
		return y+this.mask.height/2;
	}
	
	public double getXViewCenter(){
		return xView+this.mask.width/2;
	}
	
	public double getYViewCenter(){
		return yView+this.mask.height/2;
	}
	
	public double getDirection(){
		if (direction%360 >= 0){
			return direction%360;
		} else {
			return 360-Math.abs(direction%360);
		}
	}
	
	public double getXPrevious(){
		return xPrevious;
	}
	
	public double getYPrevious(){
		return yPrevious;
	}
	
	public double getDirectionPrevious(){
		return directionPrevious;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public Rendering getImage(){
		return image;
	}
	
	public Animation getAnimation(){
		if (image.isAnim()){
			return (Animation) image;
		} else {
			Global.error("Try receive animation from sprite");
			return null;
		}
	}
	
	public void p(String s){
		System.out.println(s);
	}
	
	public void p(int x){
		System.out.println(x);
	}
	
	public void p(){
		System.out.println("ALARM!!!");
	}
	
	public void updateChildStart(long delta){}
	public void updateChildMid(long delta){}
	public void updateChildFinal(long delta){}
	public void collReport(Obj obj){}

}