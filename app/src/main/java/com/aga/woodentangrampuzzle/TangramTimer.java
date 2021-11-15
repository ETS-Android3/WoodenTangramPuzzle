package com.aga.woodentangrampuzzle;

import android.os.Handler;
import android.view.View;

import java.util.Locale;

/**
 *
 * Created by Andrii Husiev on 12.02.2016 for Wooden Tangram.
 *
 */
public class TangramTimer {
    public static final long DEFAULT_TIMER_TICK = 1000;

    private long startTime;
    private long pausedTime;
    private long elapsedTime;
    private long timerTick;
    private boolean timerActivated;
    private final Handler timerHandler;
    private final Runnable timerRunnable;

    public TangramTimer(final View view) {
        startTime = 0;
        pausedTime = 0;
        timerActivated = false;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                elapsedTime = System.currentTimeMillis() - startTime + pausedTime;
                view.invalidate();

                timerHandler.postDelayed(this, timerTick);
            }
        };
    }

    /**
     * Start new timer. If timer is already activated it will be restarted.
     * @param timerTick Timer's tick, which will be used after starting.
     *                  If it equals zero so wil be used DEFAULT_TIMER_TICK.
     */
    public void start(long timerTick) {
        timerActivated = true;
        pausedTime = 0;
        this.timerTick = (timerTick == 0) ? DEFAULT_TIMER_TICK : timerTick;
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    /**
     * Pause timer.
     */
    public void pause() {
        pausedTime += System.currentTimeMillis() - startTime;
        timerHandler.removeCallbacks(timerRunnable);
    }

    /**
     * Resume previously started and then paused timer. Used the same timer's tick before pause.
     */
    public void resume() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    /**
     * Stop timer.
     */
    public void stop() {
        timerActivated = false;
        startTime = 0;
        pausedTime = 0;
        //elapsedTime = 0;
        timerHandler.removeCallbacks(timerRunnable);
    }

    /**
     * Defines, is timer in use.
     * @return Returns true if timer is started, and false if it stopped. Pause don't take effect on this property.
     */
    public boolean isTimerActivated() {
        return timerActivated;
    }

    /**
     * Retrieve elapsed time excluding pause if it was.
     *
     * @return Elapsed time from the start. Time format: hh:mm:ss.
     */
    public String getElapsedTime() {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }
        else
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public static String msToString(long ms) {
        int seconds = (int) (ms / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;

        if (hours > 0) {
            minutes = minutes % 60;
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }
        else
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }
}
