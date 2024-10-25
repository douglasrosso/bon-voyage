package com.example.bon_voyage_android;

import com.example.bon_voyage_android.databinding.ActivitySignupBinding;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;


public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        binding.signupButton.setOnClickListener(view -> HandleSignUpButtonClicked());
        binding.loginRedirectText.setOnClickListener(view -> NavigateToLogin());
    }

    private void HandleSignUpButtonClicked() {
        String email = binding.signupEmail.getText().toString();
        String password = binding.signupPassword.getText().toString();
        String confirmPassword = binding.signupConfirm.getText().toString();
        boolean hasAnyFieldEmpty = email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty();

        if (hasAnyFieldEmpty) {
            Toast.makeText(SignupActivity.this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(SignupActivity.this, "Senha inválida!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean hasUserEmail = databaseHelper.checkEmail(email);

        if (hasUserEmail) {
            Toast.makeText(SignupActivity.this, "Usuário já existe! Por favor Realizar login", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = databaseHelper.insertUser(email, password);

        if (!inserted) {
            Toast.makeText(SignupActivity.this, "Falha na inscrição!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(SignupActivity.this, "Inscrito com sucesso!", Toast.LENGTH_SHORT).show();
        NavigateToLogin();
    }

    private void NavigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}