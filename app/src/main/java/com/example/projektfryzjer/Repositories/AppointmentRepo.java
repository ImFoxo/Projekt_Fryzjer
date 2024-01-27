package com.example.projektfryzjer.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Daos.AppointmentDao;
import com.example.projektfryzjer.Models.Appointment;

import java.util.List;

public class AppointmentRepo {
    private final AppointmentDao appointmentDao;
    private final LiveData<List<Appointment>> appointments;

    public AppointmentRepo(Application application)
    {
        MyDatabase dataBase = MyDatabase.getDatabaseInstance(application);
        appointmentDao = dataBase.appointmentDao();
        appointments = appointmentDao.findAll();
    }

    public LiveData<List<Appointment>> findAllAppointments() { return appointments; }

    public void insert(Appointment appointment) {
        MyDatabase.databaseWriteExecutor.execute(() -> appointmentDao.insert(appointment));
    }

    public void update(Appointment appointment) {
        MyDatabase.databaseWriteExecutor.execute(() -> appointmentDao.update(appointment));
    }

    public void delete(Appointment appointment) {
        MyDatabase.databaseWriteExecutor.execute(() -> appointmentDao.delete(appointment));
    }
}
