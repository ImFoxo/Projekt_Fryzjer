package com.example.projektfryzjer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Models.Employee;
import com.example.projektfryzjer.Repositories.EmployeeRepo;

import java.util.List;

public class EmployeeViewModel extends AndroidViewModel {
    private final EmployeeRepo employeeRepo;
    private final LiveData<List<Employee>> employees;

    public EmployeeViewModel(@NonNull Application application) {
        super(application);
        employeeRepo = new EmployeeRepo(application);
        employees = employeeRepo.findAllEmployees();
    }

    public LiveData<List<Employee>> findAll() { return employees; }

    public void insert(Employee employee) { employeeRepo.insert(employee); }

    public void update(Employee employee) { employeeRepo.update(employee); }

    public void delete(Employee employee) { employeeRepo.delete(employee); }
}
