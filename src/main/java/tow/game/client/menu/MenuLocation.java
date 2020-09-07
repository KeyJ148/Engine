package tow.game.client.menu;

import org.liquidengine.legui.component.*;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.font.FontRegistry;
import tow.engine.Global;
import tow.engine.gameobject.GameObject;
import tow.engine.gameobject.GameObjectFactory;
import tow.engine.gameobject.components.render.GuiElement;
import tow.engine.image.Color;
import tow.engine.map.Location;
import tow.game.client.ClientData;

import java.util.function.Consumer;

import static tow.game.client.menu.InterfaceStyles.*;

public abstract class MenuLocation extends Location {

    protected static class ButtonConfiguration {

        public String text;
        public MouseClickEventListener event;

        public ButtonConfiguration(String text, MouseClickEventListener event) {
            this.text = text;
            this.event = event;
        }
    }

    public MenuLocation(){
        super(Global.engine.render.getWidth(), Global.engine.render.getHeight());
        background = new tow.engine.map.Background(Color.GRAY, Color.GRAY);
    }

    public void addComponent(Component component){
        GameObject gameObject = GameObjectFactory.create(component.getPosition().x, component.getPosition().y);
        objAdd(gameObject);
        gameObject.setComponent(new GuiElement(component, (int) component.getSize().x, (int) component.getSize().y));
    }

    public void addComponent(Component component, int x, int y, int width, int height){
        GameObject gameObject = GameObjectFactory.create(x, y);
        objAdd(gameObject);
        gameObject.setComponent(new GuiElement(component));
        component.setSize(width, height);
    }

    //Координаты (x;y) задают левый верхний угол компоненты
    public void addComponentLU(Component component, int x, int y, int width, int height){
        GameObject gameObject = GameObjectFactory.create(x+width/2, y+height/2);
        objAdd(gameObject);
        gameObject.setComponent(new GuiElement(component, width, height));
        component.setPosition(x, y);
    }

    public void addComponentToParent(Component component, int x, int y, int width, int height, Component parent){
        x += parent.getPosition().x;
        y += parent.getPosition().y;
        addComponent(component, x, y, width, height);
    }

    public void addComponentToParentLU(Component component, int x, int y, int width, int height, Component parent){
        component.setPosition(x, y);
        component.setSize(width, height);
        parent.add(component);
    }

    public Panel createPanel(int x, int y, int width, int height) {
        Panel panel = new Panel();
        panel.setStyle(createPanelStyle());
        panel.setFocusable(false);

        addComponent(panel, x, y, width, height);
        return panel;
    }

    public Panel createPanelToParent(int x, int y, int width, int height, Component parent) {
        Panel panel = new Panel();
        panel.setStyle(createPanelStyle());
        panel.setFocusable(false);

        addComponentToParent(panel, x, y, width, height, parent);
        panel.setPosition(x, y);
        return panel;
    }

    public Button createButton(String text, int x, int y, int width, int height, MouseClickEventListener event, Component parent){
        Button button = new Button(text);
        button.setStyle(createButtonStyle());
        button.getListenerMap().addListener(MouseClickEvent.class, event);
        addComponentToParentLU(button, x, y, width, height, parent);
        return button;
    }

    protected void createMenuButtons(ButtonConfiguration... buttonConfigurations){
        Panel menuPanel = createPanel(width/2, height/2, MENU_ELEMENT_WIDTH, buttonConfigurations.length*MENU_ELEMENT_HEIGHT);
        for(int i = 0; i < buttonConfigurations.length; i++) {
            addComponentToParentLU(createMenuButton(buttonConfigurations[i]), 0, i * MENU_ELEMENT_HEIGHT, MENU_ELEMENT_WIDTH, MENU_ELEMENT_HEIGHT, menuPanel);
        }
    }

    protected Button createMenuButton(ButtonConfiguration buttonConfiguration){
        Button button = new Button(buttonConfiguration.text);
        button.setStyle(createMenuButtonStyle());
        button.getListenerMap().addListener(MouseClickEvent.class, buttonConfiguration.event);
        button.getTextState().setFont(FontRegistry.ROBOTO_BOLD);
        button.getTextState().setFontSize(30);
        return button;
    }

    public TextAreaField createTextAreaField(int x, int y, int width, int height, Component parent) {
        TextAreaField textAreaField = new TextAreaField();
        textAreaField.setStyle(createTextAreaFieldStyle());

        addComponentToParentLU(textAreaField, x, y, width, height, parent);
        return textAreaField;
    }

    public void createToggleButton(int x, int y, int width, int height, Component parent) {
        ToggleButton toggleButton = new ToggleButton();

        addComponentToParentLU(toggleButton, x, y, width, height, parent);
    }

    public MouseClickEventListener getActivateLocationMouseReleaseListener(Class<? extends MenuLocation> menuLocationClass){
        return getMouseReleaseListener(event -> ClientData.menuLocationStorage.getMenuLocation(menuLocationClass).activate());
    }

    public MouseClickEventListener getMouseReleaseListener(Consumer<MouseClickEvent> mouseReleaseAction){
        return event -> {
            event.getTargetComponent().setFocused(false);
            if (event.getAction() == MouseClickEvent.MouseClickAction.RELEASE) {
                mouseReleaseAction.accept(event);
            }
        };
    }
}
