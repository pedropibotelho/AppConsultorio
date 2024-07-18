package com.example.appconsultorio.ui.relatorioconsulta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.appconsultorio.ModificarConsulta;
import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentRelatorioConsultaBinding;
import com.example.appconsultorio.ui.consulta.Consulta;
import com.example.appconsultorio.ui.consulta.ConsultaAdapter;


import java.util.ArrayList;
import java.util.List;

public class RelatorioConsultaFragment extends Fragment {

    private static final String TAG = "RelatorioConsultaFrag";
    private FragmentRelatorioConsultaBinding binding;
    private SQLiteDatabase db;

    private RecyclerView recyclerView;
    private ConsultaAdapter consultaAdapter;
    private List<Consulta> consultaList = new ArrayList<>();
    Button btnModificar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento de consulta");
        View rootView = inflater.inflate(R.layout.fragment_relatorio_consulta, container, false);
        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.autoedit_nome_paciente_consulta_relatorio);

        if (autoCompleteTextView != null) {
            configAutoCompleteTextView(autoCompleteTextView);
        }

        recyclerView = rootView.findViewById(R.id.recycleview_datas_consultas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        consultaAdapter = new ConsultaAdapter(consultaList);
        recyclerView.setAdapter(consultaAdapter);

        Button btnProcurar = rootView.findViewById(R.id.butao_procurar_paciente_relatorio);

        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePaciente = autoCompleteTextView.getText().toString();
                procurarConsultas(nomePaciente);
            }
        });

        btnModificar = rootView.findViewById(R.id.butao_modificar_consulta_relatorio);
        btnModificar.setEnabled(false);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePaciente = autoCompleteTextView.getText().toString();
                Intent intent = new Intent(getActivity(), ModificarConsulta.class);
                intent.putExtra("nomePaciente", nomePaciente);
                startActivity(intent);
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

    private void procurarConsultas(String nomePaciente){
        consultaList.clear();
        btnModificar.setEnabled(false);
        int idPaciente = buscarIdPaciente(nomePaciente);
        if(idPaciente != -1) {
            String idPacienteString = String.valueOf(idPaciente);
            Cursor cursor = null;
            try {
                cursor = db.rawQuery("SELECT data_procedimento, procedimento FROM consulta WHERE id_paciente = ?", new String[]{idPacienteString});
                while (cursor.moveToNext()){
                    @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex("data_procedimento"));
                    @SuppressLint("Range") String procedimento = cursor.getString(cursor.getColumnIndex("procedimento"));
                    @SuppressLint("Range") float preco = cursor.getFloat(cursor.getColumnIndex("preco"));
                    consultaList.add(new Consulta(data, procedimento, preco));
                }
                consultaAdapter.notifyDataSetChanged();
                btnModificar.setEnabled(true);
                Toast.makeText(getContext(), "Relatório de Consultas completo!", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Log.e(TAG, "Erro ao procurar consultas", e);
                Toast.makeText(getContext(), "Erro ao procurar consultas", Toast.LENGTH_SHORT).show();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            Toast.makeText(getContext(), "Paciente inválido!", Toast.LENGTH_SHORT).show();
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