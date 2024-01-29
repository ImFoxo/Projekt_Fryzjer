package com.example.projektfryzjer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.Models.UserFieldType;
import com.example.projektfryzjer.R;
import com.example.projektfryzjer.ViewModels.AppointmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListForEmployeesActivity extends AppCompatActivity {
    private AppointmentViewModel appointmentViewModel;
    private SessionManager sessionManager;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_list_for_employees);

        RecyclerView recyclerView = findViewById(R.id.appointment_for_employees_recyclerview);
        final AppointmentListForEmployeesActivity.AppointmentAdapter adapter = new AppointmentListForEmployeesActivity.AppointmentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentViewModel = new ViewModelProvider(this).get(AppointmentViewModel.class);
        appointmentViewModel.findAll().observe(this, adapter::setAppointments);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(AppointmentListForEmployeesActivity.this, LoginActivity.class));
        }
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

    private class AppointmentHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView appointmentDateTextView;
        private TextView appointmentTimeTextView;
        private TextView appointmentClientTextView;
        private TextView appointmentEmployeeTextView;
        private ImageView appointmentPictureTextView;
        private Appointment appointment;

        public AppointmentHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_appointment_for_employee, parent, false));
            itemView.setOnLongClickListener(this);

            appointmentDateTextView = itemView.findViewById(R.id.appointment_item_date);
            appointmentTimeTextView = itemView.findViewById(R.id.appointment_item_time);
            appointmentClientTextView = itemView.findViewById(R.id.appointment_item_client);
            appointmentEmployeeTextView = itemView.findViewById(R.id.appointment_item_employee);
            appointmentPictureTextView = itemView.findViewById(R.id.appointment_item_picture);
        }

        public void bind(Appointment appointment)
        {
            this.appointment = appointment;
            Log.d("Custom", "bind " + appointment.getClientFullName());
            appointmentDateTextView.setText("Data:\n" + appointment.getDate());
            appointmentTimeTextView.setText("Godzina:\n" + appointment.getTime());
            appointmentClientTextView.setText("Klient:\n" + appointment.getClientFullName());
            appointmentEmployeeTextView.setText("Pracownik:\n" + appointment.getEmployeeFullName());
            appointmentPictureTextView.setImageBitmap(Converters.base64ToBitmap(appointment.getImgAsString()));
        }

        @Override
        public boolean onLongClick(View v) {
            createAlertDialogForAppointmentAssignment(appointment);
            return false;
        }
    }

    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentListForEmployeesActivity.AppointmentHolder> {
        private List<Appointment> appointments;

        @NonNull
        @Override
        public AppointmentListForEmployeesActivity.AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AppointmentListForEmployeesActivity.AppointmentHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentListForEmployeesActivity.AppointmentHolder holder, int position) {
            if (appointments != null)
            {
                Appointment appointment = appointments.get(position);
                holder.bind(appointment);
            }
            else
                Log.d("AppointmentListForEmployeesActivity", "No appointments");
        }

        @Override
        public int getItemCount() {
            if (appointments != null)
                return appointments.size();
            else
                return 0;
        }

        void setAppointments(List<Appointment> appointments) {
            this.appointments = appointments;
            notifyDataSetChanged();
        }
    }

    private void createAlertDialogForAppointmentAssignment(Appointment appointment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentListForEmployeesActivity.this);

        String confirm_msg;
        if (appointment.getEmployeeId() != sessionManager.loggedInUserId()) {
            builder.setMessage(R.string.appointment_dialog_emp_assign_message);
            confirm_msg = getString(R.string.appointment_dialog_emp_assign_confirm);
        }
        else {
            builder.setMessage(R.string.appointment_dialog_emp_remove_message);
            confirm_msg = getString(R.string.appointment_dialog_emp_remove_confirm);
        }

        builder.setTitle(R.string.appointment_dialog_title);

        builder.setCancelable(false);

        builder.setPositiveButton(confirm_msg, (DialogInterface.OnClickListener) (dialog, which) -> {
            assignNewEmployee(appointment);
        });

        builder.setNegativeButton(R.string.appointment_dialog_emp_assign_cancel, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void assignNewEmployee (Appointment appointment) {
        if (appointment.getEmployeeId() != sessionManager.loggedInUserId()) {
            appointment.setEmployeeId(sessionManager.loggedInUserId());
            String empFullname = DatabaseHelper.getUserFieldSQLQuery(getApplicationContext(), UserFieldType.FULLNAME, sessionManager.loggedInUsername());
            appointment.setEmployeeFullName(empFullname);
            String msg = getString(R.string.appointment_assign_employee) + appointment.getClientFullName();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        else {
            String msg = getString(R.string.appointment_remove_employee) + appointment.getClientFullName();
            appointment.setEmployeeId(User.NO_EMPLOYEE);
            appointment.setEmployeeFullName("");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        appointmentViewModel.update(appointment);
    }
}