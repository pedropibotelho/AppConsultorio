package com.example.appconsultorio.ui.relatoriopaciente;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
    String nomePacienteProcurado, cpfPacienteProcurado;


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


        btnProcurar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Obter o nome do paciente do AutoCompleteTextView
                String nomePaciente = autoCompleteTextView.getText().toString().trim();
                consultarPacientes(nomePaciente);
            }
        });

        Button btnExcluir = rootView.findViewById(R.id.butao_excluir_paciente);

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePaciente = autoCompleteTextView.getText().toString().trim();
                excluirPaciente(nomePaciente);
            }
        });

        Button btnAlterar = rootView.findViewById(R.id.butao_alterar_paciente);

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePaciente = autoCompleteTextView.getText().toString().trim();
                alterarPaciente(nomePaciente);
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

    @SuppressLint("Range")
    public void consultarPacientes(String nomePaciente) {
        // Verificar se o nome do paciente não está vazio
        if (!nomePaciente.isEmpty()) {
            // Realizar a busca no banco de dados pelo nome do paciente
            Cursor cursor = db.rawQuery("SELECT * FROM paciente WHERE nome=?", new String[]{nomePaciente});


            // Verificar se o cursor tem resultados
            if (cursor.moveToFirst()) {
                // Preencher os EditTexts com as informações do paciente
                EditText edtNomePaciente = getView().findViewById(R.id.edit_nome_paciente_relatorio);
                EditText edtDataNascimento = getView().findViewById(R.id.edit_data_nascimento_relatorio);
                EditText edtTelefone = getView().findViewById(R.id.edit_telefone_relatorio);
                EditText edtCPF = getView().findViewById(R.id.edit_cpf_relatorio);

                edtNomePaciente.setText(cursor.getString(cursor.getColumnIndex("nome")));
                edtDataNascimento.setText(cursor.getString(cursor.getColumnIndex("data_nascimento")));
                edtTelefone.setText(cursor.getString(cursor.getColumnIndex("telefone")));
                edtCPF.setText(cursor.getString(cursor.getColumnIndex("cpf")));

                nomePacienteProcurado = edtNomePaciente.getText().toString();
                cpfPacienteProcurado = edtCPF.getText().toString();

                editTextComportamento(true);
                // Fechar o cursor
                cursor.close();
                Toast.makeText(getContext(), "Paciente consultado", Toast.LENGTH_SHORT).show();
            } else {
                editTextComportamento(false);
                Toast.makeText(getContext(), "Paciente não encontrado!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Digite o nome do paciente!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirPaciente(String nomePaciente){
        if(!nomePaciente.isEmpty()){
            int comandoDelete= db.delete("paciente", "nome=?", new String[]{nomePaciente});
            if(comandoDelete>0){
                Toast.makeText(getContext(), "Paciente excluído com sucesso!", Toast.LENGTH_SHORT).show();
                editTextComportamento(false);
            }else {
                Toast.makeText(getContext(), "Paciente não encontrado!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(),"Digite o nome do paciente!", Toast.LENGTH_SHORT).show();
        }
    }

    public void alterarPaciente(String nomePaciente) {
        if (!nomePaciente.isEmpty()) {
            EditText edtNomePaciente = getView().findViewById(R.id.edit_nome_paciente_relatorio);
            EditText edtDataNascimento = getView().findViewById(R.id.edit_data_nascimento_relatorio);
            EditText edtTelefone = getView().findViewById(R.id.edit_telefone_relatorio);
            EditText edtCPF = getView().findViewById(R.id.edit_cpf_relatorio);

            String nomePacienteText = edtNomePaciente.getText().toString();
            String dataNascimentoText = edtDataNascimento.getText().toString();
            String telefoneText = edtTelefone.getText().toString();
            String cpfText = edtCPF.getText().toString();

            if (nomePacienteText.isEmpty() || dataNascimentoText.isEmpty() || telefoneText.isEmpty() || cpfText.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            } else if (dataNascimentoText.contains("_") || telefoneText.contains("_") || cpfText.contains("_")) {
                Toast.makeText(getContext(), "Preencha todos os campos corretamente para realizar o cadastro!", Toast.LENGTH_SHORT).show();
            } else {
                // Verificar se já existe um paciente com o novo nome ou CPF, excluindo o paciente atual
                Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM paciente WHERE (nome=? OR cpf=?) AND nome<>?", new String[]{nomePacienteText, cpfText, nomePacienteProcurado});
                cursor.moveToFirst();
                int count = cursor.getInt(0);
                cursor.close();

                if (count == 0) {
                    ContentValues valores = new ContentValues();
                    valores.put("nome", nomePacienteText);
                    valores.put("data_nascimento", dataNascimentoText);
                    valores.put("telefone", telefoneText);
                    valores.put("cpf", cpfText);

                    String whereClause = "nome=?";
                    String[] whereArgs = {nomePaciente};

                    int comandoAlterar = db.update("paciente", valores, whereClause, whereArgs);

                    if (comandoAlterar > 0) {
                        Toast.makeText(getContext(), "Paciente alterado com sucesso!", Toast.LENGTH_SHORT).show();
                        editTextComportamento(false);
                    } else {
                        Toast.makeText(getContext(), "Erro ao alterar!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Já existe um paciente com esse nome ou CPF!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getContext(), "Digite o nome do paciente!", Toast.LENGTH_SHORT).show();
        }
    }


    public void editTextComportamento(boolean op){
        EditText edtNomePaciente = getView().findViewById(R.id.edit_nome_paciente_relatorio);
        EditText edtDataNascimento = getView().findViewById(R.id.edit_data_nascimento_relatorio);
        EditText edtTelefone = getView().findViewById(R.id.edit_telefone_relatorio);
        EditText edtCPF = getView().findViewById(R.id.edit_cpf_relatorio);

        if(op){
            edtNomePaciente.setFocusable(true);
            edtNomePaciente.setFocusableInTouchMode(true);
            edtDataNascimento.setFocusable(true);
            edtDataNascimento.setFocusableInTouchMode(true);
            edtTelefone.setFocusable(true);
            edtTelefone.setFocusableInTouchMode(true);
            edtCPF.setFocusable(true);
            edtCPF.setFocusableInTouchMode(true);
        }else{
            edtNomePaciente.setText("");
            edtDataNascimento.setText("");
            edtTelefone.setText("");
            edtCPF.setText("");
            edtNomePaciente.setFocusable(false);
            edtNomePaciente.setFocusableInTouchMode(false);
            edtDataNascimento.setFocusable(false);
            edtDataNascimento.setFocusableInTouchMode(false);
            edtTelefone.setFocusable(false);
            edtTelefone.setFocusableInTouchMode(false);
            edtCPF.setFocusable(false);
            edtCPF.setFocusableInTouchMode(false);
        }
    }


}