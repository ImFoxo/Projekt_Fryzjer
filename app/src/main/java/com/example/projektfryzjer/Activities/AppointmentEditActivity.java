package com.example.projektfryzjer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.projektfryzjer.Database.Helpers.Converters;
import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.R;

import java.util.Calendar;

public class AppointmentEditActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_CODE = 2;
    public static final String EXTRA_EDIT_APPOINTMENT_DATE = "pb.edu.pl.EDIT_APPOINTMENT_DATE";
    public static final String EXTRA_EDIT_APPOINTMENT_TIME = "pb.edu.pl.EDIT_APPOINTMENT_TIME";
    public static final String EXTRA_EDIT_APPOINTMENT_PICTURE = "pb.edu.pl.EDIT_APPOINTMENT_PICTURE";
    public static final String EXTRA_EDIT_APPOINTMENT_USERID = "pb.edu.pl.EDIT_APPOINTMENT_USERID";
    public static final String EXTRA_EDIT_APPOINTMENT_ID = "pb.edu.pl.EDIT_APPOINTMENT_ID";

    private EditText txtDate;
    private EditText txtTime;
    private ImageView imgPicture;
    private Button btnDatePicker;
    private Button btnTimePicker;
    private Button btnOpenCamera;

    private int userId;
    private String pictureAsString;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_edit);

        sessionManager = new SessionManager(AppointmentEditActivity.this);
        userId = sessionManager.loggedInUserId();

        txtDate=findViewById(R.id.appointment_edit_item_date);
        txtTime=findViewById(R.id.appointment_edit_item_time);
        imgPicture=findViewById(R.id.appointment_edit_item_picture);
        btnDatePicker=findViewById(R.id.button_appointment_date);
        btnTimePicker=findViewById(R.id.button_appointment_time);
        btnOpenCamera=findViewById(R.id.button_open_camera);


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
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (minute < 10)
                                    txtTime.setText(hourOfDay + ":0" + minute);
                                else
                                    txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AppointmentEditActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Jeśli uprawnienie nie zostało przyznane, poproś użytkownika o nie
                    ActivityCompat.requestPermissions(AppointmentEditActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                }
                else {
                    openCamera();
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_DATE))
            txtDate.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_DATE));
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_TIME))
            txtTime.setText(intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_TIME));
        if (intent.hasExtra(EXTRA_EDIT_APPOINTMENT_PICTURE)) {
            pictureAsString = intent.getStringExtra(EXTRA_EDIT_APPOINTMENT_PICTURE);
            imgPicture.setImageBitmap(Converters.base64ToBitmap(pictureAsString));
        }

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
                String appointmentTime = txtTime.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_TIME, appointmentTime);
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_USERID, userId);
                replyIntent.putExtra(EXTRA_EDIT_APPOINTMENT_PICTURE, pictureAsString);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            pictureAsString = Converters.bitmapToBase64(imageBitmap);
            imgPicture.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Uprawnienie zostało przyznane, otwórz kamerę
                openCamera();
            } else {
                // Uprawnienie zostało odrzucone, możesz poinformować użytkownika o konieczności uprawnienia
                Toast.makeText(this, "Aby korzystać z aparatu, musisz udzielić uprawnienia.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}