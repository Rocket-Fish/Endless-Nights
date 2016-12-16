package com.bearfishapps.shadowarcher.Physics;

public class BodyUserDataClass {
    private String type;
    private float deltaTime;
    private boolean isSticky;

    public BodyUserDataClass(String type) {
        this.type = type;
        deltaTime = 0;
        isSticky = false;
    }

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public boolean isSticky() {
        return isSticky;
    }
    public String getType() {
        return type;
    }

    public void step(float delta) {
        deltaTime += delta;
    }

    public float getDeltaTime() {
        return deltaTime;
    }
}
