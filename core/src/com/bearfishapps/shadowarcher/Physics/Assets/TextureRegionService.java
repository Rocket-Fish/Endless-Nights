package com.bearfishapps.shadowarcher.Physics.Assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRegionService {

    public static TextureRegion
            playButton ,
            quitButton ,
            pauseButton,
            creditsBtn ,
            deleteBtn  ,
            downButton ,
            exitButton ,
            fireButton ,
            gameservBtn,
            iconXButton,
            leftButton ,
            menuButton ,
            musicButton,
            musicOffBtn,
            noButton   ,
            questionBtn,
            restartBtn ,
            rightButton,
            settingsBtn,
            setting2Btn,
            simpleBtn  ,
            simpleBlock,
            soundButton,
            soundOffBtn,
            upButton   ,
            yesButton  ;

    public static void mapAll(TextureAtlas atlas) {
        playButton  = atlas.findRegion("_UI/playBtn");
        quitButton  = atlas.findRegion("_UI/ExitBig");
        pauseButton = atlas.findRegion("_UI/pause");

        creditsBtn  = atlas.findRegion("_UI/credits");
        deleteBtn   = atlas.findRegion("_UI/DeleteBtn");
        downButton  = atlas.findRegion("_UI/down");
        exitButton  = atlas.findRegion("_UI/Exit");
        fireButton  = atlas.findRegion("_UI/FireShootBtn");
        gameservBtn = atlas.findRegion("_UI/gameService");
        iconXButton = atlas.findRegion("_UI/IconX");
        leftButton  = atlas.findRegion("_UI/left");
        menuButton  = atlas.findRegion("_UI/Menu");
        musicButton = atlas.findRegion("_UI/music");
        musicOffBtn = atlas.findRegion("_UI/musicOff");
        noButton    = atlas.findRegion("_UI/NoBtn");
        questionBtn = atlas.findRegion("_UI/Question");
        restartBtn  = atlas.findRegion("_UI/Restart");
        rightButton = atlas.findRegion("_UI/right");
        settingsBtn = atlas.findRegion("_UI/settings");
        setting2Btn = atlas.findRegion("_UI/settings2");
        simpleBtn   = atlas.findRegion("_UI/simple");
        simpleBlock = atlas.findRegion("_UI/simpleBlock");
        soundButton = atlas.findRegion("_UI/sound");
        soundOffBtn = atlas.findRegion("_UI/soundOff");
        upButton    = atlas.findRegion("_UI/up");
        yesButton   = atlas.findRegion("_UI/Yes Button");
    }

    private TextureRegionService() {

    }
}
