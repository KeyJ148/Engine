package game.client.tanks.player;


import engine.Global;
import engine.image.TextureHandler;
import engine.image.TextureManager;
import engine.map.Border;
import engine.obj.Obj;
import engine.obj.components.Collision;
import engine.obj.components.CollisionDirect;
import engine.obj.components.Movement;
import engine.obj.components.Position;
import engine.obj.components.render.Sprite;
import engine.setting.ConfigReader;
import game.client.ClientData;
import game.client.map.Wall;
import game.client.tanks.enemy.EnemyArmor;
import game.client.tanks.equipment.armor.AVampire;
import game.client.tanks.equipment.bullet.BVampire;
import game.client.tanks.equipment.gun.GVampire;

public class Bullet extends Obj implements Collision.CollisionListener{

	public static final String PATH_SETTING = "game/bullet/";
	public String name;

	public double damage; //Дамаг (пушка и в loadData добавляем дамаг пули)
	public int range; //Дальность (пушка и в loadData добавляем дальность пули)
	
	public double startX;
	public double startY;
	public long idNet;

	public Player player;
	public TextureHandler texture;

	public void init(Player player, double x, double y, double dir, double damage, int range, String name){
		this.player = player;
		this.name = name;
		this.damage = damage; //Дамаг исключительно от выстрелевшей пушки
		this.range = range; //Дальность исключительно от выстрелевшей пушки
		this.idNet = ClientData.idNet;
		this.startX = x;
		this.startY = y;

		this.movement = new Movement(this);
		this.movement.setDirection(dir);
		loadData();

		this.position = new Position(this, x, y, texture.depth, dir);
		this.rendering = new Sprite(this, texture);

		this.collision = new CollisionDirect(this, texture.mask, range);
		this.collision.addCollisionObjects(new Class[] {Wall.class, EnemyArmor.class, Border.class});
		this.collision.addListener(this);
		((CollisionDirect) collision).init();

		Global.tcpControl.send(13, getData());
		ClientData.idNet++;
	}

	@Override
	public void collision(Obj obj) {
		if (destroy) return;

		if (obj.getClass().equals(Wall.class) ||
			obj.getClass().equals(Border.class)){
				destroy();
		}

		if (obj.getClass().equals(EnemyArmor.class)){
			EnemyArmor ea = (EnemyArmor) obj;

			Global.tcpControl.send(14, damage + " " + ea.enemy.id);
			destroy();

			//Для вампирского сета
			if (ea.enemy.alive) {
				if (player.armor instanceof AVampire) {
					((AVampire) player.armor).hitting();
				}
				if (player.gun instanceof GVampire) {
					((GVampire) player.gun).hitting();
				}
				if (player.bullet.equals(BVampire.nameVampire)) {
					BVampire.hitting();
				}
			}
		}
	}

	@Override
	public void destroy(){
		super.destroy();
		Global.tcpControl.send(15, String.valueOf(idNet));
	}

	@Override
	public void update(long delta) {
		if (!destroy) {
			if (Math.sqrt(Math.pow(startX - position.x, 2) + Math.pow(startY - position.y, 2)) >= range) {
				destroy();
			}
		}

		super.update(delta);
	}

	public String getData(){
		return  Math.round(position.x)
		+ " " +	Math.round(position.y)
		+ " " +	movement.getDirection()
		+ " " +	movement.speed
		+ " " +	texture.name
		+ " " +	idNet;
	}

	public String getConfigFileName(){
		return PATH_SETTING + name + ".properties";
	}
	
	public void loadData(){
		ConfigReader cr = new ConfigReader(getConfigFileName());
		
		movement.speed = cr.findDouble("SPEED");
		damage += cr.findDouble("DAMAGE");//К дамагу пушки прибавляем дамаг патрона
		range += cr.findInteger("RANGE");//К дальности пушки прибавляем дальность патрона
		texture = TextureManager.getTexture(cr.findString("IMAGE_NAME"));
	}

}