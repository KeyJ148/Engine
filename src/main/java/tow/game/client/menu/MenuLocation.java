package tow.game.client.menu;

import org.liquidengine.legui.component.Component;
import tow.engine.Global;
import tow.engine.gameobject.GameObject;
import tow.engine.gameobject.GameObjectFactory;
import tow.engine.gameobject.components.render.GuiRender;
import tow.engine.image.Color;
import tow.engine.map.Background;
import tow.engine.map.Location;

public abstract class MenuLocation extends Location {

    protected final static int MENU_ELEMENT_WIDTH = 250;
    protected final static int MENU_ELEMENT_HEIGHT = 70;
    protected final static int MENU_TEXT_FIELD_HEIGHT = 30;

    public MenuLocation(){
        super(Global.engine.render.getWidth(), Global.engine.render.getHeight());
        background = new Background(Color.GRAY, Color.GRAY);
        activate();
    }

    public void addComponent(Component component){
        GameObject gameObject = GameObjectFactory.create(component.getPosition().x, component.getPosition().y);
        objAdd(gameObject);
        gameObject.setComponent(new GuiRender(component, (int) component.getSize().x, (int) component.getSize().y));
    }

    public void addComponent(Component component, int x, int y, int width, int height){
        GameObject gameObject = GameObjectFactory.create(x, y);
        objAdd(gameObject);
        gameObject.setComponent(new GuiRender(component, width, height));
    }

    //Координаты (x;y) задают левый верхний угол компоненты
    public void addComponentLU(Component component, int x, int y, int width, int height){
        GameObject gameObject = GameObjectFactory.create(x+width/2, y+height/2);
        objAdd(gameObject);
        gameObject.setComponent(new GuiRender(component, width, height));
    }
}
