package cc.abro.orchengine;

import cc.abro.orchengine.audio.AudioPlayer;
import cc.abro.orchengine.cycle.Engine;
import cc.abro.orchengine.gui.CachedGuiPanelStorage;
import cc.abro.orchengine.implementation.GameInterface;
import cc.abro.orchengine.implementation.NetGameReadInterface;
import cc.abro.orchengine.implementation.NetServerReadInterface;
import cc.abro.orchengine.implementation.ServerInterface;
import cc.abro.orchengine.logger.AggregateLogger;
import cc.abro.orchengine.logger.Logger;
import cc.abro.orchengine.map.Location;
import cc.abro.orchengine.net.client.Ping;
import cc.abro.orchengine.net.client.tcp.TCPControl;
import cc.abro.orchengine.net.client.tcp.TCPRead;
import cc.abro.orchengine.net.client.udp.UDPControl;
import cc.abro.orchengine.net.client.udp.UDPRead;
import cc.abro.orchengine.resources.animations.AnimationStorage;
import cc.abro.orchengine.resources.audios.AudioStorage;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import cc.abro.orchengine.resources.settings.SettingsStorageHandler;
import cc.abro.orchengine.resources.sprites.SpriteStorage;
import org.lwjgl.glfw.GLFWErrorCallback;

import java.io.IOException;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

public class Loader {

	public static void start(GameInterface game, NetGameReadInterface netGameRead,
							 ServerInterface server, NetServerReadInterface netServerRead) {
		Global.game = game;
		Global.server = server;
		Global.netGameRead = netGameRead;
		Global.netServerRead = netServerRead;

		try {
			loggerInit();//Загрузка логгера для вывода ошибок
			init(); //Инициализация перед запуском
			Global.engine.run();//Запуск главного цикла
		} catch (Exception e) {
			e.printStackTrace();
			Global.logger.println("Unknown exception: ", e, Logger.Type.ERROR); //TODO: если logger не создан
			exit();
		}
	}

	private static void loggerInit() {
		try {
			SettingsStorageHandler.init();//Загрузка настроек
		} catch (IOException e) {
			e.printStackTrace();
			exit();
		}

		Global.logger = new AggregateLogger();

		//Установка настроек логирования
		Global.logger.enableType(Logger.Type.INFO);
		Global.logger.enableType(Logger.Type.SERVER_INFO);
		if (SettingsStorage.LOGGER.ERROR_CONSOLE) Global.logger.enableType(Logger.Type.ERROR);
		if (SettingsStorage.LOGGER.ERROR_CONSOLE_SERVER) Global.logger.enableType(Logger.Type.SERVER_ERROR);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE) Global.logger.enableType(Logger.Type.DEBUG);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_IMAGE) Global.logger.enableType(Logger.Type.DEBUG_TEXTURE);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_MASK) Global.logger.enableType(Logger.Type.DEBUG_MASK);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_AUDIO) Global.logger.enableType(Logger.Type.DEBUG_AUDIO);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_FPS) Global.logger.enableType(Logger.Type.CONSOLE_FPS);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_SERVER) Global.logger.enableType(Logger.Type.SERVER_DEBUG);
		if (SettingsStorage.LOGGER.DEBUG_CONSOLE_MPS) Global.logger.enableType(Logger.Type.MPS);
	}

	//Инициализация движка перед запуском
	private static void init() {
		Global.engine = new Engine();//Создание класса для главного цикла
		Global.engine.init();

		Global.tcpControl = new TCPControl();
		Global.tcpRead = new TCPRead();
		Global.udpControl = new UDPControl();
		Global.udpRead = new UDPRead();

		Global.pingCheck = new Ping();

		Global.audioPlayer = new AudioPlayer();
		Global.audioStorage = new AudioStorage();

		Global.spriteStorage = new SpriteStorage();
		Global.animationStorage = new AnimationStorage();
		Global.cachedGuiPanelStorage = new CachedGuiPanelStorage();

		new Location(640, 480).activate(false);

		Global.logger.println("Initialization end", Logger.Type.DEBUG);

		//Инициализация игры
		Global.game.init();
	}

	public static void exit() {
		try {
			glfwFreeCallbacks(Global.engine.render.getWindowID());
			glfwDestroyWindow(Global.engine.render.getWindowID());
			glfwTerminate();
			GLFWErrorCallback errorCallback = glfwSetErrorCallback(null);
			if (errorCallback != null) errorCallback.free();
			Global.audioPlayer.close();

			Global.logger.println("Exit stack trace: ", new Exception(), Logger.Type.DEBUG);
		} catch (Exception e) {
			Global.logger.println("Unknown exception: ", e, Logger.Type.ERROR); //TODO: если logger не создан
		} finally {
			Global.logger.close();
			System.exit(0);
		}
	}


}
