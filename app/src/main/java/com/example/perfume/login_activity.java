package com.example.perfume;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
    LinearLayout bt_back_login;
    TextView noaccountTextView,forgotpwTextView;
    ImageView eyeIcon;
    EditText passwordEditText,emailEditText;
    com.example.perfume.DatabaseHelper dbHelper;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        bt_back_login=findViewById(R.id.button_back_login);
        noaccountTextView=findViewById(R.id.noaccountTextView);
        forgotpwTextView=findViewById(R.id.forgotpwTextView);
        eyeIcon = findViewById(R.id.eye_icon);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        dbHelper = new com.example.perfume.DatabaseHelper(this);
        loginButton = findViewById(R.id.button_login);

        bt_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noaccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_register=new Intent(login_activity.this,com.example.perfume.register_activity.class);
                startActivity(intent_register);
            }
        });

        eyeIcon.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon.setImageResource(R.drawable.eye_ic_closed);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon.setImageResource(R.drawable.eye_ic_open);
                }

                // Set lại font, size, và vị trí con trỏ
                Typeface averageFont = ResourcesCompat.getFont(com.example.perfume.login_activity.this, R.font.average);
                passwordEditText.setTypeface(averageFont);
                passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                passwordEditText.setSelection(passwordEditText.length());

                isPasswordVisible = !isPasswordVisible;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login_activity.this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = dbHelper.checkUser(email, password);
                    if (isValid) {
                        Toast.makeText(login_activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(login_activity.this,com.example.perfume.home_activity.class));

                    } else {
                        Toast.makeText(login_activity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        forgotpwTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });
    }
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Layout tổng
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(80, 60, 80, 60); // Padding rộng

        // Font
        Typeface arapeyFont = ResourcesCompat.getFont(this, R.font.arapey);
        Typeface balthazarFont = ResourcesCompat.getFont(this, R.font.balthazar);

        // Tiêu đề
        TextView customTitle = new TextView(this);
        customTitle.setText("Forgot your password?");
        customTitle.setTypeface(arapeyFont);
        customTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        customTitle.setTextColor(Color.BLACK); // Màu chữ đen
        customTitle.setPadding(10, 10, 10, 30);
        customTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(customTitle);

        // Nội dung
        TextView message = new TextView(this);
        message.setText("Enter your email address, we will send your password.");
        message.setTextSize(18);
        message.setTypeface(balthazarFont);
        message.setTextColor(Color.BLACK); // Màu chữ đen
        message.setPadding(10, 50, 10, 30);
        layout.addView(message);

        // Ô nhập email
        EditText emailInput = new EditText(this);
        emailInput.setHint("Email address");
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setTypeface(balthazarFont);
        emailInput.setPadding(30, 40, 30, 30);
        emailInput.setTextSize(16);
        emailInput.setTextColor(Color.BLACK); // Màu chữ đen
        layout.addView(emailInput);

        builder.setView(layout);

        // Nút Gửi
        builder.setPositiveButton("Send", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            if (!email.isEmpty()) {
                String pass = dbHelper.getPasswordByEmail(email);
                Log.d("EMAIL_DEBUG", "Password from DB: " + pass);
                com.example.perfume.EmailSender.sendEmail(
                        email,
                        "Your Password Recovery",
                        "Here is your password: " + dbHelper.getPasswordByEmail(email) // hoặc nội dung bạn muốn
                );
                Toast.makeText(this, "Password sent to " + email, Toast.LENGTH_SHORT).show();

            } else {
                emailInput.setError("Vui lòng nhập email");
            }
        });

        // Nút Hủy
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Phóng to cửa sổ
        dialog.getWindow().setLayout(1000, LinearLayout.LayoutParams.WRAP_CONTENT);
    }





}

