package com.bearfishapps.libs.Tools.CustomClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.FontGenerator;

public class CustomLabel {
    public static Label.LabelStyle style;

    public static void make(int size) {
        make(size, Color.WHITE);
    }

    public static void make(int size, Color color) {
        FontGenerator.generate(size, Constants.tycho);
        style = new Label.LabelStyle(FontGenerator.returnFont(), color);
    }

    public static void dispose() {
        FontGenerator.dispose();
    }
}
