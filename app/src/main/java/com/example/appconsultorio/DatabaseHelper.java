package com.example.appconsultorio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appconsultorio.db";
    private static final int DATABASE_VERSION = 2;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Exemplos de migrações futuras
            // db.execSQL("ALTER TABLE login ADD COLUMN new_column VARCHAR");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Se necessário, implemente o downgrade
    }
}
