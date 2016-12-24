package com.bearfishapps.shadowarcher;

import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.shadowarcher.Screens.GameScreen;
import com.bearfishapps.shadowarcher.Screens.SplashScreen;

public class MainGameClass extends GdxGame{
	@Override
	public void create () {
		super.create();
		this.setScreen(new SplashScreen(this));
//		this.setScreen(new GameScreen(this));
	}
}
