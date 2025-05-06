package com.example.perfume;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class register_activity extends AppCompatActivity {
    LinearLayout bt_back_register;
    ImageView eyeIcon;
    com.example.perfume.DatabaseHelper dbHelper;
    Button registerButton;
    EditText firstnameEditText, lastnameEditText, emailEditText, passwordEditText;
    TextView haveaccountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        bt_back_register=findViewById(R.id.button_back_register);
        eyeIcon = findViewById(R.id.eye_icon);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstnameEditText = findViewById(R.id.firstnameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        registerButton = findViewById(R.id.button_register);
        haveaccountTextView=findViewById(R.id.haveaccountTextView);
        dbHelper = new com.example.perfume.DatabaseHelper(this); // Khởi tạo DB
        bt_back_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Typeface averageFont = ResourcesCompat.getFont(register_activity.this, R.font.average);
                passwordEditText.setTypeface(averageFont);
                passwordEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                passwordEditText.setSelection(passwordEditText.length());

                isPasswordVisible = !isPasswordVisible;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameEditText.getText().toString().trim();
                String lastname = lastnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(register_activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertUser(firstname, lastname, email, password);
                    if (isInserted) {
                        Toast.makeText(register_activity.this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(com.example.perfume.register_activity.this,com.example.perfume.home_activity.class)); // Quay về màn hình trước
                    } else {
                        Toast.makeText(register_activity.this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        haveaccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login= new Intent(register_activity.this,com.example.perfume.login_activity.class);
                startActivity(intent_login);
            }
        });

    }
}
