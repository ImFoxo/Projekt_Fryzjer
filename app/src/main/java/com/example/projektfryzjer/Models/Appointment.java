package com.example.projektfryzjer.Models;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointment",
        foreignKeys = {
            @ForeignKey(
                entity = User.class,
                parentColumns = {"user_id"},
                childColumns = {"clientId"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            ),
            @ForeignKey(
                entity = User.class,
                parentColumns = {"user_id"},
                childColumns = {"employeeId"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        })
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    private int appointmentId;
    private int clientId;
    private String clientFullName;
    private int employeeId;
    private String employeeFullName;
    private String date;
    private String time;

    private String imgAsString;

    public Appointment() {};

    @Ignore
    public Appointment(String date, String time, int clientId, String clientFullName, String imgAsString){
        this.date = date;
        this.time = time;
        this.clientId = clientId;
        this.clientFullName = clientFullName;
        this.employeeId = 2; // id 2 is reserved for placeholder employee which signals, that no real employee is assigned
        this.employeeFullName = "";
        this.imgAsString = imgAsString;
    }
    @Ignore
    public Appointment(Appointment appointment){
        this.appointmentId = appointment.getAppointmentId();
        this.clientId = appointment.getClientId();
        this.clientFullName = appointment.getClientFullName();
        this.employeeId = appointment.getEmployeeId();
        this.employeeFullName = appointment.getEmployeeFullName();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.imgAsString = appointment.getImgAsString();
    }

    // Getters Setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getImgAsString() {
        return imgAsString;
    }

    public void setImgAsString(String imgAsString) {
        this.imgAsString = imgAsString;
    }
}
