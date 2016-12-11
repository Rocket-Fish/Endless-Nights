package com.bearfishapps.shadowarcher.Physics.InputInterpretors;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Arrow;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.Humanoid;

public class TouchSensor implements InputProcessor {

    private Camera camera;
    private Body arm, arm2;
    private Humanoid humanoid;
    public TouchSensor(Humanoid humanoid, Camera camera) {
        this.arm = humanoid.getBodies()[2];
        this.arm2 = humanoid.getBodies()[3];
        this.humanoid = humanoid;
        this.camera = camera;
    }

    private Vector3 touchPos = new Vector3(0, 0, 0);
    private Vector3 moveVec = new Vector3(0, 0, 0);
    private boolean isPressed = false, hasMoved = false;

    public void refresh() {
        if(isPressed && hasMoved) {
            float dx = touchPos.x - moveVec.x;
            if (Math.abs(dx) < 0.001f) {
                if(dx < 0)
                    dx = -0.001f;
                else
                    dx = 0.001f;
            }
            float dy = touchPos.y - moveVec.y;

            float angle = MathUtils.atan2(dy, dx);
            float halfPI = MathUtils.PI/2;
            arm.setTransform(arm.getPosition(), angle + halfPI);
            arm2.setTransform(arm.getPosition(), angle + halfPI);

            Arrow a = humanoid.getArrow();
            if(a != null) {
                humanoid.getArrow().rotateTo(angle);
            }
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
        hasMoved = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        hasMoved = true;
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
