package com.bearfishapps.libs.Tools.Rendering;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RenderHelper {
    public static void filledPolygon(float[] points, ShapeRenderer renderer) {
        for(int i = 0; i < (points.length/2)-2; i ++) {
            renderer.triangle(points[0], points[1], points[(i+1)*2], points[(i+1)*2+1], points[(i+2)*2], points[(i+2)*2+1]);
        }
    }
}
