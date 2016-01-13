package com.vihanchaudhry.raptor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vihanchaudhry.raptor.Raptor;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Raptor";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new Raptor(), config);
	}
}
