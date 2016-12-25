package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.libs.Tools.Rendering.RenderHelper;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

public class Obstacle extends CustomPhysicsBody {
    private final float friction = 1f;
    private final float density = 10.1f;
    protected float bodyPos[];
    private float scale;
    private Vector2[] path;
    private float rotations[];

    public Obstacle(World world, Vector2 position, float scale) {
        super(world, 1);

        this.scale = scale;
        int count = MathUtils.random(3, 8);
        bodyPos = new float[count*2];
        float e = MathUtils.random(5f, 10f);
        for(int i = 0; i < count; i+=2) {
            bodyPos[i] = MathUtils.random(-e,e);
            bodyPos[i+1] = MathUtils.random(-e,e);
        }

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.KinematicBody,bodyPos, position.x, position.y, density, 0f, friction);
        bodies[0].setUserData(new BodyUserDataClass("ground_movable"));

    }

    @Override
    public void draw(ShapeRenderer renderer) {
        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(bodyPos, bodies[0]);
        renderPos = WorldUtils.scaleF(renderPos, scale);
        RenderHelper.filledPolygon(renderPos, renderer);
    }
}
