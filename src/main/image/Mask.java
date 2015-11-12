package main.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import main.Global;
import main.obj.Obj;
import main.obj.ObjLight;

public class Mask implements Cloneable{
	
	public int[] maskX; //����� ������������ ������ ������
	public int[] maskY;
	public int[] maskXDraw; //����� ������������ ������ ���������
	public int[] maskYDraw;
	
	public int width; //������ � ������ �������
	public int height;
	public boolean calcInThisStep = false;
	public boolean dynamic; //���������� ����� ������ ��� (true = ����������)
							//����� ��� ���������� ��� ���������������� ��������
	
	public Mask (String path, int width, int height) {
		this.width = width;
		this.height = height;
		//�������� ����� �� �����
		StringBuffer Bpath = new StringBuffer(path); 
		Bpath.delete(path.lastIndexOf('.'),path.length());
		path = new String(Bpath);
		path = path + ".txt";
		if (new File(path).exists()){
			try{
				BufferedReader fileReader = new BufferedReader(new FileReader(path));
				parser(fileReader);
				if (Global.setting.DEBUG_CONSOLE_MASK) System.out.println("Load mask \"" + path + "\" complited.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { //�������� ����� ��� ���������� �����
			maskX = new int[] {0,this.width-1,this.width-1,0};
			maskY = new int[] {0,0,this.height-1,this.height-1};
		}
		center();
	}
	
	//������������ �������
	public Mask clone() throws CloneNotSupportedException {
		return (Mask)super.clone();
	}
	
	//���������� ����� �� �����
    public void parser(BufferedReader fileReader) throws IOException{
		String s;
		int x;
		int y;
		int lengthArray = 0;
		Vector<Integer> maskXVec = new Vector<Integer>();
		Vector<Integer> maskYVec = new Vector<Integer>();
		while (true){ 
			s = fileReader.readLine();
			if (s == null){
				break;
			}
			x = Integer.parseInt(s.substring(0,s.indexOf(' ')));
			y = Integer.parseInt(s.substring(s.indexOf(' ')+1));
			maskXVec.add(x);
			maskYVec.add(y);
			lengthArray++;
		}
		maskX = new int[lengthArray];
		maskY = new int[lengthArray];
		for (int i=0;i<maskX.length;i++){
			maskX[i] = maskXVec.get(i);
			maskY[i] = maskYVec.get(i);
		}
	}
	
	//��������� ��� ����� ������������ ������ �����
	public void center(){
		for (int i=0;i<maskX.length;i++){
			maskX[i] -= this.width/2;
			maskY[i] -= this.height/2;
		}
	}
	
	//��������: ����� �� ������ � ���� �������� �������� ������������
	public void collCheck(double x, double y, double directionDraw, String[] collObj, Obj obj){
		ObjLight objGlobal; //������ �� ����������� �������
		
		for(int vectorON=0;vectorON<Global.obj.size();vectorON++){			//Vector Object Number - ���� �������� �������� � �������
			objGlobal = Global.obj.get(vectorON);
			
			if ((objGlobal != null) && (!objGlobal.isLight())){
				for (int stringON=0;stringON<collObj.length;stringON++){ 	//String Object Number - ���� �������� �������� �� ������� �������
					if ((objGlobal.getClass().getName().equals(collObj[stringON])) && (collCheckConti(x, y, directionDraw, (Obj) objGlobal))){
						obj.collReport((Obj) objGlobal);
					}
				}
			}
		}
	}
	
	//�������� ������������
	public boolean collCheckConti(double x, double y, double directionDraw, Obj obj){
		//�������� ���������� �� ������� ������������
		double xCenter = x + this.width/2; //�������� ������ ��� � ����� �������
		double yCenter = y + this.height/2;
		double gipMe = Math.sqrt(sqr(height) + sqr(width)); //���������� �������
		double gipOther = Math.sqrt(sqr(obj.getImage().getHeight()) + sqr(obj.getImage().getWidth())); //���������� �������, � ������� ����������
		double disMeToOther = Math.sqrt(sqr(xCenter-obj.getXcenter())+sqr(yCenter-obj.getYcenter())); //���������� �� ������ �� ������
		
		if (disMeToOther > gipMe/2 + gipOther/2 + 30){//���� �� ������� ������� ������
			return false;
		} else {
			if ((dynamic) && (!calcInThisStep)){
				calc(x, y, directionDraw);
				calcInThisStep = true;
			}
		}
		
		//������� ������������
		Polygon pMe = new Polygon(maskXDraw,maskYDraw,maskXDraw.length);
		Polygon pOther = new Polygon(obj.mask.maskXDraw,obj.mask.maskYDraw,obj.mask.maskXDraw.length);
		for (int i = 0;i<obj.mask.maskXDraw.length;i++){
			Point point = new Point(obj.mask.maskXDraw[i],obj.mask.maskYDraw[i]);
			if (pMe.contains(point)){
				return true;
			}
		}
		for (int i = 0;i<maskXDraw.length;i++){
			Point point = new Point(maskXDraw[i],maskYDraw[i]);
			if (pOther.contains(point)){
				return true;
			}
		}
		return false;
	}
	
	public int sqr(int x){
		return x*x;
	}
	
	public double sqr(double x){
		return x*x;
	}
	
	//������ ����� ������������ ������ ���������
	public void calc(double x, double y, double direction){
		direction = Math.toRadians(direction)-Math.PI/2;//������� ���������� ���� � ������� �� �����
		x+=this.width/2; //�������� ������ ��� � ����� �������
		y+=this.height/2; //��� �� ���������� ��� �������� �����
			
		//������� �����
		this.maskXDraw = new int[maskX.length];
		this.maskYDraw = new int[maskX.length];
		double XDouble;
		double YDouble; 
		double XDouble2;
		double YDouble2; 
		double cos = Math.cos(direction);
		double sin = Math.sin(direction);
		for (int i=0;i<maskX.length;i++){
			XDouble = cos * maskX[i];//������ ������
			YDouble = sin * maskX[i];//"�����"
			XDouble2 = sin * maskY[i];//������ ������ //Math.cos(direction-Math.PI/2) * ...
			YDouble2 = -cos * maskY[i];//"� ���" //Math.sin(direction-Math.PI/2) * ...
			this.maskXDraw[i] = (int) (x + XDouble + XDouble2);
			this.maskYDraw[i] = (int) (y - YDouble - YDouble2);
		}
	}
	
	public void draw(Graphics2D g){
		AffineTransform at = new AffineTransform(); 
		g.setTransform(at);
		g.setColor(new Color(0,0,255));
		int maskXDrawView[] = new int[maskXDraw.length];
		int maskYDrawView[] = new int[maskYDraw.length];
		double xd,yd;
		for (int i=0;i<maskXDraw.length;i++){
			xd = Global.cameraXView - (Global.cameraX - maskXDraw[i]);
			yd = Global.cameraYView - (Global.cameraY - maskYDraw[i]);
			maskXDrawView[i] = (int) xd;
			maskYDrawView[i] = (int) yd;
		}
		g.drawPolygon(maskXDrawView,maskYDrawView,maskXDrawView.length);
	}
}