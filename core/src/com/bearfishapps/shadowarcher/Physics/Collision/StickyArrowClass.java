package com.bearfishapps.shadowarcher.Physics.Collision;

import com.badlogic.gdx.physics.box2d.Body;

public class StickyArrowClass {
    protected Body arrow;
    protected Body body2;

    public StickyArrowClass(Body arrow, Body body2) {
        this.arrow = arrow;
        this.body2 = body2;
    }

    public Body getArrow() {
        return arrow;
    }

    public Body getBody2() {
        return body2;
    }
}
