package com.example.projektfryzjer.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Daos.ClientDao;
import com.example.projektfryzjer.Models.Client;

import java.util.List;

public class ClientRepo {
    private final ClientDao clientDao;
    private final LiveData<List<Client>> clients;

    public ClientRepo(Application application)
    {
        MyDatabase dataBase = MyDatabase.getDatabaseInstance(application);
        clientDao = dataBase.clientDao();
        clients = clientDao.findAll();
    }

    public LiveData<List<Client>> findAllClients() { return clients; }

    public void insert(Client client) {
        MyDatabase.databaseWriteExecutor.execute(() -> clientDao.insert(client));
    }

    public void update(Client client) {
        MyDatabase.databaseWriteExecutor.execute(() -> clientDao.update(client));
    }

    public void delete(Client client) {
        MyDatabase.databaseWriteExecutor.execute(() -> clientDao.delete(client));
    }
}
