package com.aga.woodentangrampuzzle;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 *
 * Created by Andrii Husiev on 17.12.2015 for Wooden Tangram.
 *
 */
public class TangramLevel {

    private float[] x;
    private float[] y;
    private Path path;
    private Path pathHole;
    private Rect tileBounds;
    private PointF pivotPoint;
    private boolean hasHole;
    private String levelTimerString;

    public TangramLevel(int[] x, int[] y) {
        int n = x.length;
        this.x = new float[n];
        this.y = new float[n];
        hasHole = false;

        for (int i = 0; i < n; i++) {
            this.x[i] = (float) x[i];
            this.y[i] = (float) y[i];
            if (x[i] == 0)
                hasHole = true;
        }

        calcBounds();
        setPivotPoint();
        xyToPath();
    }

    /**
     * Centers the polygon to given rectangle.
     *
     * @param bounds Bounds of work area.
     */
    public void setToCenter(RectF bounds) {
        // Offset between pivot point and center.
        float offsetX = bounds.centerX() - pivotPoint.x;
        float offsetY = bounds.centerY() - pivotPoint.y;
        //Log.d("debug", "offsetX: " + offsetX);
        //Log.d("debug", "offsetY: " + offsetY);
        offset(offsetX, offsetY);
        calcBounds();
        setPivotPoint();
        xyToPath();
    }

    public void scale(float scaleFactor) {
        float xx, yy;

        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            xx = x[i] - pivotPoint.x;
            yy = y[i] - pivotPoint.y;
            xx *= scaleFactor;
            yy *= scaleFactor;
            x[i] = xx + pivotPoint.x;
            y[i] = yy + pivotPoint.y;
        }

        calcBounds();
        xyToPath();
    }

    /**
     * Change size of polygon to the given size.
     *
     * @param size Bounding value for height and width of polygon.
     * @param independentResize If true both parameters will be scaled independently to fit to given size.
     *                          If false they will be scaled to fit to given size, saving aspect ratio.
     */
    public void resize(float size, boolean independentResize) {
        float xx, yy;
        float scaleFactorX, scaleFactorY;
        if (independentResize) {
            scaleFactorX = size / tileBounds.width();
            scaleFactorY = size / tileBounds.height();
        }
        else {
            if (tileBounds.width() > tileBounds.height())
                scaleFactorX = scaleFactorY = size / tileBounds.width();
            else
                scaleFactorX = scaleFactorY = size / tileBounds.height();
        }

        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            xx = x[i] - pivotPoint.x;
            yy = y[i] - pivotPoint.y;
            xx *= scaleFactorX;
            yy *= scaleFactorY;
            x[i] = xx + pivotPoint.x;
            y[i] = yy + pivotPoint.y;
        }

        calcBounds();
        xyToPath();
    }

    public Path getPath() {
        return path;
    }

    public Path getPathHole() {
        return pathHole;
    }

    public Rect getPolygonBounds() {
        return tileBounds;
    }

    public boolean isHasHole() {
        return hasHole;
    }

    public void setLevelTimerString(String time) {
        levelTimerString = time;
    }

    public String getLevelTimerString() {
        return levelTimerString;
    }

    private void offset(float dx, float dy) {
        for (int i = 0; i < x.length; i++) {
            if (x[i] == 0) continue;
            x[i] += dx;
            y[i] += dy;
        }
    }

    private void calcBounds() {
        tileBounds = new Rect((int) x[0], (int) y[0], (int) x[0], (int) y[0]);

        for (int i = 1; i < x.length; i++) {
            if (x[i] == 0) continue;
            tileBounds.left = Math.min(tileBounds.left, (int) x[i]);
            tileBounds.top = Math.min(tileBounds.top, (int) y[i]);
            tileBounds.right = Math.max(tileBounds.right, (int) x[i]);
            tileBounds.bottom = Math.max(tileBounds.bottom, (int) y[i]);
        }
        //Log.d("debug", "tileBounds: " + tileBounds.left + ", " + tileBounds.top + tileBounds.right + ", " + tileBounds.bottom);
    }

    private void setPivotPoint() {
        pivotPoint = new PointF(0, 0);
        pivotPoint.x = tileBounds.left + tileBounds.width() / 2f;
        pivotPoint.y = tileBounds.top + tileBounds.height() / 2f;

        //Log.d("debug", "pivotPoint: " + pivotPoint.x + ", " + pivotPoint.y);
    }

    private void xyToPath() {
        boolean initPathHole = false;

        pathHole = null;
        path = null;
        path = new Path();
        path.moveTo(x[0],y[0]);
        int n = x.length;

        for (int i = 1; i < n; i++) {
            if (x[i] == 0) {
                path.close();
                i++;
                pathHole = new Path();
                pathHole.moveTo(x[i],y[i]);
                initPathHole = true;
            }
            if (!initPathHole)
                path.lineTo(x[i], y[i]);
            else
                pathHole.lineTo(x[i], y[i]);
        }

        if (pathHole == null)
            path.close();
        else
            pathHole.close();
    }
}
