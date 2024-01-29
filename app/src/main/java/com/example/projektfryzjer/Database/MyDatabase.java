package com.example.projektfryzjer.Database;

import static com.example.projektfryzjer.Database.Migrations.Migrations.MIGRATION_1_2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projektfryzjer.Daos.AppointmentDao;
import com.example.projektfryzjer.Daos.UserDao;
import com.example.projektfryzjer.Database.Helpers.Converters;
import com.example.projektfryzjer.Models.Appointment;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(Converters.class)
@Database(entities = {User.class, Appointment.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase databaseInstance;
    public static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract AppointmentDao appointmentDao();
    public abstract UserDao userDao();

    private static final Callback roomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                UserDao dao = databaseInstance.userDao();
                User admUser = new User("admin", "admin", "admin", true, "admin123");
                admUser.setUserId(1);
                User empUser = new User("employee", "no", "employee", true, "employee123");
                empUser.setUserId(2);
                User[] predefinedUsers = { empUser, admUser };
                dao.insertALL(predefinedUsers);
            });
        }
    };

    public static MyDatabase getDatabaseInstance(final Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, context.getString(R.string.database_name))
                    .addCallback(roomDatabaseCallback)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return databaseInstance;
    }

}
