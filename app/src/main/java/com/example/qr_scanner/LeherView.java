package com.example.qr_scanner;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LeherView extends AppCompatActivity {

    MySQLStatements stmts = new MySQLStatements();
    private String code;

    Statement statement;
    Connection connection;
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
        }

        ResultSet result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode=" + code, 0, connection, statement);

        try {
            if (result != null && result.next()) {
                String description = result.getString("description");
            tvProdukt.setText(description);
            }else{
                startNewObject();
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
                startAnnahme();
            }
        });

        buttonNeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hier folgt die Eintragung von etwas neuem (Datenbank usw.)
                startNewObject();
                //stmts.performDatabaseOperation("INSERT INTO artikel", 2);
            }
        });

        buttonLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageDialog();
            }
        });
    }

    private void startAusgabe() {

        Intent intent = new Intent(this, AusgabeView.class);
        intent.putExtra("teacherid", getIntent().getStringExtra("teacherid"));
        intent.putExtra("code", code);
        startActivity(intent);
    }

    private void startAnnahme() {

        Intent intent = new Intent(this, AnnahmeView.class);
        intent.putExtra("code", code);
        intent.putExtra("teacherid", getIntent().getStringExtra("teacherid"));
        startActivity(intent);
    }

    private void startNewObject() {

        Intent intent = new Intent(this, NewObjectView.class);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Löschen"); // Set the title of the dialog (optional)

        // Set the message you want to display
        builder.setMessage("Wollen Sie das gescannte Objekt wirklich entfernen?");

        // Set a button for the user to dismiss the dialog (OK button)
        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                setDBAccess();
                stmts.performDatabaseOperation("DELETE FROM leihobjekt WHERE scancode=" + code, 1, connection, statement);
                dialog.dismiss(); // Close the dialog
            }
        });
        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setDBAccess () {
        try {
            connection = MySQLConnection.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
