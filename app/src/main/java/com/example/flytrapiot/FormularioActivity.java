
package com.example.flytrapiot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FormularioActivity extends AppCompatActivity {

    DBHelper dbHelper;

    EditText txtCodigoSector;
    EditText txtTipoTrampa;
    EditText txtNivelCaptura;
    EditText txtObservaciones;

    Button btnGuardar;
    Button btnActualizar;
    Button btnEliminar;

    int idTrampa = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        dbHelper = new DBHelper(this);

        txtCodigoSector = findViewById(R.id.txtCodigoSector);
        txtTipoTrampa = findViewById(R.id.txtTipoTrampa);
        txtNivelCaptura = findViewById(R.id.txtNivelCaptura);
        txtObservaciones = findViewById(R.id.txtObservaciones);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Recibir el ID mediante Intent
        idTrampa = getIntent().getIntExtra(
                "id_trampa", -1
        );

        if (idTrampa != -1) {

            btnGuardar.setVisibility(Button.GONE);
            btnActualizar.setVisibility(Button.VISIBLE);
            btnEliminar.setVisibility(Button.VISIBLE);

            cargarDatos();

        } else {

            btnGuardar.setVisibility(Button.VISIBLE);
            btnActualizar.setVisibility(Button.GONE);
            btnEliminar.setVisibility(Button.GONE);
        }

        // Boton para guardar
        btnGuardar.setOnClickListener(v -> guardarTrampa());

        // Boton para actualizar
        btnActualizar.setOnClickListener(v -> actualizarTrampa());

        // Boton para eliminar
        btnEliminar.setOnClickListener(v -> eliminarTrampa());
    }

    // VALIDAR CAMPOS
    private boolean validarCampos() {

        String sector = txtCodigoSector.getText().toString().trim();
        String tipo = txtTipoTrampa.getText().toString().trim();
        String nivel = txtNivelCaptura.getText().toString().trim();

        if (sector.isEmpty()) {
            txtCodigoSector.setError("Ingrese el codigo del sector");
            return false;
        }

        if (tipo.isEmpty()) {
            txtTipoTrampa.setError("Ingrese el tipo de trampa");
            return false;
        }

        if (nivel.isEmpty()) {
            txtNivelCaptura.setError("Ingrese el nivel de captura");
            return false;
        }

        try {

            int cantidad = Integer.parseInt(nivel);

            if (cantidad < 0) {
                txtNivelCaptura.setError(
                        "El nivel no puede ser negativo"
                );
                return false;
            }

        } catch (NumberFormatException e) {

            txtNivelCaptura.setError(
                    "Ingrese un numero valido"
            );

            return false;
        }

        return true;
    }

    // INSERTAR NUEVA TRAMPA
    private void guardarTrampa() {

        if (!validarCampos()) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put("codigo_sector",
                txtCodigoSector.getText().toString().trim());

        valores.put("tipo_trampa",
                txtTipoTrampa.getText().toString().trim());

        valores.put("nivel_captura",
                Integer.parseInt(
                        txtNivelCaptura.getText().toString().trim()
                ));

        valores.put("observaciones",
                txtObservaciones.getText().toString().trim());

        long resultado = db.insert(
                "trampas",
                null,
                valores
        );

        db.close();

        if (resultado != -1) {

            Toast.makeText(this,
                    "Trampa registrada correctamente",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "Error al registrar la trampa",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // CONSULTAR DATOS POR ID
    private void cargarDatos() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM trampas WHERE id = ?",
                new String[]{String.valueOf(idTrampa)}
        );

        if (cursor.moveToFirst()) {

            txtCodigoSector.setText(cursor.getString(1));

            txtTipoTrampa.setText(cursor.getString(2));

            txtNivelCaptura.setText(
                    String.valueOf(cursor.getInt(3))
            );

            txtObservaciones.setText(cursor.getString(4));

        } else {

            Toast.makeText(this,
                    "No se encontro la trampa",
                    Toast.LENGTH_SHORT).show();

            finish();
        }

        cursor.close();
        db.close();
    }

    // ACTUALIZAR TRAMPA
    private void actualizarTrampa() {

        if (!validarCampos()) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put("codigo_sector",
                txtCodigoSector.getText().toString().trim());

        valores.put("tipo_trampa",
                txtTipoTrampa.getText().toString().trim());

        valores.put("nivel_captura",
                Integer.parseInt(
                        txtNivelCaptura.getText().toString().trim()
                ));

        valores.put("observaciones",
                txtObservaciones.getText().toString().trim());

        int resultado = db.update(
                "trampas",
                valores,
                "id = ?",
                new String[]{String.valueOf(idTrampa)}
        );

        db.close();

        if (resultado > 0) {

            Toast.makeText(this,
                    "Trampa actualizada correctamente",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "No se realizaron cambios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // ELIMINAR TRAMPA SIN CONFIRMACION
    private void eliminarTrampa() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int resultado = db.delete(
                "trampas",
                "id = ?",
                new String[]{String.valueOf(idTrampa)}
        );

        db.close();

        if (resultado > 0) {

            Toast.makeText(this,
                    "Trampa eliminada correctamente",
                    Toast.LENGTH_SHORT).show();

            finish();

        } else {

            Toast.makeText(this,
                    "Error al eliminar la trampa",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
