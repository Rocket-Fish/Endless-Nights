package com.bearfishapps.shadowarcher.Screens.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class LabelButtonBundle {
    private Table table;
    private Label label;
    private ImageButton button;
    public LabelButtonBundle(String text, int size, Color color, String font) {
        table = new Table();
//        table.setFillParent(true);
        CustomLabel.make(size, color, font);
        label = new Label(text, CustomLabel.style);
        CustomImageButton.make(TextureRegionService.simpleBtn);
        button = new ImageButton(CustomImageButton.style);
        table.add(button).right().padRight(10f);
        table.add(label).left();
    }

    public void setButton(TextureRegion path) {
        CustomImageButton.make(path);
        button.setStyle(CustomImageButton.style);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void addListener(ClickListener listener) {
        button.addListener(listener);
        label.addListener(listener);
    }

    public Table getTable() {
        return table;
    }
}
