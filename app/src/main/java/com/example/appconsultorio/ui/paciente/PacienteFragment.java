package com.example.appconsultorio.ui.paciente;

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
import android.widget.Toast;

import com.example.appconsultorio.R;
import com.example.appconsultorio.databinding.FragmentPacienteBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PacienteFragment extends Fragment {
    private static final String TAG = "PacienteFrag";
    private FragmentPacienteBinding binding;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Inflando o layout do fragmento do paciente.");
        View rootView = inflater.inflate(R.layout.fragment_paciente, container, false);

        Button btnCadastro = rootView.findViewById(R.id.butao_cadastrar);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarPaciente();
            }
        });

        db = getActivity().openOrCreateDatabase("appconsultorio", getContext().MODE_PRIVATE, null);
        return rootView;
    }

    public void cadastrarPaciente(){
        EditText edtNomePaciente = getView().findViewById(R.id.edit_nome_paciente);
        EditText edtDataNascimento = getView().findViewById(R.id.edit_data_nascimento);
        EditText edtTelefone = getView().findViewById(R.id.edit_telefone);
        EditText edtCPF = getView().findViewById(R.id.edit_cpf);

        String nomePacienteText = edtNomePaciente.getText().toString();
        String dataNascimentoText = edtDataNascimento.getText().toString();
        String telefoneText = edtTelefone.getText().toString();
        String cpfText = edtCPF.getText().toString();

        if(nomePacienteText.isEmpty() || dataNascimentoText.isEmpty() || telefoneText.isEmpty() || cpfText.isEmpty()){
            Toast.makeText(getContext(), "Preencha todos os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
        } else if (dataNascimentoText.contains("_")||telefoneText.contains("_")||cpfText.contains("_")) {
            Toast.makeText(getContext(), "Preencha todos os campos corretamente para realizar o cadastro!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date dataInicial = sdf.parse("01/01/1920");
                Date dataAtual = new Date();
                Date dataNascimento = sdf.parse(dataNascimentoText);

                if(dataNascimento.after(dataInicial) && dataNascimento.before(dataAtual)){
                    Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM paciente WHERE nome=? AND data_nascimento=? AND telefone=? AND cpf=?", new String[]{nomePacienteText, dataNascimentoText, telefoneText, cpfText});
                    cursor.moveToFirst();
                    int count = cursor.getInt(0);
                    cursor.close();

                    if (count == 0) {
                        Cursor cursor2 = db.rawQuery("SELECT COUNT(*) FROM paciente WHERE nome=? OR cpf=?", new String[]{nomePacienteText, cpfText});
                        cursor2.moveToFirst();
                        int count2 = cursor2.getInt(0);
                        cursor2.close();

                        if (count2 == 0) {
                            db.execSQL("INSERT INTO paciente (nome, data_nascimento, telefone, cpf) VALUES (?, ?, ?, ?)", new String[]{nomePacienteText, dataNascimentoText, telefoneText, cpfText});
                            Toast.makeText(getContext(), "Paciente cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                            edtNomePaciente.setText("");
                            edtDataNascimento.setText("");
                            edtTelefone.setText("");
                            edtCPF.setText("");
                        } else {
                            Toast.makeText(getContext(), "Já existe um paciente com esse nome/cpf!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Paciente já cadastrado!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Preencha uma data válida!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Erro ao cadastrar paciente!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
