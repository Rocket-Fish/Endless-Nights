package com.bearfishapps.libs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GeneralScreens implements Screen {
    // needed for screen changes
    protected GdxGame game;
    // default background color (black) 0/255f for all paremeters
    private int color[] = {0, 0, 0, 0};
    // keyframe for 2d animation
    private float keyFrame = 0;

    // camera and ui stuff
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Stage stage;
    protected Table table;

    // constructor with default screen size;
    public GeneralScreens(GdxGame game) {
        this(game, 800, 480);
    }

    // constructor with variable screen size;
    public GeneralScreens(GdxGame game, int sizeX, int sizeY) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(sizeX, sizeY, camera);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport);
        table = new Table();

        camera.update();
    }

    // change the background color to (rgb values must be between 0 and 255, and alpha must be between 0 and 100)
    protected void setBackgroundColor(int r, int g, int b, int a) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    @Override
    public void show() {
        // for input detection
        InputMultiplexer multiplexer = new InputMultiplexer();

        // UI and interaction stuff
        table.setFillParent(true);
        stage.addActor(table);
        stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1f)));
        multiplexer.addProcessor(stage);

        // abstract class in order to do custom settings
        preShow(table, multiplexer);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        keyFrame += delta;
        // flush everything on screen
        Gdx.gl.glClearColor(color[0] / 255.0f, color[1] / 255.0f, color[2] / 255.0f, color[3] / 100f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // for drawing
        step(delta, keyFrame);

        // for ui
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // resize camera
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        destroy();
    }

    // abstract classes for customization
    public abstract void step(float delta, float animationKeyFrame);

    public abstract void preShow(Table table, InputMultiplexer multiplexer);

    public abstract void destroy();
}
