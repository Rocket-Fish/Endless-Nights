package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.Collision.StickyArrowClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionListener;
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.HumanInputProcessor;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.CustomPhysicsBody;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Obstacle;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.StaticObjects.DeathPlatform;

import java.util.ArrayList;
import java.util.Iterator;

import box2dLight.RayHandler;

public class PhysicsWorld extends Actor{
    protected ShapeRenderer shapeRenderer;
    private World world;
    private HumanInputProcessor humanInputProcessorP1;
    private HumanInputProcessor humanInputProcessorP2;

    private HumanGroundBundleGroup p1, p2;
    private ArrayList<HumanGroundBundleGroup> humanoidBundles = new ArrayList<HumanGroundBundleGroup>();
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    ArrayList<CustomPhysicsBody> otherBodies = new ArrayList<CustomPhysicsBody>();

    private ArrayList<Body> bodiesToChange = new ArrayList<Body>();
    private ArrayList<Body> bodiesToDelete = new ArrayList<Body>();

    ArrayList<StickyArrowClass> arrowsToStick = new ArrayList<StickyArrowClass>();

    // Box2d Lights
    private RayHandler rayHandler;

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    public PhysicsWorld(OrthographicCamera camera) {
        this.camera = camera;
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
        rayHandler.setBlurNum(3);

        /** BOX2D LIGHT STUFF END */

        p1 = new HumanGroundBundleGroup(world, rayHandler, new Vector2(1, 6f), 1);
        p2 = new HumanGroundBundleGroup(world, rayHandler, new Vector2(44f, 6f), 1);
        arrows.add(p1.getHumanoid().drawArrow());
        arrows.add(p2.getHumanoid().drawArrow());

        humanoidBundles.add(p1);
        humanoidBundles.add(p2);

        DeathPlatform dp = new DeathPlatform(world, new Vector2(-500, -4), new Vector2(545f, -4));

        otherBodies.add(new Obstacle(world, new Vector2(22.5f, 10f), 1));

    }

    public void step(float delta) {

        p1.resetHumanoidContactWithGround();
        p2.resetHumanoidContactWithGround();

        Gdx.app.log("PhysicsWorld", "FPS - "+String.valueOf(Gdx.graphics.getFramesPerSecond())+" Arrows - "+arrows.size());

        world.step(1 / 45f, 6, 2);

        humanInputProcessorP1.refresh();
        humanInputProcessorP2.refresh();

        for(Arrow a: arrows) {
            a.applyDrag();
            a.flicker();
            a.incrementStep();
        }
        Vector2 tip = arrows.get(0).returnArrowTip();
        for(StickyArrowClass sc: arrowsToStick) {
            Vector2 anchorPoint = sc.getArrow().getWorldPoint(tip);

            WeldJointDef weldJointDef = new WeldJointDef();
            weldJointDef.bodyA = sc.getArrow();
            weldJointDef.bodyB = sc.getBody2();

            weldJointDef.localAnchorA.set(weldJointDef.bodyA.getLocalPoint(anchorPoint));
            Vector2 pos = weldJointDef.bodyB.getLocalPoint(anchorPoint);
            weldJointDef.localAnchorB.set(pos);

            weldJointDef.referenceAngle = weldJointDef.bodyB.getAngle() - weldJointDef.bodyA.getAngle();
            world.createJoint(weldJointDef);
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
            while (it.hasNext()) {
                HumanGroundBundleGroup hh = (HumanGroundBundleGroup) it.next();
                Humanoid h = hh.getHumanoid();
                for(Body hb:h.getBodies()) {
                    if(hb.getPosition().equals(b.getPosition())) {
                        h.kill();
                    }
                }
                if(!h.isAlive()) {
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
        }
        bodiesToDelete.clear();

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
        humanInputProcessorP1 = new HumanInputProcessor(arrows, p1, camera, 0, (int)camera.viewportWidth/2);
        humanInputProcessorP2 = new HumanInputProcessor(arrows, p2, camera, (int)camera.viewportWidth/2, (int)camera.viewportWidth);
        multiplexer.addProcessor(humanInputProcessorP1);
        multiplexer.addProcessor(humanInputProcessorP2);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
    }

}

