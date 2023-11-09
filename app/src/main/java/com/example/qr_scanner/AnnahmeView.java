package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AnnahmeView extends AppCompatActivity implements View.OnClickListener {

    String code, teacherid, borrowedid, studentName;
    CheckBox cbDamaged;
    TextView tvAusgabe, tvDamaged, tvStudentName;
    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    String desc = "";
    Button btnSubmit;
    Boolean isDamaged = false;
    EditText etDamaged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annahme_view);
        code = getIntent().getStringExtra("code");
        teacherid = getIntent().getStringExtra("teacherid");
        borrowedid = getIntent().getStringExtra("idborrowed");
        studentName = getIntent().getStringExtra("studentName");
        etDamaged = findViewById(R.id.etDamaged);
        tvStudentName = findViewById(R.id.tvStudentName);
        tvStudentName.setText(studentName);
        cbDamaged = findViewById(R.id.cbDamaged);
        tvAusgabe = findViewById(R.id.tvAusgabe);
        tvDamaged = findViewById(R.id.tvDamaged);
        btnSubmit = findViewById(R.id.btnAnnahme);
        etDamaged.setVisibility(View.INVISIBLE);
        cbDamaged.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbDamaged.isChecked()) {
                    isDamaged = true;
                    etDamaged.setVisibility(View.VISIBLE);

                } else {
                    isDamaged = false;
                    etDamaged.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSubmit.setOnClickListener(this);


        setDBAccess();
        setTxtTitle(connection, statement);
        //tvAusgabe.setText(borrowedid);
    }

    private void setTxtTitle(Connection connection, Statement statement) {
        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode =" + code, 0, connection, statement);

        try {
            if (result != null) {
                try {
                    if (result.next()) {
                        desc = result.getString("description");
                        tvAusgabe.setText(desc);
                    }

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        } finally {
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

    private void setDBAccess() {
        try {
            connection = MySQLConnection.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        @Override
        public void onClick (View v){
            setDBAccess();

            String update = "UPDATE borrowed SET isback = 1 WHERE idborrowed =" + borrowedid;
            stmts.performDatabaseOperation(update, 1, connection, statement);
            if (stmts.rows == 1) {
                Toast.makeText(this, "Ausleihe wurde zurückgegeben", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Keine Ausleihe wurde zu dieser Person gefunden", Toast.LENGTH_LONG).show();
            }
            if (isDamaged && stmts.rows == 1) {
                String sql = "INSERT INTO error VALUES(null, " + borrowedid + "," + "'" + etDamaged.getText() + "')";
                stmts.performDatabaseOperation(sql, 1, connection, statement);

            }
            stmts.rows = 0;
        }
    }

