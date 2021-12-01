package com.aga.woodentangrampuzzle;

import static com.aga.woodentangrampuzzle.utils.ObjectBuildHelper.logDebugOut;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.core.content.res.ResourcesCompat;

import java.util.Locale;

/**
 *
 * Created by Andrii Husiev on 17.12.2015 for Wooden Tangram.
 *
 */
public class TangramView extends View{

    //<editor-fold desc="Constants">
    //<editor-fold desc="Colors">
    private static final int COLOR_TEXT_ON_BUTTONS = 0xFFFFA216;
    //private static final int COLOR_TEXT_INGAME_HEADER = 0x9f702100;
    private static final int COLOR_TEXT_INGAME_HEADER = 0xde5B1B00;
    private static final int COLOR_LEVEL_BG = 0x5f07c446;
    private static final int COLOR_SHADOW = 0x80242424;
    private static final int COLOR_CLEANUP = 0x00ffffff;
    private static final int COLOR_GREEN_FILTER = 0x0000ff00;
    private static final int COLOR_RED_FILTER = 0x00ff0000;
    private static final int COLOR_GREEN_BOUND = 0x0000d200;
    private static final int COLOR_RED_BOUND = 0x00dc0000;
    private static final int COLOR_BLACK_BOUND = 0x00202020;
    //</editor-fold>

    //<editor-fold desc="Main Menu">
    private static final float MM_TITLE_OFFSET_FROM_TOP = 0.18f; // percents of screen height
    private static final float MM_BUTTON_SIZE = 0.15f;
    private static final float MM_BUTTON_GAP = 0.05f;
    //</editor-fold>

    //<editor-fold desc="Credits">
    private static final float CREDITS_TITLE_OFFSET_FROM_TOP = 0.1f;
    private static final float CREDITS_OFFSET_FROM_TOP = 0.2f;
    //</editor-fold>

    //<editor-fold desc="Levels Set Selection Menu">
    private static final float LSS_TITLE_OFFSET_FROM_TOP = 0.1f;
    private static final float LSS_OFFSET_FROM_TOP = 0.2f;
    private static final float LSS_GRADIENT_HEADER_OFFSET_FROM_TOP = 0.14f;
    private static final float LSS_GRADIENT_HEADER_HEIGHT = 0.06f;
    private static final float LSS_BUTTON_SIZE = 0.25f;
    private static final float LSS_BUTTON_GAP = 0.04f;
    private static final float LSS_LOCK_SIZE = 0.17f;
    //</editor-fold>

    //<editor-fold desc="Levels Selection Menu">
    private static final float LS_TITLE_OFFSET_FROM_TOP = 0.1f;
    private static final float LS_OFFSET_FROM_TOP = 0.2f;
    private static final float LS_OFFSET_TIMER_FROM_BUTTONS_BOTTOM = 0.047f;
    private static final float LS_GRADIENT_HEADER_OFFSET_FROM_TOP = 0.14f;
    private static final float LS_GRADIENT_HEADER_HEIGHT = 0.06f;
    private static final float LS_BUTTON_WIDTH = 0.25f;
    private static final float LS_BUTTON_GAP = 0.03f;
//    private static final float LS_CUP_SIZE = 0.10875f; //0.087f; //0.175f;
    private static final float LS_PREVIEW_SIZE = 0.18f;
    private static final float LS_PREVIEW_OFFSET_FROM_TOP = 0.075f;
    private static final float LS_LOCK_SIZE = 0.17f;
    private static final float LS_LOCK_OFFSET_FROM_TOP = 0.08f;
    private static final float LS_LOCK_TEXT_BITMAP_START_SIZE = 0.6f;
    //</editor-fold>

    //<editor-fold desc="Ingame Constants">
    private static final float INGAME_TITLE_OFFSET_FROM_TOP = 0.03f;
    private static final float INGAME_HEADER_HEIGHT = 0.06f;
    private static final float INGAME_ANGULAR_HEADER_HEIGHT = 0.09f;
    private static final float INGAME_HEADER_SHADOW_HEIGHT = 0.015f;
    private static final float INGAME_OFFSET_FROM_BORDER = 0.005f;
    private static final float INGAME_TIMER_OFFSET_FROM_TOP = 0.045f;
    private static final float INGAME_TIMER_OFFSET_FROM_LEFT = 0.11f;
    private static final float INGAME_LEVEL_PROGRESS_OFFSET_FROM_TOP = 0.04f;
    private static final float INGAME_LEVEL_PROGRESS_OFFSET_FROM_RIGHT = 0.153f;
//    private static final float INGAME_LEVEL_SIZE = 0.9f;
    private static final float INGAME_BACK_BUTTON_SIZE = 0.065f;
    private static final float INGAME_CUP_OFFSET_FROM_TOP = 0.012f;
    private static final float INGAME_CUP_OFFSET_FROM_RIGHT = 0.01f;
    private static final float INGAME_CUP_HEIGHT = 0.055f;
    private static final float INGAME_CUP_WIDTH = 0.11f;
    private static final float INGAME_CUP_BRONZE = 80.0f;
    private static final float INGAME_CUP_SILVER = 95.0f;
    private static final float INGAME_CUP_GOLD = 99.0f;

    private static final float INGAME_TILE0_OFFSET_Y = 0.237f;
    private static final float INGAME_TILE1_OFFSET_Y = 0.414f;
    private static final float INGAME_TILE2_OFFSET_Y = 0.494f;
    private static final float INGAME_TILE3_OFFSET_Y = 0.678f;
    private static final float INGAME_TILE4_OFFSET_Y = 0.787f;
    private static final float INGAME_TILE5_OFFSET_Y = 0.267f;
    private static final float INGAME_TILE6_OFFSET_Y = 0.689f;
    private static final float INGAME_TILE0_OFFSET_X = 0.2f;
    private static final float INGAME_TILE1_OFFSET_X = 0.23f;
    private static final float INGAME_TILE2_OFFSET_X = 0.17f;
    private static final float INGAME_TILE3_OFFSET_X = 0.242f;
    private static final float INGAME_TILE4_OFFSET_X = 0.158f;
    private static final float INGAME_TILES_OFFSET_FROM_RIGHT = 0.2f;
    //</editor-fold>

    //<editor-fold desc="Mixed Constants">
    private static final float SHADOW_LAYER_OFFSET = 2;
    private static final float INSENSITIVE_BACKLASH_ON_SCROLL = 0.005f;
    private static final long TILE_ANIMATION_DURATION = 300;
    private static final long LOADING_ANIMATION_DURATION = 3000;
    private static final long LEVEL_LOCKED_TEXT_ANIMATION_DURATION = 4000;
    private static final int CUPS_NUMBER = 4;
    //private static final int NO_CUP = 0;
    private static final int BRONZE_CUP = 1;
    private static final int SILVER_CUP = 2;
    private static final int GOLDEN_CUP = 3;
    private static final int TILES_NUMBER = 7;
    private static final int LEVELS_IN_THE_ROW = 5;
    private static final int LEVELS_NUMBER = 25;
    private static final int LEVEL_SET_NUMBER = 4;
    private static final int LEVEL_PROGRESS_CALC_SPEED = 2;
    private static float TILE_SHADOW_SIZE;
    private static float TILES_SCALE_FACTOR;
    // TODO: 19.01.2016 Следующие наборы уровней будут добавлены в будущем.
    private static final int INSTANTIATED_LEVEL_SET_NUMBER = 2;
    private static final String TAG = "TangramView";
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Variables">
    private enum Mode {LOADING_SCREEN, MAIN_MENU, LEVELS_SET_SELECTION, LEVEL_SELECTION, LEVEL, CREDITS}
    private Mode playMode;
    private TangramLevel level;
    private TangramTile[] tile;
    private TangramButton buttonLeftArrow, buttonRightArrow, buttonIngameBackToMenu, buttonResetLevel, lockedLevelText; //, buttonIngameSettings;
    private TangramButton buttonMMStart, buttonMMCredits, buttonMMExit;
    private TangramButton[] buttonLSS, buttonLS;
    private TangramButton[] angularHeader;
    public TangramTimer universalTimer;
    private BitmapDrawable bitmapBack;
    private Bitmap[] cupAward;
    private Bitmap shadowButtonInLSS, shadowButtonInLS, ingameHeader, gradientHeaderLS, gradientHeaderLSS; //, ingameHintCups;
    private Bitmap scratchpadBitmap;
    private Canvas scratchpadCanvas;
    private Paint textPaint, simplePaint, levelPaint;
    private Typeface digitalTF;
    private Shader woodShader;
//    private Vibrator v;
    private ValueAnimator tileRotation;
    private ValueAnimator universalAnimator;
    private ValueAnimator.AnimatorUpdateListener universalUpdateListener;
    private ValueAnimator.AnimatorListener universalListener;
    private Runnable runnableCalcNumberOfLevelPixels, runnableCalcLevelProgress;
    private Matrix m;
    //private Rect[] multipurposeRect;
    private Rect textBounds;
    private Rect cupDstRect;
    private RectF screenRect;
    private RectF ingamePlayableRect;
    private RectF textCreditsBounds;
    private PointF prevTouch;
    private String[] levelsNames;
    private String auxString;
    private boolean touchIntoTile;
    private boolean isAnimationEnds;
    private boolean isLoadingEnds;
    private boolean isFirstInvalidating;
    private boolean isStartScrollingLS, isStartScrollingLSS;
    private int[] levelsCups;
    //private String[] levelsTimes;
    private int selectedTile;
    private int selectedLevelSet;
    private int selectedLevel;
    private int numberOfLevelPixels;
    private float fraction;
    private float prevAngle;
    private float levelProgress;
    //</editor-fold>

    public TangramView(Context context) {
        super(context);

        doTheVeryFirstInitializations();
        setPaints();
        loadBaseResources();
        setUniversalAnimation();
    }

    //<editor-fold desc="All about drawing on screen">
    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(scratchpadCanvas);
        switch (playMode) {
            case LOADING_SCREEN:
                if (isFirstInvalidating){
                    isFirstInvalidating = false;
                    playLoadingAnimation();
                    loadResourcesInBackgroundThread();
                    universalTimer.start(0);
                }
                if (isAnimationEnds && isLoadingEnds){
                    universalTimer.stop();
                    playMode = Mode.MAIN_MENU;
                }
                break;
            case MAIN_MENU:
                drawMMButtons(scratchpadCanvas);
                break;
            case LEVELS_SET_SELECTION:
                drawLSSButtons(scratchpadCanvas);
                break;
            case LEVEL_SELECTION:
                drawLSButtons(scratchpadCanvas);
                drawLockedLevelText(scratchpadCanvas);
                break;
            case LEVEL:
                drawLevel(scratchpadCanvas);
                drawTiles(scratchpadCanvas);
                drawLevelHeader(scratchpadCanvas);
                drawLevelAngularHeaders(scratchpadCanvas);
                drawLevelProgress(scratchpadCanvas);
                drawLevelTimer(scratchpadCanvas);
                drawCup(scratchpadCanvas);
                drawLevelButtons(scratchpadCanvas);
                break;
            case CREDITS:
                drawCreditsText(scratchpadCanvas);
        }
        drawTitle(scratchpadCanvas);

        canvas.drawBitmap(scratchpadBitmap, 0, 0, simplePaint);
        invalidate();
    }

    //<editor-fold desc="onDraw Common Functions">
    private void drawBackground(Canvas canvas) {
        switch (playMode) {
            case LOADING_SCREEN:
                canvas.drawColor(Color.BLACK);
                break;
            case MAIN_MENU:
            case LEVELS_SET_SELECTION:
            case LEVEL_SELECTION:
            case LEVEL:
            case CREDITS:
                bitmapBack.draw(canvas);
                break;
        }
    }

    private void drawTitle(Canvas canvas) {
        float x, y;

        switch (playMode) {
            case LOADING_SCREEN:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_loading_title));
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(0, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(woodShader);
                canvas.drawText(getResources().getString(R.string.app_name), screenRect.width()/2, screenRect.height()/2, textPaint);

                int alpha = (int) universalAnimator.getAnimatedValue();
                canvas.drawARGB(alpha, 0, 0, 0);
                break;
            case MAIN_MENU:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_mm_title));
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(2, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(null);
                auxString = getResources().getString(R.string.app_name);
                textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
                x = screenRect.width()/2;
                y = screenRect.height() * MM_TITLE_OFFSET_FROM_TOP - textBounds.exactCenterY();
                canvas.drawText(auxString, x, y, textPaint);

                // Draw wooden bitmap over text
                textPaint.setShadowLayer(0, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(woodShader);
                canvas.drawText(auxString, x, y, textPaint);
                break;
            case LEVELS_SET_SELECTION:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_choose_level_title));
                textPaint.setColor(COLOR_TEXT_ON_BUTTONS);
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(2, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(null);
                auxString = getResources().getString(R.string.levels_set_title);
                textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
                x = screenRect.width()/2;
                y = screenRect.height() * LSS_TITLE_OFFSET_FROM_TOP - textBounds.exactCenterY();
                canvas.drawText(auxString, x, y, textPaint);
                break;
            case LEVEL_SELECTION:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_choose_level_title));
                textPaint.setColor(COLOR_TEXT_ON_BUTTONS);
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(2, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(null);
                int id = getResources().getIdentifier("levels_set_" + selectedLevelSet, "string", this.getContext().getPackageName());
                auxString = getResources().getString(id);
                textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
                x = screenRect.width()/2;
                y = screenRect.height() * LS_TITLE_OFFSET_FROM_TOP - textBounds.exactCenterY();
                canvas.drawText(auxString, x, y, textPaint);
                break;
            case LEVEL:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_level_title));
                textPaint.setColor(COLOR_TEXT_INGAME_HEADER);
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(0, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(null);
                auxString = String.format(getResources().getString(R.string.level_title), (selectedLevel+1)) + levelsNames[selectedLevel];
                textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
                x = screenRect.width()/2;
                y = screenRect.height() * INGAME_TITLE_OFFSET_FROM_TOP - textBounds.exactCenterY();
                canvas.drawText(auxString, x, y, textPaint);
                break;
            case CREDITS:
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(getResources().getInteger(R.integer.font_credits_title));
                textPaint.setColor(COLOR_TEXT_ON_BUTTONS);
                textPaint.setTypeface(Typeface.DEFAULT_BOLD);
                textPaint.setShadowLayer(2, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
                textPaint.setShader(null);
                auxString = getResources().getString(R.string.button_MM_credits);
                textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
                x = screenRect.width()/2;
                y = screenRect.height() * CREDITS_TITLE_OFFSET_FROM_TOP - textBounds.exactCenterY();
                canvas.drawText(auxString, x, y, textPaint);
        }
    }
    //</editor-fold>

    //<editor-fold desc="onDraw MAIN_MENU">
    private void drawMMButtons(Canvas canvas) {
        float x, y, scaleFactor;

        canvas.drawBitmap(buttonMMStart.getBitmap(), buttonMMStart.getSrcRect(), buttonMMStart.getDstRect(), simplePaint);
        canvas.drawBitmap(buttonMMCredits.getBitmap(), buttonMMCredits.getSrcRect(), buttonMMCredits.getDstRect(), simplePaint);
        canvas.drawBitmap(buttonMMExit.getBitmap(), buttonMMExit.getSrcRect(), buttonMMExit.getDstRect(), simplePaint);

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(COLOR_TEXT_ON_BUTTONS);
        textPaint.setShadowLayer(3, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setShader(null);

        scaleFactor = buttonMMStart.getPressed()?TangramButton.BUTTON_PRESSED_SCALE_FACTOR:1;
        textPaint.setTextSize(buttonMMStart.getTextSize() * scaleFactor);
        textPaint.getTextBounds(buttonMMStart.getTitle(), 0, buttonMMStart.getTitle().length(), textBounds);
        x = buttonMMStart.getDstRect().left + buttonMMStart.getDstRect().width()/2f;
        y = buttonMMStart.getDstRect().top + buttonMMStart.getDstRect().height()/2f - textBounds.exactCenterY();
        canvas.drawText(buttonMMStart.getTitle(), x, y, textPaint);

        scaleFactor = buttonMMCredits.getPressed()?TangramButton.BUTTON_PRESSED_SCALE_FACTOR:1;
        textPaint.setTextSize(buttonMMCredits.getTextSize() * scaleFactor);
        textPaint.getTextBounds(buttonMMCredits.getTitle(), 0, buttonMMCredits.getTitle().length(), textBounds);
        x = buttonMMCredits.getDstRect().left + buttonMMCredits.getDstRect().width()/2f;
        y = buttonMMCredits.getDstRect().top + buttonMMCredits.getDstRect().height()/2f - textBounds.exactCenterY();
        canvas.drawText(buttonMMCredits.getTitle(), x, y, textPaint);

        scaleFactor = buttonMMExit.getPressed()?TangramButton.BUTTON_PRESSED_SCALE_FACTOR:1;
        textPaint.setTextSize(buttonMMExit.getTextSize() * scaleFactor);
        textPaint.getTextBounds(buttonMMExit.getTitle(), 0, buttonMMExit.getTitle().length(), textBounds);
        x = buttonMMExit.getDstRect().left + buttonMMExit.getDstRect().width()/2f;
        y = buttonMMExit.getDstRect().top + buttonMMExit.getDstRect().height()/2f - textBounds.exactCenterY();
        canvas.drawText(buttonMMExit.getTitle(), x, y, textPaint);
    }
    //</editor-fold>

    //<editor-fold desc="onDraw LEVELS_SET_SELECTION">
    private void drawLSSButtons(Canvas canvas) {
        float x, y;

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(COLOR_TEXT_ON_BUTTONS);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setShadowLayer(3, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
        textPaint.setShader(null);

        for (int i = 0; i < LEVEL_SET_NUMBER; i++) {
            // Draw background image
            canvas.drawBitmap(buttonLSS[i].getBitmap(), buttonLSS[i].getSrcRect(), buttonLSS[i].getDstRect(), simplePaint);

            // Draw title of selected levels' set
            textPaint.setTextSize(buttonLSS[i].getTextSize()*(buttonLSS[i].getPressed()?TangramButton.BUTTON_PRESSED_SCALE_FACTOR:1));
            textPaint.getTextBounds(buttonLSS[i].getTitle(), 0, buttonLSS[i].getTitle().length(), textBounds);
            x = buttonLSS[i].getDstRect().left + buttonLSS[i].getDstRect().width()/2f;
            y = buttonLSS[i].getDstRect().top + buttonLSS[i].getDstRect().height()/2f - textBounds.exactCenterY();
            canvas.drawText(buttonLSS[i].getTitle(), x, y, textPaint);

            if (i >= INSTANTIATED_LEVEL_SET_NUMBER) {
                // Draw shadow
                canvas.drawBitmap(shadowButtonInLSS, buttonLSS[i].getSrcRect(), buttonLSS[i].getDstRect(), simplePaint);
                // Draw lock
                canvas.drawBitmap(buttonLSS[i].getLockBitmap(), buttonLSS[i].getLockSrcRect(), buttonLSS[i].getLockDstRect(), simplePaint);
            }
        }

        canvas.drawBitmap(gradientHeaderLSS, 0, 0, simplePaint);
    }
    //</editor-fold>

    //<editor-fold desc="onDraw LEVEL_SELECTION">
    private void drawLSButtons(Canvas canvas) {
        int rowOfSelectedLevel = 0;
        boolean allLevelsInThePrevRowSolved;

        for (int i = 0; i < LEVELS_NUMBER; i+=LEVELS_IN_THE_ROW) {
            // Define is the levels unlocked in the current row.
            // Current row unlocked when all levels in the previous row get at least bronze cup.
            // But note, first 5 levels are always unlocked.
            allLevelsInThePrevRowSolved = true;
            if (i >= LEVELS_IN_THE_ROW) {
                rowOfSelectedLevel = (int) Math.floor(i/5f);
                for(int j = (rowOfSelectedLevel*5-5); j < (rowOfSelectedLevel*5); j++) {
                    allLevelsInThePrevRowSolved &= (levelsCups[j] > 0);
                }
            }
            for(int j = (rowOfSelectedLevel*5); j < (rowOfSelectedLevel*5+5); j++) {
                // Draw background image
                canvas.drawBitmap(buttonLS[j].getBitmap(), buttonLS[j].getSrcRect(), buttonLS[j].getDstRect(), simplePaint);
                // Draw timer
                textPaint.setTypeface(digitalTF);
                textPaint.setTextAlign(Paint.Align.CENTER);
                textPaint.setTextSize(buttonLS[j].getTextSize());
                textPaint.setColor(Color.BLACK);
                textPaint.setShadowLayer(0,0,0,0);
                canvas.drawText(buttonLS[j].getTimeString(), buttonLS[j].getTimeDstPoint().x, buttonLS[j].getTimeDstPoint().y, textPaint);
                if (allLevelsInThePrevRowSolved) {
                    // Draw preview
                    canvas.drawBitmap(buttonLS[j].getPreviewBitmap(), buttonLS[j].getPreviewSrcRect(), buttonLS[j].getPreviewDstRect(), simplePaint);
                    // Draw cup
                    canvas.drawBitmap(cupAward[levelsCups[j]], buttonLS[j].getCupSrcRect(), buttonLS[j].getCupDstRect(), simplePaint);
                }
                else {
                    // Draw shadow
                    canvas.drawBitmap(shadowButtonInLS, buttonLS[j].getSrcRect(), buttonLS[j].getDstRect(), simplePaint);
                    // Draw lock
                    canvas.drawBitmap(buttonLS[j].getLockBitmap(), buttonLS[j].getLockSrcRect(), buttonLS[j].getLockDstRect(), simplePaint);
                }
            }
        }

        canvas.drawBitmap(gradientHeaderLS, 0, 0, simplePaint);
    }

    private void drawLockedLevelText(Canvas canvas) {
        if (!isAnimationEnds) {
            int x = (int)screenRect.width()/2;
            int y = (int)screenRect.height()/2;
            int left, top, right, bottom;
            int width, height;
            float animatedValue = (float) universalAnimator.getAnimatedValue();

            width = (int) (animatedValue * screenRect.width());
            height = (int) (animatedValue * screenRect.height());
            left = x - width / 2;
            top = y - height / 2;
            right = left + width;
            bottom = top + height;

            lockedLevelText.setDstRect(left, top, right, bottom);
            canvas.drawBitmap(lockedLevelText.getBitmap(), lockedLevelText.getSrcRect(), lockedLevelText.getDstRect(), simplePaint);
        }
    }
    //</editor-fold>

    //<editor-fold desc="onDraw LEVEL">
    private void drawLevel(Canvas canvas) {
        levelPaint.setColor(COLOR_LEVEL_BG);
        levelPaint.setStyle(Paint.Style.FILL);
        if (level.isHasHole()) {
            canvas.save();
            canvas.clipPath(level.getPathHole(), Region.Op.DIFFERENCE);
            canvas.drawPath(level.getPath(), levelPaint);
            canvas.restore();
        }
        else
            canvas.drawPath(level.getPath(), levelPaint);

        levelPaint.setColor(Color.BLACK);
        levelPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(level.getPath(), levelPaint);
        if (level.isHasHole())
            canvas.drawPath(level.getPathHole(), levelPaint);
    }

    private void drawTiles(Canvas canvas) {
        Path tilePath, tileShadowPath;

        for(int i = tile.length - 1; i >= 0; i--) {
            tilePath = tile[i].getPath();
            tileShadowPath = tile[i].getShadowPath();

            if (i == selectedTile) {
                tile[i].boundPaint.setStyle(Paint.Style.STROKE);
                tile[i].boundPaint.setARGB(0x33, 0x00, 0x00, 0x00);
                tile[i].boundPaint.setStrokeWidth(TILE_SHADOW_SIZE/2.5f);
                canvas.drawPath(tileShadowPath, tile[i].boundPaint);

                tile[i].boundPaint.setStrokeWidth(TILE_SHADOW_SIZE/2);
                canvas.drawPath(tileShadowPath, tile[i].boundPaint);

                tile[i].boundPaint.setStrokeWidth(TILE_SHADOW_SIZE);
                canvas.drawPath(tileShadowPath, tile[i].boundPaint);
            }

            canvas.drawPath(tilePath, tile[i].shaderPaint);

            tile[i].boundPaint.setStyle(Paint.Style.STROKE);
            tile[i].boundPaint.setColor(Color.BLACK);
            tile[i].boundPaint.setStrokeWidth(0);
            canvas.drawPath(tilePath, tile[i].boundPaint);
        }
    }

    private void drawLevelHeader(Canvas canvas) {
        float y;
        float shadowHeight = INGAME_HEADER_SHADOW_HEIGHT * screenRect.height();
        int color = Color.BLACK; //== 0xff000000;
        int colorOffset = ((int) Math.floor(255f / shadowHeight)) * 0x1000000;

        canvas.drawBitmap(ingameHeader, 0, 0, simplePaint);
        // Draw shadow under header.
        for (int i = 0; i < (int) shadowHeight; i++) {
            color -= colorOffset;
            simplePaint.setColor(color);
            y = screenRect.height() * INGAME_HEADER_HEIGHT + i;
            canvas.drawLine(0, y, screenRect.width(), y, simplePaint);
        }
        simplePaint.setColor(Color.BLACK);
    }

    private void drawLevelAngularHeaders(Canvas canvas) {
        for (TangramButton ah: angularHeader)
            canvas.drawBitmap(ah.getBitmap(), ah.getSrcRect(), ah.getDstRect(), simplePaint);
    }

    private void drawLevelProgress(Canvas canvas) {
        float x, y;

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(getResources().getInteger(R.integer.font_level_progress));
        textPaint.setColor(COLOR_TEXT_INGAME_HEADER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setShadowLayer(0, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
        textPaint.setShader(null);

        auxString = String.format(Locale.getDefault(), "%.1f%%", levelProgress);
        textPaint.getTextBounds(auxString, 0, auxString.length(), textBounds);
        x = screenRect.width() - screenRect.height() * INGAME_LEVEL_PROGRESS_OFFSET_FROM_RIGHT;
        y = screenRect.height() * INGAME_LEVEL_PROGRESS_OFFSET_FROM_TOP - textBounds.exactCenterY();
        canvas.drawText(auxString, x, y, textPaint);
    }

    private void drawLevelTimer(Canvas canvas) {
        float x, y;

        textPaint.setTypeface(digitalTF);
        textPaint.setTextSize(getResources().getInteger(R.integer.font_digital));
        auxString = universalTimer.getElapsedTime();
        x = screenRect.height() * INGAME_TIMER_OFFSET_FROM_LEFT;
        y = screenRect.height() * INGAME_TIMER_OFFSET_FROM_TOP - textBounds.exactCenterY();
        canvas.drawText(auxString, x, y, textPaint);
    }

    private void drawCup (Canvas canvas) {
        canvas.drawBitmap(cupAward[levelsCups[selectedLevel]], buttonLS[0].getCupSrcRect(), cupDstRect, simplePaint);
    }

    private void drawLevelButtons(Canvas canvas) {
        canvas.drawBitmap(buttonLeftArrow.getBitmap(), buttonLeftArrow.getSrcRect(), buttonLeftArrow.getDstRect(), simplePaint);
        canvas.drawBitmap(buttonRightArrow.getBitmap(), buttonRightArrow.getSrcRect(), buttonRightArrow.getDstRect(), simplePaint);
        canvas.drawBitmap(buttonResetLevel.getBitmap(), buttonResetLevel.getSrcRect(), buttonResetLevel.getDstRect(), simplePaint);
        canvas.drawBitmap(buttonIngameBackToMenu.getBitmap(), buttonIngameBackToMenu.getSrcRect(), buttonIngameBackToMenu.getDstRect(), simplePaint);
    }
    //</editor-fold>

    //<editor-fold desc="onDraw CREDITS">
    private void drawCreditsText(Canvas canvas) {
        float x, y, lineSpacing, prevHalfWidth, offset;
        int charsNumber, first=0, last, i=0;
        boolean lastRow = false;
        String row;

        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(getResources().getInteger(R.integer.font_credits_text));
        textPaint.setColor(COLOR_TEXT_INGAME_HEADER);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setShadowLayer(0, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, 0xff000000);
        textPaint.setShader(null);

        auxString = getResources().getString(R.string.credits_text);

        last = charsNumber = textPaint.breakText(auxString, true, screenRect.width() * 0.85f, null);
        lineSpacing =  textBounds.height();
        row = auxString.substring(0, charsNumber);
        textPaint.getTextBounds(row, 0, row.length(), textBounds);
        prevHalfWidth = textBounds.width()/2f;

        do {
            row = auxString.substring(first, last);
            row = substringTillLastSpace(row);
            textPaint.getTextBounds(row, 0, row.length(), textBounds);
            offset = prevHalfWidth - textBounds.width()/2f;
            x = screenRect.width()/2 - offset;
            y = textCreditsBounds.top - textBounds.exactCenterY() + i * lineSpacing;
            canvas.drawText(row, x, y, textPaint);
            first += row.length()+1;
            last = first + charsNumber;
            if (last > auxString.length()) {
                lastRow = true;
                last = auxString.length();
            }
            i++;
        } while (!lastRow);

        row = auxString.substring(first, last);
        textPaint.getTextBounds(row, 0, row.length(), textBounds);
        offset = prevHalfWidth - textBounds.width()/2f;
        x = screenRect.width()/2 - offset;
        y = textCreditsBounds.top - textBounds.exactCenterY() + i * lineSpacing;
        canvas.drawText(row, x, y, textPaint);
        textCreditsBounds.bottom = y + textBounds.height();

        canvas.drawBitmap(gradientHeaderLSS, 0, 0, simplePaint);
    }

    private String substringTillLastSpace(String text) {
        int index = text.lastIndexOf(" ");
        return text.substring(0, index);
    }
    //</editor-fold>

    //</editor-fold>




    //<editor-fold desc="All about screen interaction">
    public boolean onBackPressed() {
        logDebugOut(TAG, "onBackPressed", "Go to prev screen.");

        switch (playMode) {
            case MAIN_MENU:
                return true;
            case CREDITS:
            case LEVELS_SET_SELECTION:
                playMode = Mode.MAIN_MENU;
                invalidate();
                break;
            case LEVEL_SELECTION:
                saveData();
                isStartScrollingLSS = false;
                playMode = Mode.LEVELS_SET_SELECTION;
                invalidate();
                break;
            case LEVEL:
                saveData(selectedLevelSet, selectedLevel);
                isStartScrollingLS = false;
                universalTimer.stop();
                buttonLS[selectedLevel].setTimeString(level.getLevelTimerString());

                playMode = Mode.LEVEL_SELECTION;
                invalidate();
                break;
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((getSystemUiVisibility() & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
            hideUI();

        if (!isAnimationEnds) return false;

        switch (playMode) {
            case LOADING_SCREEN:
                break;
            case MAIN_MENU:
                return touchMainMenu(event);
            case LEVELS_SET_SELECTION:
                return touchLevelSetSelection(event);
            case LEVEL_SELECTION:
                return touchLevelSelection(event);
            case LEVEL:
                return touchLevel(event);
            case CREDITS:
                return touchCredits(event);
        }
        return false;
    }

    //<editor-fold desc="onTouchEvent MAIN_MENU">
    private boolean touchMainMenu(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (buttonMMStart.getDstRect().contains((int)event.getX(), (int)event.getY()))
                    buttonMMStart.setPressed(true);
                else if (buttonMMCredits.getDstRect().contains((int)event.getX(), (int)event.getY()))
                    buttonMMCredits.setPressed(true);
                else if (buttonMMExit.getDstRect().contains((int)event.getX(), (int)event.getY()))
                    buttonMMExit.setPressed(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (buttonMMStart.getDstRect().contains((int)event.getX(), (int)event.getY())) {
                    isStartScrollingLSS = false;
                    playMode = Mode.LEVELS_SET_SELECTION;
                }
                else if (buttonMMCredits.getDstRect().contains((int)event.getX(), (int)event.getY())) {
                    playMode = Mode.CREDITS;
                }
                else if (buttonMMExit.getDstRect().contains((int)event.getX(), (int)event.getY())) {
                    Activity activity = (Activity) getContext();
                    activity.finish();
                    System.exit(0);
                }
                buttonMMStart.setPressed(false);
                buttonMMCredits.setPressed(false);
                buttonMMExit.setPressed(false);
                break;
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="onTouchEvent LEVELS_SET_SELECTION">
    private boolean touchLevelSetSelection(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startLevelSetScroll(event);
                for (TangramButton t: buttonLSS) {
                    if (t.getDstRect().contains((int)event.getX(), (int)event.getY())) {
                        t.setPressed(true);
                        break;
                    }
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                updateLevelSetScroll(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                for (TangramButton t: buttonLSS)
                    t.setPressed(false);
                if (isStartScrollingLSS)
                    return finishLevelSetScroll();
                if (event.getY() < LSS_OFFSET_FROM_TOP * screenRect.height())
                    break;
                for (int i = 0; i < INSTANTIATED_LEVEL_SET_NUMBER; i++) {
                    if (buttonLSS[i].getDstRect().contains((int)event.getX(), (int)event.getY())) {
                        selectedLevelSet = i;
                        setLSCoordinates();
                        setLevelsNames(selectedLevelSet);
                        setLevelsPreviewsToButtons(selectedLevelSet);
                        loadData();
                        isStartScrollingLS = false;
                        playMode = Mode.LEVEL_SELECTION;
                        invalidate();
                        break;
                    }
                }
        }
        return false;
    }

    private void startLevelSetScroll(MotionEvent event) {
        prevTouch.y = event.getY();
    }

    private void updateLevelSetScroll(MotionEvent event) {
        int dy = (int) (event.getY() - prevTouch.y);
        prevTouch.y = event.getY();

        // Limitation for scrolling
        if (Math.abs(dy) < INSENSITIVE_BACKLASH_ON_SCROLL * screenRect.height())
            return;
        if ((buttonLSS[0].getDstRect().top + dy) > LSS_OFFSET_FROM_TOP * screenRect.height())
            dy = (int) (LSS_OFFSET_FROM_TOP * screenRect.height()) - buttonLSS[0].getDstRect().top;
        if ((buttonLSS[LEVEL_SET_NUMBER-1].getDstRect().bottom + dy) < (1 - LSS_BUTTON_GAP) * screenRect.height())
            dy = (int) ((1 - LSS_BUTTON_GAP) * screenRect.height()) - buttonLSS[LEVEL_SET_NUMBER-1].getDstRect().bottom;

        for (TangramButton t: buttonLSS) {
            t.setDstRect(t.getDstRect().left, t.getDstRect().top+dy, t.getDstRect().right, t.getDstRect().bottom+dy);
            t.setCupDstRect(t.getCupDstRect().left, t.getCupDstRect().top+dy, t.getCupDstRect().right, t.getCupDstRect().bottom+dy);
            t.setLockDstRect(t.getLockDstRect().left, t.getLockDstRect().top+dy, t.getLockDstRect().right, t.getLockDstRect().bottom+dy);
            t.setPreviewDstRect(t.getPreviewDstRect().left, t.getPreviewDstRect().top+dy, t.getPreviewDstRect().right, t.getPreviewDstRect().bottom+dy);
        }

        isStartScrollingLSS = true;
    }

    private boolean finishLevelSetScroll() {
        isStartScrollingLSS = false;
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="onTouchEvent LEVEL_SELECTION">
    private boolean touchLevelSelection(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startLevelsScroll(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                updateLevelsScroll(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                if (isStartScrollingLS)
                    return finishLevelsScroll();
                if (event.getY() < LS_OFFSET_FROM_TOP * screenRect.height())
                    break;
                for (int i = 0; i < LEVELS_NUMBER; i++) {
                    if (buttonLS[i].getDstRect().contains((int)event.getX(), (int)event.getY())) {
                        if (allLevelsInThePrevRowSolved(i)) {
                            logDebugOut(TAG, "onTouchEvent.ACTION_UP", "Selected level is unlocked.");
                            selectedLevel = i;
                            level = loadLevelByNumber(selectedLevelSet, selectedLevel);
                            //level.resize(ingamePlayableRect.height()*INGAME_LEVEL_SIZE, false);
                            level.scale(TILES_SCALE_FACTOR);
                            level.setToCenter(ingamePlayableRect);
                            level.setLevelTimerString(buttonLS[i].getTimeString());
                            setTiles();
                            shiftTilesToStartPosition();
                            calcNumberOfLevelPixels();
                            levelProgress = 0;
                            universalTimer.start(0);
                            playMode = Mode.LEVEL;
                        }
                        else {
                            logDebugOut(TAG, "onTouchEvent.ACTION_UP", "Selected level is still locked.");
                            playWarningLockedLevelAnimation();
                        }
                        logDebugOut(TAG, "onTouchEvent.ACTION_UP", "play.Mode = LEVEL_SELECTION. Selected level is " + i);
                        invalidate();
                        break;
                    }
                }
                break;
        }
        return false;
    }

    private void startLevelsScroll(MotionEvent event) {
        prevTouch.y = event.getY();
    }

    private void updateLevelsScroll(MotionEvent event) {
        int dy = (int) (event.getY() - prevTouch.y);
        prevTouch.y = event.getY();

        // Limitation for scrolling
        if (Math.abs(dy) < INSENSITIVE_BACKLASH_ON_SCROLL * screenRect.height())
            return;
        if ((buttonLS[0].getDstRect().top + dy) > LS_OFFSET_FROM_TOP * screenRect.height())
            dy = (int) (LS_OFFSET_FROM_TOP * screenRect.height()) - buttonLS[0].getDstRect().top;
        if ((buttonLS[LEVELS_NUMBER-1].getDstRect().bottom + dy) < (1 - LS_BUTTON_GAP) * screenRect.height())
            dy = (int) ((1 - LS_BUTTON_GAP) * screenRect.height()) - buttonLS[LEVELS_NUMBER-1].getDstRect().bottom;

        for (TangramButton t: buttonLS) {
            t.setDstRect(t.getDstRect().left, t.getDstRect().top+dy, t.getDstRect().right, t.getDstRect().bottom+dy);
            t.setCupDstRect(t.getCupDstRect().left, t.getCupDstRect().top+dy, t.getCupDstRect().right, t.getCupDstRect().bottom+dy);
            t.setLockDstRect(t.getLockDstRect().left, t.getLockDstRect().top+dy, t.getLockDstRect().right, t.getLockDstRect().bottom+dy);
            t.setPreviewDstRect(t.getPreviewDstRect().left, t.getPreviewDstRect().top+dy, t.getPreviewDstRect().right, t.getPreviewDstRect().bottom+dy);
            t.setTimeDstPoint(t.getTimeDstPoint().x, t.getTimeDstPoint().y+dy);
        }

        isStartScrollingLS = true;
    }

    private boolean finishLevelsScroll() {
        isStartScrollingLS = false;
        return true;
    }
    //</editor-fold>

    //<editor-fold desc="onTouchEvent LEVEL">
    private boolean touchLevel(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchIngameButtons(event))
                    return true;
                touchIntoTile = startTileDrag(event);
            case MotionEvent.ACTION_MOVE:
                if (touchIntoTile) {
                    updateTileDrag(event);
                    invalidate();
                }
                return touchIntoTile;
            case MotionEvent.ACTION_UP:
                if (touchIntoTile) return finishTileDrag();
                if (buttonIngameBackToMenu.getDstRect().contains((int)event.getX(), (int)event.getY()))
                    onBackPressed();
                if (buttonResetLevel.getDstRect().contains((int)event.getX(), (int)event.getY())) {
//                    v.vibrate(VIBRATE_DRAG);
                    setTiles();
                    shiftTilesToStartPosition();
                    calcLevelProgress();
                    invalidate();
                }

        }
        return false;
    }

    private boolean startTileDrag(MotionEvent event) {
        boolean result = false;
        prevTouch.x = event.getX();
        prevTouch.y = event.getY();

        selectedTile = -1;
        for (int i = 0; i < tile.length; i++) {
            result = tile[i].isPointInTile((int)event.getX(), (int)event.getY());
            if (result) {
                selectedTile = i;
//                v.vibrate(VIBRATE_DRAG);
                break;
            }
        }
        // Sequence of tiles in array is changed so that selected tile always be on top
        if (result && (selectedTile > 0)) {
            TangramTile[] reorderedTiles = new TangramTile[TILES_NUMBER];
            int shift = 1;
            reorderedTiles[0] = tile[selectedTile];
            for (int i = 0; i < TILES_NUMBER; i++) {
                if (i == selectedTile){
                    shift = 0;
                    continue;
                }
                reorderedTiles[i+shift] = tile[i];
            }
            tile = reorderedTiles;
            selectedTile = 0;
        }

        invalidate();
        // Чтобы сбросить выделение плитки, если ни одна не выбрана.
        return result;
    }

    private void updateTileDrag(MotionEvent event) {
        float dx, dy;

        dx = event.getX() - prevTouch.x;
        dy = event.getY() - prevTouch.y;
        prevTouch.x = event.getX();
        prevTouch.y = event.getY();
        tile[selectedTile].offset(dx, dy);
    }

    private boolean finishTileDrag() {
        calcLevelProgress();
//        v.vibrate(VIBRATE_DRAG);
        invalidate();
        return true;
    }

    private boolean touchIngameButtons(MotionEvent event) {
        boolean result;
        result = buttonIngameBackToMenu.getDstRect().contains((int)event.getX(), (int)event.getY());
        result |= buttonResetLevel.getDstRect().contains((int)event.getX(), (int)event.getY());
        result |= touchRotationArrows(event);

        return result;
    }

    private boolean touchRotationArrows(MotionEvent event) {
        if (selectedTile < 0 || selectedTile >= tile.length) return false;

        if (buttonLeftArrow.getDstRect().contains((int)event.getX(), (int)event.getY())){
            tileRotation.setFloatValues(0, -TangramTile.ANGLE);
        }
        else if (buttonRightArrow.getDstRect().contains((int)event.getX(), (int)event.getY())) {
            tileRotation.setFloatValues(0, TangramTile.ANGLE);
        }
        else return false;

        tileRotation.start();

        return true;
    }
    //</editor-fold>

    //<editor-fold desc="onTouchEvent Credits">
    private boolean touchCredits(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startLevelSetScroll(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                updateCreditsScroll(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return false;
    }

    private void updateCreditsScroll(MotionEvent event) {
        float dy = event.getY() - prevTouch.y;
        prevTouch.y = event.getY();

        // Limitation for scrolling
        if (Math.abs(dy) < INSENSITIVE_BACKLASH_ON_SCROLL * screenRect.height())
            return;
        if ((textCreditsBounds.top + dy) > CREDITS_OFFSET_FROM_TOP * screenRect.height())
            dy = CREDITS_OFFSET_FROM_TOP * screenRect.height() - textCreditsBounds.top;
        if ((textCreditsBounds.bottom + dy) < (1 - LSS_BUTTON_GAP) * screenRect.height())
            dy = (1 - LSS_BUTTON_GAP) * screenRect.height() - textCreditsBounds.bottom;

        textCreditsBounds.top += dy;
    }
    //</editor-fold>

    /**
     * Define is the levels unlocked in the current row.
     * Current row unlocked when all levels in the previous row get at least bronze cup.
     * @param currentLevel Number of current level.
     * @return Returns true if current level is unlocked.
     */
    private boolean allLevelsInThePrevRowSolved(int currentLevel) {
        // Define is the levels unlocked in the current row.
        // Current row unlocked when all levels in the previous row get at least bronze cup.
        boolean result = true;
        if (levelsCups[currentLevel] == 0) {
            // Note, first 5 levels are always unlocked.
            if (currentLevel >= LEVELS_IN_THE_ROW) {
                int rowOfSelectedLevel = (int) Math.floor(currentLevel/5f);
                for(int j = (rowOfSelectedLevel*5-5); j < (rowOfSelectedLevel*5); j++) {
                    result &= (levelsCups[j] > 0);
                }
            }
        }
        return result;
    }
    //</editor-fold>




    //<editor-fold desc="Initializing resources">
    //<editor-fold desc="Load Resources">
    private void loadBaseResources() {
        logDebugOut(TAG, "loadBaseResources", "Start method.-----");

        loadWoodShader();

        logDebugOut(TAG, "loadBaseResources", "End method.-------");
    }

    private void loadResourcesInBackgroundThread() {
        Thread background = new Thread(() -> {
            logDebugOut(TAG, "loadResourcesInBackgroundThread", "Start method.-----");
            isLoadingEnds = false;
            loadResources();
            setTileRotationAnimation();
            setButtonsCoordinates();
            isLoadingEnds = true;
            logDebugOut(TAG, "loadResourcesInBackgroundThread", "End method.-------");
        });
        background.start();
    }

    private void loadResources() {
        loadBackgroundBitmap();
        loadBitmaps();
        loadButtons();
        loadButtonsResourcesTitle();
        loadButtonsResourcesTextSize();
        setGradientHeaderLS();
        setGradientHeaderLSS();
        setAngularHeaders();
        setLockedLevelText();
        setBackgroundThreads();
    }

    private void loadButtons() {
        Resources res = getResources();
        int color;
        Bitmap lockBitmap = BitmapFactory.decodeResource(res, R.drawable.lock);

        buttonLeftArrow = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.rotate_left));
        buttonRightArrow = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.rotate_right));
        buttonIngameBackToMenu = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.tolevelselection));
        buttonResetLevel = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.reset_level));
        buttonMMStart = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.button01));
        buttonMMCredits = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.button01));
        buttonMMExit = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.button01));
        buttonLSS = new TangramButton[LEVEL_SET_NUMBER];
        for (int i = 0; i < LEVEL_SET_NUMBER; i++){
            buttonLSS[i] = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.button02));
            buttonLSS[i].setLockBitmap(lockBitmap);
        }
        buttonLS = new TangramButton[LEVELS_NUMBER];
        for (int i = 0; i < LEVELS_NUMBER; i++) {
            buttonLS[i] = new TangramButton(BitmapFactory.decodeResource(res, R.drawable.button03));
            buttonLS[i].setLockBitmap(lockBitmap);
            buttonLS[i].setCupBitmap(cupAward[0]);
        }

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        shadowButtonInLSS = BitmapFactory.decodeResource(res, R.drawable.button02, opt);
        for (int i = 0; i < shadowButtonInLSS.getWidth(); i++)
            for (int j = 0; j < shadowButtonInLSS.getHeight(); j++) {
                color = shadowButtonInLSS.getPixel(i, j);
                color |= COLOR_CLEANUP;
                color &= COLOR_SHADOW;
                shadowButtonInLSS.setPixel(i, j, color);
            }

        shadowButtonInLS = BitmapFactory.decodeResource(res, R.drawable.button03, opt);
        for (int i = 0; i < shadowButtonInLS.getWidth(); i++)
            for (int j = 0; j < shadowButtonInLS.getHeight(); j++) {
                color = shadowButtonInLS.getPixel(i, j);
                color |= COLOR_CLEANUP;
                color &= COLOR_SHADOW;
                shadowButtonInLS.setPixel(i, j, color);
            }

        logDebugOut(TAG, "loadButtons", "End method.");
    }

    private void loadButtonsResourcesTitle() {
        int id;

        buttonMMStart.setTitle(getResources().getString(R.string.button_MM_start));
        buttonMMCredits.setTitle(getResources().getString(R.string.button_MM_credits));
        buttonMMExit.setTitle(getResources().getString(R.string.button_MM_exit));

        for (int i = 0; i < LEVEL_SET_NUMBER; i++) {
            id = getResources().getIdentifier("levels_set_" + i, "string", this.getContext().getPackageName());
            buttonLSS[i].setTitle(getResources().getString(id));
        }

    }

    private void loadButtonsResourcesTextSize() {
        int textSize = getResources().getInteger(R.integer.font_mm_buttons);

        buttonMMStart.setTextSize(textSize);
        buttonMMCredits.setTextSize(textSize);
        buttonMMExit.setTextSize(textSize);

        textSize = getResources().getInteger(R.integer.font_lss_buttons);
        for (TangramButton t: buttonLSS)
            t.setTextSize(textSize);

        textSize = getResources().getInteger(R.integer.font_digital);
        for (TangramButton t: buttonLS)
            t.setTextSize(textSize);
    }

    private void loadBitmaps() {
        Resources res = getResources();

        cupAward = new Bitmap[CUPS_NUMBER];
        cupAward[0] = BitmapFactory.decodeResource(res, R.drawable.no_cup);
        cupAward[1] = BitmapFactory.decodeResource(res, R.drawable.bronze_cup);
        cupAward[2] = BitmapFactory.decodeResource(res, R.drawable.silver_cup);
        cupAward[3] = BitmapFactory.decodeResource(res, R.drawable.gold_cup);

        ingameHeader = Bitmap.createBitmap((int)screenRect.width(), (int)(screenRect.height() * INGAME_HEADER_HEIGHT), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(ingameHeader);
        BitmapDrawable headerDrawable = new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.oak_full));
        headerDrawable.setTileModeX(Shader.TileMode.REPEAT);
        headerDrawable.setTileModeY(Shader.TileMode.REPEAT);
        headerDrawable.setBounds(0, 0, (int)screenRect.width(), (int)screenRect.height());
        headerDrawable.draw(c);

        logDebugOut(TAG, "loadBitmaps", "End method.");
    }

    private void setGradientHeaderLS() {
        Resources res = getResources();

        gradientHeaderLS = Bitmap.createBitmap((int)screenRect.width(), (int)(screenRect.height() * (LS_GRADIENT_HEADER_OFFSET_FROM_TOP + LS_GRADIENT_HEADER_HEIGHT)), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(gradientHeaderLS);
        c.drawColor(Color.TRANSPARENT);
        BitmapDrawable gradientHeaderDrawable = new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.maple_full));
        gradientHeaderDrawable.setTileModeX(Shader.TileMode.REPEAT);
        gradientHeaderDrawable.setTileModeY(Shader.TileMode.REPEAT);
        Rect bounds = new Rect();
        bounds.left = 0;
        bounds.right = (int)screenRect.width();
        bounds.top =  0;
        bounds.bottom = (int)(screenRect.height() * (LS_GRADIENT_HEADER_OFFSET_FROM_TOP + LS_GRADIENT_HEADER_HEIGHT));
        gradientHeaderDrawable.setBounds(bounds);
        gradientHeaderDrawable.draw(c);
        int color, currentColor = 0xffffffff;
        int step = ((int) Math.ceil(255f / (screenRect.height() * LS_GRADIENT_HEADER_HEIGHT))) * 0x1000000;
        for (int i =  (int) (screenRect.height() * LS_GRADIENT_HEADER_OFFSET_FROM_TOP); i < gradientHeaderLS.getHeight(); i++) {
            for (int j = 0; j < gradientHeaderLS.getWidth(); j++) {
                color = gradientHeaderLS.getPixel(j, i);
                color &= currentColor;
                gradientHeaderLS.setPixel(j, i, color);
            }
            if (currentColor > 0 && (currentColor - step) < 0)
                currentColor = 0x00ffffff;
            else
                currentColor -= step;
        }
        logDebugOut(TAG, "setGradientHeaderLS", "End method.");
    }

    private void setGradientHeaderLSS() {
        Resources res = getResources();

        gradientHeaderLSS = Bitmap.createBitmap((int)screenRect.width(), (int)(screenRect.height() * (LSS_GRADIENT_HEADER_OFFSET_FROM_TOP + LSS_GRADIENT_HEADER_HEIGHT)), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(gradientHeaderLSS);
        c.drawColor(Color.TRANSPARENT);
        BitmapDrawable gradientHeaderDrawable = new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.maple_full));
        gradientHeaderDrawable.setTileModeX(Shader.TileMode.REPEAT);
        gradientHeaderDrawable.setTileModeY(Shader.TileMode.REPEAT);
        Rect bounds = new Rect();
        bounds.left = 0;
        bounds.right = (int)screenRect.width();
        bounds.top =  0;
        bounds.bottom = (int)(screenRect.height() * (LSS_GRADIENT_HEADER_OFFSET_FROM_TOP + LSS_GRADIENT_HEADER_HEIGHT));
        gradientHeaderDrawable.setBounds(bounds);
        gradientHeaderDrawable.draw(c);
        int color, currentColor = 0xffffffff;
        int step = ((int) Math.ceil(255f / (screenRect.height() * LSS_GRADIENT_HEADER_HEIGHT))) * 0x1000000;
        for (int i =  (int) (screenRect.height() * LSS_GRADIENT_HEADER_OFFSET_FROM_TOP); i < gradientHeaderLSS.getHeight(); i++) {
            for (int j = 0; j < gradientHeaderLSS.getWidth(); j++) {
                color = gradientHeaderLSS.getPixel(j, i);
                color &= currentColor;
                gradientHeaderLSS.setPixel(j, i, color);
            }
            if (currentColor > 0 && (currentColor - step) < 0)
                currentColor = 0x00ffffff;
            else
                currentColor -= step;
        }
        logDebugOut(TAG, "setGradientHeaderLSS", "End method.");
    }

    private void loadWoodShader() {
        Resources res = getResources();

        Bitmap bitmapWood = BitmapFactory.decodeResource(res, R.drawable.woodentexture);
        woodShader = new BitmapShader(bitmapWood, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        logDebugOut(TAG, "loadWoodShader", "End method.");
    }

    private void loadBackgroundBitmap() {
        Resources res = getResources();

        bitmapBack = new BitmapDrawable(res, BitmapFactory.decodeResource(res, R.drawable.maple_full));
        bitmapBack.setTileModeX(Shader.TileMode.REPEAT);
        bitmapBack.setTileModeY(Shader.TileMode.REPEAT);
        bitmapBack.setBounds(0, 0, (int)screenRect.width(), (int)screenRect.height());

        logDebugOut(TAG, "loadBackgroundBitmap", "End method.");
    }

    private void setAngularHeaders() {
        float baseScreenDimension = screenRect.height();
        Bitmap b0 = BitmapFactory.decodeResource(getResources(), R.drawable.angular_header);
        float buttonRatio = (float) b0.getWidth() / b0.getHeight();
        Rect rect = new Rect();

        rect.left = 0;
        rect.top = 0;
        rect.right = (int) (baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio);
        rect.bottom = (int) (baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT);
        angularHeader = new TangramButton[4];
        angularHeader[0] = new TangramButton(b0);
        angularHeader[0].setDstRect(rect);

        m.preScale(-1, 1);
        Bitmap b1 = Bitmap.createBitmap(b0, 0, 0, b0.getWidth(), b0.getHeight(), m, true);
        rect.right = (int) screenRect.width();
        rect.left = (int) (rect.right - baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio);
        angularHeader[1] = new TangramButton(b1);
        angularHeader[1].setDstRect(rect);

        m.setScale(1, -1);
        Bitmap b2 = Bitmap.createBitmap(b1, 0, 0, b0.getWidth(), b0.getHeight(), m, true);
        rect.bottom = (int) baseScreenDimension;
        rect.top = (int) (rect.bottom - baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT);
        angularHeader[2] = new TangramButton(b2);
        angularHeader[2].setDstRect(rect);

        m.setScale(1, -1);
        Bitmap b3 = Bitmap.createBitmap(b0, 0, 0, b0.getWidth(), b0.getHeight(), m, true);
        rect.left = 0;
        rect.right = (int) (baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT * buttonRatio);
        angularHeader[3] = new TangramButton(b3);
        angularHeader[3].setDstRect(rect);
        logDebugOut(TAG, "setAngularHeaders", "End method.");
    }

    private void setLockedLevelText() {
        float x = screenRect.width()/2;
        float y = screenRect.height()/2;
        Resources res = getResources();

        Bitmap lockedLevelBitmap = Bitmap.createBitmap((int)screenRect.width(), (int)screenRect.height(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(lockedLevelBitmap);
        auxString = getResources().getString(R.string.locked_level_touched);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(res.getInteger(R.integer.level_locked_font_end_size));
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setShader(null);
        textPaint.setColor(Color.RED);
        TextPaint tp = new TextPaint(textPaint);
        tp.setShadowLayer(1f, SHADOW_LAYER_OFFSET, SHADOW_LAYER_OFFSET, Color.BLACK);
        StaticLayout mTextLayout = new StaticLayout(auxString, tp, c.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        c.save();
        c.translate(x, y);
        mTextLayout.draw(c);
        c.restore();
        lockedLevelText = new TangramButton(lockedLevelBitmap);
        lockedLevelText.setDstRect(new Rect(0,0,0,0));
        logDebugOut(TAG, "setLockedLevelText", "End method.");
    }

    private void setBackgroundThreads() {
        runnableCalcNumberOfLevelPixels = () -> {
            long threadExecutionTime = System.currentTimeMillis();
            int color, colorGreen, colorRed;
            RectF levelBounds = new RectF();

            Bitmap b = Bitmap.createBitmap((int) screenRect.width(), (int) screenRect.height(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            drawBackground(c);
            drawLevel(c);
            level.getPath().computeBounds(levelBounds, true);
            numberOfLevelPixels = 0;
            for (int i = (int) levelBounds.left; i < levelBounds.right; i+=LEVEL_PROGRESS_CALC_SPEED) {
                for (int j = (int) levelBounds.top; j < levelBounds.bottom; j+=LEVEL_PROGRESS_CALC_SPEED) {
//                        logDebugOut(TAG, "calcNumberOfLevelPixels x.y", i + "." + j);
                    color = b.getPixel(i, j);
                    colorGreen = color & COLOR_GREEN_FILTER;
                    colorRed = color & COLOR_RED_FILTER;
                    // If pixel's color does not match tile color and background color and black frame
                    if (colorGreen > COLOR_GREEN_BOUND && colorRed < COLOR_RED_BOUND & colorRed > COLOR_BLACK_BOUND)
                        numberOfLevelPixels++;
                }
            }
            logDebugOut(TAG, "calcNumberOfLevelPixels", "numberOfLevelPixels == " + numberOfLevelPixels);
            threadExecutionTime = System.currentTimeMillis() - threadExecutionTime;
            logDebugOut(TAG, "calcNumberOfLevelPixels", "threadExecutionTime == " + threadExecutionTime + " ms.");
        };

        runnableCalcLevelProgress = () -> {
            long threadExecutionTime = System.currentTimeMillis();
            int color, colorGreen, colorRed, uncoveredPixels = 0;
            Rect levelBounds;

            levelBounds = level.getPolygonBounds();
            for (int i = levelBounds.left; i < levelBounds.right; i+=LEVEL_PROGRESS_CALC_SPEED) {
                for (int j = levelBounds.top; j < levelBounds.bottom; j+=LEVEL_PROGRESS_CALC_SPEED) {
                    color = scratchpadBitmap.getPixel(i, j);
                    colorGreen = color & COLOR_GREEN_FILTER;
                    colorRed = color & COLOR_RED_FILTER;
                    // If pixel's color does not match tile color and background color and black frame
                    if (colorGreen > COLOR_GREEN_BOUND && colorRed < COLOR_RED_BOUND & colorRed > COLOR_BLACK_BOUND)
                        uncoveredPixels++;
                }
            }
            logDebugOut(TAG, "calcLevelProgress", "uncoveredPixels == " + uncoveredPixels);
            levelProgress = 100 - 100 * (float) uncoveredPixels / numberOfLevelPixels;

            if ((levelProgress >= INGAME_CUP_GOLD) && (levelsCups[selectedLevel] < GOLDEN_CUP)) {
                level.setLevelTimerString(universalTimer.getElapsedTime());
                levelsCups[selectedLevel] = GOLDEN_CUP;
                universalTimer.stop();
            }
            else if ((levelProgress >= INGAME_CUP_SILVER) && (levelsCups[selectedLevel] < SILVER_CUP)) {
                level.setLevelTimerString(universalTimer.getElapsedTime());
                levelsCups[selectedLevel] = SILVER_CUP;
            }
            else if ((levelProgress >= INGAME_CUP_BRONZE) && (levelsCups[selectedLevel] < BRONZE_CUP)) {
                level.setLevelTimerString(universalTimer.getElapsedTime());
                levelsCups[selectedLevel] = BRONZE_CUP;
            }

            threadExecutionTime = System.currentTimeMillis() - threadExecutionTime;
            logDebugOut(TAG, "calcLevelProgress", "threadExecutionTime == " + threadExecutionTime + " ms.");
        };
        logDebugOut(TAG, "setBackgroundThreads", "End method.");
    }
    //</editor-fold>

    //<editor-fold desc="Set Coordinates">
    private void setButtonsCoordinates() {
        // initialize source size and coordinates on screen.
        setMainMenuCoordinates();
        setLSSCoordinates();
        //setLSCoordinates();   <-- moved to LSS menu
        setIngameButtonsCoordinates();
        setArrowsCoordinates();
        logDebugOut(TAG, "setButtonsCoordinates", "End method.");
    }

    private void setArrowsCoordinates() {
        float baseScreenDimension = screenRect.height();
        Rect rect = new Rect();

        rect.left = (int) (baseScreenDimension * INGAME_OFFSET_FROM_BORDER);
        rect.top = (int) (baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT);
        rect.right = rect.left + (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * 1.5);
        rect.bottom = rect.top + (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * 1.5);
        buttonLeftArrow.setDstRect(rect);

        rect.left = rect.right + (int) (baseScreenDimension * INGAME_OFFSET_FROM_BORDER);
        rect.top = (int) (baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT);
        rect.right = rect.left + (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * 1.5);
        rect.bottom = rect.top + (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * 1.5);
        buttonRightArrow.setDstRect(rect);
    }

    private void setIngameButtonsCoordinates() {
        float baseScreenDimension = screenRect.height();
        float horizontalSpace = baseScreenDimension * INGAME_ANGULAR_HEADER_HEIGHT * angularHeader[0].getSrcRect().width() / angularHeader[0].getSrcRect().height() / 2;
        float buttonRatio = (float) buttonIngameBackToMenu.getSrcRect().width() / buttonIngameBackToMenu.getSrcRect().height();
        Rect rect = new Rect();

        // Left bottom corner.
        rect.left = (int) (horizontalSpace - (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * buttonRatio / 2));
        rect.right = rect.left + (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * buttonRatio);
        rect.bottom = (int) (baseScreenDimension * (1 - INGAME_OFFSET_FROM_BORDER));
        rect.top = rect.bottom - (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE);
        buttonIngameBackToMenu.setDstRect(rect);

        // Right bottom corner.
        rect.right = (int) (screenRect.width() - (horizontalSpace - (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * buttonRatio / 2)));
        rect.left = rect.right - (int) (baseScreenDimension * INGAME_BACK_BUTTON_SIZE * buttonRatio);
        buttonResetLevel.setDstRect(rect);

        cupDstRect = new Rect();
        // Centered with levelProgress
        cupDstRect.top = (int) (baseScreenDimension * INGAME_CUP_OFFSET_FROM_TOP);
        cupDstRect.bottom = cupDstRect.top + (int) (baseScreenDimension * INGAME_CUP_HEIGHT);
        cupDstRect.right = (int) (screenRect.width() + baseScreenDimension * INGAME_CUP_OFFSET_FROM_RIGHT);
        cupDstRect.left = cupDstRect.right - (int) (baseScreenDimension * INGAME_CUP_WIDTH);
    }

    private void setMainMenuCoordinates() {
        float baseScreenDimension = screenRect.height();
        Rect rect = new Rect();

        rect.left = (int) (screenRect.width()/2 - baseScreenDimension * MM_BUTTON_SIZE * buttonMMStart.getSrcRect().width()/buttonMMStart.getSrcRect().height()/ 2);
        rect.right = rect.left + (int) (baseScreenDimension * MM_BUTTON_SIZE * buttonMMStart.getSrcRect().width()/buttonMMStart.getSrcRect().height());
        rect.top = (int) (screenRect.height()/2 - baseScreenDimension * MM_BUTTON_SIZE);
        rect.bottom = rect.top + (int) (baseScreenDimension * MM_BUTTON_SIZE);
        buttonMMStart.setDstRect(rect);

        rect.top = rect.bottom + (int) (baseScreenDimension * MM_BUTTON_GAP);
        rect.bottom = rect.top + (int) (baseScreenDimension * MM_BUTTON_SIZE);
        buttonMMCredits.setDstRect(rect);

        rect.top = rect.bottom + (int) (baseScreenDimension * MM_BUTTON_GAP);
        rect.bottom = rect.top + (int) (baseScreenDimension * MM_BUTTON_SIZE);
        buttonMMExit.setDstRect(rect);
    }

    private void setLSSCoordinates() {
        float baseScreenDimension = screenRect.height();
        float lockSize = screenRect.height() * LSS_LOCK_SIZE;
        Rect rect = new Rect();
        Rect lockRect = new Rect();

        //multipurposeRect = new Rect[2];
        //multipurposeRect[0] = new Rect();
        //multipurposeRect[1] = new Rect();//don't initialize 'cause it have multiple usage

        // Buttons of levels' sets.
        rect.left = (int) (screenRect.width()/2 - baseScreenDimension * LSS_BUTTON_SIZE * buttonLSS[0].getSrcRect().width()/buttonLSS[0].getSrcRect().height() / 2);
        rect.right = rect.left + (int) (baseScreenDimension * LSS_BUTTON_SIZE * buttonLSS[0].getSrcRect().width()/buttonLSS[0].getSrcRect().height());
        rect.top = (int) (LSS_OFFSET_FROM_TOP * baseScreenDimension);
        rect.bottom = rect.top + (int) (baseScreenDimension * LSS_BUTTON_SIZE);
        buttonLSS[0].setDstRect(rect);

        for (int i = 1; i < LEVEL_SET_NUMBER; i++) {
            rect.top = rect.bottom + (int) (baseScreenDimension * LSS_BUTTON_GAP);
            rect.bottom = rect.top + (int) (baseScreenDimension * LSS_BUTTON_SIZE);
            buttonLSS[i].setDstRect(rect);

            lockRect.left = (int) (buttonLSS[i].getDstRect().left + buttonLSS[i].getDstRect().width()/2 - lockSize /2);
            lockRect.right = lockRect.left + (int) lockSize;
            lockRect.top = (int) (buttonLSS[i].getDstRect().top + buttonLSS[i].getDstRect().height()/2 - lockSize /2);
            lockRect.bottom = lockRect.top + (int) lockSize;
            buttonLSS[i].setLockDstRect(lockRect);
        }
    }

    private void setLSCoordinates() {
        float baseScreenDimension = screenRect.height();
        float buttonRatio = (float) buttonLS[0].getSrcRect().height() / buttonLS[0].getSrcRect().width();
        float lockSize = baseScreenDimension * LS_LOCK_SIZE;
        float previewSize = baseScreenDimension * LS_PREVIEW_SIZE;
        float cupRatio = (float) buttonLS[0].getCupSrcRect().height() / buttonLS[0].getCupSrcRect().width();
        Rect rect = new Rect();
        Rect lockRect = new Rect();
        Rect cupRect = new Rect();
        Rect previewRect = new Rect();
        PointF timerPoint = new PointF();

        // Сложность в том, что первая кнопка находится не в центре по горизонтали,
        // а левее на (2.5 * LS_BUTTON_SIZE) + (LS_BUTTON_GAP * 2).
        rect.left = (int) (screenRect.width()/2 - baseScreenDimension * (LS_BUTTON_WIDTH * 2.5 + LS_BUTTON_GAP * 2));
        rect.right = rect.left + (int) (baseScreenDimension * LS_BUTTON_WIDTH);
        rect.top = (int) (LS_OFFSET_FROM_TOP * baseScreenDimension);
        rect.bottom = rect.top + (int) (baseScreenDimension * LS_BUTTON_WIDTH * buttonRatio);
        buttonLS[0].setDstRect(rect);
        cupRect.right = rect.right;
        cupRect.left = rect.left;
        cupRect.top = rect.top;
        cupRect.bottom = cupRect.top + (int) (cupRect.width() * cupRatio);
        buttonLS[0].setCupDstRect(cupRect);
        previewRect.left = (int) (rect.centerX() - previewSize / 2);
        previewRect.right = previewRect.left + (int) previewSize;
        previewRect.top = rect.top + (int) (baseScreenDimension * LS_PREVIEW_OFFSET_FROM_TOP);
        previewRect.bottom = previewRect.top + (int) previewSize;
        buttonLS[0].setPreviewDstRect(previewRect);
        textPaint.setTypeface(digitalTF);
        textPaint.getTextBounds("00:00", 0, 5, textBounds);
        timerPoint.x = rect.centerX();
        timerPoint.y = rect.bottom - (int) (baseScreenDimension * LS_OFFSET_TIMER_FROM_BUTTONS_BOTTOM + textBounds.exactCenterY());
        buttonLS[0].setTimeDstPoint(timerPoint);

        // Первый ряд кнопок. Ориентиром является первая кнопка.
        for (int i = 1; i < LEVELS_IN_THE_ROW; i++) {
            rect.left = rect.right + (int) (baseScreenDimension * LS_BUTTON_GAP);
            rect.right = rect.left + (int) (baseScreenDimension * LS_BUTTON_WIDTH);
            buttonLS[i].setDstRect(rect);

            cupRect.right = rect.right;
            cupRect.left = rect.left;
            cupRect.top = rect.top;
            cupRect.bottom = cupRect.top + (int) (cupRect.width() * cupRatio);
            buttonLS[i].setCupDstRect(cupRect);

            previewRect.left = (int) (rect.centerX() - previewSize / 2);
            previewRect.right = previewRect.left + (int) previewSize;
            previewRect.top = rect.top + (int) (baseScreenDimension * LS_PREVIEW_OFFSET_FROM_TOP);
            previewRect.bottom = previewRect.top + (int) previewSize;
            buttonLS[i].setPreviewDstRect(previewRect);

            timerPoint.x = rect.centerX();
            timerPoint.y = rect.bottom - (int) (baseScreenDimension * LS_OFFSET_TIMER_FROM_BUTTONS_BOTTOM + textBounds.exactCenterY());
            buttonLS[i].setTimeDstPoint(timerPoint);
        }

        // Все последующие кнопки. Ориентиром является первый ряд.
        for (int i = LEVELS_IN_THE_ROW; i < LEVELS_NUMBER; i++) {
            rect.left = buttonLS[i-5].getDstRect().left;
            rect.right = buttonLS[i-5].getDstRect().right;
            rect.top = buttonLS[i-5].getDstRect().bottom + (int) (baseScreenDimension * LS_BUTTON_GAP);
            rect.bottom = rect.top + (int) (baseScreenDimension * LS_BUTTON_WIDTH * buttonRatio);
            buttonLS[i].setDstRect(rect);

            lockRect.left = (int) (buttonLS[i].getDstRect().left + buttonLS[i].getDstRect().width()/2 - lockSize /2);
            lockRect.right = lockRect.left + (int) lockSize;
            lockRect.top = rect.top + (int) (baseScreenDimension * LS_LOCK_OFFSET_FROM_TOP);
            lockRect.bottom = lockRect.top + (int) lockSize;
            buttonLS[i].setLockDstRect(lockRect);

            cupRect.right = rect.right;
            cupRect.left = rect.left;
            cupRect.top = rect.top;
            cupRect.bottom = cupRect.top + (int) (cupRect.width() * cupRatio);
            buttonLS[i].setCupDstRect(cupRect);

            previewRect.left = (int) (rect.centerX() - previewSize / 2);
            previewRect.right = previewRect.left + (int) previewSize;
            previewRect.top = rect.top + (int) (baseScreenDimension * LS_PREVIEW_OFFSET_FROM_TOP);
            previewRect.bottom = previewRect.top + (int) previewSize;
            buttonLS[i].setPreviewDstRect(previewRect);

            timerPoint.x = rect.centerX();
            timerPoint.y = rect.bottom - (int) (baseScreenDimension * LS_OFFSET_TIMER_FROM_BUTTONS_BOTTOM + textBounds.exactCenterY());
            buttonLS[i].setTimeDstPoint(timerPoint);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Animation">
    private void setTileRotationAnimation() {
        ValueAnimator.AnimatorUpdateListener tileAnimUpdateListener = animation -> {
            if (isAnimationEnds) return;
            float gav = (float) tileRotation.getAnimatedValue();
            fraction = gav - prevAngle;
            prevAngle = gav;
            tile[selectedTile].rotate(fraction);
            invalidate();
        };

        ValueAnimator.AnimatorListener tileAnimListener = new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationEnds = false;
                prevAngle = 0;
            }
            public void onAnimationCancel(Animator animation){
                logDebugOut(TAG, "TileRotationAnimation", "Animation canceled.");
            }
            public void onAnimationRepeat(Animator animation){
                logDebugOut(TAG, "TileRotationAnimation", "Animation repeat.");
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimationEnds = true;
            }
        };

        tileRotation = new ValueAnimator();
        tileRotation.addUpdateListener(tileAnimUpdateListener);
        tileRotation.addListener(tileAnimListener);
        tileRotation.setEvaluator(new FloatEvaluator());
        tileRotation.setInterpolator(new AccelerateDecelerateInterpolator());
        tileRotation.setDuration(TILE_ANIMATION_DURATION);
        logDebugOut(TAG, "setTileRotationAnimation", "End method.");
    }

    private void setUniversalAnimation() {
        universalUpdateListener = animation -> invalidate();

        universalListener = new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                logDebugOut(TAG, "UniversalAnimation", "Animation started.");
                isAnimationEnds = false;
            }
            public void onAnimationCancel(Animator animation){
                logDebugOut(TAG, "UniversalAnimation", "Animation canceled.");
            }
            public void onAnimationRepeat(Animator animation){
                logDebugOut(TAG, "UniversalAnimation", "Animation repeat.");
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                logDebugOut(TAG, "UniversalAnimation", "Animation ended.");
                isAnimationEnds = true;
                invalidate();
            }
        };
    }

    private void playLoadingAnimation () {
        universalAnimator = new ValueAnimator();
        universalAnimator.addUpdateListener(universalUpdateListener);
        universalAnimator.addListener(universalListener);
        universalAnimator.setEvaluator(new IntEvaluator());
        universalAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        universalAnimator.setDuration(LOADING_ANIMATION_DURATION);
        universalAnimator.setIntValues(0xff, 0);
        universalAnimator.start();
    }

    private void playWarningLockedLevelAnimation() {
        float endSize = 1.0f;

        universalAnimator = new ValueAnimator();
        universalAnimator.addUpdateListener(universalUpdateListener);
        universalAnimator.addListener(universalListener);
        universalAnimator.setInterpolator(new DecelerateInterpolator());
        universalAnimator.setDuration(LEVEL_LOCKED_TEXT_ANIMATION_DURATION);
        universalAnimator.setFloatValues(LS_LOCK_TEXT_BITMAP_START_SIZE, endSize);
        universalAnimator.start();
    }
    //</editor-fold>

    //<editor-fold desc="Save-Load Data">
    private void loadData() {
        int id;
        String str, strTime;
        Resources res = getResources();
        try {
            SharedPreferences sharedPref = this.getContext().getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            logDebugOut(TAG, "loadData", "Method start.");

            for (int i = 0; i < LEVELS_NUMBER; i++) {
                str = "set" + selectedLevelSet + "_level_cup_" + i;
                id = res.getIdentifier(str, "string", this.getContext().getPackageName());
                levelsCups[i] = sharedPref.getInt(res.getString(id), 0);

                str = "set" + selectedLevelSet + "_level_time_" + i;
                id = res.getIdentifier(str, "string", this.getContext().getPackageName());
                strTime = sharedPref.getString(res.getString(id), " ");
                buttonLS[i].setTimeString(strTime);
            }
        }
        catch (Exception ex) {
            logDebugOut(TAG, "loadData", "Exception" + ex.getMessage());
        }
    }

    /**
     * Saving data of all levels in selected set.
     * Recommend to use if app suddenly being destroyed by the system (I hope it helps :-)).
     */
    public void saveData() {
        int id;
        Resources res = getResources();
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        logDebugOut(TAG, "saveData", "Method start.");

        try {
            for (int i = 0; i < LEVELS_NUMBER; i++) {
                id = res.getIdentifier("set" + selectedLevelSet + "_level_cup_" + i, "string", this.getContext().getPackageName());
                editor.putInt(res.getString(id), levelsCups[i]);

                id = res.getIdentifier("set" + selectedLevelSet + "_level_time_" + i, "string", this.getContext().getPackageName());
                editor.putString(res.getString(id), buttonLS[i].getTimeString());
            }
        }
        catch (Exception ex) {
            logDebugOut(TAG, "saveData", "Exception" + ex.getMessage());
        }

        editor.apply();
    }

    private void saveData(int levelsSet, int level) {
        int id;
        Resources res = getResources();
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(res.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        id = res.getIdentifier("set" + levelsSet + "_level_cup_" + level, "string", this.getContext().getPackageName());
        editor.putInt(res.getString(id), levelsCups[level]);
        id = res.getIdentifier("set" + levelsSet + "_level_time_" + level, "string", this.getContext().getPackageName());
        editor.putString(res.getString(id), buttonLS[level].getTimeString());
        editor.apply();
        logDebugOut(TAG, "saveData", "Save data of level No" + level);
    }
    //</editor-fold>

    //<editor-fold desc="Mix">
    public void setScreenRect(float width, float height) {
        screenRect = new RectF();
        screenRect.left = 0;
        screenRect.top = 0;
        screenRect.right = width;
        screenRect.bottom = height;

        scratchpadBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        scratchpadCanvas = new Canvas(scratchpadBitmap);

        ingamePlayableRect = new RectF();
        ingamePlayableRect.left = 0;
        ingamePlayableRect.top = height * INGAME_HEADER_HEIGHT;
        ingamePlayableRect.right = width;
        ingamePlayableRect.bottom = height;

        textCreditsBounds.top = screenRect.height() * CREDITS_OFFSET_FROM_TOP;
    }

    private void doTheVeryFirstInitializations() {
        prevTouch = new PointF();
        textBounds = new Rect();
        textCreditsBounds = new RectF();
        levelsCups = new int[LEVELS_NUMBER];

//        v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        m = new Matrix();
        universalTimer = new TangramTimer(this);
        selectedTile = -1;
        selectedLevelSet = 0;
        isFirstInvalidating = true;
        isLoadingEnds = false;
        TILES_SCALE_FACTOR = (float) getResources().getInteger(R.integer.level_scale_factor)/100;
        //ANGULAR_HEADER_SCALE_FACTOR = (float) getResources().getInteger(R.integer.angular_header_scale_factor)/100;
        TILE_SHADOW_SIZE = getResources().getInteger(R.integer.tile_shadow_size);
        playMode = Mode.LOADING_SCREEN;
    }

    private void setPaints() {
        textPaint = new Paint();
        textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
        textPaint.setAntiAlias(true);

        digitalTF = ResourcesCompat.getFont(getContext(), R.font.digitaldismay);

        simplePaint = new Paint();
        simplePaint.setAntiAlias(true);
        simplePaint.setDither(true);
        simplePaint.setFilterBitmap(true);

        levelPaint = new Paint();
        levelPaint.setAntiAlias(true);
        levelPaint.setDither(true);
        levelPaint.setStrokeWidth(3);
    }

    private void setTiles() {
        Resources res = getResources();
        int[] x, y;
        int id;
        Bitmap bitmapWood = BitmapFactory.decodeResource(res, R.drawable.woodentexture);
        tile = new TangramTile[TILES_NUMBER];

        for (int i = 0; i < TILES_NUMBER; i++) {
            id = res.getIdentifier("tile" + i + "_x", "array", this.getContext().getPackageName());
            x = res.getIntArray(id);

            id = res.getIdentifier("tile" + i + "_y", "array", this.getContext().getPackageName());
            y = res.getIntArray(id);

            tile[i] = new TangramTile(x, y);
            tile[i].setShadowPath(0, 2);
            tile[i].scale(TILES_SCALE_FACTOR);
            tile[i].setBounds(ingamePlayableRect);
            tile[i].setShader(bitmapWood);
        }
    }

    private TangramLevel loadLevelByNumber(int levelSetNumber, int levelNumber){
        int id;
        int[] x, y;
        TangramLevel level;
        Resources res = getResources();

        try {
            id = res.getIdentifier("ls" + levelSetNumber + "_level_" + levelNumber + "_x", "array", this.getContext().getPackageName());
            x = res.getIntArray(id);
            id = res.getIdentifier("ls" + levelSetNumber + "_level_" + levelNumber + "_y", "array", this.getContext().getPackageName());
            y = res.getIntArray(id);

            level = new TangramLevel(x, y);
        }
        catch (Exception ex) {
            level = null;
            logDebugOut(TAG, "loadLevelByNumber(" + levelNumber + ")", "Exception: " + ex.getMessage());
        }

        return level;
    }

    private void setLevelsNames(int levelsSet) {
        int id;
        Resources res = getResources();
        levelsNames = new String[LEVELS_NUMBER];

        for (int i = 0; i < LEVELS_NUMBER; i++) {
            id = res.getIdentifier("ls" + levelsSet + "_level_" + i, "string", this.getContext().getPackageName());
            levelsNames[i] = res.getString(id);
        }
    }

    private void setLevelsPreviewsToButtons(int levelSetNumber) {
        float baseScreenDimension = screenRect.height();
        float previewSize = baseScreenDimension * LS_PREVIEW_SIZE * 0.94f;
        float previewSizeBitmap = baseScreenDimension * LS_PREVIEW_SIZE;
        RectF previewBounds = new RectF(0, 0, previewSizeBitmap, previewSizeBitmap);
        TangramLevel level;
        Bitmap bitmap;
        Canvas canvas;

        // Заглушка до тех пор, пока не реализованы все уровни LEVELS_NUMBER
        // А не фпесду ли заглушку? А то забуду потом.
        for (int i = 0; i < LEVELS_NUMBER; i++) {
            bitmap = Bitmap.createBitmap((int) previewSizeBitmap, (int) previewSizeBitmap, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(Color.TRANSPARENT);

            level = loadLevelByNumber(levelSetNumber, i);
            // Заглушка до тех пор, пока не реализованы все уровни
            if (level != null) {
                level.resize(previewSize, false);
                level.setToCenter(previewBounds);
                levelPaint.setColor(COLOR_LEVEL_BG);
                levelPaint.setStyle(Paint.Style.FILL);
                if (level.isHasHole()) {
                    canvas.save();
                    canvas.clipPath(level.getPathHole(), Region.Op.DIFFERENCE);
                    canvas.drawPath(level.getPath(), levelPaint);
                    canvas.restore();
                }
                else
                    canvas.drawPath(level.getPath(), levelPaint);

                levelPaint.setColor(Color.BLACK);
                levelPaint.setStyle(Paint.Style.STROKE);
                canvas.drawPath(level.getPath(), levelPaint);
                if (level.isHasHole())
                    canvas.drawPath(level.getPathHole(), levelPaint);
            }

            buttonLS[i].setPreviewBitmap(bitmap);
        }
    }

    private void calcNumberOfLevelPixels() {
        Thread bgThreadCalcNumberOfLevelPixels = new Thread(runnableCalcNumberOfLevelPixels);
        bgThreadCalcNumberOfLevelPixels.start();
    }

    private void calcLevelProgress() {
        Thread bgThreadCalcLevelProgress = new Thread(runnableCalcLevelProgress);
        bgThreadCalcLevelProgress.start();
    }

    private void shiftTilesToStartPosition() {
        float baseScreenDimension = screenRect.height();
        float x, y;

        x = baseScreenDimension * INGAME_TILE0_OFFSET_X;
        y = baseScreenDimension * INGAME_TILE0_OFFSET_Y;
        tile[0].move(x, y);
        x = baseScreenDimension * INGAME_TILE1_OFFSET_X;
        y = baseScreenDimension * INGAME_TILE1_OFFSET_Y;
        tile[1].move(x, y);
        x = baseScreenDimension * INGAME_TILE2_OFFSET_X;
        y = baseScreenDimension * INGAME_TILE2_OFFSET_Y;
        tile[2].move(x, y);
        x = baseScreenDimension * INGAME_TILE3_OFFSET_X;
        y = baseScreenDimension * INGAME_TILE3_OFFSET_Y;
        tile[3].move(x, y);
        x = baseScreenDimension * INGAME_TILE4_OFFSET_X;
        y = baseScreenDimension * INGAME_TILE4_OFFSET_Y;
        tile[4].move(x, y);

        x = screenRect.width() - baseScreenDimension * INGAME_TILES_OFFSET_FROM_RIGHT;
        y = baseScreenDimension * INGAME_TILE5_OFFSET_Y;
        tile[5].move(x, y);
        y = baseScreenDimension * INGAME_TILE6_OFFSET_Y;
        tile[6].move(x, y);
    }

    private void hideUI() {
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        logDebugOut(TAG, "hideUI", "Hide UI.");
    }

    public void releaseAllResources() {
        logDebugOut(TAG, "releaseAllResources", "Start method.");
        level = null;
        tile = null;
        buttonLeftArrow = buttonRightArrow = buttonIngameBackToMenu = buttonResetLevel = null;
        buttonMMStart = buttonMMCredits = buttonMMExit = null;
        buttonLSS = buttonLS = null;
        gradientHeaderLS = gradientHeaderLSS = null;
        universalTimer = null;
        levelsCups = null;
        bitmapBack = null;
        shadowButtonInLSS = shadowButtonInLS = null;
        lockedLevelText = null;
        scratchpadBitmap = null;
        scratchpadCanvas = null;
        cupAward = null;
        textPaint = simplePaint = levelPaint = null;
        woodShader = null;
//        v = null;
        tileRotation = null;
        universalAnimator = null;
        universalListener = null;
        universalUpdateListener = null;
        textBounds = null;
        screenRect = null;
        prevTouch = null;
        playMode = null;
        levelsNames = null;
        auxString = null;
    }
    //</editor-fold>
    //</editor-fold>
}
