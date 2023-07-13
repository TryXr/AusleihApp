package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AusgabeView extends AppCompatActivity {

    String code;
    Spinner spinnerClass, spinnerStudent;
    CheckBox cbAll;
    TextView tvAusgabe;
    MySQLStatements stmts = new MySQLStatements();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausgabe_view);
        code = getIntent().getStringExtra("code");


        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerStudent = findViewById(R.id.spinnerStudent);
        cbAll = findViewById(R.id.cbAll);
        tvAusgabe = findViewById(R.id.tvAuswahl);

        setTxtTitle();
        setSpinnerClass();
        checkIfcbAll();



    }

    private void checkIfcbAll() {
        if(cbAll.isChecked()){
            spinnerStudent.setVisibility(View.INVISIBLE);
        }else{
            setSpinnerStudent();
        }
    }

    private void setSpinnerStudent() {
        String[] spinnerStudentItems = null;
        String klasse = (String)spinnerClass.getSelectedItem();
        String[] klasseid = klasse.split(" ");

        ResultSet resultSet = stmts.performDatabaseOperation("SELECT name, firstname FROM class WHERE classid=" + klasseid[0], 0);

        try{
            int i = 0;
            while(resultSet.next()){
                spinnerStudentItems[i] = resultSet.getString("name") + " " + resultSet.getString("firsname");
                i++;
            }
        }catch (Exception exception){
            Log.e("Error: ", exception.getMessage());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerStudentItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudent.setAdapter(adapter);
    }

    private void setSpinnerClass() {
        String[] spinnerClassItems = null;

        ResultSet resultSet = stmts.performDatabaseOperation("SELECT name, idclass FROM class", 0);

        try{
            int i = 0;
            while(resultSet.next()){
                spinnerClassItems[i] = resultSet.getString("idclass") + " " + resultSet.getString("name");
                i++;
            }
        }catch (Exception exception){
            Log.e("Error: ", exception.getMessage());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerClassItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapter);
    }

    private void setTxtTitle() {
        ResultSet result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode =" + code, 0);

        try {
            String desc = result.getString("description");
            tvAusgabe.setText(desc);

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
    }
}