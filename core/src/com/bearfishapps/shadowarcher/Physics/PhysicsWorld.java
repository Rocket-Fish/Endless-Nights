package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.StaticObjects.DeathPlatform;

import java.util.ArrayList;
import java.util.Iterator;

public class PhysicsWorld extends Actor{
    protected ShapeRenderer shapeRenderer;
    private World world;
    private HumanInputProcessor humanInputProcessorP1;
    private HumanInputProcessor humanInputProcessorP2;

    com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup p1, p2;
    private ArrayList<HumanGroundBundleGroup> humanoidBundles = new ArrayList<HumanGroundBundleGroup>();
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    private ArrayList<Body> bodiesToChange = new ArrayList<Body>();
    private ArrayList<Body> bodiesToDelete = new ArrayList<Body>();

    ArrayList<StickyArrowClass> arrowsToStick = new ArrayList<StickyArrowClass>();

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;

    public PhysicsWorld() {
        CollisionMasks.printShorts();
        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -12.8f), true);
        world.setContactListener(new CollisionListener(arrowsToStick, bodiesToChange, bodiesToDelete));
        debugRenderer = new Box2DDebugRenderer();

        p1 = new HumanGroundBundleGroup(world, new Vector2(1, 6f), 1);
        p2 = new HumanGroundBundleGroup(world, new Vector2(44f, 6f), 1);
        arrows.add(p1.getHumanoid().drawArrow());
        arrows.add(p2.getHumanoid().drawArrow());

        humanoidBundles.add(p1);
        humanoidBundles.add(p2);

        DeathPlatform dp = new DeathPlatform(world, new Vector2(-500, -1), new Vector2(540.9f, -1));
    }

    public void step(float delta) {

        p1.resetHumanoidContactWithGround();
        p2.resetHumanoidContactWithGround();

        world.step(1 / 60f, 6, 2);

        humanInputProcessorP1.refresh();
        humanInputProcessorP2.refresh();
        for(Arrow a: arrows) {
            a.applyDrag();
            if(a.getBodies()[0].isActive())
                a.incrementTime(delta);
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
                    if(hb == b) {
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
                if(a.getBodies()[0] == b) {
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
           shapeRenderer.end();

        batch.begin();
    }

    public void initUserInterface(InputMultiplexer multiplexer, final Camera camera) {
 //       multiplexer.addProcessor(new MouseDrag(world, camera, groundPlatform.getBodies()[0]));
        humanInputProcessorP1 = new HumanInputProcessor(arrows, p1, camera, 0, (int)camera.viewportWidth/2);
        humanInputProcessorP2 = new HumanInputProcessor(arrows, p2, camera, (int)camera.viewportWidth/2, (int)camera.viewportWidth);
        multiplexer.addProcessor(humanInputProcessorP1);
        multiplexer.addProcessor(humanInputProcessorP2);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
