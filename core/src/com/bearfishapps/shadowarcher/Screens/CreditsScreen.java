package com.bearfishapps.shadowarcher.Screens;

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
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class CreditsScreen extends GeneralScreens{
    private Label title, content;
    private ImageButton backButton;
    private String message = "Created By: RocketFish \n" +
            "Powered By: LibGDX & Box2D \n\n" +
            "If you want to make some music for this game, \n" +
            "email rocket.fish@mail.com. ";

    public CreditsScreen(GdxGame game) {
        super(game);
        CustomImageButton.make(TextureRegionService.leftButton);
        backButton = new ImageButton(CustomImageButton.style);

        CustomLabel.make(84, Color.WHITE, Constants.blackopsone);
        title = new Label("Credits", CustomLabel.style);
        CustomLabel.make(24, Color.WHITE, Constants.blackopsone);
        content = new Label(message, CustomLabel.style);

    }


    @Override
    public void step(float delta, float animationKeyFrame) {

    }

    @Override
    public void postRender(float delta, float animationKeyFrame) {

    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenuScreen(game));
                    }
                })));
            }
        });
        table.center().top();
        table.add(title).pad(14).colspan(4).row();
        table.add(content).pad(7).maxWidth(700).row();
        table.add(backButton).pad(7).row();

    }

    @Override
    public void destroy() {

    }
}
