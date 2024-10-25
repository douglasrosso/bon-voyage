package com.example.bon_voyage_android;


import com.example.bon_voyage_android.databinding.ActivityLoginBinding;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        String sharedEmail = sharedPreferences.getString("email", "");
        String sharedPassword = sharedPreferences.getString("password", "");

        if(!sharedEmail.isEmpty() && !sharedPassword.isEmpty()){
            NavigateToLogin();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(this::HandleSignInClicked);

        binding.signupRedirectText.setOnClickListener(this::NavigateToSignUp);
    }

    private void HandleSignInClicked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String email = binding.loginEmail.getText().toString();
        String password = binding.loginPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCredentialsValid = databaseHelper.checkEmailPassword(email, password);

        if (!isCredentialsValid) {
            Toast.makeText(LoginActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
        Toast.makeText(LoginActivity.this, "Conectado com sucesso!", Toast.LENGTH_SHORT).show();

        NavigateToLogin();
    }

    private void NavigateToLogin() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void NavigateToSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}