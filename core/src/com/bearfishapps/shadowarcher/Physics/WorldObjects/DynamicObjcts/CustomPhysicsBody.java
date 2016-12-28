package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.shadowarcher.Graphics.Renderable;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

public abstract class CustomPhysicsBody implements Renderable{
    protected Body[] bodies;
    protected World world;

    public CustomPhysicsBody(Body b) {
        bodies = new Body[]{b};
    }

    public CustomPhysicsBody(World world, int size) {
        this.world = world;
        bodies = new Body[size];
    }

    public Body[] getBodies() {
        return bodies;
    }

    public void incrementStep() {
        for(Body b: bodies) {
            ((BodyUserDataClass)b.getUserData()).step();
        }
    }

    public void destroy() {
        for(Body b: bodies) {
            if(b!=null)
                world.destroyBody(b);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomPhysicsBody)) return false;

        CustomPhysicsBody cpb = (CustomPhysicsBody) o;
        if(bodies[0] == null)
            return false;
        if(cpb.getBodies()[0] == null)
            return false;

        return bodies[0].getPosition().equals(cpb.getBodies()[0].getPosition());
    }

    @Override
    public int hashCode() {
        return bodies[0].getPosition().hashCode();
    }
}
