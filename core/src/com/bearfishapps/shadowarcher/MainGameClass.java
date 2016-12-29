package com.bearfishapps.shadowarcher;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.Screens.GameScreen;
import com.bearfishapps.shadowarcher.Screens.SplashScreen;

public class MainGameClass extends GdxGame{
	public BitmapFont superLargeFont;
	@Override
	public void create () {
		super.create();
		Prefs.init();
		this.setScreen(new SplashScreen(this));
//		this.setScreen(new GameScreen(this));
	}
}
