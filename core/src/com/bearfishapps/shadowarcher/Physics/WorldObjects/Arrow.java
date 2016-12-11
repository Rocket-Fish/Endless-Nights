package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.CollisionMasks;

public class Arrow extends CustomPhysicsBody {
    private final float density = 1f;
    private final float friction = 1f;
    protected float bodyPos[] = {0f, 0f, 0.07f, -0.7f, 0f, -0.8f, -0.07f, -0.7f};

//    private float initialAngle;
    public Arrow(World world, Vector2 pos, float scale, float rotation) {
        super(1);
        if(world == null)
            return;

//        initialAngle = rotation;
//        bodyPos = WorldUtils.rotateFRadians(bodyPos, rotation);
        bodyPos = WorldUtils.scaleF(bodyPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction);

//        bodies[0].setAngularDamping(3);
        bodies[0].setActive(false);
        rotateTo(rotation);
    }

    public void rotateTo(float angle) {
// I know this way of doing this is a mess ...
// should have rotated the arrow as a whole isntead of moving the points
//        angle += initialAngle;
//        angle += MathUtils.PI/2;
        bodies[0].setTransform(bodies[0].getPosition(), angle);
    }

    public void release(float velocity, float angle) {
        bodies[0].setActive(true);

        bodies[0].setLinearVelocity(velocity*MathUtils.sin(angle), velocity*-MathUtils.cos(angle));
    }

    public void applyDrag() {
        if(bodies[0].isActive()) {
            float dragConstant = 0.005f;
            Vector2 pointingDirection = bodies[0].getWorldVector(new Vector2(0, -1));
            Vector2 flightDirection = bodies[0].getLinearVelocity().nor();

            float dot = new Vector2(flightDirection).dot(pointingDirection);
            float dragForceMagnitude = (1 - Math.abs(dot)) * dragConstant * bodies[0].getMass();

            Vector2 tail = bodies[0].getWorldPoint(new Vector2(bodyPos[0], bodyPos[1]));
            bodies[0].applyForce(new Vector2(flightDirection).nor().scl(-dragForceMagnitude),tail, true);
        }
    }
}
