package com.example.appconsultorio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appconsultorio.db";
    private static final int DATABASE_VERSION = 1;  // Começando com a versão 1

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS login(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", user VARCHAR" +
                ", senha VARCHAR)");
        db.execSQL("INSERT INTO login (user, senha) VALUES ('admin','admin')");

        db.execSQL("CREATE TABLE IF NOT EXISTS paciente(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", nome VARCHAR" +
                ", data_nascimento DATE" +
                ", telefone VARCHAR" +
                ", cpf VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS consulta(" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", id_paciente INTEGER" +
                ", data_procedimento DATE" +
                ", procedimento VARCHAR" +
                ", preco FLOAT" +
                ", FOREIGN KEY (id_paciente) REFERENCES paciente(id))");  // Sem a coluna preco na versão 1
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Exemplo de migração para a versão 2, adicionando a coluna preco

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se necessário, implemente o downgrade
    }
}
