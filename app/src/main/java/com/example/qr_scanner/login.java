package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        MySQLStatements statements = new MySQLStatements();
        setDBAccess();
        int dbGetZugriff = 1;
        ResultSet result = null;
        String pwdd = md5(txtPwd.getText().toString());
        result = stmts.performDatabaseOperation("SELECT * FROM user WHERE username ='" + txtBname.getText().toString() + "' AND password='" + md5(txtPwd.getText().toString()) + "'", 0, connection, statement);

        try {
            if(result != null) {
                try {
                    if(result.next()){
                        Intent intent = new Intent(this, ScannerKlasse.class);
                        intent.putExtra("zugriff", dbGetZugriff);
                        intent.putExtra("teacherid", result.getString("idteacher"));
                        startActivity(intent);
                    }else{
                        Toast.makeText(this, "Falsches Passwort", Toast.LENGTH_LONG).show();
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
    private void setDBAccess () {
        try {
            connection = MySQLConnection.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(final String s) {
            final MessageDigest sha512;
            try {
                sha512 = MessageDigest.getInstance("SHA-512");
            } catch (NoSuchAlgorithmException e) {
                return "404";
            }
            sha512.update(s.getBytes());
            byte byteData[] = sha512.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

    }
}