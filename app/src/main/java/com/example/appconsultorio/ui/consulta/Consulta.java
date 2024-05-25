package com.example.appconsultorio.ui.consulta;
public class Consulta {
    private String data;
    private String procedimento;

    public Consulta(String data, String procedimento) {
        this.data = data;
        this.procedimento = procedimento;
    }

    public String getData() {
        return data;
    }

    public String getProcedimento() {
        return procedimento;
    }
}

