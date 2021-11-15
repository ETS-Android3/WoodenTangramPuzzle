package com.aga.woodentangrampuzzle;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;

/**
 *
 * Created by Andrii Husiev on 04.02.2016 for Wooden Tangram.
 *
 */
public class TangramTile {
    public static final float ANGLE = 0.785398f;

    private float[] startX, startY;
    private PointF pivotPoint, shadowOffset;
    private RectF boundsRect, tileBounds;
    public Paint shaderPaint, boundPaint;
    private Shader tileShader;
    private Matrix matrix;
    private Path mainPath, shadowPath;
    private Region tileRegion, clipRegion;
    private float accumulatedAngle = 0;
    private boolean boundsDefined = false;

    public TangramTile(int[] x, int[] y) {
        int n = x.length;
        startX = new float[n];
        startY = new float[n];

        for (int i = 0; i < n; i++) {
            startX[i] = (float) x[i];
            startY[i] = (float) y[i];
        }
        mainPath = xyToPath(startX, startY);
        setPivotPoint(startX, startY);

        boundsRect = new RectF(0,0,0,0);

        boundPaint = new Paint();
        boundPaint.setAntiAlias(true);
        boundPaint.setDither(true);
        boundPaint.setFilterBitmap(true);
        boundPaint.setStrokeWidth(0);
        boundPaint.setColor(Color.BLACK);
        boundPaint.setStyle(Paint.Style.STROKE);

        shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        shaderPaint.setDither(true);
        shaderPaint.setFilterBitmap(true);
        shaderPaint.setColor(Color.WHITE);
        shaderPaint.setStyle(Paint.Style.FILL);

        matrix = new Matrix();
        tileShader = new Shader();
        tileRegion = new Region();
        clipRegion = new Region();
        tileBounds = new RectF();
    }

    /**
     * Bounds of work area in which tile moved.
     *
     * @param bounds Rectangle of work area in which tile moved.
     */
    public void setBounds(RectF bounds) {
        boundsRect.left = bounds.left;
        boundsRect.top = bounds.top;
        boundsRect.right = bounds.right;
        boundsRect.bottom = bounds.bottom;

        boundsDefined = true;
    }

    public void setShader(Bitmap bitmap) {
        tileShader = null;
        tileShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        matrix.setTranslate(pivotPoint.x, pivotPoint.y);
        tileShader.setLocalMatrix(matrix);
        shaderPaint.setShader(tileShader);
    }

    public Path getPath() {
        matrix.setTranslate(pivotPoint.x, pivotPoint.y);
        matrix.postRotate(accumulatedAngle, pivotPoint.x, pivotPoint.y);
        tileShader.setLocalMatrix(matrix);
        shaderPaint.setShader(tileShader);

        return mainPath;
    }

    /**
     * Sets path, that can use like shadow of tile.
     * Make sure that you use this function before scaling.
     *
     * @param dx The horizontal offset from main polygon.
     * @param dy The vertical offset from main polygon.
     */
    public void setShadowPath(float dx, float dy) {
        shadowPath = xyToPath(startX, startY);
        shadowPath.offset(dx, dy);
        shadowOffset = new PointF(dx, dy);
    }

    public Path getShadowPath() {
        return shadowPath;
    }

    //<editor-fold desc="Custom public methods">
    public void scale(float scaleFactor) {
        matrix.setScale(scaleFactor, scaleFactor, pivotPoint.x, pivotPoint.y);
        mainPath.transform(matrix);
        if (shadowPath != null) {
            matrix.setScale(scaleFactor, scaleFactor, pivotPoint.x + shadowOffset.x, pivotPoint.y + shadowOffset.y);
            shadowPath.transform(matrix);
        }
    }

    /**
     * Offset the tile by (dx,dy) in bounds defined by {@link #setBounds}.
     * If bounds isn't defined the tile will be moved anyway.
     *
     * @param dx  The amount in the X direction to offset the entire tile.
     * @param dy  The amount in the Y direction to offset the entire tile.
     */
    public void offset(float dx, float dy) {
        unsafeOffset(dx, dy);
        if (boundsDefined) {
            backTileIntoTheBounds();
        }
    }

    /**
     * Moves the tile to the given coordinates. Defined bounds are ignored.
     *
     * @param x New absolute X-coordinate of tile pivot point (geometrical center).
     * @param y New absolute Y-coordinate of tile pivot point (geometrical center).
     */
    public void move(float x, float y) {
        float dx, dy;

        dx = x - pivotPoint.x;
        dy = y - pivotPoint.y;
        mainPath.offset(dx, dy);
        if (shadowPath != null)
            shadowPath.offset(dx + shadowOffset.x, dy + shadowOffset.x);

        pivotPoint.x = x;
        pivotPoint.y = y;
    }

    public void rotate(float angle) {
        float degrees = (float) Math.toDegrees(angle);

        matrix.setRotate(degrees, pivotPoint.x, pivotPoint.y);
        mainPath.transform(matrix);
        if (shadowPath != null) {
            matrix.setRotate(degrees, pivotPoint.x + shadowOffset.x, pivotPoint.y + shadowOffset.y);
            shadowPath.transform(matrix);
        }
        accumulatedAngle += degrees;

        backTileIntoTheBounds();
    }

    public boolean isPointInTile(int x, int y) {
        boolean result;

        mainPath.computeBounds(tileBounds, true);
        clipRegion.set((int)tileBounds.left, (int)tileBounds.top, (int)tileBounds.right, (int)tileBounds.bottom);
        tileRegion.setPath(mainPath, clipRegion);
        result = tileRegion.contains(x, y);

        return result;
    }
    //</editor-fold>

    //<editor-fold desc="Auxiliary private functions">
    private Path xyToPath(float[] x, float[] y) {
        Path path = new Path();
        path.moveTo(x[0],y[0]);
        int n = x.length;
        for (int i = 1; i < n; i++) {
            path.lineTo(x[i], y[i]);
        }
        // TODO: Самое время добавить path.cubicTo();
        path.close();

        return path;
    }

    private void setPivotPoint(float[] x, float[] y) {
        float x0, y0, x1, y1, a, signedArea = 0;
        int n = x.length - 1;
        pivotPoint = new PointF(0, 0);

        for (int i = 0; i < n; i++) {
            x0 = x[i];
            y0 = y[i];
            x1 = x[i+1];
            y1 = y[i+1];
            a = x0*y1 - x1*y0;
            signedArea += a;
            pivotPoint.x += (x0 + x1)*a;
            pivotPoint.y += (y0 + y1)*a;
        }
        x0 = x[n];
        y0 = y[n];
        x1 = x[0];
        y1 = y[0];
        a = x0*y1 - x1*y0;
        signedArea += a;
        pivotPoint.x += (x0 + x1)*a;
        pivotPoint.y += (y0 + y1)*a;

        signedArea *= 0.5;
        pivotPoint.x /= (6.0 * signedArea);
        pivotPoint.y /= (6.0 * signedArea);
    }

    /**
     * Returns tile to defined bounds if needed.
     */
    private void backTileIntoTheBounds() {
        float dx=0, dy=0;
        boolean isTileOutOfBounds = false;

        mainPath.computeBounds(tileBounds, true);
        if (tileBounds.left < boundsRect.left) {
            dx = boundsRect.left - tileBounds.left;
            isTileOutOfBounds = true;
        }
        if (tileBounds.right > boundsRect.right) {
            dx = boundsRect.right - tileBounds.right;
            isTileOutOfBounds = true;
        }
        if (tileBounds.top < boundsRect.top) {
            dy = boundsRect.top - tileBounds.top;
            isTileOutOfBounds = true;
        }
        if (tileBounds.bottom > boundsRect.bottom) {
            dy = boundsRect.bottom - tileBounds.bottom;
            isTileOutOfBounds = true;
        }
        if (isTileOutOfBounds)
            unsafeOffset(dx, dy);
    }

    /**
     * Shifts tile to given values without checking any conditions.
     *
     * @param dx  The amount in the X direction to offset the entire tile.
     * @param dy  The amount in the Y direction to offset the entire tile.
     */
    private void unsafeOffset(float dx, float dy) {
        mainPath.offset(dx, dy);
        if (shadowPath != null)
            shadowPath.offset(dx, dy);

        pivotPoint.x += dx;
        pivotPoint.y += dy;
    }
    //</editor-fold>
}
