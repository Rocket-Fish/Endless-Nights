package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.libs.Tools.Rendering.RenderHelper;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

public class Obstacle extends CustomPhysicsBody {
    private final float friction = 1f;
    private final float density = 1000.1f;
    protected float bodyPos[];
    private float scale;

    public Obstacle(World world, Vector2 position, float scale) {
        super(world, 1);

        this.scale = scale;
        int count = MathUtils.random(4, 8);
        bodyPos = new float[count*2];
        float e = MathUtils.random(0.5f, 1.9f);
        float minR = MathUtils.random(e+0.9f, e+2.3f);
        int c = 0;
        for(int i = 0; i < bodyPos.length-1; i+=2) {
            float angle = MathUtils.PI*2f*(((float)c) +
                    MathUtils.random(-MathUtils.PI*2f*0.35f/((float)count), MathUtils.PI*2f*0.25f/((float)count)))/((float)count);
            float r = MathUtils.random(-e, e);
            bodyPos[i] = (minR+r)*MathUtils.cos(angle);
            bodyPos[i+1] = (minR+r)*MathUtils.sin(angle);
            c++;
        }

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody,bodyPos, position.x, position.y, density, 0f, friction);
        bodies[0].setGravityScale(0);
        bodies[0].setUserData(new BodyUserDataClass("obstacle"));

    }

    @Override
    public void draw(ShapeRenderer renderer) {
        renderer.setColor(0, 0, 0, 1);
        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(bodyPos, bodies[0]);
        renderPos = WorldUtils.scaleF(renderPos, scale);
        RenderHelper.filledPolygon(renderPos, renderer);
    }
}
