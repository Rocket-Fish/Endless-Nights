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

    public Obstacle(World world, Vector2 position, float scale) {
        super(world, 1);

        this.scale = scale;
        int count = MathUtils.random(4, 8);
        bodyPos = new float[count*2];
        float e = MathUtils.random(2f, 3.5f);
        float minR = MathUtils.random(e+1.5f, e+4f);
        int c = 0;
        for(int i = 0; i < bodyPos.length-1; i+=2) {
            bodyPos[i] = (minR+MathUtils.random(-e,e))*MathUtils.cos(MathUtils.PI*2f*((float)c)/((float)count));
            bodyPos[i+1] = (minR+MathUtils.random(-e,e))*MathUtils.sin(MathUtils.PI*2f*((float)c)/((float)count));
            c++;
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
