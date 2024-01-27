package com.example.projektfryzjer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Models.Client;
import com.example.projektfryzjer.Repositories.ClientRepo;

import java.util.List;

public class ClientViewModel extends AndroidViewModel {
    private final ClientRepo clientRepo;
    private final LiveData<List<Client>> clients;

    public ClientViewModel (@NonNull Application application)
    {
        super(application);

        clientRepo = new ClientRepo(application);
        clients = clientRepo.findAllClients();
    }

    public LiveData<List<Client>> findAll() { return clients; }

    public void insert(Client client) { clientRepo.insert(client); }

    public void update(Client client) { clientRepo.update(client); }

    public void delete(Client client) { clientRepo.delete(client); }
}
