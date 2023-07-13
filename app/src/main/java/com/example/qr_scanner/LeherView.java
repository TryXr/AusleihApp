package com.example.qr_scanner;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LeherView extends AppCompatActivity {

    private String code;
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
            }
        });

        buttonNeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hier folgt die Eintragung von etwas neuem (Datenbank usw.)

                stmts.performDatabaseOperation("INSERT INTO artikel", 2);
            }
        });

        buttonLoeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // //Hier folgt die Austragung von etwas (Datenbank usw.)
            }
        });
    }

    private void startAusgabe() {

        Intent intent = new Intent(this, AusgabeView.class);
        intent.putExtra("code", code);
        startActivity(intent);
    }
}