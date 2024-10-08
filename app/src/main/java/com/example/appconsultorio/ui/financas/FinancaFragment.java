package com.example.appconsultorio.ui.financas;

import android.annotation.SuppressLint;
import android.database.Cursor;
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
import android.widget.Toast;

import com.example.appconsultorio.DatabaseHelper;
import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentFinancaBinding;

public class FinancaFragment extends Fragment {
    private static final String TAG = "FinancasFrag";
    private FragmentFinancaBinding binding;
    private SQLiteDatabase db;
    private DatabaseHelper dh;

    float valorTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento das finanças.");
        View rootView = inflater.inflate(R.layout.fragment_financa, container, false);


        Button btnProcurar = rootView.findViewById(R.id.butao_procurar_financa);
        btnProcurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularValor();
            }
        });

        dh = new DatabaseHelper(getContext());
        db = dh.getWritableDatabase();
        return rootView;
    }

    @SuppressLint("Range")
    public void calcularValor(){
        EditText edtAno = getView().findViewById(R.id.edit_financa_ano);
        String anoText = edtAno.getText().toString();

        Spinner spinnerMeses = getView().findViewById(R.id.spinner_mes);
        int mesesInt =(int) spinnerMeses.getSelectedItemId();
        String mesFormatado = String.format("%02d", mesesInt);

        Log.e(TAG, "Mes " + mesesInt);

        Cursor cursor;
        if(!anoText.isEmpty()) {
            if(mesesInt == 0) {
                cursor = db.rawQuery("SELECT SUM(preco) AS total_valor FROM consulta WHERE substr(data_procedimento, 7, 4) = ? ", new String[]{anoText});
                if (cursor.moveToFirst()) {
                    valorTotal = cursor.getFloat(cursor.getColumnIndex("total_valor"));
                }
                cursor.close();
            }else {
                cursor = db.rawQuery("SELECT SUM(preco) AS total_valor FROM consulta WHERE substr(data_procedimento, 7, 4) = ? AND substr(data_procedimento, 4, 2) = ? ", new String[]{anoText, mesFormatado});
                if (cursor.moveToFirst()) {
                    valorTotal = cursor.getFloat(cursor.getColumnIndex("total_valor"));
                }
                cursor.close();
            }
            EditText edtValorTotal = getView().findViewById(R.id.edit_financa_valor_total);
            edtValorTotal.setText("R$" + valorTotal);
            Toast.makeText(getContext(), "Valor calculado com sucesso!", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getContext(), "Digite o ano!", Toast.LENGTH_SHORT).show();

    }
}