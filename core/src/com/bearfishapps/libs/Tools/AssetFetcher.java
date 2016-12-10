package com.bearfishapps.libs.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetFetcher {
    private AssetManager assetManager;

    public AssetFetcher() {
        assetManager = new AssetManager();
    }

    public void loadLogo() {
        assetManager.load(Constants.logo1, Texture.class);
        assetManager.load(Constants.logo2, Texture.class);
    }

    public void loadEverything() {
        assetManager.load(Constants.atlas, TextureAtlas.class);
    }

    public AssetManager get() {
        return assetManager;
    }

    public void clear() {
        assetManager.clear();
    }
}
