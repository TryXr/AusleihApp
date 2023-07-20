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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AusgabeView extends AppCompatActivity implements View.OnClickListener {

    String code;
    Spinner spinnerClass, spinnerStudent;
    CheckBox cbAll;
    TextView tvAusgabe, tvStudent;
    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    Button btnSubmit;
    String desc = "";
    Boolean isAll = false;
    String klasse = "";
    int id = 0;
    int menge=0;

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
        btnSubmit = findViewById(R.id.buttonAusgabe);
        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbAll.isChecked()) {
                    spinnerStudent.setVisibility(View.INVISIBLE);
                    tvStudent.setVisibility(View.INVISIBLE);
                    isAll = true;

                } else {
                    spinnerStudent.setVisibility(View.VISIBLE);
                    tvStudent.setVisibility(View.VISIBLE);
                    isAll = false;
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
                        klasse = (String) parent.getSelectedItem();
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

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }


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

    private void setSpinnerClass(Connection connection, Statement statement) throws
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

    private void setTxtTitle(Connection connection, Statement statement) {
        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT description, idleihobjekt FROM leihobjekt WHERE scancode =" + code, 0, connection, statement);

        try {
            if (result != null) {
                try {
                    if (result.next()) {
                        desc = result.getString("description");
                        tvAusgabe.setText(desc);
                        id = result.getInt("idleihobjekt");
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
    public void onClick(View v) {

        String student = (String) spinnerStudent.getSelectedItem();
        String[] students = student.split(" ");
        String tid = getIntent().getStringExtra("teacherid");
        String stundentselect = ", (SELECT idstudent FROM student WHERE lastname='" + students[0] + "'" + " AND firstname='" + students[1] + "')";
        String leihselect = ", (SELECT idleihobjekt FROM leihobjekt WHERE description ='" + desc + "')";
        String currentTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()).toString();
        menge = getMenge();
        if (isAll == false) {
            setDBAccess();
            try {
                if (menge>=1) {
                stmts.performDatabaseOperation("INSERT INTO borrowed VALUES(" + "null" + ", " + tid + stundentselect + leihselect + ", 0, " + "'" + currentTimeStamp + "')", 3, connection, statement);
                Toast.makeText(this, "Ausleihe wurde in der Datenbank hinzugefügt", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Ausleihe ist nicht möglich", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } finally {

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
        } else {
            setDBAccess();
            ResultSet result = null;
            ResultSet resultRows = null;
            klasse = (String) spinnerClass.getSelectedItem();
            String[] klasseid = klasse.split(" ");
            resultRows = stmts.performDatabaseOperation("SELECT COUNT('idstudent') anz FROM student WHERE idclass ='" + klasseid[0] + "'", 0, connection, statement);

            String[] studentLength = null;

            try {
                if (resultRows != null) {

                    try {

                        while (resultRows.next()) {
                            studentLength = new String[resultRows.getInt("anz")];

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

                result = stmts.performDatabaseOperation("SELECT idstudent FROM student WHERE idclass='" + klasseid[0] + "'", 0, connection, statement);

                try {
                    if (result != null) {

                        try {
                            int i = 0;
                            while (result.next()) {
                                studentLength[i] = result.getString("idstudent");
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
                }
                try {
                    try {
                        if (menge>=studentLength.length) {
                            for (int i = 0; i < studentLength.length; i++) {
                                String sid = studentLength[i];
                                stmts.performDatabaseOperation("INSERT INTO borrowed VALUES(" + "null" + ", " + tid + ", " + sid + leihselect + ", 0, " + "'" + currentTimeStamp + "')", 1, connection, statement);
                            }
                            //stmts.performDatabaseOperation("UPDATE leihobjekt SET quantity = quantity -" + menge, 1, connection, statement);
                            Toast.makeText(this, "Ausleihe wurde für die Klasse hinzugefügt", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(this, "Ausleihe ist nicht möglich", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception exception) {
                        Log.e("Error: ", exception.getMessage());
                    }
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                } finally {

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
    }

    private int getMenge() {

        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT l.idleihobjekt,description, scancode, quantity - (SELECT count(idlendingobject) FROM borrowed b WHERE b.idlendingobject = l.idleihobjekt AND isback = 0) as quantity, title FROM leihobjekt l JOIN category ON category.idcategory = l.idcategory WHERE idleihobjekt=" + id, 3, connection, statement);
        int ret = 0;
        try {
            if (result != null) {
                try {
                    if (result.next()) {
                        ret = result.getInt("quantity");
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


        return ret;
    }
}

