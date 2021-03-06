package cc.abro.orchengine.cycle;

import cc.abro.orchengine.Loader;
import cc.abro.orchengine.analysis.Analyzer;
import cc.abro.orchengine.resources.settings.SettingsStorage;
import org.lwjgl.glfw.GLFW;

public class Engine {

	public Update update;
	public Render render;
	public GUI gui;
	public Analyzer analyzer;
	private FPSLimit fpsLimit;

	public void init() {
		update = new Update();
		render = new Render();
		gui = new GUI();
		analyzer = new Analyzer();
		fpsLimit = new FPSLimit(SettingsStorage.DISPLAY.FPS_LIMIT);
	}

	public void run() {
		while (!GLFW.glfwWindowShouldClose(render.getWindowID())) {
			//Цикл Update
			analyzer.startUpdate();
			update.loop(); //Обновление состояния у всех объектов в активной локации
			analyzer.endUpdate();

			//Цикл Render
			analyzer.startRender();
			render.loop(); //Отрисовка всех объектов в активной локациии
			analyzer.endRender();

			//Пауза для синхронизации кадров
			analyzer.startSync();
			render.vsync(); //Вертикальная синхронизация
			fpsLimit.sync(); //Ограничитель FPS (если вертикальная синхронизация отключена или не сработала)
			analyzer.endSync();
		}

		Loader.exit();
	}

}
