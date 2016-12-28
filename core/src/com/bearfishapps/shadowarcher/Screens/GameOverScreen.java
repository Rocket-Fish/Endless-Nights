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
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class GameOverScreen extends GeneralScreens {
    private Label title, comment;
    private ImageButton retryButton, quitButton, questionButton;
    private boolean is2PlayerGame;
    public GameOverScreen(GdxGame game, int score) {
        super(game, 900, 480);

        CustomLabel.make(84, Color.WHITE, Constants.armalite);
        title = new Label("Game Over", CustomLabel.style);

        String content = "";
        if(score<0) {
            is2PlayerGame = true;
            switch (score) {
                case -2: content = "Left Side Wins";break;
                case -1: content = "Right Side Wins";break;
            }
        } else {
            is2PlayerGame = false;
            content = "Score: "+score;
        }

        CustomLabel.make(48, Color.WHITE, Constants.armalite);
        comment = new Label(content, CustomLabel.style);

        CustomImageButton.make(TextureRegionService.restartBtn);
        retryButton = new ImageButton(CustomImageButton.style);

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
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, is2PlayerGame));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        table.setDebug(true);
        table.center().top();
        table.add(title).pad(14).row();
        table.add(comment).pad(7).row();
        table.add(retryButton).pad(7).height(34).width(34).row();
        table.add(questionButton).pad(7).width(34).height(34).center().row();
        table.add(quitButton).pad(7).width(64).height(64).center().row();
    }

    @Override
    public void destroy() {

    }
}
