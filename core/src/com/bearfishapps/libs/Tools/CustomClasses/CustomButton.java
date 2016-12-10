package com.bearfishapps.libs.Tools.CustomClasses;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.FontGenerator;

public class CustomButton {

    private static BitmapFont font;

    public static TextButton.TextButtonStyle style;

    public static void make(int size, TextureRegion region1, TextureRegion region2, Color color) {

        FontGenerator.generate(size, Constants.virgo);
        font = FontGenerator.returnFont();

        style = new TextButton.TextButtonStyle();
        style.up = new TextureRegionDrawable(region1);
        style.down = new TextureRegionDrawable(region2);
        style.font = font;
        style.fontColor = color;
    }

    public static void dispose() {
        font.dispose();
        FontGenerator.dispose();
    }
}
