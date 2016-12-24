package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.CustomClasses.CustomButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class MainMenuScreen extends GeneralScreens{
    private Label title;
    private ImageButton playButton, quitButton;

    public MainMenuScreen(GdxGame game) {
        super(game, 900, 480);

        CustomLabel.make(48, Color.WHITE, Constants.tycho);
        title = new Label("Shadow Archers", CustomLabel.style);

        CustomImageButton.make(TextureRegionService.playButton);
        playButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.quitButton);
        quitButton = new ImageButton(CustomImageButton.style);

    }

    @Override
    public void step(float delta, float animationKeyFrame) {

    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.setDebug(true);
        table.top().right();
        table.add(title).pad(20).row();
        table.add(playButton).pad(10).width(100).height(100).right().row();
        table.add(quitButton).pad(10).width(100).height(100).right().row();
    }

    @Override
    public void destroy() {
        CustomLabel.dispose();
        CustomButton.dispose();
    }
}
