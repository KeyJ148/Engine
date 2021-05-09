package cc.abro.orchengine.gui;

import cc.abro.orchengine.gameobject.components.render.CachedGuiElement;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.ToggleButton;
import cc.abro.orchengine.gameobject.components.render.GuiElement;

/**
 * Абстрактный класс от которого наследуются классы готовых панелей меню.
 * Подробнее {@link GuiElement} и {@link CachedComponent}
 */
public abstract class CachedGuiPanel extends Panel implements CachedComponent<Panel> {

    private CachedGuiElement<Panel> cachedGuiElementOnActiveLocation;

    @Override
    public void setCachedGuiElementOnActiveLocation(CachedGuiElement<Panel> cachedGuiElementOnActiveLocation) {
        this.cachedGuiElementOnActiveLocation = cachedGuiElementOnActiveLocation;
    }

    @Override
    public CachedGuiElement<Panel> getCachedGuiElementOnActiveLocation() {
        return cachedGuiElementOnActiveLocation;
    }

    @Override
    public Panel getComponent() {
        return this;
    }

    /**
     * Инициализация панели с элементами интерфейса. Должна вызываться из самого дочернего элемента в конструкторе,
     * чтобы инициализировать дефолтные стили.
     */
    public abstract void init();

    public void addComponentShiftedToCenter(Component component, int x, int y, int width, int height) {
        component.setPosition(x - width / 2, y - height / 2);
        component.setSize(width, height);
        add(component);
    }

    public void addComponent(Component component, int x, int y, int width, int height) {
        component.setPosition(x, y);
        component.setSize(width, height);
        add(component);
    }

    public void addToggleButton(int x, int y, int width, int height) {
        ToggleButton toggleButton = new ToggleButton();
        addComponentShiftedToCenter(toggleButton, x, y, width, height);
    }
}
