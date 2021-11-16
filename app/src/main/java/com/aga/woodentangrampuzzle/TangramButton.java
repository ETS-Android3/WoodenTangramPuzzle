package com.aga.woodentangrampuzzle;

import static com.aga.woodentangrampuzzle.utils.ObjectBuildHelper.logDebugOut;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

/**
 *
 * Created by Andrii Husiev on 19.01.2016 for Wooden Tangram.
 *
 */
public class TangramButton {
    public static final float BUTTON_PRESSED_SCALE_FACTOR = 0.9f;
    private static final String TAG = "TangramButton";

    private Bitmap bitmap, lockBitmap, cupBitmap, previewBitmap;
    private Rect srcRect, dstRect, dstPressedRect, lockSrcRect, lockDstRect, cupSrcRect, cupDstRect, previewSrcRect, previewDstRect;
    private PointF timeDstPoint;
    private boolean isPressed;
    private String title, timeString;

    public TangramButton(Bitmap bitmap) {
        this.bitmap = bitmap;

        srcRect = new Rect();
        srcRect.top =
                srcRect.left = 0;
        srcRect.bottom = bitmap.getHeight();
        srcRect.right = bitmap.getWidth();
        dstRect = new Rect(0, 0, 0, 0);
        dstPressedRect = new Rect(0, 0, 0, 0);
        lockSrcRect = new Rect(0, 0, 0, 0);
        lockDstRect = new Rect(0, 0, 0, 0);
        cupSrcRect = new Rect(0, 0, 0, 0);
        cupDstRect = new Rect(0, 0, 0, 0);
        previewSrcRect = new Rect(0, 0, 0, 0);
        previewDstRect = new Rect(0, 0, 0, 0);
        cupDstRect = new Rect(0, 0, 0, 0);
        timeDstPoint = new PointF(0, 0);
        timeString = "";

        isPressed = false;
        title = "";
    }

    //<editor-fold desc="All about Main Bitmap">
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setDstRect(Rect rect) {
        if (isPressed) {
            dstPressedRect.top = rect.top;
            dstPressedRect.left = rect.left;
            dstPressedRect.bottom = rect.bottom;
            dstPressedRect.right = rect.right;
            unpressButton();
        }
        else {
            dstRect.top = rect.top;
            dstRect.left = rect.left;
            dstRect.bottom = rect.bottom;
            dstRect.right = rect.right;
            setDstPressedRect();
        }

    }

    public void setDstRect(int left, int top, int right, int bottom) {
        if (isPressed) {
            dstPressedRect.top = top;
            dstPressedRect.left = left;
            dstPressedRect.bottom = bottom;
            dstPressedRect.right = right;
            unpressButton();
        }
        else {
            dstRect.top = top;
            dstRect.left = left;
            dstRect.bottom = bottom;
            dstRect.right = right;
            setDstPressedRect();
        }

    }

    private void unpressButton() {
        float x, y;

        x = dstPressedRect.centerX() - dstPressedRect.left;
        x /= BUTTON_PRESSED_SCALE_FACTOR;
        dstRect.left = (int) (dstPressedRect.centerX() - x);
        dstRect.right = (int) (dstPressedRect.centerX() + x);

        y = dstPressedRect.centerY() - dstPressedRect.top;
        y /= BUTTON_PRESSED_SCALE_FACTOR;
        dstRect.top = (int) (dstPressedRect.centerY() - y);
        dstRect.bottom = (int) (dstPressedRect.centerY() + y);
    }

    private void setDstPressedRect() {
        float x, y;

        x = dstRect.centerX() - dstRect.left;
        x *= BUTTON_PRESSED_SCALE_FACTOR;
        dstPressedRect.left = (int) (dstRect.centerX() - x);
        dstPressedRect.right = (int) (dstRect.centerX() + x);

        y = dstRect.centerY() - dstRect.top;
        y *= BUTTON_PRESSED_SCALE_FACTOR;
        dstPressedRect.top = (int) (dstRect.centerY() - y);
        dstPressedRect.bottom = (int) (dstRect.centerY() + y);
    }

    /**
     * Get destination rectangle for usage in canvas.drawBitmap.
     * Values changes accordingly to Pressed state.
     * @return Rectangle with destination coordinates.
     */
    public Rect getDstRect() {
        if (isPressed)
            return dstPressedRect;
        else
            return dstRect;
    }

    public Rect getSrcRect() {
        return srcRect;
    }
    //</editor-fold>

    //<editor-fold desc="All about Lock Bitmap">
    public void setLockBitmap(Bitmap bitmap) {
        lockBitmap = bitmap;
        lockSrcRect.left =
                lockSrcRect.top = 0;
        lockSrcRect.bottom = bitmap.getHeight();
        lockSrcRect.right = bitmap.getWidth();
    }

    public Bitmap getLockBitmap() {
        return lockBitmap;
    }

    public Rect getLockSrcRect() {
        return lockSrcRect;
    }

    public void setLockDstRect(Rect rect) {
        lockDstRect.top = rect.top;
        lockDstRect.left = rect.left;
        lockDstRect.bottom = rect.bottom;
        lockDstRect.right = rect.right;
    }

    public void setLockDstRect(int left, int top, int right, int bottom) {
        lockDstRect.top = top;
        lockDstRect.left = left;
        lockDstRect.bottom = bottom;
        lockDstRect.right = right;
    }

    public Rect getLockDstRect() {
        return lockDstRect;
    }
    //</editor-fold>

    //<editor-fold desc="All about Preview Bitmap">
    public void setPreviewBitmap(Bitmap bitmap) {
        previewBitmap = bitmap;
        previewSrcRect.left =
                previewSrcRect.top = 0;
        previewSrcRect.bottom = bitmap.getHeight();
        previewSrcRect.right = bitmap.getWidth();
    }

    public Bitmap getPreviewBitmap() {
        return previewBitmap;
    }

    public Rect getPreviewSrcRect() {
        return previewSrcRect;
    }

    public void setPreviewDstRect(Rect rect) {
        previewDstRect.top = rect.top;
        previewDstRect.left = rect.left;
        previewDstRect.bottom = rect.bottom;
        previewDstRect.right = rect.right;
    }

    public void setPreviewDstRect(int left, int top, int right, int bottom) {
        previewDstRect.top = top;
        previewDstRect.left = left;
        previewDstRect.bottom = bottom;
        previewDstRect.right = right;
    }

    public Rect getPreviewDstRect() {
        return previewDstRect;
    }
    //</editor-fold>

    //<editor-fold desc="All about Cup Bitmap">
    /**
     * Note, that transfer cupBitmap need only for instantiate lockSrcRect.
     * @param bitmap Bitmap contained example of cup.
     */
    public void setCupBitmap(Bitmap bitmap) {
        cupBitmap = bitmap;
        cupSrcRect.left =
                cupSrcRect.top = 0;
        cupSrcRect.bottom = bitmap.getHeight();
        cupSrcRect.right = bitmap.getWidth();
    }

    public Rect getCupSrcRect() {
        return cupSrcRect;
    }

    public void setCupDstRect(Rect rect) {
        cupDstRect.top = rect.top;
        cupDstRect.left = rect.left;
        cupDstRect.bottom = rect.bottom;
        cupDstRect.right = rect.right;
    }

    public void setCupDstRect(int left, int top, int right, int bottom) {
        cupDstRect.top = top;
        cupDstRect.left = left;
        cupDstRect.bottom = bottom;
        cupDstRect.right = right;
    }

    public Rect getCupDstRect() {
        return cupDstRect;
    }
    //</editor-fold>

    //<editor-fold desc="All about Time String">
    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeDstPoint(PointF point) {
        timeDstPoint.x = point.x;
        timeDstPoint.y = point.y;
    }
    public void setTimeDstPoint(float x, float y) {
        timeDstPoint.x = x;
        timeDstPoint.y = y;
    }

    public PointF getTimeDstPoint() {
        return timeDstPoint;
    }
    //</editor-fold>

    //<editor-fold desc="All other operations">
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public boolean getPressed() {
        return isPressed;
    }
    //</editor-fold>
}
