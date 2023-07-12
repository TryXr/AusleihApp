package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class login extends AppCompatActivity implements View.OnClickListener {

TextView txtBname, txtPwd;
Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtBname = findViewById(R.id.editTextUsername);
        txtPwd = findViewById(R.id.editTextPassword);
        btnSubmit = findViewById(R.id.buttonLogin);

        btnSubmit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}