
package com.example.flytrapiot;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;

    ListView listView;
    Button btnAgregar;

    ArrayList<Trampa> listaTrampas;
    ArrayAdapter<Trampa> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        listView = findViewById(R.id.listViewTrampas);
        btnAgregar = findViewById(R.id.btnAgregar);

        listaTrampas = new ArrayList<>();

        btnAgregar.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    FormularioActivity.class
            );

            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view,
                                         position, id) -> {

            Trampa trampa = listaTrampas.get(position);

            Intent intent = new Intent(
                    MainActivity.this,
                    FormularioActivity.class
            );

            intent.putExtra("id_trampa", trampa.getId());

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTrampas();
    }

    private void cargarTrampas() {

        listaTrampas.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM trampas ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(0);
                String sector = cursor.getString(1);
                String tipo = cursor.getString(2);
                int nivel = cursor.getInt(3);
                String observaciones = cursor.getString(4);

                Trampa trampa = new Trampa(
                        id,
                        sector,
                        tipo,
                        nivel,
                        observaciones
                );

                listaTrampas.add(trampa);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaTrampas
        );

        listView.setAdapter(adapter);
    }
}
