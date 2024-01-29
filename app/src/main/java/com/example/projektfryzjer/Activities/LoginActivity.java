package com.example.projektfryzjer.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Database.Helpers.DatabaseHelper;
import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.MainActivity;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.Models.UserFieldType;
import com.example.projektfryzjer.R;

import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// LoginActivity
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputUsernameField;
    private EditText inputPasswordField;
    private Button loginBtn;
    private Button registerBtn;
    private String username;
    private String password;
    private DatabaseHelper db;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUsernameField = findViewById(R.id.input_username);
        inputPasswordField = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);
        // create SQLite database
        db = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        initialize(inputUsernameField, inputPasswordField);
    }
    public void initialize(EditText inputUsernameField, EditText inputPasswordField) {
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = inputUsernameField.getText().toString().trim();
                String password = inputPasswordField.getText().toString().trim();
                if(username != null && password != null){
                    loginProcess(username, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.login_error_empty_fields,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loginProcess(String username, String password) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        MyDatabase myDatabase = MyDatabase.getDatabaseInstance(getApplicationContext());
        String storedUserPassword = databaseHelper.getUserFieldSQLQuery(
                getApplicationContext(),
                UserFieldType.PASSWORD,
                username
        );
        String storedPasswordSalt = databaseHelper.getUserFieldSQLQuery(
                getApplicationContext(),
                UserFieldType.PASSWORD_SALT,
                username
        );
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String message = null;
                if (storedUserPassword != null) {
                    String hashedInputPassword = BCrypt.hashpw(password, storedPasswordSalt);
                    if (hashedInputPassword.equals(storedUserPassword)) {
                        message = getString(R.string.login_successful);
                        sessionManager.setLogin(true, myDatabase.userDao().findByUsername(username));
                    } else {
                        message = getString(R.string.login_error_wrong_password);
                    }
                } else {
                    message = getString(R.string.login_error_login_not_found);
                }
                String finalMessage = message;
                LoginActivity.this.runOnUiThread(() -> {
                    Toast.makeText(getApplicationContext(), finalMessage, Toast.LENGTH_LONG).show();
                });
                if (sessionManager.isLoggedIn())
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }, 500);
    }
//    private DialogFragment showDialog(String title){
//        return Functions.showProgressDialog(LoginActivity.this, title);
//    }
}