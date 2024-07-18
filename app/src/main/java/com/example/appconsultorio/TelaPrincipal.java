package com.example.appconsultorio;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appconsultorio.databinding.ActivityTelaPrincipalBinding;
import com.google.android.material.navigation.NavigationView;

public class TelaPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityTelaPrincipalBinding binding;
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
                R.id.nav_relatorio_paciente, R.id.nav_cadastro_consulta, R.id.nav_relatorio_consulta, R.id.nav_financas)
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
                        ", preco FLOAT" +  // Adicionando preco diretamente na criação da tabela
                        ", FOREIGN KEY (id_paciente) REFERENCES paciente(id))");
            } else {
                // Adiciona a coluna preco se não existir
                if (!colunaExiste("consulta", "preco")) {
                    bancoDados.execSQL("ALTER TABLE consulta ADD COLUMN preco FLOAT");
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private boolean colunaExiste(String tableName, String columnName) {
        boolean existe = false;
        Cursor cursor = null;
        try {
            cursor = bancoDados.rawQuery("PRAGMA table_info(" + tableName + ")", null);
            int columnIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                if (cursor.getString(columnIndex).equals(columnName)) {
                    existe = true;
                    break;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return existe;
    }
}
