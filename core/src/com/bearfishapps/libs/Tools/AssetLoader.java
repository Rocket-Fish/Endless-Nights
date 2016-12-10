package com.bearfishapps.libs.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetLoader {
    private AssetManager assetManager;

    public AssetLoader() {
        assetManager = new AssetManager();
    }

    public void loadLogo() {
        assetManager.load(Constants.logo, Texture.class);
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
