package com.bearfishapps.shadowarcher.Physics.WorldObjects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.Humanoid;
import com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts.MovableGround;

import box2dLight.Light;
import box2dLight.RayHandler;

public class HumanGroundBundleGroup {
    private Vector2 target;
    private Humanoid humanoid;
    private MovableGround movableGround;
    private float scale;

    private Light light;
    public final float LIGHT_INTENSITY = 3f;

    public HumanGroundBundleGroup(World world, RayHandler rayHandler, Vector2 position, float scale) {
        humanoid = new Humanoid(world,rayHandler, position, scale);
        movableGround = new MovableGround(world, position, scale);
        target = new Vector2(position);
        this.scale = scale;

        light = WorldUtils.initPointLight(rayHandler, LIGHT_INTENSITY, humanoid.getBodies()[3], new Vector2(0, -0.67f));
        light.setXray(true);
/*        light.setContactFilter(CollisionMasks.Mask_DEFAULT,
                humanoid.getBodies()[3].getFixtureList().get(0).getFilterData().groupIndex,
                (short)(CollisionMasks.Mask_DEFAULT|CollisionMasks.Mask_Humanoid));*/
        light.setColor(1, 1, 1, 1);
    }

    public void check() {
        Vector2 pos = new Vector2(humanoid.getBodies()[0].getWorldCenter()).scl(scale);
        Vector2 diff = new Vector2(target.x - pos.x, target.y - pos.y);

        if(diff.len2() < 0.1f) {
            movableGround.setVelocity(0, 0);
            humanoid.setVelocity(0,0);
        }
    }

    public void setTarget(float x, float y) {
        target.set(x, y);

        shift();
    }

    private void shift() {
        Vector2 pos = new Vector2(humanoid.getBodies()[0].getWorldCenter()).scl(scale);
        Vector2 diff = new Vector2(target.x - pos.x, target.y - pos.y);

        Vector2 vel = WorldUtils.normalize(diff).scl(5);
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
        light.remove();
    }

    public void draw(ShapeRenderer renderer) {
        humanoid.draw(renderer);
        movableGround.draw(renderer);
    }

    public void resetHumanoidContactWithGround() {
        humanoid.falsifyContact();
    }

}
