package com.example.appconsultorio.ui.financas;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentFinancaBinding;

public class FinancaFragment extends Fragment {
    private static final String TAG = "FinancasFrag";
    private FragmentFinancaBinding binding;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento das finanças.");
        View rootView = inflater.inflate(R.layout.fragment_financa, container, false);


        Button btnProcurar = rootView.findViewById(R.id.butao_procurar_financa);
        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        db = getActivity().openOrCreateDatabase("appconsultorio", getContext().MODE_PRIVATE, null);
        return rootView;
    }

    public void calcularValor(){
        EditText edtAno = getView().findViewById(R.id.edit_financa_ano);
        String anoText = edtAno.getText().toString();

        Spinner spinnerMeses = getView().findViewById(R.id.spinner_mes);
        int mesesInt = spinnerMeses.getSelectedItemPosition();
    }
}