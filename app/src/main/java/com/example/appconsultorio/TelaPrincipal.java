package com.example.appconsultorio;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
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

public class TelaPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
private ActivityTelaPrincipalBinding binding;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityTelaPrincipalBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarTelaPrincipal.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_cadastro_paciente,
                R.id.nav_relatorio_paciente, R.id.nav_cadastro_consulta, R.id.nav_relatorio_consulta)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_tela_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        btn = (Button) findViewById(R.id.butao_cadastrar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarPaciente();
            }
        });
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

    public void cadastrarPaciente(){
        EditText edtNomePaciente = findViewById(R.id.edit_nome_paciente);
        EditText edtDataNascimento = findViewById(R.id.edit_data_nascimento);
        EditText edtTelefone = findViewById(R.id.edit_telefone);
        EditText edtCPF = findViewById(R.id.edit_cpf);

        String nomePacienteText = edtNomePaciente.getText().toString();
        String dataNascimentoText = edtDataNascimento.getText().toString();
        String telefoneText = edtTelefone.getText().toString();
        String cpfText = edtCPF.getText().toString();

        if(nomePacienteText.isEmpty() || dataNascimentoText.isEmpty() || telefoneText.isEmpty() || cpfText.isEmpty()){
            Toast.makeText(this, "Preencha todos os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
        }
    }
}