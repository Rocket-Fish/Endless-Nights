package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class CustomPhysicsBody {
    protected Body[] bodies;

    public CustomPhysicsBody(int size) {
        bodies = new Body[size];
    }

    public Body[] getBodies() {
        return bodies;
    }

}
