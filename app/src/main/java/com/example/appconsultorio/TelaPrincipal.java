package com.example.appconsultorio;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconsultorio.databinding.ActivityTelaPrincipalBinding;

import java.util.ArrayList;
import java.util.List;

public class TelaPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTelaPrincipalBinding binding;
    Button btn;
    private SQLiteDatabase bancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTelaPrincipal.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_cadastro_paciente,
                R.id.nav_relatorio_paciente, R.id.nav_cadastro_consulta, R.id.nav_relatorio_consulta)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tela_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        criarTabelas();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tela_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tela_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void criarTabelas() {
        try {
            bancoDados = openOrCreateDatabase("appconsultorio", MODE_PRIVATE, null);

            // Cria tabela Paciente se não existir
            Cursor cursorPaciente = bancoDados.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='paciente'", null);
            cursorPaciente.moveToFirst();
            int pacienteTableCount = cursorPaciente.getInt(0);
            cursorPaciente.close();

            if (pacienteTableCount == 0) {
                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS paciente("+
                        " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ", nome VARCHAR" +
                        ", data_nascimento DATE" +
                        ", telefone VARCHAR" +
                        ", cpf VARCHAR)");
            }

            Cursor cursorConsulta = bancoDados.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='consulta'", null);
            cursorConsulta.moveToFirst();
            int consultaTableCount = cursorConsulta.getInt(0);
            cursorConsulta.close();

            if (consultaTableCount == 0) {
                bancoDados.execSQL("CREATE TABLE IF NOT EXISTS consulta("+
                        " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ", id_paciente INTEGER" +
                        ", data_procedimento DATE" +
                        ", procedimento VARCHAR" +
                        ", FOREIGN KEY (id_paciente) REFERENCES paciente(id))");
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}