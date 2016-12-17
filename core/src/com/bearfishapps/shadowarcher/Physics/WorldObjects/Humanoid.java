package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;

public class Humanoid extends CustomPhysicsBody{

    private final float density = 6.1f;
    private final float friction = 0.6f;
    private final float maxStiffness = 10000000000f;
    private float stiffness = maxStiffness;
    private boolean isAlive = true;

    protected float bodyPos[] = {-0.1f, -0f, 0.1f, -0f, 0.1f, 0.6f, -0.1f, 0.6f},
                    headPos[] = {-0.25f, -0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, 0.25f},
                    lArmPos[] = {-0.075f, 0f, 0.075f, 0f, 0.045f, -0.675f, -0.045f, -0.675f},
                    rArmPos[] = new float[lArmPos.length],
                    lLegPos[] = {-0.1f, 0f, 0.1f, -0f, 0.2f, -0.6f, 0.05f, -0.6f},
                    rLegPos[] = {-0.1f, 0f, 0.1f, -0f, -0.05f, -0.6f, -0.2f, -0.6f},
    // The bow pos is a bit bugged, dunno why though ...
                    bowPos[] = {/**/ 0.8f, -0.55f,/**/  0.3f, -0.675f, -0.3f, -0.675f, /**/ -0.8f, -0.55f};

    protected float scale;

    private RevoluteJoint headJoint, armJoint1, armJoint2, legJoint1, legJoint2;
    private Arrow arrow;

    public Humanoid(World world, Vector2 pos, float scale) {
        super(world, 7);
        System.arraycopy(lArmPos, 0, rArmPos, 0, lArmPos.length);
        this.scale = scale;

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        headPos = WorldUtils.scaleF(headPos, scale);
        lArmPos = WorldUtils.scaleF(lArmPos, scale);
        rArmPos = WorldUtils.scaleF(rArmPos, scale);
        lLegPos = WorldUtils.scaleF(lLegPos, scale);
        rLegPos = WorldUtils.scaleF(rLegPos, scale);
        bowPos = WorldUtils.scaleF(bowPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density*0.6f, 0.1f, friction, CollisionMasks.Mask_BODY, (short)(CollisionMasks.Mask_DEFAULT | CollisionMasks.Mask_LEG | CollisionMasks.Mask_ARROW));
        bodies[1] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, headPos, pos.x, pos.y+(bodyPos[5]+headPos[5]), density*0.95f, 0.2f, friction, CollisionMasks.Mask_HEAD, (short)(CollisionMasks.Mask_DEFAULT | CollisionMasks.Mask_LEG | CollisionMasks.Mask_ARROW));
        bodies[2] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, lArmPos, pos.x, pos.y-lArmPos[5], density*0.01f, 0.1f, friction, CollisionMasks.Mask_ARM, (short)(CollisionMasks.Mask_DEFAULT| CollisionMasks.Mask_ARROW));
        bodies[3] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, rArmPos, pos.x, pos.y-rArmPos[5], density*0.01f, 0.1f, friction, CollisionMasks.Mask_ARM, (short)(CollisionMasks.Mask_DEFAULT| CollisionMasks.Mask_ARROW));
        bodies[4] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, lLegPos, pos.x, pos.y+(bodyPos[2]), density*0.8f, 0.1f, friction, CollisionMasks.Mask_LEG, (short)(CollisionMasks.Mask_DEFAULT|CollisionMasks.Mask_BODY| CollisionMasks.Mask_ARROW));
        bodies[5] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, rLegPos, pos.x, pos.y+(bodyPos[2]), density*0.8f, 0.1f, friction, CollisionMasks.Mask_ARM, (short)(CollisionMasks.Mask_DEFAULT|CollisionMasks.Mask_BODY| CollisionMasks.Mask_ARROW));
        bodies[6] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bowPos, pos.x, pos.y-lArmPos[5], 0.001f, 0.1f, friction, CollisionMasks.Mask_BOW, CollisionMasks.Mask_DEFAULT);

        for(Body b: bodies) {
            b.setUserData(new BodyUserDataClass("humanoid"));
        }

        headJoint = WorldUtils.makeRevJoint(world, bodies[0], bodies[1],
                                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((headPos[0]+headPos[2])/2, headPos[1]), true, 1.04f, -1.04f, true, stiffness);
        armJoint1 = WorldUtils.makeRevJoint(world, bodies[0], bodies[2],
                                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((lArmPos[0]+lArmPos[2])/2, lArmPos[1]), false, 0, 0, true, stiffness);
        armJoint2 = WorldUtils.makeRevJoint(world, bodies[0], bodies[3],
                                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((rArmPos[0]+rArmPos[2])/2, rArmPos[1]), false, 0, 0, true, stiffness);
        legJoint1 = WorldUtils.makeRevJoint(world, bodies[0], bodies[4],
                                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[1]), new Vector2((lLegPos[0]+lLegPos[2])/2, lLegPos[1]), true, 1.7f, -1.7f, true, stiffness);
        legJoint2 = WorldUtils.makeRevJoint(world, bodies[0], bodies[5],
                                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[1]), new Vector2((rLegPos[0]+rLegPos[2])/2, rLegPos[1]), true, 1.7f, -1.7f, true, stiffness);

        WeldJoint bowJoint = WorldUtils.weldJoint(world, bodies[2], bodies[6], new Vector2(0, lArmPos[5]+0.06f*scale), new Vector2(0, bowPos[1]));

        float halfPI = MathUtils.PI/2;
        bodies[2].setTransform(bodies[2].getPosition(), halfPI);
        bodies[3].setTransform(bodies[3].getPosition(), halfPI);

        arrow = null;

    }

    public void remainActive() {
        for(Body b: bodies) {
            if(!b.isActive())
                b.setActive(true);
        }
    }

    public Arrow drawArrow() {
        arrow = new Arrow(world, new Vector2(bodies[6].getPosition().x,bodies[6].getPosition().y), scale, armJoint1.getJointAngle());
//        arrow = new Arrow(world, new Vector2(300, 100), scale, armJoint1.getJointAngle());
        return arrow;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void shootArrow() {
        arrow.release(240, armJoint1.getJointAngle());
        arrow = null;
    }

    public void damage() {
        stiffness = 100f;
        setStiffness();
    }

    public void heal() {
        if(stiffness < maxStiffness) {
            stiffness = maxStiffness;
            setStiffness();
        }
    }

    private void setStiffness() {
        headJoint.setMaxMotorTorque(stiffness);
        armJoint1.setMaxMotorTorque(stiffness);
        armJoint2.setMaxMotorTorque(stiffness);
        legJoint1.setMaxMotorTorque(stiffness);
        legJoint2.setMaxMotorTorque(stiffness);
    }

    public void kill() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void destroyArrow() {
        if(arrow != null) {
            arrow.destroy();
            arrow = null;
        }
    }
}
