package com.example.projektfryzjer.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projektfryzjer.Database.Helpers.Converters;
import com.example.projektfryzjer.Database.Helpers.DatabaseHelper;
import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.Models.Appointment;
import com.example.projektfryzjer.Models.UserFieldType;
import com.example.projektfryzjer.R;
import com.example.projektfryzjer.ViewModels.AppointmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListActivity extends AppCompatActivity {
    private AppointmentViewModel appointmentViewModel;
    private static final int NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_APPOINTMENT_ACTIVITY_REQUEST_CODE = 2;
    private Appointment editedAppointment = null;
    private SessionManager sessionManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_list);

        RecyclerView recyclerView = findViewById(R.id.appointment_recyclerview);
        final AppointmentListActivity.AppointmentAdapter adapter = new AppointmentListActivity.AppointmentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        appointmentViewModel.findAll().observe(this, adapter::setAppointments);

        context = this;
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(AppointmentListActivity.this, LoginActivity.class));
            finish();
        }

        FloatingActionButton addBookButton = findViewById(R.id.add_appointment_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppointmentListActivity.this, AppointmentEditActivity.class);
                startActivityForResult(intent, NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.setLogin(false, null);
            Toast.makeText(getApplicationContext(), "Logout successfull", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_APPOINTMENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            String clientFullName = DatabaseHelper.getUserFieldSQLQuery(context, UserFieldType.FULLNAME, sessionManager.loggedInUsername());
            String date = data.getStringExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_DATE);
            String time = data.getStringExtra((AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_TIME));
            int userId = data.getIntExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_USERID, 0);
            String imgAsString = data.getStringExtra((AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_PICTURE));
            Appointment appointment = new Appointment(date, time, userId, clientFullName, imgAsString);
            appointmentViewModel.insert(appointment);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.appointment_added), Snackbar.LENGTH_LONG).show();
        }
        else if (requestCode == EDIT_APPOINTMENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            editedAppointment.setDate(data.getStringExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_DATE));
            editedAppointment.setTime(data.getStringExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_TIME));
            editedAppointment.setImgAsString(data.getStringExtra((AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_PICTURE)));
            appointmentViewModel.update(editedAppointment);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.appointment_edited), Snackbar.LENGTH_LONG).show();
        }
        else
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.appointment_empty_not_saved), Snackbar.LENGTH_LONG).show();
    }

    private class AppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView appointmentDateTextView;
        private TextView appointmentTimeTextView;
        private TextView appointmentEmployeeTextView;
        private ImageView appointmentPictureImageView;
        private Appointment appointment;

        public AppointmentHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_appointment, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            appointmentDateTextView = itemView.findViewById(R.id.appointment_item_date);
            appointmentTimeTextView = itemView.findViewById(R.id.appointment_item_time);
            appointmentEmployeeTextView = itemView.findViewById(R.id.appointment_item_employee);
            appointmentPictureImageView = itemView.findViewById(R.id.appointment_item_picture);
        }

        public void bind(Appointment appointment)
        {
            this.appointment = appointment;
            appointmentDateTextView.setText("Data:\n" + appointment.getDate());
            appointmentTimeTextView.setText("Godzina:\n" + appointment.getTime());
            appointmentEmployeeTextView.setText("Pracownik:\n" + appointment.getEmployeeFullName());
            appointmentPictureImageView.setImageBitmap(Converters.base64ToBitmap(appointment.getImgAsString()));
        }

        @Override
        public void onClick(View v) {
            editedAppointment = appointment;
            Log.d("Click", "OnClick" + appointment.getClientFullName());
            Intent intent = new Intent(AppointmentListActivity.this, AppointmentEditActivity.class);
            intent.putExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_DATE, appointment.getDate());
            intent.putExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_TIME, appointment.getTime());
            intent.putExtra(AppointmentEditActivity.EXTRA_EDIT_APPOINTMENT_PICTURE, appointment.getImgAsString());
            startActivityForResult(intent, EDIT_APPOINTMENT_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            createAlertDialogForAppointmentDelete(appointment);
            return false;
        }
    }

    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentListActivity.AppointmentHolder> {
        private List<Appointment> appointments;

        @NonNull
        @Override
        public AppointmentListActivity.AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AppointmentListActivity.AppointmentHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentListActivity.AppointmentHolder holder, int position) {
            if (appointments != null)
            {
                Appointment appointment = appointments.get(position);
                holder.bind(appointment);
            }
            else
                Log.d("AppointmentListActivity", "No appointments");
        }

        @Override
        public int getItemCount() {
            if (appointments != null)
                return appointments.size();
            else
                return 0;
        }

        void setAppointments(List<Appointment> appointments) {
            List<Appointment> userAppointments = new ArrayList<>();
            for (Appointment app: appointments) {
                if (app.getClientId() == sessionManager.loggedInUserId()) {
                    userAppointments.add(new Appointment(app));
                }
            }
            this.appointments = userAppointments;
            notifyDataSetChanged();
        }
    }

    private void createAlertDialogForAppointmentDelete(Appointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentListActivity.this);

        builder.setMessage(R.string.appointment_dialog_message);
        builder.setTitle(R.string.appointment_dialog_title);

        builder.setCancelable(false);

        builder.setPositiveButton(R.string.appointment_dialog_confirm, (DialogInterface.OnClickListener) (dialog, which) -> {
            appointmentViewModel.delete(appointment);
        });

        builder.setNegativeButton(R.string.appointment_dialog_cancel, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}