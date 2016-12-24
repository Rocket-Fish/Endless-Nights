package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;
import com.bearfishapps.shadowarcher.Physics.PhysicsWorld;

public class GameScreen extends GeneralScreens {
    private PhysicsWorld physicsWorld;
    private ImageButton pauseButton;
    public GameScreen(GdxGame game) {
        super(game, 45, 24);
        physicsWorld = new PhysicsWorld();
        setBackgroundColor(255, 255, 0, 1);

        CustomImageButton.make(TextureRegionService.pauseButton);
        pauseButton = new ImageButton(CustomImageButton.style);

    }

    @Override
    public void step(float delta, float animationKeyFrame) {
        physicsWorld.step(delta);
    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        physicsWorld.initUserInterface(multiplexer, camera);
        stage.addActor(physicsWorld);

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Pause Button", "Pressed");
            }
        });
        table.setDebug(true);
        table.center().top();
        table.add(pauseButton).pad(0.1f).width(2.2f).height(2.2f).row();

    }

    @Override
    public void destroy() {
        physicsWorld.dispose();
    }
}
