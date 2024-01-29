package com.example.projektfryzjer.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.mindrot.jbcrypt.BCrypt;

@Entity(tableName="user")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="user_id")
    @NonNull
    private int userId;
    @ColumnInfo(name="username")
    @NonNull
    private String username;
    @ColumnInfo(name="first_name")
    @NonNull
    private String firstName;
    @ColumnInfo(name="last_name")
    @NonNull
    private String lastName;
    @ColumnInfo(name="is_employee")
    @NonNull
    private boolean isEmployee;
    @ColumnInfo(name="password")
    @NonNull
    private String password;
    @ColumnInfo(name="password_salt")
    @NonNull
    private String passwordSalt;
    public User() {
    }

    @Ignore
    public static final int NO_EMPLOYEE = 2; // id 2 is reserved for placeholder employee which signals, that no real employee is assigned

    @Ignore
    public User(String username, String firstName, String lastName, boolean isEmployee, String password){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEmployee = isEmployee;

        String passwordSalt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, passwordSalt);
        this.password = hashedPassword;
        this.passwordSalt = passwordSalt;
    }

    @Ignore
    public String getFullname () {
        return firstName + " " + lastName;
    }

    // Getters Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    @NonNull
    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(@NonNull String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
}
