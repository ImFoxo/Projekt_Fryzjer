package com.example.projektfryzjer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projektfryzjer.Database.Helpers.DatabaseHelper;
import com.example.projektfryzjer.R;

public class BeforeLoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_login);
        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.sign_up_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BeforeLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(BeforeLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        DatabaseHelper.getDatabase(getApplicationContext());
    }
}