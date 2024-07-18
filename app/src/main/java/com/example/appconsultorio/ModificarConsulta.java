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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ModificarConsulta extends AppCompatActivity {

    private SQLiteDatabase db;
    private String nomePaciente;
    private static final String TAG = "ModificarConsulta";

    private AutoCompleteTextView autoCompleteTextView;
    private EditText edtDataConsulta;
    private EditText edtProcedimento;
    private EditText edtPreco;

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
        edtPreco = findViewById(R.id.edit_preco_consulta_modificar);

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

        Button btnAlterar = findViewById(R.id.butao_alterar_consulta_modificar);
        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataAutoIncrement = autoCompleteTextView.getText().toString().trim();
                alterarConsulta(dataAutoIncrement);
            }
        });

        Button btnExcluir = findViewById(R.id.butao_excluir_consulta_modificar);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataAutoIncrement = autoCompleteTextView.getText().toString().trim();
                excluirConsulta(dataAutoIncrement);
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
            int idPaciente = buscarIdPaciente(nomePaciente);
            String idPacienteString = String.valueOf(idPaciente);
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT * FROM consulta WHERE data_procedimento=? AND id_paciente = ?", new String[]{dataAutoIncrement, idPacienteString});

                if (cursor.moveToFirst()) {
                    edtDataConsulta.setText(cursor.getString(cursor.getColumnIndex("data_procedimento")));
                    edtProcedimento.setText(cursor.getString(cursor.getColumnIndex("procedimento")));
                    edtPreco.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndex("preco"))));

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
        if (edtDataConsulta == null || edtProcedimento == null || edtPreco == null) {
            Log.e(TAG, "EditTexts are not initialized");
            return;
        }

        if (op) {
            edtDataConsulta.setFocusable(true);
            edtDataConsulta.setFocusableInTouchMode(true);
            edtProcedimento.setFocusable(true);
            edtProcedimento.setFocusableInTouchMode(true);
            edtPreco.setFocusable(true);
            edtPreco.setFocusableInTouchMode(true);
        } else {
            edtDataConsulta.setText("");
            edtProcedimento.setText("");
            edtPreco.setText("");
            edtDataConsulta.setFocusable(false);
            edtDataConsulta.setFocusableInTouchMode(false);
            edtProcedimento.setFocusable(false);
            edtProcedimento.setFocusableInTouchMode(false);
            edtPreco.setFocusable(false);
            edtPreco.setFocusableInTouchMode(false);
        }
    }

    public void alterarConsulta(String dataAutoIncrement) {
        if (!dataAutoIncrement.isEmpty()) {
            String dataConsultaText = edtDataConsulta.getText().toString().trim();
            String procedimentoText = edtProcedimento.getText().toString().trim();
            String precoText = edtPreco.getText().toString().trim();

            if (dataConsultaText.isEmpty() || procedimentoText.isEmpty() || precoText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (dataConsultaText.contains("_")) {
                Toast.makeText(this, "Preencha todos os campos corretamente para realizar a alteração!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date dataConsulta = sdf.parse(dataConsultaText);
                    Date dataPronta = sdf.parse("01/01/2000");

                    if (dataConsulta.before(dataPronta)) {
                        Toast.makeText(this, "A data do procedimento não pode ser cadastrada!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    float preco = Float.parseFloat(precoText);

                    int idPaciente = buscarIdPaciente(nomePaciente);
                    if (idPaciente != -1) {
                        db.execSQL("UPDATE consulta SET data_procedimento = ?, procedimento = ?, preco = ? WHERE id_paciente = ? AND data_procedimento = ?",
                                new Object[]{dataConsultaText, procedimentoText, preco, idPaciente, dataAutoIncrement});

                        Toast.makeText(this, "Consulta alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        editTextComportamento(false);
                    } else {
                        Toast.makeText(this, "Erro ao encontrar o paciente!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao alterar consulta!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Preencha o campo de data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirConsulta(String dataAutoIncrement){
        if (!dataAutoIncrement.isEmpty()) {
            try {
                int idPaciente = buscarIdPaciente(nomePaciente);
                if (idPaciente != -1) {
                    db.execSQL("DELETE FROM consulta WHERE id_paciente = ? AND data_procedimento = ?", new Object[]{idPaciente, dataAutoIncrement});

                    Toast.makeText(this, "Consulta excluída com sucesso!", Toast.LENGTH_SHORT).show();
                    editTextComportamento(false);
                } else {
                    Toast.makeText(this, "Erro ao encontrar o paciente!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao excluir consulta", e);
                Toast.makeText(this, "Erro ao excluir consulta!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha o campo de data!", Toast.LENGTH_SHORT).show();
        }
    }
}
