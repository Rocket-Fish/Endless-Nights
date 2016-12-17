package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

public class MoveableGround extends CustomPhysicsBody{
    private final float friction = 10f;
    private final float density = 10.1f;
    protected float bodyPos[] = {-0.4f, -0.6f, 0.4f, -0.6f, 0.4f, -0.65f, -0.4f, -0.65f};
    private Vector2 velocity = new Vector2(0, 0);

    public MoveableGround(World world, Vector2 position, float scale) {
        super(world, 1);

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.KinematicBody,bodyPos, position.x, position.y, density, 0f, friction, CollisionMasks.Mask_ARROW, (short)(CollisionMasks.Mask_Humanoid|CollisionMasks.Mask_DEFAULT));
        bodies[0].setUserData(new BodyUserDataClass("ground_movable"));
    }

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
        bodies[0].setLinearVelocity(velocity);
    }

}
