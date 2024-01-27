package com.example.projektfryzjer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.R;

import java.util.Calendar;

public class AppointmentEditActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_APPOINTMENT_DATE = "pb.edu.pl.EDIT_APPOINTMENT_DATE";
    public static final String EXTRA_EDIT_APPOINTMENT_TIME = "pb.edu.pl.EDIT_APPOINTMENT_TIME";
    public static final String EXTRA_EDIT_APPOINTMENT_USERID = "pb.edu.pl.EDIT_APPOINTMENT_USERID";
    public static final String EXTRA_EDIT_APPOINTMENT_ID = "pb.edu.pl.EDIT_APPOINTMENT_ID";

    private EditText txtDate;
    private EditText txtTime;
    private Button btnDatePicker;
    private Button btnTimePicker;

    private int userId;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_edit);

        sessionManager = new SessionManager(AppointmentEditActivity.this);
        userId = sessionManager.loggedInUserId();

        txtDate=findViewById(R.id.appointment_edit_item_date);
        txtTime=findViewById(R.id.appointment_edit_item_time);
        btnDatePicker=findViewById(R.id.button_appointment_date);
        btnTimePicker=findViewById(R.id.button_appointment_time);


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AppointmentEditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_DATE))
            txtDate.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_DATE));
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_TIME))
            txtTime.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_TIME));

        final Button saveButton = findViewById(R.id.button_appointment_save);
        saveButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(txtDate.getText()) || TextUtils.isEmpty(txtTime.getText()))
            {
                setResult(RESULT_CANCELED, replyIntent);
            }
            else
            {
                String appointmentDate = txtDate.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_DATE, appointmentDate);
                String appointmentSurname = txtTime.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_TIME, appointmentSurname);
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_USERID, userId);
//                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_USERID, appointmentId);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }
}