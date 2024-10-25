package com.example.bon_voyage_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import models.EntertainmentModelDB;
import models.AccommodationModelDB;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Intent;
import java.text.NumberFormat;
import models.GasolineModelDB;
import android.widget.Button;
import models.AirfareModelDB;
import adapter.TravelAdapter;
import models.TravelModelDB;
import models.MealModelDB;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;


public class ReportFormActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private long travelId;
    private TravelAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_form);
        databaseHelper = new DatabaseHelper(this);
        adapter = new TravelAdapter(ReportFormActivity.this);

        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnAdd = findViewById(R.id.btnAdd);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("travelId")) {
            travelId = intent.getLongExtra("travelId", -1);

            if (travelId != -1) {
                TravelModelDB travel = databaseHelper.getTravelById(travelId);

                TextView travelNameTextView = findViewById(R.id.travel_name);
                travelNameTextView.setText(travel.getTravelName());

                TextView travelDescriptionTextView = findViewById(R.id.travel_description);
                travelDescriptionTextView.setText(travel.getDescription());

                TextView qtdPeopleTextView = findViewById(R.id.people_qtd);
                qtdPeopleTextView.setText("Quantidade de pessoas: " + String.valueOf(travel.getNumberOfPeople()));

                TextView departureLocationTextView = findViewById(R.id.departure_location);
                departureLocationTextView.setText("Local de partida: " + travel.getDepartureLocation());

                TextView arrivalLocationTextView = findViewById(R.id.arrival_location);
                arrivalLocationTextView.setText("Local de chegada: " + travel.getArrivalLocation());

                TextView transportationModeTextView = findViewById(R.id.transportation_mode);
                transportationModeTextView.setText("Meio de locomoção: " + travel.getTransportationMode());

                GasolineModelDB gasoline = databaseHelper.getGasolineById(travelId);
                AirfareModelDB airfare = databaseHelper.getAirfareById(travelId);
                AccommodationModelDB accommodation = databaseHelper.getAccommodationById(travelId);
                MealModelDB meal = databaseHelper.getMealById(travelId);
                EntertainmentModelDB entertainment = databaseHelper.getEntertainmentById(travelId);

                double totalLocomotion;

                if (gasoline != null && gasoline.getTotal() >= 0) {
                    totalLocomotion = gasoline.getTotal();
                } else if (airfare != null && airfare.getTotal() >= 0) {
                    totalLocomotion = airfare.getTotal();
                } else {
                    totalLocomotion = 0.0;
                }


                TextView totalLocomotionTextView = findViewById(R.id.total_locomotion);
                totalLocomotionTextView.setText("Gastos com locomoção: " + formatCurrency(totalLocomotion));

                TextView totalAccommodationTextView = findViewById(R.id.total_accommodation);
                totalAccommodationTextView.setText("Gastos com Hospedagem: " + formatCurrency(accommodation.getTotal()));

                TextView totalMealTextView = findViewById(R.id.total_meal);
                totalMealTextView.setText("Gastos com Alimentação: " + formatCurrency(meal.getTotal()));

                TextView totalEntertainmentTextView = findViewById(R.id.total_entertainment);
                totalEntertainmentTextView.setText("Gastos com Adicionais: " + formatCurrency(entertainment.getTotal()));

                btnEdit.setOnClickListener(view -> NavigateToEditForm(travelId));
                btnDelete.setOnClickListener(view -> HandleDeleteClicked(travel));
                btnAdd.setOnClickListener(view -> NavigateToMain());
            }
        }

    }

    private void NavigateToMain() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private String formatCurrency(double value) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormat.format(value);
    }

    private void NavigateToEditForm(long travelId) {
        Intent intent = new Intent(ReportFormActivity.this, TravelFormActivity.class );
        intent.putExtra("travelId", (int)travelId);
        startActivity(intent);
    }

    private void HandleDeleteClicked(TravelModelDB travel) {
        new AlertDialog.Builder(ReportFormActivity.this)
                .setTitle("Excluir Viagem")
                .setMessage("Tem certeza de que deseja excluir este item?")
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteTravelById((int)travelId);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ReportFormActivity.this, "Viagem Excluida com sucesso.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ReportFormActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }
}
