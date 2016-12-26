package com.bearfishapps.shadowarcher.Physics.Collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.HumanoidUserDataClass;

import java.util.ArrayList;

public class CollisionListener implements ContactListener {

    ArrayList<StickyArrowClass> stuffToStick;
    ArrayList<Body> humanoidContacts, bodiesToDelete;

    public CollisionListener(ArrayList<StickyArrowClass> stuffToStick, ArrayList<Body> humanoidContacts, ArrayList<Body> bodiesToDeleteList) {
        this.stuffToStick = stuffToStick;
        this.humanoidContacts = humanoidContacts;
        bodiesToDelete = bodiesToDeleteList;
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

                if(a.getType().equals("deathPlatform")){
                    bodiesToDelete.add(contact.getFixtureB().getBody());
                    return;
                }
                if(b.getType().equals("deathPlatform")){
                    bodiesToDelete.add(contact.getFixtureA().getBody());
                    return;
                }

                if (a.getType().equals("ground_movable") && b.getType().equals("humanoid")) {
                    ((HumanoidUserDataClass)objB).setInContactWithMatchingPlatform(true);
                } else if (b.getType().equals("ground_movable") && a.getType().equals("humanoid")) {
                    ((HumanoidUserDataClass)objA).setInContactWithMatchingPlatform(true);
                }

                int graceSteps = 3;
                if (a.getType().equals("arrow") &&
                        ((BodyUserDataClass) contact.getFixtureA().getBody().getUserData()).getStepCount() > graceSteps) {
                    if(a.isSticky()) {
                        a.setSticky(false);
                        StickyArrowClass sc = new StickyArrowClass(contact.getFixtureA().getBody(), contact.getFixtureB().getBody());
                        stuffToStick.add(sc);
                        if ((b.getType().equals("humanoid")))
                            humanoidContacts.add(contact.getFixtureB().getBody());
                    }
                } else if (b.getType().equals("arrow")&&
                        ((BodyUserDataClass) contact.getFixtureB().getBody().getUserData()).getStepCount() > graceSteps) {
                    if(b.isSticky()) {
                        b.setSticky(false);
                        StickyArrowClass sc = new StickyArrowClass(contact.getFixtureB().getBody(), contact.getFixtureA().getBody());
                        stuffToStick.add(sc);
                        if ((a.getType().equals("humanoid")))
                            humanoidContacts.add(contact.getFixtureA().getBody());
                    }
                }
            }
        }
    }
}
