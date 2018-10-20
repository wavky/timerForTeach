package com.wavky.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);

        // 读取上次登陆过的用户名，不存在时返回空字符串
        String lastLoginUser = getSharedPreferences("login", MODE_PRIVATE)
                .getString("username", "");

        // 如果成功获取上次登陆的用户名，则自动赋值
        if (!TextUtils.isEmpty(lastLoginUser))
            usernameText.setText(lastLoginUser);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(usernameText.getText())){
                    new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Warning")
                    .setMessage("用户名不能为空！")
                    .show();
                    return;
                }
                if (TextUtils.isEmpty(passwordText.getText())){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("密码不能为空！")
                            .show();
                    return;
                }
                if (!usernameText.getText().toString().equals(getString(R.string.username))){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("用户名不存在！")
                            .show();
                    return;
                }
                if (!passwordText.getText().toString().equals(getString(R.string.password))){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("密码错误！")
                            .show();
                    return;
                }

                // 记录本次成功登陆的用户名
                getSharedPreferences("login", MODE_PRIVATE).edit().putString("username", usernameText.getText().toString()).apply();

                startActivity(new Intent(LoginActivity.this,
                        TimerActivity.class));
            }
        });
    }


}
