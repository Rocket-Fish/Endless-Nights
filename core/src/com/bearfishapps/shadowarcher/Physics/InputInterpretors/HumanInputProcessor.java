package com.bearfishapps.shadowarcher.Physics.InputInterpretors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.HumanGroundBundleGroup;

import java.util.ArrayList;

public class HumanInputProcessor implements InputProcessor {

    private Camera camera;
    private Body arm, arm2;
    private Humanoid humanoid;
    private HumanGroundBundleGroup humanoidBundle;
    private Vector2 targetPos = null;
    ArrayList<Arrow> arrows;
    private int screenXstart, screeXend;
    public HumanInputProcessor(ArrayList<Arrow> arrows, HumanGroundBundleGroup humanoidBundle, Camera camera, int screenXstart, int screenXend) {
        this.arm = humanoidBundle.getHumanoid().getBodies()[2];
        this.arm2 = humanoidBundle.getHumanoid().getBodies()[3];
        this.humanoidBundle = humanoidBundle;
        humanoid = humanoidBundle.getHumanoid();
        this.camera = camera;
        this.arrows = arrows;

        this.screenXstart = screenXstart;
        this.screeXend = screenXend;
    }

    private Vector3 touchPos = new Vector3(0, 0, 0);
    private Vector3 moveVec = new Vector3(0, 0, 0);
    private boolean isPressed = false, hasMoved = false, shooting = false, tapped = false;
    public void refresh() {
        Arrow a = humanoid.getArrow();

        if(!humanoid.isAlive()) {
            if(a != null)
                humanoid.destroyArrow();
            return;
        }

        humanoidBundle.check();
        if(targetPos != null) {
            humanoidBundle.setTarget(targetPos.x, targetPos.y);
            targetPos = null;
        }

        float dx = touchPos.x - moveVec.x;
        float dy = touchPos.y - moveVec.y;
        humanoid.remainActive();
        if (Math.abs(dx) < 0.00000000001f) {
            dx = (dx < 0)?-0.00000000001f:0.00000000001f;
        }

        float angle = MathUtils.atan2(dy, dx);
        float halfPI = MathUtils.PI/2;

        if(isPressed && hasMoved) {
            arm.setTransform(arm.getPosition(), angle + halfPI);
            arm2.setTransform(arm.getPosition(), angle + halfPI);
        }
        else if(shooting && humanoid.getArrow() != null) {
            humanoid.shootArrow();
            arrows.add(humanoid.drawArrow());
            shooting = false;
        }

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        if(touchPos.x >  screenXstart && touchPos.x < screeXend) {
            isPressed = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        moveVec.set(screenX, screenY, 0);
        camera.unproject(moveVec);
        if(isPressed && (!hasMoved || new Vector2(touchPos.x, touchPos.y).sub(new Vector2(moveVec.x, moveVec.y)).len2() < 1)) {
            isPressed = false;
            targetPos = new Vector2(touchPos.x, touchPos.y);
        }
        else if(isPressed && hasMoved) {
            isPressed = false;
            hasMoved = false;
            shooting = true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        moveVec.set(screenX, screenY, 0);
        camera.unproject(moveVec);
        if(touchPos.x >  screenXstart && touchPos.x < screeXend && isPressed) {
            hasMoved = true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

}
