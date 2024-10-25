package com.example.bon_voyage_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Button;
import adapter.TravelAdapter;
import adapter.TravelModel;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private ListView travelList;
    private TravelAdapter adapter;
    private Button btnAdd;
    private Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnAdd = findViewById(R.id.btnAdd);
        travelList = findViewById(R.id.travelList);
        adapter = new TravelAdapter(MainActivity.this);

        ArrayList<TravelModel> travels = new ArrayList<TravelModel>();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        travels.addAll(databaseHelper.findAllTravels());
        adapter.setTravelList(travels);
        travelList.setAdapter(adapter);

        btnAdd.setOnClickListener(this::NavigateToForm);
        btnLogout.setOnClickListener(this::HandleLogout);
    }

    private void NavigateToForm(View v) {
        Intent intent = new Intent(getApplicationContext(), TravelFormActivity.class);
        startActivity(intent);
    }

    private void NavigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void HandleLogout(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", "");
        editor.putString("password", "");
        editor.apply();

        NavigateToLogin();
    }
}