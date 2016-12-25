package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.libs.Tools.Rendering.RenderHelper;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

import box2dLight.Light;
import box2dLight.RayHandler;

public class Arrow extends CustomPhysicsBody {
    private final float density = 12.3f;
    private final float friction = 1f;
    protected float bodyPos[] = {0f, 0f, 0.03f, -0.75f, 0f, -0.8f, -0.03f, -0.75f};

    private float scale;
    private Light light;
    public final float LIGHT_INTENSITY = 6.3f;

//    private float initialAngle;
    public Arrow(World world, RayHandler rayHandler, Vector2 pos, float scale, float rotation) {
        super(world, 1);
        if(world == null)
            return;

        this.scale = scale;

//        initialAngle = rotation;
//        bodyPos = WorldUtils.rotateFRadians(bodyPos, rotation);
//        bodyPos = WorldUtils.scaleF(bodyPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction, CollisionMasks.Mask_ARROW, (short)(CollisionMasks.Mask_Humanoid | CollisionMasks.Mask_DEFAULT));
        BodyUserDataClass b = new BodyUserDataClass("arrow");
        b.setSticky(true);
        bodies[0].setUserData(b);

        bodies[0].setAngularDamping(1.5f);
        bodies[0].setActive(false);
        rotateTo(rotation);

        light = WorldUtils.initPointLight(rayHandler, LIGHT_INTENSITY, bodies[0], new Vector2(bodyPos[4], bodyPos[5]-0.1f));
        light.setContactFilter(bodies[0].getFixtureList().get(0).getFilterData().categoryBits,
                bodies[0].getFixtureList().get(0).getFilterData().groupIndex,
                bodies[0].getFixtureList().get(0).getFilterData().maskBits);
    }

    private boolean lightState = false;
    public void flicker() {
        if(light != null) {
            float luminocity = lightState?light.getDistance() + 0.06f:light.getDistance() - 0.06f;
            if(luminocity<=LIGHT_INTENSITY*0.75 || luminocity>=LIGHT_INTENSITY)
                lightState^=true;
            light.setDistance(luminocity);
        }
    }

    public void rotateTo(float angle) {
// I know this way of doing this is a mess ...
// should have rotated the arrow as a whole isntead of moving the points
//        angle += initialAngle;
//        angle += MathUtils.PI/2;
        transformTo(bodies[0].getPosition(), angle);
    }

    public void release(float velocity, float angle) {
        bodies[0].setActive(true);

        bodies[0].setLinearVelocity(velocity*MathUtils.sin(angle), velocity*-MathUtils.cos(angle));
    }

    public void transformTo(Vector2 pos, float angle) {
        bodies[0].setTransform(pos, angle);
    }

    public void applyDrag() {
//        float flightVelocity = new Vector2(bodies[0].getLinearVelocity()).len();
//        if(bodies[0].isActive() && Math.abs(flightVelocity) > 1) {
        if(bodies[0].isActive()) {
            Vector2 pointingDirection = new Vector2(bodies[0].getWorldVector(new Vector2(0, -1)));
            Vector2 flightDirection = WorldUtils.normalize(bodies[0].getLinearVelocity());
            float flightVelocity = new Vector2(bodies[0].getLinearVelocity()).len();

            float dot = new Vector2(flightDirection).dot(pointingDirection);
            float dragForceMagnitude = (1 - Math.abs(dot))*flightVelocity*flightVelocity* bodies[0].getMass();

            Vector2 tail = bodies[0].getWorldPoint(new Vector2(bodyPos[0], bodyPos[1]));
            bodies[0].applyForce(new Vector2(flightDirection).scl(-dragForceMagnitude),tail, true);

            if(bodies[0].getAngularVelocity() > 0.3f)
                bodies[0].setAngularVelocity(0.29f);
            else if(bodies[0].getAngularVelocity() < -0.3f)
                bodies[0].setAngularVelocity(-0.29f);
        }
    }

    public Vector2 returnArrowTip() {
        return new Vector2(bodyPos[4], bodyPos[5]);
    }

    @Override
    public void draw(ShapeRenderer renderer) {
        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(bodyPos, bodies[0]);
        renderPos = WorldUtils.scaleF(renderPos, scale);
        RenderHelper.filledPolygon(renderPos, renderer);
    }

    @Override
    public void destroy() {
        super.destroy();
        if(light != null ) {
            light.remove();
            light = null;
        }
    }

}
