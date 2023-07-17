package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AusgabeView extends AppCompatActivity {

    String code;
    Spinner spinnerClass, spinnerStudent;
    CheckBox cbAll;
    TextView tvAusgabe, tvStudent;
    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausgabe_view);
        code = getIntent().getStringExtra("code");


        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerStudent = findViewById(R.id.spinnerStudent);
        cbAll = findViewById(R.id.cbAll);
        tvAusgabe = findViewById(R.id.tvAusgabe);
        tvStudent = findViewById(R.id.tvStudent);
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cbAll.isChecked()){
                    spinnerStudent.setVisibility(View.INVISIBLE);
                    tvStudent.setVisibility(View.INVISIBLE);
                   
                }else{
                    spinnerStudent.setVisibility(View.VISIBLE);
                    tvStudent.setVisibility(View.VISIBLE);
                }
            }
        });


        setDBAccess();
        try {
            setSpinnerClass(connection, statement);
        }catch(Exception e){
            Log.e("Error: ",e.getMessage());
        }
        setDBAccess();
        try {
            //setSpinnerStudent(connection, statement);
        }catch(Exception e){
            Log.e("Error: ",e.getMessage());
        }




        //setTxtTitle(connection, statement);
        //setSpinnerClass(connection, statement);




    }

    private void setSpinnerStudent(Connection connection, Statement statement) {
        String[] spinnerStudentItems = new String[50];
        String klasse = (String)spinnerClass.getSelectedItem();
        String[] klasseid = klasse.split(" ");

        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT lastname, firstname FROM class WHERE classid=" + klasseid[0], 0, connection, statement);
        try {
            if (result != null && result.next()) {

                try{
                    int i = 0;
                    while(result.next()){
                        spinnerStudentItems[i] = result.getString("lastname") + " " + result.getString("firstname");
                        i++;
                    }
                }catch (Exception exception){
                    Log.e("Error: ", exception.getMessage());
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }finally {
            // ResultSet schließen
            if (result != null) {

            }
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



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerStudentItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudent.setAdapter(adapter);
    }

    private void setSpinnerClass(Connection connection, Statement statement) throws SQLException {

        ResultSet result = null;
        ResultSet resultRows = null;
        resultRows = stmts.performDatabaseOperation("SELECT COUNT('idclass') anz FROM class", 0, connection, statement);

        String[] spinnerClassItems = null;

        try {
            if (resultRows != null) {

                try {

                    while (resultRows.next()) {
                        spinnerClassItems = new String[resultRows.getInt("anz")];

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
            result = stmts.performDatabaseOperation("SELECT title, idclass FROM class", 0, connection, statement);
            try {
                if (result != null) {

                    try {
                        int i = 0;
                        while (result.next()) {
                            spinnerClassItems[i] = result.getString("idclass") + " " + result.getString("title");
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


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerClassItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClass.setAdapter(adapter);
            }
        }
    }

    private void setTxtTitle(Connection connection, Statement statement) {
        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode =" + code, 0,connection, statement);

        try {
            String desc = result.getString("description");
            tvAusgabe.setText(desc);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }

    private void setDBAccess(){
        try {
            connection = MySQLConnection.getConnection();
            statement = connection.createStatement();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}