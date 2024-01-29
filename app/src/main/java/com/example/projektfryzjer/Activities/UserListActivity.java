package com.example.projektfryzjer.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projektfryzjer.Database.Helpers.SessionManager;
import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.R;
import com.example.projektfryzjer.ViewModels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_list);

        RecyclerView recyclerView = findViewById(R.id.user_recyclerview);
        final UserListActivity.UserAdapter adapter = new UserListActivity.UserAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.findAll().observe(this, adapter::setUsers);

        sessionManager = new SessionManager(this);
    }

    private class UserHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView userNameTextView;
        private TextView userLastNameTextView;
        private TextView userIsEmployeeTextView;
        private User user;

        public UserHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_user, parent, false));
            itemView.setOnLongClickListener(this);

            userNameTextView = itemView.findViewById(R.id.user_item_name);
            userLastNameTextView = itemView.findViewById(R.id.user_item_lastName);
            userIsEmployeeTextView = itemView.findViewById(R.id.user_item_isEmployee);
        }

        public void bind(User user)
        {
            this.user = user;
            Log.d("Custom", "bind " + user.getUsername());
            userNameTextView.setText(user.getFirstName());
            userLastNameTextView.setText(user.getLastName());
            if (user.isEmployee())
                userIsEmployeeTextView.setText("Pracownik");
            else
                userIsEmployeeTextView.setText("");
        }

        @Override
        public boolean onLongClick(View v) {
            if (!user.isEmployee()) {
                user.setEmployee(true);
                String msg = getString(R.string.user_employee_granted) + user.getFullname();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            else {
                user.setEmployee(false);
                String msg = getString(R.string.user_employee_revoked) + user.getFullname();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
            userViewModel.update(user);
            return false;
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserListActivity.UserHolder> {
        private List<User> users;

        @NonNull
        @Override
        public UserListActivity.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserListActivity.UserHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserListActivity.UserHolder holder, int position) {
            if (users != null)
            {
                User user = users.get(position);
                holder.bind(user);
            }
            else
                Log.d("UserListActivity", "No users");
        }

        @Override
        public int getItemCount() {
            if (users != null)
                return users.size();
            else
                return 0;
        }

        void setUsers(List<User> users) {
            this.users = users;
            notifyDataSetChanged();
        }
    }
}