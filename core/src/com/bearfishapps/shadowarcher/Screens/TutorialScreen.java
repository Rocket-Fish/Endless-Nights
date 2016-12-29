package com.bearfishapps.shadowarcher.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bearfishapps.libs.GdxGame;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.CustomClasses.CustomImageButton;
import com.bearfishapps.libs.Tools.CustomClasses.CustomLabel;
import com.bearfishapps.libs.Tools.Prefs;
import com.bearfishapps.shadowarcher.MainGameClass;
import com.bearfishapps.shadowarcher.Physics.Assets.TextureRegionService;

public class TutorialScreen extends GameScreen {
    private int state = 0;
    private Table tmpTable;
    private Label content, prompt;
    private boolean moveOn = true;
    public TutorialScreen(final GdxGame game) {
        super(game, false);
        tmpTable = new Table();
//        tmpTable.setDebug(true);
        CustomLabel.make(12, Color.WHITE, Constants.blackopsone);
        content = new Label("Tutorial", CustomLabel.style);
        prompt = new Label("Tap Here to continue", CustomLabel.style);
        tmpTable.setPosition(0, 0);
        tmpTable.add(content).row();
        tmpTable.add(prompt).right();
        tmpTable.setFillParent(false);
        tmpTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state ++;
                moveOn = true;
                physicsWorld.setPause(false);
                prevFrame = keyFrame;

                if(state >6) {
                    tmpTable.setVisible(false);
                    Prefs.setBoolean(Constants.firstRun, false);
                    if(((MainGameClass)game).getService().isSignedIn())
                        ((MainGameClass)game).getService().unlockAchievementGPGS(((MainGameClass)game).ACH_COMPLETE_TUTORIAL);
                }
            }
        });
        stage2.addActor(tmpTable);
    }

    private float prevFrame = 0;
    private float keyFrame;
    @Override
    public void step(float delta, float animationKeyFrame) {
        keyFrame = animationKeyFrame;
        super.step(delta, animationKeyFrame);
        if (moveOn) {
            if (state == 0) {
                if(animationKeyFrame-prevFrame > 0.1f) {
                    moveOn = false;
                    physicsWorld.setPause(true);
                    Vector2 target = new Vector2(physicsWorld.getP1().getHumanoid().getBodies()[0].getWorldCenter());
                    target = target.scl(13.3f);
                    target.x += 130;
                    target.y += 50;

                    tmpTable.addAction(Actions.sequence(Actions.moveTo(target.x, target.y)));
                    content.setText("This is Your Character,\nTap Screen to move\nDrag Screen to Shoot");
                }
            } else if (state == 1) {
                if(animationKeyFrame-prevFrame > 0.3f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

                    tmpTable.addAction(Actions.sequence(Actions.moveTo(400, 50)));
                    content.setText("Enemies Spawn from the Right,\nThey Will Follow You\nAnd Shoot Arrows at You");
                }
            } else if (state == 2) {
                if(animationKeyFrame-prevFrame > 0.6f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

                    tmpTable.addAction(Actions.sequence(Actions.moveTo(200, 250)));
                    content.setText("Stay Mobile to live\nYou only have one life");
                }
            } else if (state == 3) {
                if(animationKeyFrame-prevFrame > 0.3f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

                    tmpTable.addAction(Actions.sequence(Actions.moveTo(250, 200)));
                    content.setText("Watch for boxes\nThat drop from above\nThey give boosts");
                }
            } else if (state == 4) {
                if(animationKeyFrame-prevFrame > 0.3f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

//                    tmpTable.addAction(Actions.moveTo(250, 200));
                    content.setText("Boxes may change your arrows\n Or may spawn objects");
                }
            } if (state == 5) {
                if(animationKeyFrame-prevFrame > 0.3f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

//                    tmpTable.addAction(Actions.moveTo(250, 200));
                    content.setText("They are Color Coded\nSo Choose Wisely");
                }
            } else if (state == 6) {
                if(animationKeyFrame-prevFrame > 0.3f) {
                    moveOn = false;
                    physicsWorld.setPause(true);

//                    tmpTable.addAction(Actions.moveTo(250, 200));
                    content.setText("But Most Importantly\n Have Fun");
                }
            }

        }
    }

    @Override
    public void preShow(Table table, InputMultiplexer multiplexer) {
        super.preShow(table, multiplexer);
    }
}
