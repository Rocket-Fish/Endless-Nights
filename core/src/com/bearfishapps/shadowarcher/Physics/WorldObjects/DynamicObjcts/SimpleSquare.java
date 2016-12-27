package com.bearfishapps.shadowarcher.Physics.WorldObjects.DynamicObjcts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.bearfishapps.libs.Tools.PhysicsWorld.WorldUtils;
import com.bearfishapps.libs.Tools.Rendering.RenderHelper;
import com.bearfishapps.shadowarcher.Physics.Collision.CollisionMasks;
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

import java.util.ArrayList;

import box2dLight.Light;
import box2dLight.RayHandler;

// PowerupCreator :P
public class SimpleSquare extends CustomPhysicsBody {
    private final float density = 10.3f;
    private final float friction = 1f;
    protected float boxPos[] = {-0.25f, -0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, 0.25f};

    private float scale;
    private Light light;
    public float LIGHT_INTENSITY;
    private RayHandler rayHandler;
    private ArrayList<Arrow> arrows;

    public SimpleSquare(Body b) {
        super(b);
    }

    private int type;
    private Color[] typeColors = {Color.FIREBRICK, Color.WHITE};
    public SimpleSquare(World world, RayHandler rayHandler, Vector2 position, float scale, ArrayList<Arrow> arrows) {
        this(world, rayHandler, position, scale, -1, arrows);
    }
    public SimpleSquare(World world, RayHandler rayHandler, Vector2 position, float scale, int type, ArrayList<Arrow> arrows) {
        super(world, 1);
        this.arrows = arrows;
        this.rayHandler = rayHandler;
        this.scale = scale;
        LIGHT_INTENSITY = MathUtils.random(2.3f, 4.5f);

        this.type = type!=-1?type:MathUtils.random(0, typeColors.length-1);
        createBody(position);
    }

    private void createBody(Vector2 pos) {

        boxPos = WorldUtils.scaleF(boxPos, scale);
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, boxPos, pos.x, pos.y, density, 0.1f, friction);
        BodyUserDataClass bodyUserData = new BodyUserDataClass("simpleSquare");
        bodies[0].setUserData(bodyUserData);

        bodies[0].setGravityScale(0.02f);
        bodies[0].setAngularDamping(1.5f);

        light = WorldUtils.initPointLight(rayHandler, LIGHT_INTENSITY, bodies[0], new Vector2(0, 0));
        light.setColor(typeColors[type]);
        light.setXray(true);
    }

    private boolean activated = false;
    public void shotByArrow() {
        activated = true;
    }


    private boolean temporaryStateActivated = false;
    @Override
    public void draw(ShapeRenderer renderer) {
        if(bodies[0] == null)
            return;
        renderer.setColor(0, 0, 0, 1);

        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(boxPos, bodies[0]);
        RenderHelper.filledPolygon(renderPos, renderer);

        if(activated && light.isActive()) {
            if(type == 0) {
                light.setDistance(light.getDistance()-0.1f);
                if(light.getDistance()<1 && temporaryStateActivated)
                    light.setActive(false);
                if(!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    for(int i = 0; i < 45; i++){
                        Arrow a = new Arrow(world, rayHandler, 1);
                        a.release(5, new Vector2(i, 30), MathUtils.random(93f, 87f));
                        arrows.add(a);
                    }
                }
            }
            else if(type == 1) {
                if(light.isXray())
                    light.setXray(false);
                if(!temporaryStateActivated)
                    light.setDistance(light.getDistance()+5f);
                else
                    light.setDistance(light.getDistance()-0.05f);
                if(light.getDistance()>45 && !temporaryStateActivated) {
                    temporaryStateActivated = true;
                }
                if(light.getDistance()<1.1 && temporaryStateActivated)
                    light.setActive(false);
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        light.remove();
    }
}
