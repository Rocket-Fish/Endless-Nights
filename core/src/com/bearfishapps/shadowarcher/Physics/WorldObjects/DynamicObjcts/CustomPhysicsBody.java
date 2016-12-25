package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.shadowarcher.Graphics.Renderable;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

public abstract class CustomPhysicsBody implements Renderable{
    protected Body[] bodies;
    protected World world;

    public CustomPhysicsBody(World world, int size) {
        this.world = world;
        bodies = new Body[size];
    }

    public Body[] getBodies() {
        return bodies;
    }

    public void incrementTime(float delta) {
        for(Body b: bodies) {
            ((BodyUserDataClass)b.getUserData()).step(delta);
        }
    }

    public void destroy() {
        for(Body b: bodies) {
            if(b!=null)
                world.destroyBody(b);
        }

    }
}
