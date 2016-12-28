package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bearfishapps.libs.Tools.Constants;
import com.bearfishapps.libs.Tools.FontGenerator;
import com.bearfishapps.shadowarcher.AI.SmartHumanoidBundle;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.Collision.StickyArrowClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionListener;
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.HumanInputProcessor;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.CustomPhysicsBody;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Obstacle;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.SimpleObject;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.StaticObjects.DeathPlatform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import box2dLight.RayHandler;

public class PhysicsWorld extends Actor{
    protected ShapeRenderer shapeRenderer;
    private boolean paused = false;
    private World world;
    private HumanInputProcessor humanInputProcessorP1;
    private HumanInputProcessor humanInputProcessorP2;

    private HumanGroundBundleGroup p1, p2;
    private ArrayList<HumanGroundBundleGroup> humanoidBundles = new ArrayList<HumanGroundBundleGroup>();
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    ArrayList<CustomPhysicsBody> otherBodies = new ArrayList<CustomPhysicsBody>();

    private ArrayList<Body> bodiesToChange = new ArrayList<Body>();
    private ArrayList<Body> bodiesToDelete = new ArrayList<Body>();

    private ArrayList<StickyArrowClass> arrowsToStick = new ArrayList<StickyArrowClass>();

    // Box2d Lights
    private RayHandler rayHandler;

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private boolean twoPlayers;

    public PhysicsWorld(OrthographicCamera camera, boolean twoPlayers) {
        this.camera = camera;
        this.twoPlayers = twoPlayers;

        CollisionMasks.printShorts();
        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -12.8f), true);
        world.setContactListener(new CollisionListener(arrowsToStick, bodiesToChange, bodiesToDelete));
        debugRenderer = new Box2DDebugRenderer();
        /** BOX2D LIGHT STUFF BEGIN */
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.5f);
//        rayHandler.setShadows(false);
        rayHandler.setBlurNum(3);

        /** BOX2D LIGHT STUFF END */

        p1 = new HumanGroundBundleGroup(world, rayHandler, new Vector2(1, 6f), 1);
        arrows.add(p1.getHumanoid().drawArrow());
        humanoidBundles.add(p1);

        if(twoPlayers) {
            p2 = new HumanGroundBundleGroup(world, rayHandler, new Vector2(44f, 6f), 1);
            arrows.add(p2.getHumanoid().drawArrow());
            humanoidBundles.add(p2);
        } else {
            score = 0;
        }

        DeathPlatform dp = new DeathPlatform(world, new Vector2(-500, -6), new Vector2(545f, -6));
        if(!twoPlayers) {
            DeathPlatform dp2 = new DeathPlatform(world, new Vector2(-500, -6), new Vector2(-500f, 1000));
        }

        if(twoPlayers) {
            otherBodies.add(new Obstacle(world, new Vector2(22.5f, 10f), 1));
            otherBodies.add(new Obstacle(world, new Vector2(10f, 16f), 1));
            otherBodies.add(new Obstacle(world, new Vector2(35f, 16f), 1));
        }

    }

    ArrayList<Arrow> luminantArrows = new ArrayList<Arrow>();
    private float pastSteps = 0;
    private int score = -300;
    public void step(float delta) {
        if(paused)
            return;

        p1.resetHumanoidContactWithGround();
        if(twoPlayers)
            p2.resetHumanoidContactWithGround();

        Gdx.app.log("PhysicsWorld", "FPS - "+String.valueOf(1/delta)+" Arrows - "+arrows.size());

        world.step(1 / 45f, 6, 2);

        humanInputProcessorP1.refresh();
        if(twoPlayers)
            humanInputProcessorP2.refresh();

        for(Arrow a: arrows) {
            a.applyDrag();
            a.flicker();
            a.incrementStep();
        }

        ArrayList<Arrow> collidedArrows = new ArrayList<Arrow>();
        Vector2 tip = arrows.get(0).returnArrowTip();
        for(StickyArrowClass sc: arrowsToStick) {
            Arrow a2 = new Arrow(sc.getArrow());
            if(arrows.contains(a2)) {
//                Gdx.app.log("PhysicsWorld", "Contains a2");
                Arrow a = arrows.get(arrows.indexOf(a2));
                collidedArrows.add(a);
            }

            if(!twoPlayers) {
                if(humanoidBundles.contains(new HumanGroundBundleGroup(sc.getBody2())));
                    score++;
            }

            // welding starts after here
            Vector2 anchorPoint = sc.getArrow().getWorldPoint(tip);

            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.bodyA = sc.getBody2();
            weldJointDef.bodyB = sc.getArrow();

            weldJointDef.localAnchorA.set(weldJointDef.bodyA.getLocalPoint(anchorPoint));
            Vector2 pos = weldJointDef.bodyB.getLocalPoint(anchorPoint);
            weldJointDef.localAnchorB.set(pos);

            weldJointDef.referenceAngle = weldJointDef.bodyB.getAngle() - weldJointDef.bodyA.getAngle();
            world.createJoint(weldJointDef);

            if(((BodyUserDataClass)weldJointDef.bodyA.getUserData()).getType().equals("simpleObject")) {
                SimpleObject ss = ((SimpleObject)otherBodies.get(otherBodies.indexOf(new SimpleObject(weldJointDef.bodyA))));
                ss.shotByArrow();
            }

        }
        arrowsToStick.clear();

        for(Body b: bodiesToChange) {
            for(HumanGroundBundleGroup hh: humanoidBundles) {
                Humanoid h = hh.getHumanoid();
                for(Body hb:h.getBodies()) {
                    if(hb == b) {
                        h.damage();
                    }
                }
            }
        }
        bodiesToChange.clear();

        for(Body b: bodiesToDelete) {
            Iterator it = humanoidBundles.iterator();
            int i = 0;
            while (it.hasNext()) {
                i++;
                HumanGroundBundleGroup hh = (HumanGroundBundleGroup) it.next();
                Humanoid h = hh.getHumanoid();
                for(Body hb:h.getBodies()) {
                    if(hb.getPosition().equals(b.getPosition())) {
                        h.kill();
                    }
                }
                if(!h.isAlive()) {
                    if(twoPlayers)
                        score = -i;
                    hh.destroy();
                    it.remove();
                    continue;
                }
            }
            Iterator ita = arrows.iterator();
            while (ita.hasNext()) {
                Arrow a = (Arrow) ita.next();
                if(a.getBodies()[0] == null)
                    continue;
                if(a.getBodies()[0].getPosition().equals(b.getPosition())) {
                    a.destroy();
                    ita.remove();
                }
            }
            Iterator ito = otherBodies.iterator();
            while (ito.hasNext()) {
                CustomPhysicsBody body = (CustomPhysicsBody) ito.next();
//                if(body instanceof SimpleObject) {
                    if(body.getBodies()[0].getPosition().equals(b.getPosition())) {
                        body.destroy();
                        ito.remove();
                    }
//                }
            }


        }
        bodiesToDelete.clear();

        Collections.sort(collidedArrows);
        for(Arrow a: collidedArrows) {
            a.setLowGravity();
            boolean pass = true;
            for(Arrow aa:luminantArrows) {
                if (a.getDistanceTo(aa) < 3) {
                    pass = false;
                    continue;
                }
            }
            if(pass) {
                if(a.setConstantlyLight(true)) {
                    luminantArrows.add(a);
                }
            }
            a.explodeIfIsType2();
        }

        ListIterator<CustomPhysicsBody> li = otherBodies.listIterator();
        while(li.hasNext()) {
            CustomPhysicsBody b = li.next();
            if(b instanceof SimpleObject) {
                SimpleObject ss = (SimpleObject) b;
                ss.summonObjectsIfNeeded(li);
            }
        }

        if(pastSteps%500 == 0) {
            Vector2 pos;
            if(twoPlayers)
                pos =new Vector2(22.5f, 27f);
            else
                pos = new Vector2(MathUtils.random(10f, 35f), 27);
            otherBodies.add(new SimpleObject(world, rayHandler, pos, 2, arrows));
        }
        if(!twoPlayers && pastSteps%200 == 0) {
            SmartHumanoidBundle shb = new SmartHumanoidBundle(world, rayHandler, new Vector2(MathUtils.random(47f, 50f), MathUtils.random(-1f, 30f)), 1);
            humanoidBundles.add(shb);
        }
        pastSteps++;
        for(HumanGroundBundleGroup hgbg:humanoidBundles) {
            if(hgbg instanceof SmartHumanoidBundle) {
                SmartHumanoidBundle shb = (SmartHumanoidBundle) hgbg;
                shb.update(p1.getHumanoid().getBodies()[0].getWorldCenter());
                arrows.addAll(shb.getArrows());
                shb.getArrows().clear();
            }
        }
    }


    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        debugRenderer.render(world, batch.getProjectionMatrix());

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(getX(), getY(), 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        for(HumanGroundBundleGroup hgbg: humanoidBundles) {
            hgbg.draw(shapeRenderer);
        }
        for(Arrow a: arrows) {
            a.draw(shapeRenderer);
        }

        for(CustomPhysicsBody b:otherBodies) {
            b.draw(shapeRenderer);
        }
        shapeRenderer.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        batch.begin();

    }

    public void initUserInterface(InputMultiplexer multiplexer) {
 //       multiplexer.addProcessor(new MouseDrag(world, camera, groundPlatform.getBodies()[0]));
        if(twoPlayers) {
            humanInputProcessorP1 = new HumanInputProcessor(arrows, p1, camera, 0, (int)camera.viewportWidth/2);
            humanInputProcessorP2 = new HumanInputProcessor(arrows, p2, camera, (int) camera.viewportWidth / 2, (int) camera.viewportWidth);
            multiplexer.addProcessor(humanInputProcessorP2);
        } else {
            humanInputProcessorP1 = new HumanInputProcessor(arrows, p1, camera, 0, (int)camera.viewportWidth);
        }
        multiplexer.addProcessor(humanInputProcessorP1);
    }

    public boolean isSingleplayerDead() {
        if(!twoPlayers) {
            if(!p1.getHumanoid().isAlive())
                return true;
        }
        return false;
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
    }

    public void setPause(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public int checkScore() {
        return score;
    }
}

