package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.libs.Tools.FontGenerator;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;
import com.bearfishapps.shadowarcher.Physics.PhysicsWorld;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;

public class GameScreen extends GeneralScreens {
    private PhysicsWorld physicsWorld;
    private ImageButton changeGameStateButton, quitButton, restartButton;
    private Label scoreLabel;
    private boolean twoPlayer;

    private Stage stage2;
    private Camera cam2;
    // only for singleplayer
    public GameScreen(GdxGame game, boolean twoPlayer) {
        super(game, 45, 24);
        if(!twoPlayer) {
            CustomLabel.make(160, new Color(1, 1, 1, 0.1f),Constants.blackopsone);
            scoreLabel = new Label("0", CustomLabel.style);
            scoreLabel.setPosition(100, -50);

            cam2 = new OrthographicCamera();
            Viewport vp2 = new ExtendViewport(300, 160, cam2);
            ((ExtendViewport)vp2).setMaxWorldWidth(300);
            ((ExtendViewport)vp2).setMaxWorldHeight(160*2);
//            cam2.position.set(-300/2, -160/2, 0);
            cam2.update();

            stage2 = new Stage(vp2);
            stage2.addActor(scoreLabel);

            cam2.update();
        }
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
            scoreLabel.setText(String.valueOf(physicsWorld.checkScore()));
            if(physicsWorld.isSingleplayerDead()) {
                game.setScreen(new GameOverScreen(game, physicsWorld.checkScore()));
            }
        }
    }

    private ShapeRenderer renderer = new ShapeRenderer();
    private boolean fadeOut = false; private float initFrame = -1;
    @Override
    public void postRender(float delta, float animationKeyFrame) {
        if(!twoPlayer) {
            stage2.act();
            stage2.draw();
        }

        if(animationKeyFrame<=2f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            renderer.setProjectionMatrix(camera.combined);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(new Color(0, 0, 0, 1f-animationKeyFrame/2f));
            renderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
            renderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        if(fadeOut) {
            if(initFrame < 0)
                initFrame = animationKeyFrame;
            else if(animationKeyFrame-initFrame<=1) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.setProjectionMatrix(camera.combined);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(new Color(0, 0, 0, (animationKeyFrame - initFrame)));
                renderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
                renderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            } else {
                renderer.setProjectionMatrix(camera.combined);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(new Color(0, 0, 0, 1));
                renderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
                renderer.end();

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
                fadeOut = true;
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), new RunnableAction() {
                    @Override
                    public void run() {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }));
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fadeOut = true;
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), new RunnableAction() {
                    @Override
                    public void run() {
                        game.setScreen(new GameScreen(game, twoPlayer));
                    }
                }));
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        stage.addActor(physicsWorld);

//        table.setDebug(true);
        table.setRound(false);
        if(twoPlayer)
            table.center().top();
        else {
            table.right().top();
//            table.add(scoreLabel).colspan(3).expandX();
        }
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
