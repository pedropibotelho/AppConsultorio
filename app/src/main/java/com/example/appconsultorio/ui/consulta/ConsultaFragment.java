package com.example.appconsultorio.ui.consulta;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentConsultaBinding;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment {
    private static final String TAG = "ConsultaFrag";
    private FragmentConsultaBinding binding;
    private SQLiteDatabase db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento de consulta");
        View rootView = inflater.inflate(R.layout.fragment_consulta, container, false);
        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.autoedit_nome_paciente_consulta);

        if (autoCompleteTextView != null) {
            configAutoCompleteTextView(autoCompleteTextView);
        }

        Button btnCadastroConsulta = rootView.findViewById(R.id.butao_cadastrar_consulta);
        btnCadastroConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePaciente = autoCompleteTextView.getText().toString().trim();
                cadastrarConsulta(nomePaciente);
            }
        });

        db = getActivity().openOrCreateDatabase("appconsultorio", getContext().MODE_PRIVATE, null);
        return rootView;
    }

    private void configAutoCompleteTextView(AutoCompleteTextView textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) { // Evitar buscas muito frequentes se poucos caracteres forem digitados
                    atualizacaoLista(s.toString(), textView);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @SuppressLint("Range")
//    Basicamente atualiza a lista que é mostrada no TextView filtrando de acordo com os pacientes cadastrados
    private void atualizacaoLista(String searchQuery, AutoCompleteTextView textView) {
        Cursor cursor = db.rawQuery("SELECT nome FROM paciente WHERE nome LIKE ?", new String[]{"%" + searchQuery + "%"});
        ArrayList<String> names = new ArrayList<>();
        while (cursor.moveToNext()) {
            names.add(cursor.getString(cursor.getColumnIndex("nome")));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, names);
        textView.setAdapter(adapter);
    }

    public void cadastrarConsulta(String nomePaciente){
        int idPaciente = buscarIdPaciente(nomePaciente);
        if(idPaciente != -1){
            EditText edtNomePacineteConsulta = getView().findViewById(R.id.autoedit_nome_paciente_consulta);
            EditText edtDataConsulta = getView().findViewById(R.id.edit_data_consulta);
            EditText edtProcedimento = getView().findViewById(R.id.edit_procedimento_consulta);
            String nomePacineteConsultaText = edtNomePacineteConsulta.getText().toString();
            String dataConsultaText = edtDataConsulta.getText().toString();
            String procedimentoText = edtProcedimento.getText().toString();

            if(nomePacineteConsultaText.isEmpty() || dataConsultaText.isEmpty() || procedimentoText.isEmpty()){
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }else{
                
            }
        }
    }

    @SuppressLint("Range")
    public int buscarIdPaciente(String nomePaciente){
        int idPaciente = -1;
        Cursor cursor = db.rawQuery("SELECT id FROM paciente WHERE nome = ?", new String[]{nomePaciente});

        if(cursor != null && cursor.moveToFirst()){
            idPaciente = cursor.getInt(cursor.getColumnIndex("id"));
        }

        if(cursor !=null){
            cursor.close();
        }
        return idPaciente;
    }
}