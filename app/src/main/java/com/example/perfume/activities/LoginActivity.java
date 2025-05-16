package com.example.perfume.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.perfume.EmailSender;
import com.example.perfume.Navigator;
import com.example.perfume.R;
import com.example.perfume.UserRepository;

public class LoginActivity extends AppCompatActivity {
    LinearLayout btBackLogin;
    TextView noAccountTextView, forgotPwTextView;
    EditText passwordEditText, emailEditText;
    UserRepository userRepository;
    Button loginButton;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Map UI components
        btBackLogin = findViewById(R.id.button_back_login);
        noAccountTextView = findViewById(R.id.noaccountTextView);
        forgotPwTextView = findViewById(R.id.forgotpwTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.button_login);
        userRepository = new UserRepository(this);

        // Back to previous screen
        btBackLogin.setOnClickListener(v -> finish());

        // Go to Register screen
        noAccountTextView.setOnClickListener(v -> {
            Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentRegister);
        });

        // Toggle password visibility
        findViewById(R.id.eye_icon).setOnClickListener(v -> togglePasswordVisibility());

        // Login button clicked
        loginButton.setOnClickListener(v -> handleLogin());

        // Forgot password clicked
        forgotPwTextView.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            findViewById(R.id.eye_icon).setBackgroundResource(R.drawable.eye_ic_closed);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            findViewById(R.id.eye_icon).setBackgroundResource(R.drawable.eye_ic_open);
        }

        Typeface averageFont = ResourcesCompat.getFont(this, R.font.average);
        passwordEditText.setTypeface(averageFont);
        passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        passwordEditText.setSelection(passwordEditText.length());

        isPasswordVisible = !isPasswordVisible;
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.checkUser(email, password, isValid -> runOnUiThread(() -> {
            if (isValid) {
                userRepository.getUserIdByEmail(email, userId -> {
                    Navigator.saveUserId(this, userId);
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, android.R.anim.fade_out);
                });
            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(80, 60, 80, 60);

        Typeface arapeyFont = ResourcesCompat.getFont(this, R.font.arapey);
        Typeface balthazarFont = ResourcesCompat.getFont(this, R.font.balthazar);

        TextView title = new TextView(this);
        title.setText("Forgot your password?");
        title.setTypeface(arapeyFont);
        title.setTextSize(26);
        title.setTextColor(Color.BLACK);
        title.setPadding(10, 10, 10, 30);
        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(title);

        TextView message = new TextView(this);
        message.setText("Enter your email address and we will send your password.");
        message.setTextSize(18);
        message.setTypeface(balthazarFont);
        message.setTextColor(Color.BLACK);
        message.setPadding(10, 50, 10, 30);
        layout.addView(message);

        EditText emailInput = new EditText(this);
        emailInput.setHint("Email address");
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setTypeface(balthazarFont);
        emailInput.setPadding(30, 40, 30, 30);
        emailInput.setTextSize(16);
        emailInput.setTextColor(Color.BLACK);
        layout.addView(emailInput);

        builder.setView(layout);

        builder.setPositiveButton("Send", null); // Handle later to control validation
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button sendButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            sendButton.setOnClickListener(v -> {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    emailInput.setError("Please enter your email");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Invalid email address");
                    return;
                }

                userRepository.getPasswordByEmail(email, password -> runOnUiThread(() -> {
                    if (password != null) {
                        Log.d("EMAIL_DEBUG", "Password from DB: " + password);

                        EmailSender.sendEmail(
                                email,
                                "Your Password Recovery",
                                "Here is your password: " + password
                        );

                        Toast.makeText(this, "Password has been sent to " + email, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Email not found in the system", Toast.LENGTH_SHORT).show();
                    }
                }));
            });
        });

        dialog.show();
        dialog.getWindow().setLayout(1000, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
