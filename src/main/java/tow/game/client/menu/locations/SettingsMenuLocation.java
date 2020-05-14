package tow.game.client.menu.locations;


import org.joml.Vector4f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Panel;
import tow.game.client.menu.MenuLocation;

import static tow.game.client.menu.InterfaceStyles.*;

public class SettingsMenuLocation extends MenuLocation {

    protected final static int SETTINGS_PANEL_WIDTH = 2*MENU_ELEMENT_WIDTH;
    protected final static int SETTINGS_PANEL_HEIGHT = 3*MENU_ELEMENT_HEIGHT;

    public SettingsMenuLocation() {
        Panel mainPanel = createPanel(width/2, height/2, SETTINGS_PANEL_WIDTH, SETTINGS_PANEL_HEIGHT);
        Button backButton = createButton("Buck to menu", INDENT_X, SETTINGS_PANEL_HEIGHT - BUTTON_HEIGHT - INDENT_X,
                BUTTON_WIDTH, BUTTON_HEIGHT, getActivateLocationMouseReleaseListener(MainMenuLocation.class), mainPanel);
        createButton("Confirm", SETTINGS_PANEL_WIDTH - BUTTON_WIDTH - INDENT_X, SETTINGS_PANEL_HEIGHT - BUTTON_HEIGHT - INDENT_X, BUTTON_WIDTH, BUTTON_HEIGHT, event -> { }, mainPanel);
    }

}