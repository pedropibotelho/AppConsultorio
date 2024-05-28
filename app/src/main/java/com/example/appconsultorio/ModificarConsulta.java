package com.example.appconsultorio;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ModificarConsulta extends AppCompatActivity {

    private SQLiteDatabase db;
    private String nomePaciente;
    private static final String TAG = "ModificarConsulta";

    private AutoCompleteTextView autoCompleteTextView;
    private EditText edtDataConsulta;
    private EditText edtProcedimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_consulta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        autoCompleteTextView = findViewById(R.id.autoedit_data_consulta_procurar_modificar);
        edtDataConsulta = findViewById(R.id.edit_data_consulta_modificar);
        edtProcedimento = findViewById(R.id.edit_procedimento_consulta_modificar);

        if (autoCompleteTextView != null) {
            configAutoCompleteTextView(autoCompleteTextView);
        }

        nomePaciente = getIntent().getStringExtra("nomePaciente");

        if (nomePaciente == null) {
            Log.e(TAG, "nomePaciente is null");
            finish();
            return;
        }

        Button btnProcurar = findViewById(R.id.butao_procurar_data_modificar);
        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataAutoIncrement = autoCompleteTextView.getText().toString().trim();
                consultarConsulta(dataAutoIncrement);
            }
        });

        try {
            db = openOrCreateDatabase("appconsultorio", MODE_PRIVATE, null);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao abrir ou criar o banco de dados", e);
        }
    }

    private void configAutoCompleteTextView(AutoCompleteTextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) { // Evitar buscas muito frequentes se poucos caracteres forem digitados
                    atualizacaoLista(textView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @SuppressLint("Range")
    private void atualizacaoLista(AutoCompleteTextView textView) {
        int idPaciente = buscarIdPaciente(nomePaciente);
        if (idPaciente != -1) {
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT data_procedimento FROM consulta WHERE id_paciente = ?", new String[]{String.valueOf(idPaciente)});
                ArrayList<String> datas = new ArrayList<>();
                while (cursor.moveToNext()) {
                    datas.add(cursor.getString(cursor.getColumnIndex("data_procedimento")));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, datas);
                textView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao atualizar lista de datas", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    @SuppressLint("Range")
    private int buscarIdPaciente(String nomePaciente) {
        int idPaciente = -1;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id FROM paciente WHERE nome = ?", new String[]{nomePaciente});
            if (cursor != null && cursor.moveToFirst()) {
                idPaciente = cursor.getInt(cursor.getColumnIndex("id"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar ID do paciente", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return idPaciente;
    }

    @SuppressLint("Range")
    public void consultarConsulta(String dataAutoIncrement){
        if (!dataAutoIncrement.isEmpty()) {
            // Realizar a busca no banco de dados pelo nome do paciente
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT * FROM consulta WHERE data_procedimento=?", new String[]{dataAutoIncrement});

                // Verificar se o cursor tem resultados
                if (cursor.moveToFirst()) {
                    // Preencher os EditTexts com as informações do paciente
                    edtDataConsulta.setText(cursor.getString(cursor.getColumnIndex("data_procedimento")));
                    edtProcedimento.setText(cursor.getString(cursor.getColumnIndex("procedimento")));

                    editTextComportamento(true);
                    Toast.makeText(this, "Consulta encontrada", Toast.LENGTH_SHORT).show();
                } else {
                    editTextComportamento(false);
                    Toast.makeText(this, "Consulta não encontrada!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao consultar a consulta", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            Toast.makeText(this, "Digite a data da consulta!", Toast.LENGTH_SHORT).show();
        }
    }

    public void editTextComportamento(boolean op) {
        if (edtDataConsulta == null || edtProcedimento == null) {
            Log.e(TAG, "EditTexts are not initialized");
            return;
        }

        if (op) {
            edtDataConsulta.setFocusable(true);
            edtDataConsulta.setFocusableInTouchMode(true);
            edtProcedimento.setFocusable(true);
            edtProcedimento.setFocusableInTouchMode(true);
        } else {
            edtDataConsulta.setText("");
            edtProcedimento.setText("");
            edtDataConsulta.setFocusable(false);
            edtDataConsulta.setFocusableInTouchMode(false);
            edtProcedimento.setFocusable(false);
            edtProcedimento.setFocusableInTouchMode(false);
        }
    }
}
