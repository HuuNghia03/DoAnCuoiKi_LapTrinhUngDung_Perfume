package com.example.perfume.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.UserRepository;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout backButton;
    private ImageView eyeIcon;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private TextView haveAccountTextView;

    private UserRepository userRepository;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        initViews();
        setupListeners();

        userRepository = new UserRepository(this);
    }

    private void initViews() {
        backButton = findViewById(R.id.button_back_register);
        eyeIcon = findViewById(R.id.eye_icon);
        firstNameEditText = findViewById(R.id.firstnameEditText);
        lastNameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.button_register);
        haveAccountTextView = findViewById(R.id.haveaccountTextView);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        eyeIcon.setOnClickListener(v -> togglePasswordVisibility());

        registerButton.setOnClickListener(v -> handleRegister());

        haveAccountTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.eye_ic_closed);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eyeIcon.setImageResource(R.drawable.eye_ic_open);
        }

        Typeface averageFont = ResourcesCompat.getFont(this, R.font.average);
        passwordEditText.setTypeface(averageFont);
        passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        passwordEditText.setSelection(passwordEditText.length());

        isPasswordVisible = !isPasswordVisible;
    }

    private void handleRegister() {
        String firstname = firstNameEditText.getText().toString().trim();
        String lastname = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.insertUser(firstname, lastname, email, password, true, isInserted -> {
            runOnUiThread(() -> {
                if (isInserted) {
                    userRepository.getUserIdByEmail(email, userId -> {
                        Navigator.saveUserId(RegisterActivity.this, userId);
                        Toast.makeText(RegisterActivity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, WelcomeActivity.class);
                   //     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);


                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
