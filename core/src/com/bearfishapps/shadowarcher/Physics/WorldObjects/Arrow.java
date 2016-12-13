package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

public class Arrow extends CustomPhysicsBody {
    private final float density = 1.3f;
    private final float friction = 1f;
    protected float bodyPos[] = {0f, 0f, 0.03f, -0.75f, 0f, -0.8f, -0.03f, -0.75f};

//    private float initialAngle;
    public Arrow(World world, Vector2 pos, float scale, float rotation) {
        super(1);
        if(world == null)
            return;

//        initialAngle = rotation;
//        bodyPos = WorldUtils.rotateFRadians(bodyPos, rotation);
        bodyPos = WorldUtils.scaleF(bodyPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction, CollisionMasks.Mask_ARROW, (short)(CollisionMasks.Mask_Humanoid | CollisionMasks.Mask_DEFAULT));
        bodies[0].setUserData(new BodyUserDataClass("arrow"));

        bodies[0].setAngularDamping(1.5f);
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
//        float flightVelocity = new Vector2(bodies[0].getLinearVelocity()).len();
//        if(bodies[0].isActive() && Math.abs(flightVelocity) > 1) {
        if(bodies[0].isActive()) {
            Vector2 pointingDirection = new Vector2(bodies[0].getWorldVector(new Vector2(0, -1)));
            Vector2 flightDirection = normalize(bodies[0].getLinearVelocity());
            float flightVelocity = new Vector2(bodies[0].getLinearVelocity()).len();

            float dot = new Vector2(flightDirection).dot(pointingDirection);
            float dragForceMagnitude = (1 - Math.abs(dot))*flightVelocity* bodies[0].getMass();

            Vector2 tail = bodies[0].getWorldPoint(new Vector2(bodyPos[0], bodyPos[1]));
            bodies[0].applyForce(new Vector2(flightDirection).scl(-dragForceMagnitude),tail, true);

            if(bodies[0].getAngularVelocity() > 0.3f)
                bodies[0].setAngularVelocity(0.29f);
            else if(bodies[0].getAngularVelocity() < -0.3f)
                bodies[0].setAngularVelocity(-0.29f);
        }
    }

    private Vector2 normalize(Vector2 input) {
        Vector2 v = new Vector2(input);
        float div = (float)Math.sqrt(v.x*v.x+v.y*v.y);
        if(div != 0) {
            v.x/= div;
            v.y/= div;
        }
        return v;
    }

    public Vector2 returnArrowTip() {
        return new Vector2(bodyPos[4], bodyPos[5]);
    }
}
