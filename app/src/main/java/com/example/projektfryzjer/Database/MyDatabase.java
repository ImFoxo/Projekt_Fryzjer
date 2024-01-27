package com.example.projektfryzjer.Database;

import static com.example.projektfryzjer.Database.Migrations.Migrations.MIGRATION_1_2;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projektfryzjer.Daos.AppointmentDao;
import com.example.projektfryzjer.Daos.ClientDao;
import com.example.projektfryzjer.Daos.EmployeeDao;
import com.example.projektfryzjer.Daos.UserDao;
import com.example.projektfryzjer.Models.Appointment;
import com.example.projektfryzjer.Models.Client;
import com.example.projektfryzjer.Models.Employee;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Appointment.class, Client.class, Employee.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract ClientDao clientDao();
    public abstract EmployeeDao employeeDao();
    public abstract AppointmentDao appointmentDao();
    public abstract UserDao userDao();

    public static MyDatabase getDatabaseInstance(final Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, context.getString(R.string.database_name))
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2)
                            .build();
        }
        return databaseInstance;
    }

}
