package com.bearfishapps.shadowarcher.Physics.UserDataClass;

public class HumanoidUserDataClass extends BodyUserDataClass {
    private boolean isInContactWithMatchingPlatform = true;
    public HumanoidUserDataClass(String type) {
        super(type);
    }

    public boolean isInContactWithMatchingPlatform() {
        return isInContactWithMatchingPlatform;
    }

    public void setInContactWithMatchingPlatform(boolean inContactWithMatchingPlatform) {
        isInContactWithMatchingPlatform = inContactWithMatchingPlatform;
    }
}
