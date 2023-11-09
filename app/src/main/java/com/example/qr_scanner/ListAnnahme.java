package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ListAnnahme extends AppCompatActivity implements AdapterView.OnItemClickListener {

    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    ListView lvBorrowed;

    String scancode, teacherid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_annahme);
        setDBAccess();
        lvBorrowed = findViewById(R.id.lvBorrowed);
        scancode = getIntent().getStringExtra("code");
        teacherid = getIntent().getStringExtra("teacherid");
        lvBorrowed.setOnItemClickListener(this);
        setItemSource();

    }

    private void setItemSource() {
        ResultSet result = null;
        ResultSet resultRows = null;
        resultRows = stmts.performDatabaseOperation("SELECT COUNT( b.idborrowed ) as anz FROM borrowed b JOIN student s ON b.idstudent = s.idstudent JOIN leihobjekt l ON b.idlendingobject = l.idleihobjekt WHERE b.idlendingobject = (SELECT idleihobjekt FROM leihobjekt WHERE scancode = \"7350049926353\")  AND b.isback = 0;", 0, connection, statement);

        Ausleihe[] ListItemSource = null;

        try {
            if (resultRows != null) {

                try {

                    while (resultRows.next()) {
                        ListItemSource = new Ausleihe[resultRows.getInt("anz")];

                    }
                } catch (Exception exception) {
                    Log.e("Error: ", exception.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        } finally {
            // ResultSet schließen
            if (resultRows != null) {
                try {
                    resultRows.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            result = stmts.performDatabaseOperation("SELECT b.idborrowed, b.starttime, l.description, s.lastname, s.firstname FROM borrowed b JOIN student s ON b.idstudent = s.idstudent JOIN leihobjekt l ON b.idlendingobject = l.idleihobjekt WHERE b.idlendingobject = (SELECT idleihobjekt FROM leihobjekt WHERE scancode =" + scancode + ")  AND b.isback = 0;", 0, connection, statement);
            try {
                if (result != null) {

                    try {
                        int i = 0;
                        while (result.next()) {
                            String dateString = result.getString("starttime");
                            String subDateString = dateString.substring(0, 19);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime localDateTime = LocalDateTime.parse(subDateString, formatter);

                            ListItemSource[i] = new Ausleihe(result.getString("firstname"), result.getString("lastname"), result.getString("description"), localDateTime, Integer.valueOf(result.getString("idborrowed")));
                            i++;
                        }
                    } catch (Exception exception) {
                        Log.e("Error: ", exception.getMessage());
                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            } finally {
                // ResultSet schließen
                if (result != null) {
                    try {
                        result.close();
                        resultRows.close();
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


                ArrayAdapter<Ausleihe> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListItemSource);
                lvBorrowed.setAdapter(adapter);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Ausleihe a = (Ausleihe) adapterView.getItemAtPosition(i);
        Intent intent = new Intent(this, AnnahmeView.class);
        intent.putExtra("idborrowed", String.valueOf(a.getBorrowedid()));
        intent.putExtra("code", scancode);
        intent.putExtra("teacherid", teacherid);
        intent.putExtra("studentName", a.getName() + a.getVname());
        startActivity(intent);
    }
}