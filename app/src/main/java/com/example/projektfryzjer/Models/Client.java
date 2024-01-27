package com.example.projektfryzjer.Models;

import android.provider.ContactsContract;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "client")
public class Client {
    @PrimaryKey(autoGenerate = true)
    private int clientId;
    private String clientName;
    private String clientSurname;
    private int clientPhoneNumber;

    public Client() {}
    @Ignore
    public Client(String clientName, String clientSurname)
    {
        this.clientName = clientName;
        this.clientSurname = clientSurname;
        clientPhoneNumber = 123312312;
    }

    // Getters Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientSurname() {
        return clientSurname;
    }

    public void setClientSurname(String clientSurname) {
        this.clientSurname = clientSurname;
    }

    public int getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(int clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }
}
