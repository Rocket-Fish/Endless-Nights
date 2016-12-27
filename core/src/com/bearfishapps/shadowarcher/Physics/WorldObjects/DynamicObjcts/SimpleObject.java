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
import com.bearfishapps.shadowarcher.Physics.UserDataClass.BodyUserDataClass;

import java.util.ArrayList;
import java.util.ListIterator;

import box2dLight.Light;
import box2dLight.RayHandler;

// PowerupCreator :P
public class SimpleObject extends CustomPhysicsBody {
    private final float density = 5.6f;
    private final float friction = 1f;
    protected final float rectanglePos[] = {-0.25f, -0.25f, 0.25f, -0.25f, 0.25f, 0.25f, -0.25f, 0.25f},
            trianglePos[] = {-0.25f, -0.25f, 0.25f, -0.25f, 0f, 0.25f};
    protected float bodyPos[];

    private float scale;
    private Light light;
    public float LIGHT_INTENSITY;
    private RayHandler rayHandler;
    private ArrayList<Arrow> arrows;

    public SimpleObject(Body b) {
        super(b);
    }

    private int type;
    private Color[] typeColors = {Color.FIREBRICK, Color.WHITE, Color.TEAL, new Color(226 / 255f, 134 / 255f, 34 / 255f, 1), Color.CYAN, Color.GOLD};

    public SimpleObject(World world, RayHandler rayHandler, Vector2 position, float scale, ArrayList<Arrow> arrows) {
        this(world, rayHandler, position, scale, -1, arrows);
    }

    public SimpleObject(World world, RayHandler rayHandler, Vector2 position, float scale, int type, ArrayList<Arrow> arrows) {
        super(world, 1);
        this.arrows = arrows;
        this.rayHandler = rayHandler;
        this.scale = scale;
        LIGHT_INTENSITY = MathUtils.random(2.3f, 4.5f);

        this.type = type != -1 ? type : MathUtils.random(0, typeColors.length - 1);
        if (this.type <= 2) {
            bodyPos = rectanglePos;
        }
        else if (this.type > 2 && this.type < 6) {
            bodyPos = trianglePos;
        }
        createBody(position);
    }

    private void createBody(Vector2 pos) {

        bodyPos = WorldUtils.scaleF(bodyPos, scale);
        bodies[0] = WorldUtils.createPoly(world, BodyDef.BodyType.DynamicBody, bodyPos, pos.x, pos.y, density, 0.1f, friction);
        BodyUserDataClass bodyUserData = new BodyUserDataClass("simpleObject");
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
        if (bodies[0] == null)
            return;
        renderer.setColor(0, 0, 0, 1);

        float[] renderPos = WorldUtils.matchBodyPositionFromFloat(bodyPos, bodies[0]);
        RenderHelper.filledPolygon(renderPos, renderer);

    }

    public void summonObjectsIfNeeded(ListIterator<CustomPhysicsBody> otherBodyIterator) {
        if (activated && light.isActive()) {
            if (type == 0) {
                light.setDistance(light.getDistance() - 0.1f);
                if (light.getDistance() < 1 && temporaryStateActivated)
                    light.setActive(false);
                if (!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    for (int i = 0; i < 45; i+=2) {
                        Arrow a = new Arrow(world, rayHandler, 1);
                        a.release(5, new Vector2(i, 30), MathUtils.random(93f, 87f));
                        arrows.add(a);
                    }
                }
            } else if (type == 1) {
                if (light.isXray())
                    light.setXray(false);
                if (!temporaryStateActivated)
                    light.setDistance(light.getDistance() + 5f);
                else
                    light.setDistance(light.getDistance() - 0.05f);
                if (light.getDistance() > 45 && !temporaryStateActivated) {
                    temporaryStateActivated = true;
                }
                if (light.getDistance() < 1.1 && temporaryStateActivated)
                    light.setActive(false);
            } else if (type == 2) {
                if (!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    light.setActive(false);
                    Obstacle o = new Obstacle(world, new Vector2(MathUtils.random(15, 30), 26), 1);
                    o.setVelocity(new Vector2(0, -0.25f));
                    otherBodyIterator.add(o);
                }
            } else if (type == 3) {
                if (!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    Arrow.globalArrowType = 0;
                }
            } else if (type == 4) {
                if (!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    Arrow.globalArrowType = 1;
                }
            } else if (type == 5) {
                if (!temporaryStateActivated) {
                    temporaryStateActivated = true;
                    Arrow.globalArrowType = 2;
                }
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        light.remove();
    }
}
