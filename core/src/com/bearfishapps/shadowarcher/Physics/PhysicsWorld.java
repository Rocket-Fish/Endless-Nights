package com.bearfishapps.shadowarcher.Physics;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bearfishapps.shadowarcher.Physics.InputInterpretors.TouchSensor;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.GroundPlatform;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Humanoid;

public class PhysicsWorld extends Actor{
    protected ShapeRenderer shapeRenderer;
    private World world;
    private GroundPlatform groundPlatform;
    private TouchSensor touchSensor;
    private Humanoid humanoid;

    // TODO: REMOVE/Disable DEGUB RENDERER DIRNG RELEASE
    private Box2DDebugRenderer debugRenderer;

    public PhysicsWorld() {
        shapeRenderer = new ShapeRenderer();

        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        groundPlatform = new GroundPlatform(world, new Vector2(0, 50), new Vector2(800, 50));
        humanoid = new Humanoid(world, new Vector2(200, 70), 40);

    }

    public void step() {
        world.step(1 / 60f, 6, 2);
        touchSensor.refresh();
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
        touchSensor = new TouchSensor(humanoid.getBodies()[2], humanoid.getBodies()[3], camera);
        multiplexer.addProcessor(touchSensor);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }

}
