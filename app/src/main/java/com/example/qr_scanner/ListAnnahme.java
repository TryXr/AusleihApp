package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ListAnnahme extends AppCompatActivity {

    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    Button btnAuswählen;
    ListView lvBorrowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_annahme);
        setDBAccess();
        btnAuswählen = findViewById(R.id.btnAuswählen);
        lvBorrowed = findViewById(R.id.lvBorrowed);
        setItemSource();

    }

    private void setItemSource() {
        ResultSet result = null;
        ResultSet resultRows = null;
        resultRows = stmts.performDatabaseOperation("SELECT COUNT('idcategory') anz FROM category", 0, connection, statement);

        String[] ListItemSource = null;

        try {
            if (resultRows != null) {

                try {

                    while (resultRows.next()) {
                        ListItemSource = new String[resultRows.getInt("anz")];

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
            result = stmts.performDatabaseOperation("SELECT title, idcategory FROM category", 0, connection, statement);
            try {
                if (result != null) {

                    try {
                        int i = 0;
                        while (result.next()) {
                            ListItemSource[i] = result.getString("idcategory") + " " + result.getString("title");
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


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListItemSource);
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
}
