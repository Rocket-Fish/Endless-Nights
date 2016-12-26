package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.libs.Tools.Rendering.RenderHelper;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

import box2dLight.Light;
import box2dLight.RayHandler;

public class Arrow extends CustomPhysicsBody implements Comparable<Arrow>{
    private final float density = 12.3f;
    private final float friction = 1f;
    protected float bodyPos[] = {0f, 0f, 0.03f, -0.75f, 0f, -0.8f, -0.03f, -0.75f};

    private float scale;
    private Light light;
    public float LIGHT_INTENSITY;
    private RayHandler rayHandler;

    public Arrow(Body body) {
        super(null, 1);
        bodies[0] = body;
        // placeholder arrow for position
    }

//    private float initialAngle;
    public Arrow(World world, RayHandler rayHandler, float scale) {
        super(world, 1);
        if(world == null)
            return;
        this.rayHandler = rayHandler;

        this.scale = scale;
        LIGHT_INTENSITY = MathUtils.random(2.3f, 4.5f);

//        initialAngle = rotation;
//        bodyPos = WorldUtils.rotateFRadians(bodyPos, rotation);
//        bodyPos = WorldUtils.scaleF(bodyPos, scale);

    }

    private BodyUserDataClass bodyUserData;
    private void createBody(Vector2 pos, float rotation) {
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction, CollisionMasks.Mask_ARROW, (short)(CollisionMasks.Mask_Humanoid | CollisionMasks.Mask_DEFAULT));
        bodyUserData = new BodyUserDataClass("arrow");
        bodyUserData.setSticky(true);
        bodies[0].setUserData(bodyUserData);

        bodies[0].setAngularDamping(1.5f);
        bodies[0].setActive(false);
        bodies[0].setTransform(bodies[0].getPosition(), rotation);

        light = WorldUtils.initPointLight(rayHandler, LIGHT_INTENSITY, bodies[0], new Vector2(bodyPos[4], bodyPos[5]));
        light.setContactFilter(bodies[0].getFixtureList().get(0).getFilterData().categoryBits,
                bodies[0].getFixtureList().get(0).getFilterData().groupIndex,
                bodies[0].getFixtureList().get(0).getFilterData().maskBits);
        light.setColor(226/255f, 134/255f, 34/255f, 1);
        light.setXray(false);
    }

    private boolean lightState = false;
    private boolean fade = false;
    private boolean stayLight = false;
    public void flicker() {
        if(light != null && light.isActive()) {
            if(!fade || stayLight) {
                float change = MathUtils.random(1, 10) < 6 ? (MathUtils.random(0, 5) < 3 ? MathUtils.random(-0.015f, 0.035f) : MathUtils.random(0.045f, 0.06f)) : MathUtils.random(0.03f, 0.075f);
                float luminocity = lightState ? light.getDistance() + change : light.getDistance() - change;
                if (luminocity <= LIGHT_INTENSITY * 0.75 || luminocity >= LIGHT_INTENSITY)
                    lightState ^= true;
                light.setDistance(luminocity);
            } else if(!stayLight){
                float change = MathUtils.random(-0.025f, 0.065f);
                float luminocity = light.getDistance() - change;
                light.setDistance(luminocity);
            }
            if(bodyUserData.getStepCount()>340 && !fade)
                fade = true;
            if(light.getDistance() < LIGHT_INTENSITY*0.1)
                light.setActive(false);
        }
    }

    public boolean setConstantlyLight(boolean shouldStayLight) {
        if(light != null && light.isActive() && light.getDistance() > LIGHT_INTENSITY*0.15) {
            stayLight = shouldStayLight;
            return true;
        }
        return false;
    }

    public Vector2 getPosition() {
        if(bodies[0]!=null) {
            return new Vector2(bodies[0].getPosition());
        }
        return null;
    }

    public void release(float velocity, Vector2 pos, float angle) {
        createBody(pos, angle);
        bodies[0].setActive(true);
        bodies[0].setLinearVelocity(velocity*MathUtils.sin(angle), velocity*-MathUtils.cos(angle));
    }

    public void applyDrag() {
//        float flightVelocity = new Vector2(bodies[0].getLinearVelocity()).len();
//        if(bodies[0].isActive() && Math.abs(flightVelocity) > 1) {
        if(bodies[0] == null)
            return;
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
        if(bodies[0] == null)
            return;
        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(bodyPos, bodies[0]);
        renderPos = WorldUtils.scaleF(renderPos, scale);
        RenderHelper.filledPolygon(renderPos, renderer);
    }

    @Override
    public void destroy() {
        super.destroy();
        destroyLight();
    }

    public void destroyLight() {
        if(light != null ) {
            light.remove();
            light = null;
        }
    }

    @Override
    public void incrementStep() {
        if(bodies[0] != null && bodies[0].isActive())
            super.incrementStep();
    }

    @Override
    public int compareTo(Arrow o) {
        if(getPosition()!= null && o.getPosition() != null) {
            float dst = getPosition().dst(0, 0);
            float odst = o.getPosition().dst(0, 0);
            if(dst>odst)
                return 1;
            else if (dst< odst)
                return -1;
            else return 0;
        }
        else if (getPosition()== null)
            return -1;
        else if(o.getPosition()==null)
            return 1;
        return 0;
    }

    public float getDistanceTo(Arrow o) {
        return getPosition().dst(o.getPosition());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arrow)) return false;

        Arrow arrow = (Arrow) o;
        if(bodies[0] == null)
            return false;
        if(arrow.getBodies()[0] == null)
            return false;

        Gdx.app.log("Compare", "Arrow1 - "+ String.valueOf(getPosition())
                + "Arrow2 - " + String.valueOf(arrow.getPosition())
                + "distance - "+getDistanceTo(arrow));
        return getPosition().equals(arrow.getPosition());
    }

    @Override
    public int hashCode() {
        return getPosition().hashCode();
    }
}
