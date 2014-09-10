package com.iac.dmc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

/**
 * Splash activity, show the welcome image and start the DLNAService.
 * 
 * @author CharonChui
 * 
 */
public class SplashActivity extends BaseActivity {
    private ImageView iv_splash;

    private Handler handler = new Handler();

    private static final int sDelayTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findView();
        setUp();
    }

    private void findView() {
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
    }

    private void setUp() {

        playAnimation();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startMainActivity();
            }
        }, sDelayTime);
    }

    private void playAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(sDelayTime);
        alphaAnimation.setFillAfter(true);
        iv_splash.startAnimation(alphaAnimation);
    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), StartPageActivity.class);
        startActivity(intent);
        finish();
    }
}
