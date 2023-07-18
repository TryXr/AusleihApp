package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class login extends AppCompatActivity implements View.OnClickListener {

TextView txtBname, txtPwd;
Button btnSubmit;
Connection connection;
Statement statement;
MySQLStatements stmts = new MySQLStatements();

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

        // Hier wird dann die Berechtigung per DB ausgelesen 1 = Lehrer 0 = Schüler
        MySQLStatements statements = new MySQLStatements();

        int dbGetZugriff = 1;

        ResultSet result = null;

        result = stmts.performDatabaseOperation("SELECT * FROM user WHERE username =" + txtBname.getText().toString() + "AND password=" +txtPwd.getText().toString(), 0, connection, statement);

        try {
            if(result != null) {
                try {
                    if(result.next()){
                        Intent intent = new Intent(this, ScannerKlasse.class);
                        intent.putExtra("zugriff", dbGetZugriff);
                        intent.putExtra("teacherid", result.getString("idteacher"));
                        startActivity(intent);
                    }

                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }finally {
            // ResultSet schließen
            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}