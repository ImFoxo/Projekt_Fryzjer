package com.example.projektfryzjer.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projektfryzjer.Models.Client;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Client client);

    @Update
    void update(Client client);

    @Delete
    void delete(Client client);

    @Query("DELETE FROM client")
    void deleteAll();

    @Query("SELECT * FROM client ORDER BY clientSurname")
    LiveData<List<Client>> findAll();

    @Query("SELECT * FROM client ORDER BY clientSurname")
    List<Client> findAllToList();
}
