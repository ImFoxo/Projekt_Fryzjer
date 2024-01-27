package com.example.projektfryzjer;

import android.content.Intent;
import android.os.Bundle;

import com.example.projektfryzjer.Activities.AppointmentEditActivity;
import com.example.projektfryzjer.Activities.AppointmentListActivity;
import com.example.projektfryzjer.Activities.LoginActivity;
import com.example.projektfryzjer.Activities.RegisterActivity;
import com.example.projektfryzjer.Database.Helpers.SessionManager;
//import com.example.projektfryzjer.Models.AppointmentWithClientAndEmployee;
import com.example.projektfryzjer.Activities.ClientListActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AppointmentListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_main_logged_in, menu);
        else if (!sessionManager.isLoggedIn())
            getMenuInflater().inflate(R.menu.menu_main_logged_out, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else if (id == R.id.action_register) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }
        else if (id == R.id.action_logout) {
            sessionManager.setLogin(false, null);
            Toast.makeText(getApplicationContext(), R.string.logout_successful, Toast.LENGTH_LONG).show();
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}