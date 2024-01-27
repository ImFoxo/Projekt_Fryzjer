package com.example.projektfryzjer.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.Models.Client;
import com.example.projektfryzjer.R;
import com.example.projektfryzjer.ViewModels.ClientViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClientListActivity extends AppCompatActivity {
    private ClientViewModel clientViewModel;
    private static final int NEW_CLIENT_ACTIVITY_REQUEST_CODE = 1;
    private static final int EDIT_CLIENT_ACTIVITY_REQUEST_CODE = 2;
    private Client editedClient = null;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_client_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ClientAdapter adapter = new ClientAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);
        clientViewModel.findAll().observe(this, adapter::setClients);

        sessionManager = new SessionManager(this);

        FloatingActionButton addBookButton = findViewById(R.id.add_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientListActivity.this, ClientEditActivity.class);
                startActivityForResult(intent, NEW_CLIENT_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.setLogin(false, null);
            Toast.makeText(getApplicationContext(), "Logout successfull", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CLIENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Client client = new Client(data.getStringExtra(ClientEditActivity.EXTRA_EDIT_APPOINTMENT_NAME), data.getStringExtra((ClientEditActivity.EXTRA_EDIT_APPOINTMENT_SURNAME)));
            clientViewModel.insert(client);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.client_added), Snackbar.LENGTH_LONG).show();
        }
        else if (requestCode == EDIT_CLIENT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            editedClient.setClientName(data.getStringExtra(ClientEditActivity.EXTRA_EDIT_APPOINTMENT_NAME));
            editedClient.setClientSurname(data.getStringExtra(ClientEditActivity.EXTRA_EDIT_APPOINTMENT_SURNAME));
            clientViewModel.update(editedClient);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.client_edited), Snackbar.LENGTH_LONG).show();
        }
        else
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.client_empty_not_saved), Snackbar.LENGTH_LONG).show();
    }

    private class ClientHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView clientNameTextView;
        private TextView clientSurnameTextView;
        private Client client;

        public ClientHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_client, parent, false));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            clientNameTextView = itemView.findViewById(R.id.client_item_name);
            clientSurnameTextView = itemView.findViewById(R.id.client_item_surname);
        }

        public void bind(Client client)
        {
            this.client = client;
            Log.d("Custom", "bind " + client.getClientName());
            clientNameTextView.setText(client.getClientName());
            clientSurnameTextView.setText(client.getClientSurname());
        }

        @Override
        public void onClick(View v) {
            editedClient = client;
            Log.d("Click", "OnClick" + client.getClientName());
            Intent intent = new Intent(ClientListActivity.this, ClientEditActivity.class);
            intent.putExtra(ClientEditActivity.EXTRA_EDIT_APPOINTMENT_NAME, client.getClientName());
            intent.putExtra(ClientEditActivity.EXTRA_EDIT_APPOINTMENT_SURNAME, client.getClientName());
            startActivityForResult(intent, EDIT_CLIENT_ACTIVITY_REQUEST_CODE);
        }

        @Override
        public boolean onLongClick(View v) {
            clientViewModel.delete(client);
            return false;
        }
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {
        private List<Client> clients;

        @NonNull
        @Override
        public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ClientHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
            if (clients != null)
            {
                Client client = clients.get(position);
                holder.bind(client);
            }
            else
                Log.d("ClientListActivity", "No clients");
        }

        @Override
        public int getItemCount() {
            if (clients != null)
                return clients.size();
            else
                return 0;
        }

        void setClients(List<Client> clients) {
            this.clients = clients;
            notifyDataSetChanged();
        }
    }
}