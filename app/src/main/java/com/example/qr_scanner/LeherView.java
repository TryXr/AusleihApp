package com.example.qr_scanner;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    String description, idleihobjekt;
    Statement statement;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leher_view);
        code = getIntent().getStringExtra("code");
        Button buttonAusgabe = findViewById(R.id.buttonAusgabe);
        Button buttonAnnahme = findViewById(R.id.buttonAnnahme);
        Button buttonEdit = findViewById(R.id.buttonGeraetBuchBearbeiten);
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

        ResultSet result = stmts.performDatabaseOperation("SELECT description, idleihobjekt FROM leihobjekt WHERE scancode=" + code, 0, connection, statement);

        try {
            if (result != null && result.next()) {
                 description = result.getString("description");
                 idleihobjekt = result.getString("idleihobjekt");
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

        buttonLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageDialog();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEdit();
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

        Intent intent = new Intent(this, ListAnnahme.class);
        intent.putExtra("code", code);
        intent.putExtra("teacherid", getIntent().getStringExtra("teacherid"));
        startActivity(intent);
    }

    private void startNewObject() {

        Intent intent = new Intent(this, NewObjectView.class);
        intent.putExtra("code", code);
        startActivity(intent);
    }

    private void startEdit(){

        Intent intent = new Intent(this, BearbeitenView.class);
        intent.putExtra("code", code);
        intent.putExtra("description", description);
        intent.putExtra("teacherid", getIntent().getStringExtra("teacherid"));
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
                int menge = getMenge();
                if(menge > 0){
                        stmts.performDatabaseOperation("UPDATE leihobjekt SET quantity = quantity - 1 WHERE scancode = '" + code + "' AND idleihobjekt = " + idleihobjekt, 1, connection, statement);
                   // stmts.performDatabaseOperation("UPDATE leihobjekt SET quantity = quantity -1 WHERE scancode=" + "'" + code + "'" +" AND idleihobjekt=" +  idleihobjekt, 1, connection, statement);
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    if(stmts.rows >= 1) {
                        Toast.makeText(LeherView.this, "Ein Medium wurde gelöscht", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(LeherView.this, "SQL nicht funkationasdnfasfjas", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(LeherView.this, "Es sind keine Medien mehr zum löschen", Toast.LENGTH_LONG).show();
                }

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ScannerKlasse.class);

        startActivity(intent);
    }

    public int getMenge() {
        setDBAccess();
        ResultSet result = null;
        result = stmts.performDatabaseOperation("SELECT quantity - (SELECT count(idlendingobject) FROM borrowed b WHERE b.idlendingobject = l.idleihobjekt AND isback = 0) as quantity FROM leihobjekt l JOIN category ON category.idcategory = l.idcategory WHERE idleihobjekt= (SELECT idleihobjekt FROM leihobjekt WHERE scancode =" + code +")" , 3, connection, statement);
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
