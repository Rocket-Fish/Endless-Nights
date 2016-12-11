package com.bearfishapps.shadowarcher.Physics.InputInterpretors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class TouchSensor implements InputProcessor {

    private Camera camera;
    private Body arm, arm2;
    public TouchSensor(Body arm, Body arm2,  Camera camera) {
        this.arm = arm;
        this.arm2 = arm2;
        this.camera = camera;
    }

    private Vector3 touchPos = new Vector3(0, 0, 0);
    private Vector3 moveVec = new Vector3(0, 0, 0);
    private boolean isPressed = false;

    public void refresh() {
        if(isPressed) {
            float dx = touchPos.x - moveVec.x;
            if (dx < 0.001f) dx = 0.001f;
            float dy = touchPos.y - moveVec.y;

            float angle = MathUtils.atan2(dy, dx);
            arm.setTransform(arm.getPosition(), angle + 1.58f);
            arm2.setTransform(arm.getPosition(), angle + 1.58f);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isPressed = true;
        touchPos.set(screenX, screenY, 0);
        camera.unproject(touchPos);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isPressed = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        moveVec.set(screenX, screenY, 0);
        camera.unproject(moveVec);
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
