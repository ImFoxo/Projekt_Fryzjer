package com.example.projektfryzjer.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projektfryzjer.Database.Helpers.DatabaseHelper;
import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.R;

import org.mindrot.jbcrypt.BCrypt;

// RegisterActivity
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private DatabaseHelper openHelper;
    private Button registerBtn;
    private Button loginBtn;
    private EditText regUsername;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private EditText regFirstName;
    private EditText regLastName;
    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        openHelper = new DatabaseHelper(this);
        registerBtn = findViewById(R.id.register_btn);
        loginBtn =  findViewById(R.id.login_btn);
        regUsername =  findViewById(R.id.username);
        regPassword= findViewById(R.id.password);
        regConfirmPassword = findViewById(R.id.confirm_password);
        regFirstName = findViewById(R.id.first_name);
        regLastName = findViewById(R.id.last_name);
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String fUsername = regUsername.getText().toString().trim();
                String fPassword = regPassword.getText().toString().trim();
                String fConfirmPassword = regConfirmPassword.getText().toString().trim();
                String fFirstName = regFirstName.getText().toString().trim();
                String fLastName = regLastName.getText().toString().trim();
                if (fUsername.isEmpty() || fPassword.isEmpty() || fConfirmPassword.isEmpty() || fFirstName.isEmpty() || fLastName.isEmpty()) {
                    Toast.makeText(
                            RegisterActivity.this, R.string.register_error_empty_fields, Toast.LENGTH_SHORT).show();
                } else {
                    insertData(
                            fUsername,
                            fPassword,
                            fConfirmPassword,
                            fFirstName,
                            fLastName);
                    Toast.makeText(RegisterActivity.this, R.string.register_successful, Toast.LENGTH_SHORT)
                            .show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    public boolean insertData(String username, String password, String confirmPassword, String firstName, String lastName){
        User newUser = new User();
        if (username != null) {
            newUser.setUsername(username);
        }
        if (password != null) {
            newUser.setPassword(password);
        }
        if (firstName != null) {
            newUser.setFirstName(firstName);
        }
        if (lastName != null){
            newUser.setLastName(lastName);
        }
        if (password.equals(confirmPassword)) {
            String passwordSalt = BCrypt.gensalt();
            String hashedPassword = BCrypt.hashpw(password, passwordSalt);
            newUser.setPassword(hashedPassword);
            newUser.setPasswordSalt(passwordSalt);
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            MyDatabase db = databaseHelper.getDatabase(getApplicationContext());
            db.userDao().insert(newUser);
            db.userDao().findAllToList();
            return true;
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.register_error_pass_does_not_match_repeatPass,
                    Toast.LENGTH_LONG
            ).show();
            return false;
        }
    }
}