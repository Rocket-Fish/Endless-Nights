package com.bearfishapps.libs;

import com.badlogic.gdx.Game;
import com.bearfishapps.libs.Tools.AssetFetcher;

public abstract class GdxGame extends Game {
    // Shell Class

    private AssetFetcher loader;

    @Override
    public void create() {
        loader = new AssetFetcher();
        loader.loadLogo();
        loader.get().finishLoading();
    }

    public AssetFetcher getLoader() {
        return loader;
    }
}
