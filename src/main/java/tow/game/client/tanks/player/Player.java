package tow.game.client.tanks.player;

import org.liquidengine.legui.component.Label;
import tow.engine.Global;
import tow.engine.obj.Obj;
import tow.engine.obj.components.Position;
import tow.engine.obj.components.render.Animation;
import tow.engine.obj.components.render.GUIElement;
import tow.game.client.ClientData;
import tow.game.client.GameSetting;
import tow.game.client.tanks.Effect;
import tow.game.client.tanks.Stats;
import tow.game.client.tanks.Tank;
import tow.game.client.tanks.equipment.armor.ADefault;
import tow.game.client.tanks.equipment.gun.GDefault;

import java.util.ArrayList;

public class Player extends Tank {

    public boolean takeArmor = true;
    public boolean takeGun = true;
    public boolean takeBullet = true;
    public boolean takeHealth = true;

    public double hp;

    public ArrayList<Effect> effects = new ArrayList<>();
    public Stats stats;

    public PlayerController controller;
    public BulletFactory bullet;

    public double vampire = 0.5; //Сколько набрано вампиризма в процентах (от 0 до 1)

    public int lastDamagerEnemyId = -1;
    private int sendDataLast = 0;//Как давно отправляли данные
    private static long numberPackage = 0; //Номер пакета UDP

    public Obj hpLabel;
    public Obj[] statsLabel;

    public Player(double x, double y, double direction){
        position = new Position(this, x, y, 0);

        controller = new PlayerController(this);
        Global.location.objAdd(controller);

        armor = new ADefault();
        ((Armor) armor).init(this, x, y, direction, "ADefault");
        effects.add(((Armor) armor).effect);
        Global.location.objAdd(armor);

        gun = new GDefault();
        ((Gun) gun).init(this, x, y, direction, "GDefault");
        effects.add(((Gun) gun).effect);
        Global.location.objAdd(gun);

        bullet = new BulletFactory("BDefault", this);

        updateStats();
        hp = stats.hpMax;

        color = ClientData.color;
        name = ClientData.name;
        setColor(color);

        hpLabel = new Obj(1, 10, 0);
        hpLabel.position.absolute = false;
        Global.location.objAdd(hpLabel);
        hpLabel.rendering = new GUIElement(hpLabel, new Label(), 1, 1);
        ((Label) ((GUIElement) hpLabel.rendering).getComponent()).setFocusable(false);
        ((Label) ((GUIElement) hpLabel.rendering).getComponent()).getTextState().setFontSize(30);

        statsLabel = new Obj[stats.toString().split("\n").length + 4];
        for (int i = 0; i < statsLabel.length; i++) {
            statsLabel[i] = new Obj(1, 30+i*15, 0);
            statsLabel[i].position.absolute = false;
            Global.location.objAdd(statsLabel[i]);
            statsLabel[i].rendering = new GUIElement(statsLabel[i], new Label(), 1, 1);
            ((Label) ((GUIElement) statsLabel[i].rendering).getComponent()).setFocusable(false);
            ((Label) ((GUIElement) statsLabel[i].rendering).getComponent()).getTextState().setFontSize(17);
        }
    }


    @Override
    public void update(long delta){
        super.update(delta);
        if (!alive) return;

        //Обновление параметров
        updateStats();
        followToArmor(this);

        //Обновление вампирского сета
        vampire -= GameSetting.VAMPIRE_DOWN_FROM_SEC * ((double) delta/1000000000);
        if (vampire < 0.0) vampire = 0.0;

        //Отрисовка HP
        ((Label) ((GUIElement) hpLabel.rendering).getComponent()).getTextState().setText("HP: " +  Math.round(hp) + "/" + Math.round(stats.hpMax));

        //Отрисовка статов
        if (ClientData.printStats){
            String[] array = stats.toString().split("\n");
            for (int i = 0; i < array.length; i++) {
                ((Label) ((GUIElement) statsLabel[i].rendering).getComponent()).getTextState().setText(array[i]);
            }
            ((Label) ((GUIElement) statsLabel[array.length].rendering).getComponent()).getTextState().setText("Armor: " + ((Armor) armor).title);
            ((Label) ((GUIElement) statsLabel[array.length+1].rendering).getComponent()).getTextState().setText("Gun: " + ((Gun) gun).title);
            ((Label) ((GUIElement) statsLabel[array.length+2].rendering).getComponent()).getTextState().setText("Bullet: " + bullet.title);
            ((Label) ((GUIElement) statsLabel[array.length+3].rendering).getComponent()).getTextState().setText("Vampire: " + Math.round(vampire*100) + "%");
       } else {
            for (int i = 0; i < statsLabel.length; i++) {
                ((Label) ((GUIElement) statsLabel[i].rendering).getComponent()).getTextState().setText("");
            }
        }

        //Проверка HP
        if(hp <= 0){
            if (lastDamagerEnemyId != -1){
                Global.tcpControl.send(23, String.valueOf(lastDamagerEnemyId));
                ClientData.enemy.get(lastDamagerEnemyId).kill++;
            }

            Global.tcpControl.send(12, "");
            exploded();
        } else {
            if ((hp+delta/Math.pow(10, 9)*stats.hpRegen) > stats.hpMax){
                hp = stats.hpMax;
            } else {
                hp += delta/Math.pow(10, 9)*stats.hpRegen;
            }
        }

        //Отправка данных о игроке
        sendDataLast += delta;
        if (ClientData.battle && sendDataLast >= Math.pow(10,9)/GameSetting.MPS){
            sendDataLast -= Math.pow(10,9)/GameSetting.MPS;
            Global.udpControl.send(2, getData());
        }
    }

    @Override
    public void exploded(){
        super.exploded();

        controller.runUp = false;
        controller.runDown = false;
        controller.turnRight = false;
        controller.turnLeft = false;
    }

    @Override
    public void replaceArmor(Obj newArmor){
        double lastMaxHp = stats.hpMax;
        effects.remove(((Armor) armor).effect);

        super.replaceArmor(newArmor);
        effects.add(((Armor) newArmor).effect);
        updateStats();

        //Устанавливаем новой броне параметры как у текущий брони игрока
        hp = (hp/lastMaxHp) * stats.hpMax; //Устанавливаем эквивалетное здоровье в процентах
        if (controller.runUp) newArmor.movement.speed = stats.speedTankUp;
        if (controller.runDown) newArmor.movement.speed = stats.speedTankDown;

        //Отправляем сообщение о том, что мы сменили броню
        String newName = ((Armor) armor).textureHandlers[0].name;
        Global.tcpControl.send(19, newName.substring(0, newName.lastIndexOf("_")));
    }

    @Override
    public void replaceGun(Obj newGun){
        effects.remove(((Gun) gun).effect);

        super.replaceGun(newGun);
        effects.add(((Gun) newGun).effect);
        updateStats();

        //Отправляем сообщение о том, что мы сменили оружие
        Global.tcpControl.send(20, ((Gun) newGun).texture.name);
    }

    //Игрок попал по врагу и нанес damage урона
    public void hitting(double damage){
        vampire += damage*GameSetting.VAMPIRE_UP_FROM_ONE_DAMAGE;
        if (vampire > 1.0) vampire = 1.0;
    }

    public void replaceBullet(String newBullet){
        bullet = new BulletFactory(newBullet, this);
    }

    public String getData(){

        return  Math.round(armor.position.x)
                + " " + Math.round(armor.position.y)
                + " " + Math.round(armor.position.getDirectionDraw())
                + " " + Math.round(gun.position.getDirectionDraw())
                + " " + Math.round(armor.movement.speed)
                + " " + armor.movement.getDirection()
                + " " + ((Animation) armor.rendering).getFrameSpeed()
                + " " + Player.numberPackage++;
    }
    
    private void updateStats(){
        stats = new Stats();

        for (Effect effect : effects) {
            effect.calcAddStats(stats);
        }

        for (Effect effect : effects) {
            effect.calcMultiStats(stats);
        }
    }
}
