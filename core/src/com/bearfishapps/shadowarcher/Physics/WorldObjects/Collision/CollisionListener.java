package com.bearfishapps.shadowarcher.Physics.WorldObjects.Collision;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;

public class CollisionListener implements ContactListener {

    public CollisionListener() {
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Object objA = contact.getFixtureA().getBody().getUserData();
        Object objB = contact.getFixtureB().getBody().getUserData();

        if (objA != null && objA instanceof BodyUserDataClass) {
            if (objB != null && objB instanceof BodyUserDataClass) {
                BodyUserDataClass a = (BodyUserDataClass) objA;
                BodyUserDataClass b = (BodyUserDataClass) objB;
/*
                if ((a.getType().equals("arrow") &&
                        ((BodyUserDataClass) contact.getFixtureA().getBody().getUserData()).getDeltaTime() > 1f) ||
                        (b.getType().equals("arrow") &&
                                ((BodyUserDataClass) contact.getFixtureB().getBody().getUserData()).getDeltaTime() > 1f) &&
                                (!(b.getType().equals("arrow") && a.getType().equals("arrow")))) {
                    if (impulse.getNormalImpulses()[0] > a.getHardness()) {
                        StickyClass sc = new StickyClass(contact.getFixtureA().getBody(), contact.getFixtureB().getBody());
                        stuffToStick.add(sc);
                    } else if (impulse.getNormalImpulses()[0] > b.getHardness()) {
                        StickyClass sc = new StickyClass(contact.getFixtureA().getBody(), contact.getFixtureB().getBody());
                        stuffToStick.add(sc);
                    }
                }*/
            }
        }
    }
}
