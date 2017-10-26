package com.zheng.project.android.dribbble.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.zheng.project.android.dribbble.R;
import com.zheng.project.android.dribbble.dribbble.auth.Auth;
import com.zheng.project.android.dribbble.dribbble.auth.AuthActivity;
import com.zheng.project.android.dribbble.dribbble.auth.Dribbble;
import com.zheng.project.android.dribbble.dribbble.auth.DribbbleException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.activity_login_btn) TextView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Dribbble.init(this);

        if (!Dribbble.isLoggedIn()) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Auth.openAuthActivity(LoginActivity.this);
                }
            });
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Auth.REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //network call;
                        //use code to fetch auth code for future API query.
                        String token = Auth.fetchAccessToken(authCode);
                        // store access token in SharedPreferences
                        Dribbble.login(LoginActivity.this, token);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (IOException | DribbbleException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
