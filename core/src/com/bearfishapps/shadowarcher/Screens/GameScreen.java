package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.shadowarcher.Physics.PhysicsWorld;

public class GameScreen extends GeneralScreens {
    private PhysicsWorld physicsWorld;
    public GameScreen(GdxGame game) {
        super(game);
        physicsWorld = new PhysicsWorld();
//        setBackgroundColor(255, 255, 255, 1);
    }

    @Override
    public void step(float delta, float animationKeyFrame) {
        physicsWorld.step();
    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        physicsWorld.initUserInput(multiplexer, camera);
        stage.addActor(physicsWorld);
    }

    @Override
    public void destroy() {
        physicsWorld.dispose();
    }
}
