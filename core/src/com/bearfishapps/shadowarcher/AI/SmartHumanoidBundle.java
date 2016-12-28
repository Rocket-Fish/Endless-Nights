package com.bearfishapps.shadowarcher.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.CustomPhysicsBody;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup;

import java.util.ArrayList;

import box2dLight.RayHandler;

public class SmartHumanoidBundle extends HumanGroundBundleGroup{
    private int stepped = 0;

    public SmartHumanoidBundle(World world, RayHandler rayHandler, Vector2 position, float scale) {
        super(world, rayHandler, position, scale);
        setAngle(-MathUtils.PI/2);
    }

    private ArrayList<Arrow> arrows = new ArrayList<Arrow>();
    public void update(Vector2 humanPlayerPosition) {
        stepped ++;
        setTarget(humanPlayerPosition.x, humanPlayerPosition.y);

        float dx = humanPlayerPosition.x-getHumanoid().getBodies()[2].getPosition().x;
        float dy = humanPlayerPosition.y-getHumanoid().getBodies()[2].getPosition().y;
        if(Math.abs(dx)< 0.00000001f) {
            if(dx > 0)
                dx = 0.00000001f;
            else
                dx = -0.00000001f;
        }
        float angle = MathUtils.atan2(dy, dx)+MathUtils.PI*0.45f;
        Gdx.app.log("angle", String.valueOf(angle) + " dx "+dx + " dy "+dy);
        setAngle(angle);
        if(stepped%60 == 0) {
            arrows.add(getHumanoid().drawArrow());
            getHumanoid().shootArrow();
        }
    }

    private void setAngle(float angle) {
        getHumanoid().getBodies()[2].setTransform(getHumanoid().getBodies()[2].getPosition(), angle);
        getHumanoid().getBodies()[3].setTransform(getHumanoid().getBodies()[3].getPosition(), angle);
    }

    public ArrayList<Arrow> getArrows() {
        return arrows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmartHumanoidBundle)) return false;

        SmartHumanoidBundle cpb = (SmartHumanoidBundle) o;
        if(cpb.getHumanoid() == null)
            return false;

        return cpb.getHumanoid().equals(cpb.getHumanoid());
    }

    @Override
    public int hashCode() {
        return getHumanoid().getBodies().hashCode();
    }

}
