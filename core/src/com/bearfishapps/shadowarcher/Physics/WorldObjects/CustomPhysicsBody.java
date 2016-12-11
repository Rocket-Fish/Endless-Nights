package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;

public abstract class CustomPhysicsBody {
    protected Body[] bodies;

    public CustomPhysicsBody(int size) {
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


}
