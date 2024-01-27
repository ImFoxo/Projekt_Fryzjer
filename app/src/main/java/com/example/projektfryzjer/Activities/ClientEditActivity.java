package com.example.projektfryzjer.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.projektfryzjer.R;

public class ClientEditActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_APPOINTMENT_NAME = "pb.edu.pl.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_APPOINTMENT_SURNAME = "pb.edu.pl.EDIT_BOOK_AUTHOR";

    private EditText editNameEditText;
    private EditText editSurnameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit);

        editNameEditText = findViewById(R.id.edit_client_name);
        editSurnameEditText = findViewById(R.id.edit_client_surname);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_NAME))
            editNameEditText.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_NAME));
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_SURNAME))
            editSurnameEditText.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_SURNAME));

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editNameEditText.getText()) || TextUtils.isEmpty(editSurnameEditText.getText()))
            {
                setResult(RESULT_CANCELED, replyIntent);
            }
            else
            {
                String clientName = editNameEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_NAME, clientName);
                String clientSurname = editSurnameEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_SURNAME, clientSurname);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}