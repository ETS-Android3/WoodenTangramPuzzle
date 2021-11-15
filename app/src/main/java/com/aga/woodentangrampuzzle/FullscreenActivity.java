package com.aga.woodentangrampuzzle;

import static com.aga.woodentangrampuzzle.utils.ObjectBuildHelper.logDebugOut;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class FullscreenActivity extends AppCompatActivity {
    private TangramView mContentView;
    private static final String TAG = "FullscreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mContentView = new TangramView(this);
        setContentView(mContentView);
        logDebugOut(TAG, "onCreate", "Create my custom view.");
        setDisplayMetrics();
        logDebugOut(TAG, "onCreate", "Set Display Metrics.");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mContentView.universalTimer.isTimerActivated())
            mContentView.universalTimer.resume();

        logDebugOut(TAG, "onResume", "Resume work.");
        hideUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mContentView.universalTimer.isTimerActivated())
            mContentView.universalTimer.pause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        logDebugOut(TAG, "onStop", "Stop work and save data.");
        mContentView.saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        logDebugOut(TAG, "onDestroy", "Start method.");
        mContentView.saveData();
        mContentView.releaseAllResources();
        mContentView = null;
        logDebugOut(TAG, "onDestroy", "Exit method.-------------------------------------------");
        logDebugOut(TAG, "onDestroy", " ");
    }

    public void onBackPressed () {
        boolean exitButtonIsPressed = mContentView.onBackPressed();

        if (exitButtonIsPressed){
            super.onBackPressed();
        }
    }

    private void setDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mContentView.setScreenRect(metrics.widthPixels, metrics.heightPixels);
    }

    private void hideUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        if (mContentView != null )
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        logDebugOut(TAG, "hideUI", "Hide UI.");
    }
}
