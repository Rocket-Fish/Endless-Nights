package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

public class GroundPlatform extends CustomPhysicsBody{
    private final float friction = 0.8f;

    public GroundPlatform(World world, Vector2 one, Vector2 two) {
        super(1);
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
// Set its world position
        groundBodyDef.position.set(one);

// Create a body from the defintion and add it to the world
        bodies[0] = world.createBody(groundBodyDef);

        EdgeShape ground = new EdgeShape();
        ground.set(new Vector2(0, 0), two.sub(one));

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.shape = ground;
        groundFixture.friction = friction;
        groundFixture.density = 0;
        groundFixture.filter.categoryBits = CollisionMasks.Mask_ARROW;
        groundFixture.filter.maskBits = (short)(CollisionMasks.Mask_DEFAULT| CollisionMasks.Mask_Humanoid);
        // Create a fixture from our polygon shape and add it to our ground body
        bodies[0].createFixture(groundFixture);
        bodies[0].setUserData(new BodyUserDataClass("ground", 10f));
// Clean up after ourselves
        ground.dispose();
    }
}
