package com.bearfishapps.shadowarcher;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.Screens.GameScreen;
import com.bearfishapps.shadowarcher.Screens.SplashScreen;

public class MainGameClass extends GdxGame{
	public static String
	ACH_PLAY_WITH_A_FRIEND = "CgkI3eLxqt4ZEAIQBQ",
	ACH_COMPLETE_TUTORIAL = "CgkI3eLxqt4ZEAIQBg",
	ACH_FINISH_A_GAME = "CgkI3eLxqt4ZEAIQBw",
	ACH_SURVIVOR = "CgkI3eLxqt4ZEAIQCQ",
	ACH_HUNDRED = "CgkI3eLxqt4ZEAIQCA";
	public BitmapFont superLargeFont;
	private GPGSInterface gpgsInterface;
	public MainGameClass(GPGSInterface gpgsInterface) {
		super();
		this.gpgsInterface = gpgsInterface;
	}

	public GPGSInterface getService() {
		return gpgsInterface;
	}
	@Override
	public void create () {
		super.create();
		Prefs.init();
		this.setScreen(new SplashScreen(this));
//		this.setScreen(new GameScreen(this));
	}
}
