package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.CustomPhysicsBody;

public class Arrow extends CustomPhysicsBody {
    private final float density = 2f;
    private final float friction = 1f;
    protected float bodyPos[] = {-0.01f, 0f, 0.01f, 0f, 0.01f, -0.8f, -0.01f, -0.8f},
            headPos[] = {-0.05f, -0.75f, 0.05f, -0.75f, 0.05f, -0.85f, -0.05f, -0.85f};

    public Arrow(World world, Vector2 pos, float scale, float rotation) {
        super(2);
        if(world == null)
            return;
        rotation = -rotation;

        bodyPos = WorldUtils.rotateFRadians(bodyPos, rotation);
        headPos = WorldUtils.rotateFRadians(headPos, rotation);
        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        headPos = WorldUtils.scaleF(headPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density*0.1f, 0.1f, friction, CollisionMasks.Mask_BOWnARROW, CollisionMasks.Mask_DEFAULT);
        bodies[1] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, headPos, pos.x, pos.y, density, 0.1f, friction);

        WeldJoint arrowJoint = WorldUtils.weldJoint(world, bodies[0], bodies[1],
                new Vector2(bodies[0].getLocalCenter().x - (0.3f*scale)* MathUtils.sin(rotation), bodies[0].getLocalCenter().y - (0.3f*scale)* MathUtils.cos(rotation)),
                new Vector2(bodies[1].getLocalCenter().x, bodies[1].getLocalCenter().y));

        bodies[0].setActive(false);
        bodies[1].setActive(false);
    }

    public void rotateTo(float angle) {
        bodies[0].setTransform(bodies[0].getPosition(), angle);
        bodies[1].setTransform(bodies[0].getPosition(), angle);
    }
}
