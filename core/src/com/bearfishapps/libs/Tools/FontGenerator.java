package com.bearfishapps.libs.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontGenerator {

    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private static BitmapFont font;

    public static void generate(int size, String fontName) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        font = generator.generateFont(parameter);
    }

    public static BitmapFont returnFont() {
        return font;
    }

    public static void dispose() {
        generator.dispose();
    }
}
