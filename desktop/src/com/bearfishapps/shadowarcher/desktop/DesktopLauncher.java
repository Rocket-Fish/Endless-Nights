package com.bearfishapps.shadowarcher.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bearfishapps.shadowarcher.GPGSInterface;
import com.bearfishapps.shadowarcher.MainGameClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 900;
		config.height = 480;
		config.title = "Shadow Archer";
		new LwjglApplication(new MainGameClass(new GPGSInterface() {

			@Override
			public boolean signIn()
			{
				System.out.println("DesktopGoogleServies: signIn()");
				return true;
			}

			@Override
			public boolean signOut()
			{
				System.out.println("DesktopGoogleServies: signOut()");
				return false;
			}

			@Override
			public void rateGame()
			{
				System.out.println("DesktopGoogleServices: rateGame()");
			}

			@Override
			public boolean submitScore(long score)
			{
				System.out.println("DesktopGoogleServies: submitScore(" + score + ")");
				return false;
			}

			@Override
			public boolean showScores()
			{
				System.out.println("DesktopGoogleServies: showScores()");
				return false;
			}

			@Override
			public boolean isSignedIn()
			{
				System.out.println("DesktopGoogleServies: isSignedIn()");
				return false;
			}

			@Override
			public void unlockAchievementGPGS(String achievementId) {

			}

			@Override
			public boolean getAchievementsGPGS() {
				return false;
			}

			@Override
			public int loadScoreOfLeaderBoard() {
				return 0;
			}
		}), config);
	}
}
