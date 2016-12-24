package com.bearfishapps.shadowarcher.Physics.Assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionService {
    public static TextureRegion loading1, loading2, loading3, loading4, loading5, loading6, loading7, loading8;
    public static Animation loadingAnimation;

    public static void mapLoading(TextureAtlas atlas) {
        loading1 = atlas.findRegion("_loading/matt-icons_busy01-800px");
        loading2 = atlas.findRegion("_loading/matt-icons_busy02-800px");
        loading3 = atlas.findRegion("_loading/matt-icons_busy03-800px");
        loading4 = atlas.findRegion("_loading/matt-icons_busy04-800px");
        loading5 = atlas.findRegion("_loading/matt-icons_busy05-800px");
        loading6 = atlas.findRegion("_loading/matt-icons_busy06-800px");
        loading7 = atlas.findRegion("_loading/matt-icons_busy07-800px");
        loading8 = atlas.findRegion("_loading/matt-icons_busy08-800px");

        TextureRegion[] loading = {loading1, loading2, loading3, loading4, loading5, loading6, loading7, loading8};
        loadingAnimation = new Animation(0.05f, loading);
        loadingAnimation.setPlayMode(Animation.PlayMode.LOOP);

    }

    public static TextureRegion playButton, quitButton, pauseButton;
    public static TextureRegion cogWheel3, cogWheel2, cogWheel1, woodWheel, barbWheel, fanWheel, frame, coreFrame, fuelFrame;
    public static void mapAll(TextureAtlas atlas) {
        playButton = atlas.findRegion("_UI/playBtn");
        quitButton = atlas.findRegion("_UI/ExitBig");
        pauseButton = atlas.findRegion("_UI/pause");

        cogWheel1 = atlas.findRegion("_Objects/cogWheel");
        cogWheel2 = atlas.findRegion("_Objects/cogWheel2");
        cogWheel3 = atlas.findRegion("_Objects/cogWheel3");
        woodWheel = atlas.findRegion("_Objects/barbedWheel");
        barbWheel = atlas.findRegion("_Objects/woodWheel");
        fanWheel = atlas.findRegion("_Objects/fanWheel");

        frame     = atlas.findRegion("_Objects/frame");
        coreFrame = atlas.findRegion("_Objects/heart");
        fuelFrame = atlas.findRegion("_Objects/electricity");
    }

    private TextureRegionService() {

    }
}
