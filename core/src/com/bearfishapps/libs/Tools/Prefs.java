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
        if (!prefs.contains(Constants.firstRun)) {
            prefs.putBoolean(Constants.firstRun, true);
            prefs.flush();
        }
        if (!prefs.contains(Constants.soundOn)) {
            prefs.putBoolean(Constants.soundOn, false);
            prefs.flush();
        }
    }

    public static void setBoolean(String name, boolean value) {
        prefs.putBoolean(name, value);
        prefs.flush();
    }

    public static boolean isTrue(String name) {
        return prefs.getBoolean(name);
    }

    public static void setInteger(String name, int value) {
        prefs.putInteger(name, value);
        prefs.flush();
    }

    public static int getIntValue(String name) {
        return prefs.getInteger(name);
    }

    public static void setScore(int score) {
        prefs.putInteger(Constants.scoreName, score);
        prefs.flush();
    }

    public static int getScore() {
        return prefs.getInteger(Constants.scoreName);
    }
}
