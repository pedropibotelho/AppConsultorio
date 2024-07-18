package com.example.appconsultorio.ui.consulta;

public class Consulta {
    private String data;
    private String procedimento;
    private float preco;

    public Consulta(String data, String procedimento, float preco) {
        this.data = data;
        this.procedimento = procedimento;
        this.preco = preco;
    }

    public String getData() {
        return data;
    }

    public String getProcedimento() {
        return procedimento;
    }

    public float getPreco() {
        return preco;
    }
}

