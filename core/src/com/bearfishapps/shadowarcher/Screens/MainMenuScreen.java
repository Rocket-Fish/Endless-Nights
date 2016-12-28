package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.CustomClasses.CustomButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.libs.Tools.CustomClasses.CustomTextButton;
import com.bearfishapps.libs.Tools.FontGenerator;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class MainMenuScreen extends GeneralScreens{
    private Label title;
    private ImageButton playButton, multiplayerButton, quitButton, questionButton;

    public MainMenuScreen(GdxGame game) {
        super(game, 900, 480);

        CustomLabel.make(84, Color.WHITE, Constants.armalite);
        title = new Label("Shadow Archer", CustomLabel.style);

        CustomImageButton.make(TextureRegionService.playButton);
        playButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.fireButton);
        multiplayerButton = new ImageButton(CustomImageButton.style);

        CustomImageButton.make(TextureRegionService.quitButton);
        quitButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.questionBtn);
        questionButton = new ImageButton(CustomImageButton.style);

    }

    @Override
    public void step(float delta, float animationKeyFrame) {

    }

    @Override
    public void postRender(float delta, float animationKeyFrame) {

    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, false));
                    }
                })));
            }
        });

        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, true));
                    }
                })));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

//        table.setDebug(true);
        table.center().top();
        table.add(title).pad(14).colspan(2).row();
        table.add(playButton).pad(7).right().width(64).height(64);
        table.add(multiplayerButton).pad(7).left().height(64).width(64).row();
        table.add(questionButton).colspan(2).pad(7).width(34).height(34).center().row();
        table.add(quitButton).colspan(2).pad(7).width(64).height(64).center().row();
    }

    @Override
    public void destroy() {
        CustomLabel.dispose();
        CustomButton.dispose();
    }
}
