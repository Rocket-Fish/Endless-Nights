package com.bearfishapps.shadowarcher.Physics.WorldObjects.StaticObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;

public class DeathPlatform extends GroundPlatform {
    public DeathPlatform(World world, Vector2 one, Vector2 two) {
        super(world, one, two);
        bodies[0].setUserData(new BodyUserDataClass("deathPlatform"));
    }
}
