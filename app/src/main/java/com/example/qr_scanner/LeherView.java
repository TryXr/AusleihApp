package com.example.qr_scanner;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LeherView extends AppCompatActivity {

    MySQLStatements stmts = new MySQLStatements();
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leher_view);
         code = getIntent().getStringExtra("code");
        Button buttonAusgabe = findViewById(R.id.buttonAusgabe);
        Button buttonAnnahme = findViewById(R.id.buttonAnnahme);
        Button buttonNeu = findViewById(R.id.buttonNeuesGeraetBuch);
        Button buttonLoeschen = findViewById(R.id.buttonGeraetBuchLoeschen);
        MySQLStatements stmts = new MySQLStatements();
        TextView tvProdukt = findViewById(R.id.tvProdukt);

        Connection connection = null;
        Statement statement = null;
        try {
            connection = MySQLConnection.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        ResultSet result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode=" + code, 0, connection, statement);

        try {
            if (result != null && result.next()) {
                String description = result.getString("description");
            tvProdukt.setText(description);
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
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


        buttonAusgabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hier folgt die Ausgabe (Datenbank usw.)
                startAusgabe();
            }
        });

        buttonAnnahme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hier folgt die Annahme (Datenbank usw.)
            }
        });

        buttonNeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hier folgt die Eintragung von etwas neuem (Datenbank usw.)

                //stmts.performDatabaseOperation("INSERT INTO artikel", 2);
            }
        });

        buttonLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // //Hier folgt die Austragung von etwas (Datenbank usw.)
            }
        });
    }

    private void startAusgabe() {

        Intent intent = new Intent(this, AusgabeView.class);
        intent.putExtra("code", code);
        startActivity(intent);
    }
}
