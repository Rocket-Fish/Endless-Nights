package com.bearfishapps.shadowarcher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bearfishapps.shadowarcher.MainGameClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 900;
		config.height = 480;
		config.title = "Shadow Archer";
		new LwjglApplication(new MainGameClass(), config);
	}
}
