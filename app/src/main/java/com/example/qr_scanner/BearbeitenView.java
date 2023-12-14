package com.example.qr_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BearbeitenView extends AppCompatActivity implements View.OnClickListener {

    String code;
    EditText etDescription;
    String description;
    Spinner spinnerCategory;
    NumberPicker quantity;
    TextView tvAusgabe;
    MySQLStatements stmts = new MySQLStatements();
    Connection connection = null;
    Statement statement = null;
    Button btnSubmit;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bearbeiten_view);
        code = getIntent().getStringExtra("code");
        description = getIntent().getStringExtra("description");

        etDescription = findViewById(R.id.etDes);
        spinnerCategory = findViewById(R.id.spinnerCat);
        quantity = findViewById(R.id.npQuan);
        tvAusgabe = findViewById(R.id.tvAusgabe);
        btnSubmit = findViewById(R.id.buttonEdit);
        quantity.setMinValue(0);
        quantity.setMaxValue(100);
        etDescription.setText(description);
        setValueOfPicker();

        btnSubmit.setOnClickListener(this);



        setDBAccess();
        try {
            setSpinnerCategory(connection, statement);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        try {
            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    category = (String) parent.getSelectedItem();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    return;
                }
            });

        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }




    }

    private void setSpinnerCategory (Connection connection, Statement statement) throws
            SQLException {

        ResultSet result = null;
        ResultSet resultRows = null;
        resultRows = stmts.performDatabaseOperation("SELECT COUNT('idcategory') anz FROM category", 0, connection, statement);

        String[] spinnerCategoryItems = null;

        try {
            if (resultRows != null) {

                try {

                    while (resultRows.next()) {
                        spinnerCategoryItems = new String[resultRows.getInt("anz")];

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
                            spinnerCategoryItems[i] = result.getString("idcategory") + " " + result.getString("title");
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


                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerCategoryItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
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
        String[] categorys = category.split(" ");
        String code = getIntent().getStringExtra("code");
        int qnty = quantity.getValue();


        try {
            stmts.performDatabaseOperation("UPDATE leihobjekt SET description =" + "'" + etDescription.getText() + "'," + " quantity =" + qnty + ", idcategory = " + categorys[0] + " WHERE scancode = " + code, 1, connection,statement);
            Toast.makeText(this, "Medien wurden in der Datenbank geändert", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }finally {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LeherView.class);
        intent.putExtra("code", code);
        intent.putExtra("teacherid", getIntent().getStringExtra("teacherid"));
        startActivity(intent);
    }

    public void setValueOfPicker() {

        setDBAccess();
        ResultSet result = null;
        try {
            result = stmts.performDatabaseOperation("SELECT quantity FROM leihobjekt WHERE scancode = " + code, 0, connection, statement);
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        } finally {

            try {
                if (result != null) {

                    try {
                        int i = 0;
                        while (result.next()) {
                            quantity.setValue(result.getInt("quantity"));
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
    }
}
