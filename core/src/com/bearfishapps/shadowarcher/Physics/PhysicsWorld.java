package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
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
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.ArrowShooter;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.GroundPlatform;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Humanoid;

import java.util.ArrayList;

public class PhysicsWorld extends Actor{
    protected ShapeRenderer shapeRenderer;
    private World world;
    private GroundPlatform groundPlatform, groundPlatform2;
    private ArrowShooter arrowShooterP1;
    private ArrowShooter arrowShooterP2;
    private ArrayList<Humanoid> humanoids = new ArrayList<Humanoid>();
    Humanoid humanoidP1, humanoidP2;
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    private ArrayList<Body> bodiesToChange = new ArrayList<Body>();

    ArrayList<StickyArrowClass> arrowsToStick = new ArrayList<StickyArrowClass>();

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;

    public PhysicsWorld() {
        CollisionMasks.printShorts();
        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -29.8f), true);
        world.setContactListener(new CollisionListener(arrowsToStick, bodiesToChange));
        debugRenderer = new Box2DDebugRenderer();

        groundPlatform = new GroundPlatform(world, new Vector2(18, 50), new Vector2(22, 50));
        groundPlatform2 = new GroundPlatform(world, new Vector2(378, 50), new Vector2(382, 50));

        humanoidP1 = new Humanoid(world, new Vector2(20, 56f), 10);
        humanoidP2 = new Humanoid(world, new Vector2(380, 56f), 10);

        humanoids.add(humanoidP1);
        humanoids.add(humanoidP2);

        arrows.add(humanoidP1.drawArrow());
        arrows.add(humanoidP2.drawArrow());
    }

    public void step(float delta) {
        world.step(1 / 60f, 6, 2);

        arrowShooterP1.refresh();
        arrowShooterP2.refresh();
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
            for(Humanoid h: humanoids) {
                for(Body hb:h.getBodies()) {
                    if(hb == b) {
                        h.damage();
                    }
                }
            }
        }
        bodiesToChange.clear();
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        debugRenderer.render(world, batch.getProjectionMatrix());

/*        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        shapeRenderer.translate(getX(), getY(), 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(0, 0, 700, 400);
        shapeRenderer.end();*/

        batch.begin();
    }

    public void initUserInput(InputMultiplexer multiplexer, final Camera camera) {
 //       multiplexer.addProcessor(new MouseDrag(world, camera, groundPlatform.getBodies()[0]));
        arrowShooterP1 = new ArrowShooter(arrows, humanoidP1, camera, 0, (int)camera.viewportWidth/2);
        arrowShooterP2 = new ArrowShooter(arrows, humanoidP2, camera, (int)camera.viewportWidth/2, (int)camera.viewportWidth);
        multiplexer.addProcessor(arrowShooterP1);
        multiplexer.addProcessor(arrowShooterP2);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
