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
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.MainGameClass;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;
import com.bearfishapps.shadowarcher.Screens.UI.BackActionCallback;
import com.bearfishapps.shadowarcher.Screens.UI.GPGSTable;

public class GameOverScreen extends GeneralScreens {
    private Label title, comment;
    private ImageButton retryButton, quitButton, servicesButton;
    private boolean is2PlayerGame;
    private GPGSTable gtable;
    public GameOverScreen(GdxGame game, int score) {
        super(game, 900, 480);
        MainGameClass mainGame = ((MainGameClass)game);
        CustomLabel.make(84, Color.WHITE, Constants.armalite);
        title = new Label("Game Over", CustomLabel.style);
        if(mainGame.getService().isSignedIn()) {
            int ss = (mainGame.getService().loadScoreOfLeaderBoard());
            if(ss > Prefs.getScore()) {
                Prefs.setScore(ss);
            }
        }

        String content = "";
        if(score<0) {
            is2PlayerGame = true;
            switch (score) {
                case -2: content = "Left Side Wins";break;
                case -1: content = "Right Side Wins";break;
            }

            if(mainGame.getService().isSignedIn()) {
                mainGame.getService().unlockAchievementGPGS(mainGame.ACH_PLAY_WITH_A_FRIEND);
            }

        } else {
            is2PlayerGame = false;
            content = "Score: "+score;
            if(score> Prefs.getScore()) {
                Prefs.setScore(score);
                title.setText("High Score");
                ((MainGameClass)game).getService().submitScore(score);
            }
            if(mainGame.getService().isSignedIn()) {
                mainGame.getService().unlockAchievementGPGS(mainGame.ACH_FINISH_A_GAME);
                if(score>=40)
                    mainGame.getService().unlockAchievementGPGS(mainGame.ACH_SURVIVOR);
                if(score>=100)
                    mainGame.getService().unlockAchievementGPGS(mainGame.ACH_HUNDRED);
            }
        }

        CustomLabel.make(48, Color.WHITE, Constants.armalite);
        comment = new Label(content, CustomLabel.style);

        CustomImageButton.make(TextureRegionService.restartBtn);
        retryButton = new ImageButton(CustomImageButton.style);

        CustomImageButton.make(TextureRegionService.leftButton);
        quitButton = new ImageButton(CustomImageButton.style);
        CustomImageButton.make(TextureRegionService.gameservBtn);
        servicesButton = new ImageButton(CustomImageButton.style);

        gtable = new GPGSTable((MainGameClass) game);
        gtable.getTable().setVisible(false);

    }

    @Override
    public void step(float delta, float animationKeyFrame) {

    }

    @Override
    public void postRender(float delta, float animationKeyFrame) {

    }

    @Override
    public void preShow(final Table table, InputMultiplexer multiplexer) {
        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, is2PlayerGame));
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
                        game.setScreen(new MainMenuScreen(game));
                    }
                })));
            }
        });

        servicesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gtable.getTable().addAction(Actions.sequence(Actions.alpha(0f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        gtable.getTable().setVisible(true);
                    }
                }), Actions.fadeIn(1f)));
                table.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        table.setVisible(false);
                    }
                })));
            }
        });

        gtable.setBackCallback(new BackActionCallback() {
            @Override
            public void onCall() {
                table.addAction(Actions.sequence(Actions.alpha(0f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        table.setVisible(true);
                    }
                }), Actions.fadeIn(1f)));
                gtable.getTable().addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        gtable.getTable().setVisible(false);
                    }
                })));
            }
        });


//        table.setDebug(true);
        table.center().top();
        table.add(title).pad(14).row();
        table.add(comment).pad(7).row();
        table.add(retryButton).pad(7).height(34).width(34).row();
        table.add(servicesButton).pad(7).width(34).height(34).center().row();
        table.add(quitButton).pad(7).width(34).height(34).center().row();

        stage.addActor(gtable.getTable());
    }

    @Override
    public void destroy() {

    }
}
