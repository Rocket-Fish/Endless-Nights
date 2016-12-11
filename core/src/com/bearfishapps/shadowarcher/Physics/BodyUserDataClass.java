package com.bearfishapps.shadowarcher.Physics;

public class BodyUserDataClass {
    private String type;
    private float hardness;
    private float deltaTime;

    public BodyUserDataClass(String type) {
        this(type, 1);
    }

    public BodyUserDataClass(String type, float hardness) {
        this.type = type;
        this.hardness = hardness;
        deltaTime = 0;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }

    public String getType() {
        return type;
    }

    public float getHardness() {
        return hardness;
    }

    public void step(float delta) {
        deltaTime += delta;
    }

    public float getDeltaTime() {
        return deltaTime;
    }
}

