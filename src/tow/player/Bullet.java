package tow.player;

import tow.*;
import tow.image.Sprite;
import tow.obj.Obj;
import tow.player.enemy.*;
import tow.setting.ConfigReader;

public class Bullet extends Obj{
	
	@SuppressWarnings("unused")
	private Player player;
	private double damage;//����� �����+�������
	private int range;//��������� �����+�������
	
	private double startX;
	private double startY;
	private long idNet;
	
	public static final String pathSetting = "bullet/";
	
	public Bullet(Player player, double x, double y, double dir,double damage, int range, Sprite sprite){
		super(x,y,0,dir,0,true,sprite);
		this.player = player;
		this.damage = damage;//����� ������������� �� ������������ �����
		this.range = range;
		loadData(getClass().getName());
		
		this.idNet = Global.idNet;
		Global.tcpSend.send1(this);
		Global.idNet++;
		
		startX = getX();
		startY = getY();
		setCollObj(new String[] {"tow.map.Home", "tow.player.enemy.EnemyArmor"});
		mask.thisBullet(this.range, collObj, startX, startY, directionDraw);
	}
	
	public void collReport(Obj obj){
		if (obj.getClass().getName().equals("tow.map.Home")){
			destroyBullet();
		}
		if (obj.getClass().getName().equals("tow.player.enemy.EnemyArmor")){
			EnemyArmor ea = (EnemyArmor) obj;
			Global.tcpSend.send3(this,ea.enemy.name);
			destroyBullet();
		}
	}
	
	public void destroyBullet(){
		Global.tcpSend.send2(this);
		destroy();
	}
	
	@Override
	public void updateChildStart(long delta){
		if (!getDestroy()){
			try{
				if ((getX()<0) || (getY()<0) || (getX()>Global.widthMap) || (getY()>Global.heightMap)){
					destroyBullet();
				}
				if (Math.sqrt(Math.pow(startX-getX(), 2) + Math.pow(startY-getY(), 2)) >= range){
					destroyBullet();
				}
			}catch(NullPointerException e){
				p("[ERROR] Bullet null");
			}
		}
	}
	
	public void loadData(String fileName){
		ConfigReader cr = new ConfigReader(pathSetting + fileName.substring(fileName.lastIndexOf('.')+1) + ".properties");
		
		setSpeed(cr.findDouble("SPEED"));
		damage += cr.findDouble("DAMAGE");//� ������ ����� ���������� ����� �������
		range += cr.findInteger("RANGE");//� ��������� ����� ���������� ��������� �������
	}
	
	public double getDamage(){
		return damage;
	}
	
	public double getRange(){
		return range;
	}
	
	public long getIdNet(){
		return idNet;
	}
}