package com.example.appconsultorio.ui.relatorioconsulta;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentRelatorioConsultaBinding;


import java.util.ArrayList;

public class RelatorioConsultaFragment extends Fragment {

    private static final String TAG = "RelatorioConsultaFrag";
    private FragmentRelatorioConsultaBinding binding;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento de consulta");
        View rootView = inflater.inflate(R.layout.fragment_relatorio_consulta, container, false);
        AutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.autoedit_nome_paciente_consulta_relatorio);

        if (autoCompleteTextView != null) {
            configAutoCompleteTextView(autoCompleteTextView);
        }

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