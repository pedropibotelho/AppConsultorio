package com.example.appconsultorio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormLogin extends AppCompatActivity {
    private SQLiteDatabase bancoDados;
    private EditText editUser;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        criarBancoDados();

        btn = (Button) findViewById(R.id.butaoLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testeLogin();
            }
        });
    }

    public void criarBancoDados() {
        try {
            bancoDados = openOrCreateDatabase("appconsultorio", MODE_PRIVATE, null);
            Cursor cursor = bancoDados.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='login'", null);
            cursor.moveToFirst();
            int tableCount = cursor.getInt(0);
            cursor.close();
            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS login("+
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", user VARCHAR" +
                    ", senha VARCHAR)");

            bancoDados.execSQL("INSERT INTO login (user, senha) VALUES ('admin','admin')");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void testeLogin() {
        EditText edtUser = findViewById(R.id.edit_user);
        EditText edtSenha = findViewById(R.id.edit_senha);
        String digitUser = edtUser.getText().toString();
        String digitSenha = edtSenha.getText().toString();

        SQLiteDatabase bancoDados = openOrCreateDatabase("appconsultorio", MODE_PRIVATE, null);

        // Consulta para verificar se o usuário e senha correspondem
        Cursor cursor = bancoDados.rawQuery("SELECT COUNT(*) FROM login WHERE user=? AND senha=?", new String[]{digitUser, digitSenha});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        bancoDados.close();

        if (count > 0) {
            Intent i = new Intent(FormLogin.this, TelaPrincipal.class);
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            startActivity(i);
        } else {
            Toast.makeText(this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
        }
    }

}