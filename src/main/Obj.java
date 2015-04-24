package main;

import java.awt.*;

public class Obj {
	/*
	��� �������!
	�����������:
	direction, speed
	������������:
	collObj, collHave, maskDynamic, MASK_DRAW, xPre, yPre, dirPre
	
	*/
	
	public double x;
	public double y;
	public double xView = 0; //��� �������� ������
	public double yView = 0; //���� ������� � ����
	
	public double direction; //0, 360 - � �����, ������ ������� - ��������
	public double directionDraw; //0, 360 - � �����, ������ ������� - ���������
	
	public double speed; //�� ������� �������� ������ ��������� �� 1 �������
	public int depth;
	
	public String[] collObj;//������ �������� � �������� ���� ��������� ������������
	private boolean collHave = false;//���� �� ������������
	
	private boolean maskDynamic;//���������� ����� ������ ��� (true = ����������)
								//����� ��� ���������� ��� ���������������� ��������
	private final boolean MASK_DRAW = false;//��������� �����
	
	private long id; //���������� �����
	private boolean anim; //������� �������� ������ ��� ��������?(true = ��������)
	private boolean destroy = false;
	
	public double xPrevious;//���� ������� � ���������� ����
	public double yPrevious;//(��� ������������)
	public double directionPrevious;//��������� ������� � ���������� ���� (��� ������������)
	
	public Mask mask;
	@SuppressWarnings("unused")
	private Game game;
	private Sprite sprite;
	private Animation animation;
	
	
	public Obj(double x, double y, double speed, double direction, int depth, boolean maskDynamic, Sprite sprite,Game game){
		try{	
			this.sprite = sprite;
			this.anim = false;
			this.mask = sprite.getMask().clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Failed with clone object. Id = " + id);
		}
		
		init(x,y,speed,direction,depth,maskDynamic,game);
	}
	
	public Obj(double x, double y, double speed, double direction, int depth, boolean maskDynamic, Animation animation,Game game){
		try{	
			this.animation = animation.clone();
			this.anim = true;
			this.mask = animation.getMask().clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Failed with clone object. Id = " + id);
		}
		init(x,y,speed,direction,depth,maskDynamic,game);
	}
	
	private void init(double x, double y, double speed, double direction, int depth, boolean maskDynamic, Game game){
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direction = direction;
		this.directionDraw = direction;
		this.depth = depth;
		this.id = Global.id;
		Global.id++;
		this.game = game;
		this.maskDynamic = maskDynamic;
		
		Global.obj.add(this);
		
		depthAddVector(depth, this.id); //��������� ������ � ������ � ����������� �� ��� �������
		mask.calc(this.x,this.y,this.directionDraw);//������ �����
		
		if (this.anim){
			if (Game.console) System.out.println("Object \"" + animation.path + "\" create. Id = " + id);
		} else {
			if (Game.console) System.out.println("Object \"" + sprite.path + "\" create. Id = " + id);
		}
		
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
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
	
	public void setDirectionDraw(double direction){
		this.directionDraw = direction;
	}
	
	public void setDepth(int depth){
		this.depth = depth;
	}
	
	public void setCollObj(String[] collObj){
		this.collObj = collObj;
		this.collHave = true;
	}
	
	public void setMaskDynamic(boolean maskDynamic){
		this.maskDynamic = maskDynamic;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getXcenter(){
		return x+this.mask.width/2;
	}
	
	public double getYcenter(){
		return y+this.mask.height/2;
	}
	
	public double getXView(){
		return xView;
	}
	
	public double getYView(){
		return yView;
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
	
	public double getDirectionDraw(){
		if (directionDraw%360 >= 0){
			return directionDraw%360;
		} else {
			return 360-Math.abs(directionDraw%360);
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
	
	public int getDepth(){
		return depth;
	}
	
	public long getId(){
		return id;
	}
	
	public boolean getDestroy(){
		return destroy;
	}
	
	public Animation getAnimation(){
		return animation;
	}
	
	private void depthAddVector(int depth, long id){
		boolean flag = false;
		DepthVector dv;
		for (int i=0; i<Global.depth.size(); i++){
			dv = (DepthVector) Global.depth.get(i);
			if (dv.depth == depth){
				dv.add(id);
				flag = true;
				break;
			}
		}
	
		if (!flag){
			Global.depth.add(new DepthVector(depth, id));
		}
	}
	
	public void destroy(){
		DepthVector dv;
		for (int i=0; i<Global.depth.size(); i++){
			dv = (DepthVector) Global.depth.get(i);
			if (dv.depth == depth){
				dv.delete(id);
			}
		}
		this.destroy = true;
	}
	
	public void p(String s){
		System.out.println(s);
	}
	
	public void p(){
		System.out.println("ALARM!!!");
	}

	public void draw(Graphics2D g){
		//��� ��������� ������� � ��������� �� direction
		directionDrawEqulas();
		
		//��� �������� ������
		this.xView = Global.cameraXView - (Global.cameraX - this.x);
		this.yView = Global.cameraYView - (Global.cameraY - this.y);

		if (anim) {
			animation.draw(g,(int) Math.round(this.xView),(int) Math.round(this.yView), Math.toRadians(directionDraw));
		} else {
			sprite.draw(g,(int) Math.round(this.xView),(int) Math.round(this.yView), Math.toRadians(directionDraw));
		}
		
		if (MASK_DRAW){
			mask.draw(g);
		}
	}
	
	public void update() {
		updateChildStart();
		
		//������ ���� ������ updateChildMid, ��� �� ���� �� ��� ��������� � �����
		this.xPrevious = this.x;
		this.yPrevious = this.y;
		this.directionPrevious = this.direction;
		
		updateChildMid();//step � �������� ��������
		
		this.x = this.x + this.speed * Math.cos(Math.toRadians(direction));
		this.y = this.y - this.speed * Math.sin(Math.toRadians(direction));
		
		directionDrawEqulas();
		
		if (anim) {
			animation.update();
			if (maskDynamic){
				mask.calc(this.x,this.y,this.directionDraw);
			}
		} else {
			if (maskDynamic){
				mask.calc(this.x,this.y,this.directionDraw);
			}
		}
		
		if(this.collHave){
			mask.collCheck(collObj,this);
		}
		
		updateChildFinal();//step � �������� ��������
		
		if (destroy){
			Global.delObj(id);
		}
	}
	
	public void directionDrawEqulas(){
		this.directionDraw = this.direction;
	}
	
	public void updateChildStart(){}
	public void updateChildMid(){}
	public void updateChildFinal(){}
	public void collReport(Obj obj){}

}