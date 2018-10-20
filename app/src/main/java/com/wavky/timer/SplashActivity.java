package com.wavky.timer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 获取并设置版本号
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            ((TextView) findViewById(R.id.versionText)).setText("version" + pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            ((TextView) findViewById(R.id.versionText)).setText("version UNKNOWN");
        }

        // 3秒后跳转登录界面，并关闭欢迎界面
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,
                        LoginActivity.class));
                finish();
            }
        };
        timer.schedule(task, 3000);
    }
}
