package com.example.appconsultorio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormLogin extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editUser;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        dbHelper = new DatabaseHelper(this);

        btn = (Button) findViewById(R.id.butaoLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testeLogin();
            }
        });
    }

    public void testeLogin() {
        EditText edtUser = findViewById(R.id.edit_user);
        EditText edtSenha = findViewById(R.id.edit_senha);
        String digitUser = edtUser.getText().toString();
        String digitSenha = edtSenha.getText().toString();

        SQLiteDatabase bancoDados = dbHelper.getReadableDatabase();

        Cursor cursor = bancoDados.rawQuery("SELECT COUNT(*) FROM login WHERE user=? AND senha=?", new String[]{digitUser, digitSenha});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            Intent i = new Intent(FormLogin.this, TelaPrincipal.class);
            Toast.makeText(this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
            startActivity(i);
            finish();
        } else {
            Toast.makeText(this, "Usuário ou senha incorretos.", Toast.LENGTH_SHORT).show();
        }
    }
}
