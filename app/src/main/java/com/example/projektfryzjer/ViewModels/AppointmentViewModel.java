package com.example.projektfryzjer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Models.Appointment;
import com.example.projektfryzjer.Repositories.AppointmentRepo;

import java.util.List;

public class AppointmentViewModel extends AndroidViewModel {
    private final AppointmentRepo appointmentRepo;
    private final LiveData<List<Appointment>> appointments;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        appointmentRepo = new AppointmentRepo(application);
        appointments = appointmentRepo.findAllAppointments();
    }

    public LiveData<List<Appointment>> findAll() { return appointments; }

    public void insert(Appointment appointment) { appointmentRepo.insert(appointment); }

    public void update(Appointment appointment) { appointmentRepo.update(appointment); }

    public void delete(Appointment appointment) { appointmentRepo.delete(appointment); }
}
