package com.example.perfume;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class login_activity extends AppCompatActivity {
    LinearLayout btBackLogin;
    TextView noAccountTextView, forgotPwTextView;
    ImageView eyeIcon;
    EditText passwordEditText, emailEditText;
    UserRepository userRepository;
    Button loginButton;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        // Ánh xạ các thành phần giao diện
        btBackLogin = findViewById(R.id.button_back_login);
        noAccountTextView = findViewById(R.id.noaccountTextView);
        forgotPwTextView = findViewById(R.id.forgotpwTextView);
        eyeIcon = findViewById(R.id.eye_icon);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.button_login);
        userRepository = new UserRepository(this);

        // Quay lại màn hình trước
        btBackLogin.setOnClickListener(v -> finish());

        // Chuyển đến màn hình đăng ký
        noAccountTextView.setOnClickListener(v -> {
            Intent intentRegister = new Intent(login_activity.this, register_activity.class);
            startActivity(intentRegister);
        });

        // Ẩn/hiện mật khẩu
        eyeIcon.setOnClickListener(v -> togglePasswordVisibility());

        // Đăng nhập
        loginButton.setOnClickListener(v -> handleLogin());

        // Quên mật khẩu
        forgotPwTextView.setOnClickListener(v -> showForgotPasswordDialog());
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

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.checkUser(email, password, isValid -> runOnUiThread(() -> {
            if (isValid) {
                userRepository.getUserIdByEmail(email, userId -> {
                    Navigator.saveUserId(this, userId);  // Lưu userId vào SharedPreferences
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish(); // Kết thúc activity đăng nhập nếu cần
                });
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
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
        message.setText("Enter your email address, we will send your password.");
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

        builder.setPositiveButton("Send", null); // Không xử lý tại đây để kiểm soát sau
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button sendButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            sendButton.setOnClickListener(v -> {
                String email = emailInput.getText().toString().trim();

                if (email.isEmpty()) {
                    emailInput.setError("Vui lòng nhập email");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Email không hợp lệ");
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

                        Toast.makeText(this, "Đã gửi mật khẩu đến " + email, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Không tìm thấy email trong hệ thống", Toast.LENGTH_SHORT).show();
                    }
                }));
            });
        });

        dialog.show();
        dialog.getWindow().setLayout(1000, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}
