package com.bearfishapps.libs.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {
    private static Preferences prefs;

    public static void init() {
        prefs = Gdx.app.getPreferences(Constants.prefName);
        if (!prefs.contains(Constants.scoreName)) {
            prefs.putInteger(Constants.scoreName, 0);
            prefs.flush();
        }
    }

    public static void setScore(int score) {
        prefs.putInteger(Constants.scoreName, score);
        prefs.flush();
    }

    public static int getScore() {
        return prefs.getInteger(Constants.scoreName);
    }
}
