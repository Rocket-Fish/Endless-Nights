package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.bearfishapps.libs.GeneralScreens;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.FontGenerator;
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.MainGameClass;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class SplashScreen extends GeneralScreens {
    public SplashScreen(MainGameClass game) {
        super(game);
//        setBackgroundColor(255, 255, 255, 1);
        game.getLoader().loadEverything();
    }

    private boolean runOnce = false;
    @Override
    public void step(float delta, float animationKeyFrame) {
        if (game.getLoader().get().update() && animationKeyFrame >= 3 && !runOnce) {
            runOnce = true;
            FontGenerator.generate(320, Constants.blackopsone_numOnly);
            ((MainGameClass)game).superLargeFont = FontGenerator.returnFont();
            TextureRegionService.mapAll(game.getLoader().get().get(Constants.atlas, TextureAtlas.class));
            stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(new Runnable() {
                public void run() {
                    if(Prefs.isTrue(Constants.firstRun))
                        game.setScreen(new TutorialScreen(game));
                    else
                        game.setScreen(new MainMenuScreen(game));
                }
            })));

        }
    }

    @Override
    public void postRender(float delta, float animationKeyFrame) {

    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        Image logo1 = new Image(game.getLoader().get().get(Constants.logo1, Texture.class));
        logo1.setScaling(Scaling.fit);
        Image logo2 = new Image(game.getLoader().get().get(Constants.logo2, Texture.class));
        logo2.setScaling(Scaling.fit);

//        table.setDebug(true);
        table.bottom().left();
        table.add(logo1).width(300).height(200).fill().padRight(10);
        table.add(logo2).width(200).height(200).fill().row();
    }

    @Override
    public void destroy() {

    }
}
