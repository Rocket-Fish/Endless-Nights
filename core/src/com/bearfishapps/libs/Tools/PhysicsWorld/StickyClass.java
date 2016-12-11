package com.bearfishapps.libs.Tools.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;

public class StickyClass {
    protected Body body1;
    protected Body body2;

    public StickyClass(Body body1, Body body2) {
        this.body1 = body1;
        this.body2 = body2;
    }

    public Body getBody1() {
        return body1;
    }

    public Body getBody2() {
        return body2;
    }
}
