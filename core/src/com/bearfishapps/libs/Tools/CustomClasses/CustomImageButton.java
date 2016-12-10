package com.bearfishapps.libs.Tools.CustomClasses;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CustomImageButton {

    public static ImageButton.ImageButtonStyle style;

    public static void make(TextureRegion region1) {
        make(region1, region1);
    }

    public static void make(TextureRegion region1, TextureRegion region2) {

        style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(region1);
        style.down = new TextureRegionDrawable(region2);

    }
}
