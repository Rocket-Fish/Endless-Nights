package com.bearfishapps.shadowarcher.Screens.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.shadowarcher.MainGameClass;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

import java.util.ArrayList;

public class GPGSTable {
    private Table table;
    private MainGameClass game;
    private Label label;
    private ArrayList<LabelButtonBundle> bundles = new ArrayList<LabelButtonBundle>();
    public GPGSTable(MainGameClass game) {
        this.game = game;

        table = new Table();
        table.setFillParent(true);
        CustomLabel.make(48, Color.WHITE, Constants.blackopsone);
        label = new Label("Game Services", CustomLabel.style);

        initTable();
    }
    private BackActionCallback bacb = null;
    public void setBackCallback(BackActionCallback bacb) {
        this.bacb = bacb;
    }

    private int littleTextSize = 24;
    private void initTable() {
        table.clearChildren();
        bundles.clear();
        if(game.getService().isSignedIn()){
            final LabelButtonBundle b1 = new LabelButtonBundle("Sign Out", littleTextSize, Color.WHITE, Constants.blackopsone);
            b1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.getService().signOut()) {
                        initTable();
                    } else {
                        b1.setText("Sign Out Failed");
                    }
                }
            });
            bundles.add(b1);
            final LabelButtonBundle b2 = new LabelButtonBundle("Leaderboard", littleTextSize, Color.WHITE, Constants.blackopsone);
            b2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.getService().showScores()) {
                    } else {
                        b2.setText("Load Leaderboard Failed");
                    }
                }
            });
            bundles.add(b2);
            final LabelButtonBundle b3 = new LabelButtonBundle("Achievements", littleTextSize, Color.WHITE, Constants.blackopsone);
            b3.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.getService().getAchievementsGPGS()) {

                    } else {
                        b3.setText("Load Achievements Failed");
                    }
                }
            });
            bundles.add(b3);

        } else {
            final LabelButtonBundle b1 = new LabelButtonBundle("Sign In", littleTextSize, Color.WHITE, Constants.blackopsone);
            b1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(game.getService().signIn()) {
                        initTable();
                    } else {
                        b1.setText("Sign In Failed");
                    }
                }
            });
            bundles.add(b1);
        }
        final LabelButtonBundle bb = new LabelButtonBundle("Back", littleTextSize, Color.WHITE, Constants.blackopsone);
        bb.setButton(TextureRegionService.leftButton);
        bb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(bacb != null) {
                    bacb.onCall();
                }
            }
        });
        bundles.add(bb);

        table.center();
//        table.setDebug(true);
        table.add(label).row();
        for(LabelButtonBundle b: bundles) {
            table.add(b.getTable()).left().row();
        }

    }

    public Table getTable() {
        return table;
    }
}
