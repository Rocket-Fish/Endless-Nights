package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;
import com.bearfishapps.shadowarcher.Physics.PhysicsWorld;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;

public class GameScreen extends GeneralScreens {
    private PhysicsWorld physicsWorld;
    private ImageButton changeGameStateButton, quitButton, restartButton;
    private boolean twoPlayer;
    public GameScreen(GdxGame game, boolean twoPlayer) {
        super(game, 45, 24);
        Arrow.reset();
        physicsWorld = new PhysicsWorld(camera, twoPlayer);
        setBackgroundColor(255, 255, 255, 1);
        this.twoPlayer = twoPlayer;

        CustomImageButton.make(TextureRegionService.pauseButton);
        changeGameStateButton = new ImageButton(CustomImageButton.style);

        CustomImageButton.make(TextureRegionService.exitButton);
        quitButton = new ImageButton(CustomImageButton.style);
        quitButton.setVisible(false);
        CustomImageButton.make(TextureRegionService.restartBtn);
        restartButton = new ImageButton(CustomImageButton.style);
        restartButton.setVisible(false);

    }

    @Override
    public void step(float delta, float animationKeyFrame) {
        physicsWorld.step(delta);
        if(twoPlayer) {
            int res = physicsWorld.checkScore();
            if (res != -300) {
                game.setScreen(new GameOverScreen(game, res));
            }
        } else {
            if(physicsWorld.isSingleplayerDead()) {
                game.setScreen(new GameOverScreen(game, physicsWorld.checkScore()));
            }
        }
    }

    @Override
    public void show() {
        changeGameStateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!physicsWorld.isPaused()) {
                    Gdx.app.log("Pause Button", "Pressed");

                    physicsWorld.setPause(true);
                    CustomImageButton.make(TextureRegionService.rightButton);
                    changeGameStateButton.setStyle(CustomImageButton.style);

                    quitButton.addAction(Actions.sequence(Actions.alpha(0), new RunnableAction() {
                        @Override
                        public void run() {
                            quitButton.setVisible(true);
                        }
                    }, Actions.fadeIn(0.5f)));
                    restartButton.addAction(Actions.sequence(Actions.alpha(0), new RunnableAction() {
                        @Override
                        public void run() {
                            restartButton.setVisible(true);
                        }
                    }, Actions.fadeIn(0.5f)));
                } else {
                    Gdx.app.log("Play Button", "Pressed");

                    CustomImageButton.make(TextureRegionService.pauseButton);
                    changeGameStateButton.setStyle(CustomImageButton.style);
                    physicsWorld.setPause(false);

                    quitButton.addAction(Actions.sequence(Actions.fadeOut(0.5f), new RunnableAction() {
                        @Override
                        public void run() {
                            quitButton.setVisible(false);
                        }
                    }));
                    restartButton.addAction(Actions.sequence(Actions.fadeOut(0.5f), new RunnableAction() {
                        @Override
                        public void run() {
                            restartButton.setVisible(false);
                        }
                    }));
                }
            }
        });
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, twoPlayer));
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        stage.addActor(physicsWorld);

        table.setDebug(true);
        table.center().top();
        table.add(changeGameStateButton).pad(0.1f).width(2.8f).height(2.8f).row();
        table.add(restartButton).pad(0.1f).width(2.8f).height(2.8f).row();
        table.add(quitButton).pad(0.1f).width(2.8f).height(2.8f).row();

        // UI and interaction stuff
        table.setFillParent(true);
        stage.addActor(table);
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        multiplexer.addProcessor(stage);

        physicsWorld.initUserInterface(multiplexer);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {

    }

    @Override
    public void destroy() {
        physicsWorld.dispose();
    }
}
