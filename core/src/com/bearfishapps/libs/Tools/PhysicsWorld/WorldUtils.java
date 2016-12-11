package com.bearfishapps.libs.Tools.PhysicsWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

public class WorldUtils {
    public static Body createPoly(World world, BodyDef.BodyType type, float verts[], float x, float y, float density, float
            restitution, float friction) {
        return createPoly(world, type, verts, x, y, density, restitution, friction, (short) 0x0001, (short) 0xFFFF);
    }

    public static Body createCircleBody(World world, BodyDef.BodyType type, float x, float y, float density, float
            restitution, float friction, float radius) {
        return createCircleBody(world, type, x, y, density, restitution, friction, radius, (short) 0x0001, (short) 0xFFFF);
    }


    public static Body createPoly(World world, BodyDef.BodyType type, float verts[], float x, float y, float density, float
            restitution, float friction, short categoryBits, short maskBits) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(verts);

        fixtureDef.shape = polygonShape;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.resetMassData();
        polygonShape.dispose();

        return body;
    }

    public static Body createCircleBody(World world, BodyDef.BodyType type, float x, float y, float density, float
            restitution, float friction, float radius, short categoryBits, short maskBits) {
        Body body;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);

        fixtureDef.shape = circleShape;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.resetMassData();
        circleShape.dispose();

        return body;
    }

    public static float[] scaleF(float[] x, float s) {
        float ret[] = new float[x.length];
        for (int i = 0; i < x.length; i++)
            ret[i] = x[i] * s;
        return ret;
    }

    public static float[] rotateF(float[] x, float rotationInDegrees) {
        return rotateF(x, rotationInDegrees, 0, 0);
    }

    public static float[] rotateF(float[] x, float rotationInDegrees, float originX, float originY) {
        float ret[] = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            if (i % 2 == 0) {
                ret[i] = (x[i + 1] - originY) * MathUtils.sinDeg(rotationInDegrees) + originY
                        + (x[i] - originX) * MathUtils.cosDeg(rotationInDegrees) + originX;
            } else {
                ret[i] = (x[i] - originY) * MathUtils.cosDeg(rotationInDegrees) + originY
                        - (x[i - 1] - originX) * MathUtils.sinDeg(rotationInDegrees) + originX;
            }
        }
        return ret;
    }

    public static WeldJoint weldJoint(World world, Body b1, Body b2, Vector2 anchor1, Vector2 anchor2) {
        Gdx.app.log("anchor1, anchor2", anchor1.toString() + ", " + anchor2.toString());
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.collideConnected = false;
        weldJointDef.bodyA = b1;
        weldJointDef.bodyB = b2;
        weldJointDef.localAnchorA.set(anchor1);
        weldJointDef.localAnchorB.set(anchor2);
        return (WeldJoint) world.createJoint(weldJointDef);
    }

    public static RevoluteJoint makeRevJoint(World world, Body b1, Body b2, Vector2 anchor1, Vector2 anchor2) {
        return makeRevJoint(world, b1, b2,anchor1, anchor2, false, 0, 0);
    }

    public static RevoluteJoint makeRevJoint(World world, Body b1, Body b2, Vector2 anchor1, Vector2 anchor2, boolean isLimit, float limUp, float limDown) {
        return makeRevJoint(world, b1, b2,anchor1, anchor2, isLimit, limUp, limDown, false, 0);
    }

    public static RevoluteJoint makeRevJoint(World world, Body b1, Body b2, Vector2 anchor1, Vector2 anchor2, boolean isLimit, float limUp, float limDown, boolean isFriction, float torque) {
        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.bodyA = b1;
        revoluteJointDef.bodyB = b2;
        revoluteJointDef.localAnchorA.set(anchor1);
        revoluteJointDef.localAnchorB.set(anchor2);
        revoluteJointDef.collideConnected = false;
        if(isLimit) {
            revoluteJointDef.enableLimit = true;
            revoluteJointDef.upperAngle = limUp;
            revoluteJointDef.lowerAngle = limDown;
        } else {
            revoluteJointDef.enableLimit =false;
        }
        RevoluteJoint joint;
        if(isFriction) {
            revoluteJointDef.enableMotor = true;
            revoluteJointDef.maxMotorTorque = torque;
            joint = (RevoluteJoint)world.createJoint(revoluteJointDef);
            joint.setMotorSpeed(0);

        } else {
            revoluteJointDef.enableMotor = false;
            revoluteJointDef.maxMotorTorque = 0;
            joint = (RevoluteJoint)world.createJoint(revoluteJointDef);
        }
        return joint;
    }

}