package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class MainMenuScreen extends GeneralScreens{
    private Label title;
    private ImageButton playButton, multiplayerButton, quitButton, questionButton, soundButton, servicesButton, infoButton;

    public MainMenuScreen(GdxGame game) {
        super(game, 900, 480);

        CustomLabel.make(94, Color.WHITE, Constants.armalite);
        title = new Label("Shadow Archer", CustomLabel.style);

        CustomImageButton.make(TextureRegionService.playButton);
        playButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.pvpButton);
        multiplayerButton = new ImageButton(CustomImageButton.style);

        CustomImageButton.make(TextureRegionService.quitButton);
        quitButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.questionBtn);
        questionButton = new ImageButton(CustomImageButton.style);

        if(Prefs.isTrue(Constants.soundOn))
            CustomImageButton.make(TextureRegionService.soundButton);
        else
            CustomImageButton.make(TextureRegionService.soundOffBtn);
        soundButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.gameservBtn);
        servicesButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.creditsBtn);
        infoButton = new ImageButton(CustomImageButton.style);

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
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.app.exit();
                    }
                })));
            }
        });

        soundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                        Prefs.setBoolean(Constants.soundOn, !Prefs.isTrue(Constants.soundOn));
                        if(Prefs.isTrue(Constants.soundOn))
                            CustomImageButton.make(TextureRegionService.soundButton);
                        else
                            CustomImageButton.make(TextureRegionService.soundOffBtn);
                        soundButton.setStyle(CustomImageButton.style);
                    }
        });

//        table.setDebug(true);
        table.center().top();
        table.add(title).pad(14).colspan(4).row();
        table.add(playButton).colspan(2).pad(7).right().width(64).height(64);
        table.add(multiplayerButton).colspan(2).pad(7).left().height(64).width(64).row();
        table.add(questionButton).colspan(4).pad(7).width(34).height(34).center().row();

        table.add(servicesButton).pad(7).width(34).height(34);
        table.add(infoButton).colspan(2).pad(7).width(34).height(34);
        table.add(soundButton).pad(7).width(34).height(34).row();

        table.add(quitButton).colspan(4).pad(7).width(64).height(64).center().row();

    }

    @Override
    public void destroy() {
        CustomLabel.dispose();
        CustomButton.dispose();
    }
}
