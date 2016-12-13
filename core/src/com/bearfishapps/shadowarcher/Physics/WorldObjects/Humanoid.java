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

    private final float density = 12f;
    private final float friction = 0.6f;
    private float stiffness = 10000000000f;

    protected float bodyPos[] = {-0.1f, -0f, 0.1f, -0f, 0.1f, 0.6f, -0.1f, 0.6f},
                    headPos[] = {-0.25f, -0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, 0.25f},
                    lArmPos[] = {-0.075f, 0f, 0.075f, 0f, 0.045f, -0.675f, -0.045f, -0.675f},
                    rArmPos[] = new float[lArmPos.length],
                    lLegPos[] = {-0.1f, 0f, 0.1f, -0f, 0.2f, -0.6f, 0.05f, -0.6f},
                    rLegPos[] = {-0.1f, 0f, 0.1f, -0f, -0.05f, -0.6f, -0.2f, -0.6f},
    // The bow pos is a bit bugged, dunno why though ...
                    bowPos[] = {/**/ 0.8f, -0.55f,/**/  0.3f, -0.675f, -0.3f, -0.675f, /**/ -0.8f, -0.55f};

    protected World world;
    protected float scale;

    private RevoluteJoint armJoint1;
    private Arrow arrow;

    public Humanoid(World world, Vector2 pos, float scale) {
        super(7);
        System.arraycopy(lArmPos, 0, rArmPos, 0, lArmPos.length);

        this.world = world;
        this.scale = scale;

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        headPos = WorldUtils.scaleF(headPos, scale);
        lArmPos = WorldUtils.scaleF(lArmPos, scale);
        rArmPos = WorldUtils.scaleF(rArmPos, scale);
        lLegPos = WorldUtils.scaleF(lLegPos, scale);
        rLegPos = WorldUtils.scaleF(rLegPos, scale);
        bowPos = WorldUtils.scaleF(bowPos, scale);

        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction, CollisionMasks.Mask_BODY, (short)(CollisionMasks.Mask_DEFAULT | CollisionMasks.Mask_LEG));
        bodies[1] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, headPos, pos.x, pos.y+(bodyPos[5]+headPos[5]), density*0.9f, 0.1f, friction, CollisionMasks.Mask_HEAD, (short)(CollisionMasks.Mask_DEFAULT | CollisionMasks.Mask_LEG));
        bodies[2] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, lArmPos, pos.x, pos.y-lArmPos[5], density*0.01f, 0.1f, friction, CollisionMasks.Mask_ARM, CollisionMasks.Mask_DEFAULT);
        bodies[3] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, rArmPos, pos.x, pos.y-rArmPos[5], density*0.01f, 0.1f, friction, CollisionMasks.Mask_ARM, CollisionMasks.Mask_DEFAULT);
        bodies[4] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, lLegPos, pos.x, pos.y+(bodyPos[2]), density*0.6f, 0.1f, friction, CollisionMasks.Mask_LEG, (short)(CollisionMasks.Mask_DEFAULT|CollisionMasks.Mask_BODY));
        bodies[5] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, rLegPos, pos.x, pos.y+(bodyPos[2]), density*0.6f, 0.1f, friction, CollisionMasks.Mask_ARM, (short)(CollisionMasks.Mask_DEFAULT|CollisionMasks.Mask_BODY));
        bodies[6] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bowPos, pos.x, pos.y-lArmPos[5], 0.001f, 0.1f, friction, CollisionMasks.Mask_BOW, CollisionMasks.Mask_DEFAULT);

        for(Body b: bodies) {
            b.setUserData(new BodyUserDataClass("humanoid", 10f));
        }

        RevoluteJoint headJoint = WorldUtils.makeRevJoint(world, bodies[0], bodies[1],
                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((headPos[0]+headPos[2])/2, headPos[1]), true, 1.04f, -1.04f, true, stiffness);
        armJoint1 = WorldUtils.makeRevJoint(world, bodies[0], bodies[2],
                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((lArmPos[0]+lArmPos[2])/2, lArmPos[1]), false, 0, 0, true, stiffness);
        RevoluteJoint armJoint2 = WorldUtils.makeRevJoint(world, bodies[0], bodies[3],
                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[5]), new Vector2((rArmPos[0]+rArmPos[2])/2, rArmPos[1]), false, 0, 0, true, stiffness);
        RevoluteJoint legJoint1 = WorldUtils.makeRevJoint(world, bodies[0], bodies[4],
                new Vector2((bodyPos[0]+bodyPos[2])/2, bodyPos[1]), new Vector2((lLegPos[0]+lLegPos[2])/2, lLegPos[1]), true, 1.7f, -1.7f, true, stiffness);
        RevoluteJoint legJoint2 = WorldUtils.makeRevJoint(world, bodies[0], bodies[5],
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
}
