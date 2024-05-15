package com.example.appconsultorio.ui.relatoriopaciente;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentRelatorioPacienteBinding;

import java.util.ArrayList;

public class RelatorioPacienteFragment extends Fragment {
    private static final String TAG = "RelatorioPacienteFrag";
    private FragmentRelatorioPacienteBinding binding;
    private SQLiteDatabase db;
    private Button btnProcurar;

    @Override
    // Dentro do método onCreateView em RelatorioPacienteFragment.java

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento de relatório do paciente.");
        View rootView = inflater.inflate(R.layout.fragment_relatorio_paciente, container, false);
        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.autoedit_nome_relatorio);

        // Verifica se o autocomplete é existente
        if (autoCompleteTextView != null) {
            configAutoCompleteTextView(autoCompleteTextView);
        }

        // Encontrar o botão de procurar
        Button btnProcurar = rootView.findViewById(R.id.butao_procurar_paciente);

        // Adicionar um OnClickListener ao botão de procurar
        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(View v) {
                // Obter o nome do paciente do AutoCompleteTextView
                String nomePaciente = autoCompleteTextView.getText().toString().trim();

                // Verificar se o nome do paciente não está vazio
                if (!nomePaciente.isEmpty()) {
                    // Realizar a busca no banco de dados pelo nome do paciente
                    Cursor cursor = db.rawQuery("SELECT * FROM paciente WHERE nome=?", new String[]{nomePaciente});

                    // Verificar se o cursor tem resultados
                    if (cursor.moveToFirst()) {
                        // Preencher os EditTexts com as informações do paciente
                        EditText edtNomePaciente = rootView.findViewById(R.id.edit_nome_paciente_relatorio);
                        EditText edtDataNascimento = rootView.findViewById(R.id.edit_data_nascimento_relatorio);
                        EditText edtTelefone = rootView.findViewById(R.id.edit_telefone_relatorio);
                        EditText edtCPF = rootView.findViewById(R.id.edit_cpf_relatorio);

                        edtNomePaciente.setText(cursor.getString(cursor.getColumnIndex("nome")));
                        edtDataNascimento.setText(cursor.getString(cursor.getColumnIndex("data_nascimento")));
                        edtTelefone.setText(cursor.getString(cursor.getColumnIndex("telefone")));
                        edtCPF.setText(cursor.getString(cursor.getColumnIndex("cpf")));

                        // Fechar o cursor
                        cursor.close();
                    } else {
                        Toast.makeText(getContext(), "Paciente não encontrado!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Digite o nome do paciente!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Realizando conexão com o banco de dados para obtenção dos usuários já cadastrados no banco
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


}