package com.bearfishapps.shadowarcher.Physics.Collision;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bearfishapps.shadowarcher.Physics.BodyUserDataClass;

import java.util.ArrayList;

public class CollisionListener implements ContactListener {

    ArrayList<StickyArrowClass> stuffToStick;
    ArrayList<Body> humanoidContacts;

    public CollisionListener(ArrayList<StickyArrowClass> stuffToStick, ArrayList<Body> humanoidContacts) {
        this.stuffToStick = stuffToStick;
        this.humanoidContacts = humanoidContacts;
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

                float graceTime = 0.3f;

                if (((a.getType().equals("arrow") &&
                        ((BodyUserDataClass) contact.getFixtureA().getBody().getUserData()).getDeltaTime() > graceTime) ||
                        (b.getType().equals("arrow") &&
                                ((BodyUserDataClass) contact.getFixtureB().getBody().getUserData()).getDeltaTime() > graceTime)) &&
                                (!(b.getType().equals("arrow") && a.getType().equals("arrow")))) {
                    if (impulse.getNormalImpulses()[0] > a.getHardness()) {
                        StickyArrowClass sc = new StickyArrowClass( contact.getFixtureB().getBody(),contact.getFixtureA().getBody());
                        stuffToStick.add(sc);
                        if((a.getType().equals("humanoid")))
                            humanoidContacts.add(contact.getFixtureA().getBody());
                    } else if (impulse.getNormalImpulses()[0] > b.getHardness()) {
                        StickyArrowClass sc = new StickyArrowClass(contact.getFixtureA().getBody(), contact.getFixtureB().getBody());
                        stuffToStick.add(sc);
                        if((b.getType().equals("humanoid")))
                            humanoidContacts.add(contact.getFixtureB().getBody());
                    }
                }
            }
        }
    }
}
