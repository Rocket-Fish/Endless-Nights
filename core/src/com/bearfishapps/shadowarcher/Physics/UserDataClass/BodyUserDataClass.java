package com.bearfishapps.shadowarcher.Physics.UserDataClass;

public class BodyUserDataClass {
    private String type;
    private int stepCount;
    private boolean isSticky;

    public BodyUserDataClass(String type) {
        this.type = type;
        stepCount = 0;
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

    public void step() {
        stepCount++;
    }

    public float getStepCount() {
        return stepCount;
    }
}
