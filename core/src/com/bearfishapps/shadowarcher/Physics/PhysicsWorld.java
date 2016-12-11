package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.ArrowShooter;
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.OnArrowShootAction;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.GroundPlatform;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Humanoid;

import java.util.ArrayList;

public class PhysicsWorld extends Actor implements OnArrowShootAction{
    protected ShapeRenderer shapeRenderer;
    private World world;
    private GroundPlatform groundPlatform;
    private ArrowShooter arrowShooter;
    private Humanoid humanoid;
    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;

    public PhysicsWorld() {
        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -20f), true);
        debugRenderer = new Box2DDebugRenderer();

        groundPlatform = new GroundPlatform(world, new Vector2(0, 50), new Vector2(800, 50));
        humanoid = new Humanoid(world, new Vector2(200, 62), 20);

        arrows.add(humanoid.drawArrow());
    }

    @Override
    public void onShoot() {
        arrows.add(humanoid.drawArrow());
    }

    public void step() {
        world.step(1 / 60f, 6, 2);

        arrowShooter.refresh();
        for(Arrow a: arrows) {
            a.applyDrag();
        }
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
        arrowShooter = new ArrowShooter(this, humanoid, camera);
        multiplexer.addProcessor(arrowShooter);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
