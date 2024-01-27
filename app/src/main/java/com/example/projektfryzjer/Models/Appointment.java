package com.example.projektfryzjer.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "appointment",
        foreignKeys = {
            @ForeignKey(
                entity = User.class,
                parentColumns = {"user_id"},
                childColumns = {"userId"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        })
public class Appointment {
    @PrimaryKey(autoGenerate = true)
    private int appointmentId;
    private int userId;
    private String date;
    private String time;
    private String clientName;

    public Appointment() {};

    @Ignore
    public Appointment(String date, String time, int userId){
        this.date = date;
        this.time = time;
        this.userId = userId;
    }
    @Ignore
    public Appointment(Appointment appointment){
        this.appointmentId = appointment.getAppointmentId();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.userId = appointment.getUserId();
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
