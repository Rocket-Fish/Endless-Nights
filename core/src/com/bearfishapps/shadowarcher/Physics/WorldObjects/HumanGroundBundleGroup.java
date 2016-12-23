package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.MovableGround;

public class HumanGroundBundleGroup {
    private Vector2 target;
    private Humanoid humanoid;
    private MovableGround movableGround;
    private float scale;
    public HumanGroundBundleGroup(World world, Vector2 position, float scale) {
        humanoid = new Humanoid(world, position, scale);
        movableGround = new MovableGround(world, position, scale);
        target = new Vector2(position);
        this.scale = scale;
    }

    public void check() {
        Vector2 pos = new Vector2(humanoid.getBodies()[0].getPosition()).scl(scale);
        Vector2 diff = new Vector2(target.x - pos.x, target.y - pos.y);

        if(diff.len2() < 6) {
            movableGround.setVelocity(0, 0);
            humanoid.setVelocity(0,0);
        }
    }

    public void setTarget(float x, float y) {
        target.set(x, y);

        shift();
    }

    private void shift() {
        Vector2 pos = new Vector2(humanoid.getBodies()[0].getPosition()).scl(scale);
        Vector2 diff = new Vector2(target.x - pos.x, target.y - pos.y);

        Vector2 vel = WorldUtils.normalize(diff).scl(3);
        //Gdx.app.log("velocity", String.valueOf(vel));
        movableGround.setVelocity(vel.x, vel.y);
        humanoid.setVelocity(vel.x, vel.y);
    }

    public Humanoid getHumanoid() {
        return humanoid;
    }

    public void destroy() {
        humanoid.destroyArrow();
        humanoid.destroy();
        movableGround.destroy();
    }

    public void draw(ShapeRenderer renderer) {
        humanoid.draw(renderer);
        movableGround.draw(renderer);
    }

    public void resetHumanoidContactWithGround() {
        humanoid.falsifyContact();
    }

}
