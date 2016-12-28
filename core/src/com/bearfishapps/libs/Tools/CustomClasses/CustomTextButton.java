package com.bearfishapps.libs.Tools.CustomClasses;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bearfishapps.libs.Tools.FontGenerator;

public class CustomTextButton {
    public static TextButton.TextButtonStyle style;

    public static void make(TextureRegion region1, String font) {
        make(region1, Color.BLACK, 20, font);
    }

    public static void make(TextureRegion region1, Color color, int fontsize, String font) {
        make(region1, region1, color, fontsize, font);
    }

    public static void make(TextureRegion region1, TextureRegion region2, Color color, int size, String font) {
        FontGenerator.generate(size, font);
        style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(region1);
        style.down = new TextureRegionDrawable(region2);
        style.font = FontGenerator.returnFont();
        style.fontColor = color;
    }

    public static void dispose() {
        FontGenerator.dispose();
    }

}
