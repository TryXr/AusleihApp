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

public class AnnahmeView extends AppCompatActivity implements View.OnClickListener {

    String code, teacherid;
    Spinner spinnerClass, spinnerStudent;
    CheckBox cbDamaged;
    TextView tvAusgabe, tvDamaged;
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
        etDamaged = findViewById(R.id.etDamaged);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerStudent = findViewById(R.id.spinnerStudent);
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
        setDBAccess();
        try {
            setSpinnerClass(connection, statement);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        try {
            spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setDBAccess();
                    try {
                        String klasse = (String) parent.getSelectedItem();
                        setSpinnerStudent(connection, statement, klasse);
                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    return;
                }
            });

        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
        //setTxtTitle(connection, statement);
        //setSpinnerClass(connection, statement);




    }

    private void setSpinnerStudent(Connection connection, Statement statement, String klasse) {
        //String[] spinnerStudentItems = new String[50];
        klasse = (String) spinnerClass.getSelectedItem();
        String[] klasseid = klasse.split(" ");

        ResultSet result = null;
        ResultSet resultRows = null;

        resultRows = stmts.performDatabaseOperation("SELECT COUNT('idstudent') anz FROM student WHERE idclass=" + klasseid[0], 0, connection, statement);

        String[] spinnerStudentItems = null;

        try {
            if (resultRows != null) {

                try {

                    while (resultRows.next()) {
                        spinnerStudentItems = new String[resultRows.getInt("anz")];

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
            //ahsdfasdf

            result = stmts.performDatabaseOperation("SELECT lastname, firstname FROM student WHERE idclass=" + klasseid[0], 0, connection, statement);
            try {
                if (result != null) {

                    try {
                        int i = 0;
                        while (result.next()) {
                            spinnerStudentItems[i] = result.getString("lastname") + " " + result.getString("firstname");
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

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerStudentItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudent.setAdapter(adapter);
    }

    private void setSpinnerClass (Connection connection, Statement statement) throws
            SQLException {

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

    private void setTxtTitle (Connection connection, Statement statement){
        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT description FROM leihobjekt WHERE scancode =" + code, 0, connection, statement);

        try {
            if(result != null) {
                try {
                    if(result.next()){
                        desc = result.getString("description");
                        tvAusgabe.setText(desc);
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

    @Override
    public void onClick(View v) {
        setDBAccess();
        String name = (String)spinnerStudent.getSelectedItem();
        String[] names = name.split(" ");
        String klasse = (String)spinnerClass.getSelectedItem();
        String[] klassen=klasse.split(" ");
        String update = "UPDATE borrowed SET isback = 1 WHERE isback = 0 AND idlendingobject = (SELECT idleihobjekt FROM leihobjekt WHERE scancode =" + code +") AND idstudent = (SELECT idstudent FROM student WHERE lastname ="+"'"+names[0]+"'"+" AND firstname ="+"'"+names[1]+"'"+" AND idclass = "+klassen[0]+") AND idteacher =" + teacherid;
        stmts.performDatabaseOperation(update,1, connection, statement);
        if(stmts.rows == 1){
            Toast.makeText(this,"Ausleihe wurde zurückgegeben",Toast.LENGTH_LONG).show();
            stmts.rows=0;
        }else{
            Toast.makeText(this,"Keine Ausleihe wurde zu dieser Person gefunden",Toast.LENGTH_LONG).show();
        }
        if(isDamaged && stmts.rows == 1){
            String sql = "INSERT INTO error VALUES(null, (SELECT idborrowed FROM borrowed WHERE idlendingobject = (SELECT idleihobjekt FROM leihobjekt WHERE scancode =" + code +") AND idstudent = (SELECT idstudent FROM student WHERE lastname =" + "'" +names[0]+ "'" + " AND firstname ="+ "'" +names[1]+ "'"+ " AND idclass = "+klassen[0]+ ") AND idteacher = " + teacherid + "), "+ "'" + etDamaged.getText() + "')";
            stmts.performDatabaseOperation("INSERT INTO error VALUES(null, (SELECT idborrowed FROM borrowed WHERE idlendingobject = (SELECT idleihobjekt FROM leihobjekt WHERE scancode =" + code +") AND idstudent = (SELECT idstudent FROM student WHERE lastname =" + "'" +names[0]+ "'" + " AND firstname ="+ "'" +names[1]+ "'"+ " AND idclass = "+klassen[0]+ ") AND idteacher = " + teacherid + "), "+ "'" + etDamaged.getText() + "')", 1, connection, statement);
        }
        //startActivity(intent);


    }
}
