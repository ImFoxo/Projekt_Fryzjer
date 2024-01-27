package com.example.projektfryzjer.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Daos.EmployeeDao;
import com.example.projektfryzjer.Models.Employee;

import java.util.List;

public class EmployeeRepo {
    private final EmployeeDao employeeDao;
    private final LiveData<List<Employee>> employees;

    public EmployeeRepo(Application application)
    {
        MyDatabase dataBase = MyDatabase.getDatabaseInstance(application);
        employeeDao = dataBase.employeeDao();
        employees = employeeDao.findAll();
    }

    public LiveData<List<Employee>> findAllEmployees() { return employees; }

    public void insert(Employee employee) {
        MyDatabase.databaseWriteExecutor.execute(() -> employeeDao.insert(employee));
    }

    public void update(Employee employee) {
        MyDatabase.databaseWriteExecutor.execute(() -> employeeDao.update(employee));
    }

    public void delete(Employee employee) {
        MyDatabase.databaseWriteExecutor.execute(() -> employeeDao.delete(employee));
    }
}
